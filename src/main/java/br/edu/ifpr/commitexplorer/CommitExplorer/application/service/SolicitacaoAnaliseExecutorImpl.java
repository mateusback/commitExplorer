package br.edu.ifpr.commitexplorer.CommitExplorer.application.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.ProcessarSolicitacaoCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.*;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.*;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.CodeAnalyzerService;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.GitRepositoryCloner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Component
@AllArgsConstructor
public class SolicitacaoAnaliseExecutorImpl implements SolicitacaoAnaliseExecutor {

    private final CodeAnalyzerService codeAnalyzer;
    private final GitRepositoryCloner gitRepositoryCloner;
    private final SolicitacaoAnaliseRepository solicitacaoRepository;
    private final GitCommitExtractor gitCommitExtractor;
    private final CommitRepository commitRepository;
    private final AutorRepository autorRepository;
    private final AnaliseCodigoRepository analiseCodigoRepository;
    private final ArquivoAlteradoRepository arquivoAlteradoRepository;
    private final BranchRepository branchRepository;
    private final ProjetoRepository projetoRepository;
    private final RepositorioRepository repositorioRepository;
    private final AnaliseProjetoRepository analiseProjetoRepository;
    private final FeedbackAnaliseService feedbackAnaliseService;

    @Async
    public void processar(ProcessarSolicitacaoCommand command) {
        var diretorio = (java.io.File) null;
        var horarioInicio = LocalDateTime.now();

        try {
            var solicitacao = solicitacaoRepository.findById(command.getSolicitacaoId());
            log.info("Iniciando análise para a solicitação: {}", solicitacao.getIdSolicitacaoAnalise());

            solicitacao.iniciarAnalise();
            solicitacaoRepository.save(solicitacao);

            var cloneResult = gitRepositoryCloner.clone(solicitacao);
            if (!cloneResult.ok()) {
                var stringErro = "Erro ao clonar o repositório: ".concat(cloneResult.erro());
                log.error(stringErro);
                solicitacao.finalizarComErro(stringErro);
                solicitacaoRepository.save(solicitacao);
                return;
            }
            diretorio = cloneResult.dir();

            log.info("Diretório clonado: {}", diretorio);

            var projetoSalvo = projetoRepository.findById(command.getProjetoId());

            var repositorio = new Repositorio("Repositorio de Análise", solicitacao.getRepositorioUrl(), projetoSalvo);
            var repositorioSalvo = repositorioRepository.save(repositorio);

            var commitsExtraidos = gitCommitExtractor.extrairCommitsComDiffs(
                    diretorio,
                    solicitacao.getBranch(),
                    solicitacao.getDataInicio(),
                    solicitacao.getDataFim());

            var br = new Branch(solicitacao.getBranch());
            br.setRepositorio(repositorioSalvo);
            br.setDataUltimaAnalise(LocalDateTime.now());
            var branch = branchRepository.save(br);

            var novosCommits = new ArrayList<Commit>();
            for (var commitExtraido : commitsExtraidos) {

                var existente = commitRepository.findByHashAndRepo(commitExtraido.getHash(), solicitacao.getRepositorioUrl());
                if (existente != null) {
                    var existenteCompleto = commitRepository.obterInformacoesCommit(existente.getIdCommit());
                    if (existenteCompleto != null) {
                        existenteCompleto.atribuirBranch(branch);

                        if (existenteCompleto.getArquivosAlterados() != null) {
                            existenteCompleto.getArquivosAlterados()
                                    .forEach(arquivo -> arquivo.atribuirCommit(existenteCompleto));
                        }

                        existenteCompleto.calcularPontuacaoFinal();
                        commitRepository.update(existenteCompleto);
                        novosCommits.add(existenteCompleto);
                        branch.adicionarCommit(existenteCompleto);
                    } else {
                        existente.atribuirBranch(branch);
                        branch.adicionarCommit(existente);
                        novosCommits.add(existente);
                    }
                    continue;
                }

                var commitTemp = new Commit();

                var autor = obterAutor(commitExtraido);
                commitTemp.atribuirAutor(autor);

                commitTemp.atribuirBranch(branch);

                var commitNovo = commitRepository.save(commitTemp);
                var arquivos = commitExtraido.getArquivosAlterados();
                arquivos.forEach(arquivo -> arquivo.atribuirCommit(commitNovo));
                var arquivosAlterados = arquivoAlteradoRepository.saveAll(arquivos);
                commitNovo.setArquivosAlterados(arquivosAlterados);


                commitNovo.calcularPontuacaoFinal();
                commitNovo.adicionarInformacoes(commitExtraido.getMensagem(),
                        commitExtraido.getHash(),
                        commitExtraido.getCommitDate(),
                        commitExtraido.getComplexidadeGeral());
                commitNovo.marcarComoMerge(commitExtraido.ehMerge());
                commitNovo.setTipo(commitExtraido.getTipo());

                if (!codeAnalyzer.isValidCommit(commitNovo)) {
                    commitNovo.setAnalisado(false);
                    commitNovo.calcularPontuacaoFinal();
                    commitRepository.save(commitNovo);
                    novosCommits.add(commitNovo);
                    continue;
                }

                var analises = codeAnalyzer.analyze(commitNovo);
                if (!analises.isEmpty()) {
                    analiseCodigoRepository.saveAll(analises);
                    commitNovo.setAnalisado(true);
                    commitNovo.setArquivosAlterados(arquivosAlterados);
                }

                commitRepository.update(commitNovo);
                novosCommits.add(commitNovo);
            }

            var analiseProjeto = new AnaliseProjeto();
            analiseProjeto.definirTempoAnalise(horarioInicio, LocalDateTime.now());
            analiseProjeto.consolidar(novosCommits, solicitacao, projetoSalvo);
            analiseProjeto.setBranch(branch);
            analiseProjeto.setSolicitacaoAnalise(solicitacao);
            analiseProjeto.setUsuario(solicitacao.getUsuario());
            var analiseSalva = analiseProjetoRepository.save(analiseProjeto);
            branch.adicionarAnalise(analiseSalva);
            branchRepository.save(branch);

            branch.setCommits(novosCommits);
            analiseSalva.setBranch(branch);

            solicitacao.finalizarComSucesso();
            branch.vincularRepositorio(repositorioSalvo);
            branchRepository.save(branch);

            repositorioSalvo.setAnalisado(novosCommits.stream().anyMatch(Commit::isAnalisado));
            repositorioRepository.save(repositorioSalvo);
            solicitacaoRepository.save(solicitacao);

            feedbackAnaliseService.calcularFeedback(analiseSalva);

            log.info("Análise concluída com sucesso para a solicitação: {}", solicitacao.getIdSolicitacaoAnalise());

        } catch (Exception e) {
            log.error("Erro inesperado ao processar a solicitação de análise: {}", e.getMessage(), e);
            var solicitacao = solicitacaoRepository.findById(command.getSolicitacaoId());
            solicitacao.finalizarComErro("Erro inesperado: " + e.getMessage());
            solicitacaoRepository.save(solicitacao);
        } finally {
            if (diretorio != null) {
                diretorio.delete();
                log.info("Diretório temporário removido: {}", diretorio);
            }
            log.info("Processamento da solicitação de análise finalizado.");
        }
    }

    protected Autor obterAutor(Commit commit) {
        return autorRepository.buscarOuCriarPorEmail(commit.getAutor().getNome(), commit.getAutor().getEmail());
    }
}