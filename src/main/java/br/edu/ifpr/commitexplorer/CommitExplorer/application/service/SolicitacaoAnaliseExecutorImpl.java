package br.edu.ifpr.commitexplorer.CommitExplorer.application.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.ProcessarSolicitacaoCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.*;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.*;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.CodeAnalyzerService;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.GitRepositoryCloner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
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

    public SolicitacaoAnaliseExecutorImpl(CodeAnalyzerService codeAnalyzer,
                                          SolicitacaoAnaliseRepository solicitacaoRepository,
                                          GitRepositoryCloner gitRepositoryCloner,
                                          GitCommitExtractor gitCommitExtractor,
                                          CommitRepository commitRepository,
                                          AutorRepository autorRepository,
                                          AnaliseCodigoRepository analiseCodigoRepository,
                                          ArquivoAlteradoRepository arquivoAlteradoRepository,
                                          BranchRepository branchRepository,
                                          ProjetoRepository projetoRepository,
                                          RepositorioRepository repositorioRepository, AnaliseProjetoRepository analiseProjetoRepository) {
        this.codeAnalyzer = codeAnalyzer;
        this.solicitacaoRepository = solicitacaoRepository;
        this.gitRepositoryCloner = gitRepositoryCloner;
        this.gitCommitExtractor = gitCommitExtractor;
        this.commitRepository = commitRepository;
        this.autorRepository = autorRepository;
        this.analiseCodigoRepository = analiseCodigoRepository;
        this.arquivoAlteradoRepository = arquivoAlteradoRepository;
        this.branchRepository = branchRepository;
        this.projetoRepository = projetoRepository;
        this.repositorioRepository = repositorioRepository;
        this.analiseProjetoRepository = analiseProjetoRepository;
    }

    @Async
    @Transactional
    public void processar(ProcessarSolicitacaoCommand command) {
        var solicitacao = solicitacaoRepository.findById(command.getSolicitacaoId());
        log.info("Iniciando análise para a solicitação: {}", solicitacao.getIdSolicitacaoAnalise());

        solicitacao.iniciarAnalise();
        solicitacaoRepository.save(solicitacao);

        var diretorio = gitRepositoryCloner.clone(
                solicitacao.getRepositorioUrl(),
                solicitacao.getBranch(),
                solicitacao.getToken()
        );
        log.info("Diretório clonado: {}", diretorio);

        var projeto = new Projeto("Projeto de Análise", solicitacao.getProjetoUrl());
        var projetoSalvo = projetoRepository.save(projeto);

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

            if (commitRepository.existsByHashAndRepo(commitExtraido.getHash(), solicitacao.getRepositorioUrl())) {
                continue;
            }

            var commitTemp = new Commit();

            var autor = obterAutor(commitExtraido);
            commitTemp.atribuirAutor(autor);

            commitTemp.atribuirBranch(branch);

            var commitNovo = commitRepository.save(commitTemp);
            branch.adicionarCommit(commitNovo);
            autor.adicionarCommit(commitNovo);
            var arquivos = commitExtraido.getArquivosAlterados();
            arquivos.forEach(arquivo -> arquivo.atribuirCommit(commitNovo));
            var arquivosAlterados = arquivoAlteradoRepository.saveAll(arquivos);
            commitNovo.setArquivosAlterados(arquivosAlterados);


            commitNovo.calcularPontuacaoFinal();
            commitNovo.adicionarInformacoes(commitExtraido.getMensagem(),
                    commitExtraido.getHash(),
                    commitExtraido.getCommitDate(),
                    commitExtraido.getComplexidadeGeral());

            if (!codeAnalyzer.isValidCommit(commitNovo)) {
                commitNovo.calcularPontuacaoFinal();
                commitRepository.save(commitNovo);
                novosCommits.add(commitNovo);
                continue;
            }

            var analises = codeAnalyzer.analyze(commitNovo);
            if (!analises.isEmpty()) {
                analiseCodigoRepository.saveAll(analises);
                commitNovo.setArquivosAlterados(arquivosAlterados);
            }

            commitRepository.update(commitNovo);
            novosCommits.add(commitNovo);
        }

        var analiseProjeto = new AnaliseProjeto();
        analiseProjeto.consolidar(novosCommits, solicitacao, projetoSalvo);
        analiseProjeto.setBranch(branch);
        analiseProjeto.setSolicitacaoAnalise(solicitacao);
        analiseProjetoRepository.save(analiseProjeto);

        branch.adicionarAnalise(analiseProjeto);
        branchRepository.save(branch);
        solicitacao.finalizarComSucesso();

        repositorio.atribuirProjeto(projetoSalvo);
        branch.vincularRepositorio(repositorioSalvo);
        branchRepository.save(branch);

        solicitacaoRepository.save(solicitacao);

        log.info("Análise concluída com sucesso para a solicitação: {}", solicitacao.getIdSolicitacaoAnalise());

        diretorio.delete();
        log.info("Diretório temporário removido: {}", diretorio);

        //TODO - FICA PRA PROXIMA IMPLMENTAÇÃO
        //notificacaoService.enviarEmailConclusao(solicitacao.getProjetoUrl());
    }

    private Autor obterAutor(Commit commit) {
        Autor autor = autorRepository.buscarPorEmail(commit.getAutor().getEmail());
        if (autor == null) {
            autor = new Autor(commit.getAutor().getNome(), commit.getAutor().getEmail());
            autor = autorRepository.save(autor);
        }
        return autor;
    }

}