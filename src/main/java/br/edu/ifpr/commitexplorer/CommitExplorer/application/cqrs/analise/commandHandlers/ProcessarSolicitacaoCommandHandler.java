package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commandHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.ProcessarSolicitacaoCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ProcessarSolicitacaoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.CommandHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.AutorRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.CommitRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.SolicitacaoAnaliseRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.CodeAnalyzerService;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.GitCommitExtractor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.GitRepositoryCloner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Async
@Component
public class ProcessarSolicitacaoCommandHandler implements CommandHandler<ProcessarSolicitacaoCommand, ProcessarSolicitacaoView> {

    private final CodeAnalyzerService codeAnalyzer;
    private final GitRepositoryCloner gitRepositoryCloner;
    private final SolicitacaoAnaliseRepository solicitacaoRepository;
    private final GitCommitExtractor gitCommitExtractor;
    private final CommitRepository commitRepository;
    private final AutorRepository autorRepository;

    public ProcessarSolicitacaoCommandHandler(
            CodeAnalyzerService codeAnalyzer,
            SolicitacaoAnaliseRepository solicitacaoRepository,
            GitRepositoryCloner gitRepositoryCloner,
            GitCommitExtractor gitCommitExtractor,
            CommitRepository commitRepository,
            AutorRepository autorRepository
    ) {
        this.codeAnalyzer = codeAnalyzer;
        this.solicitacaoRepository = solicitacaoRepository;
        this.gitRepositoryCloner = gitRepositoryCloner;
        this.gitCommitExtractor = gitCommitExtractor;
        this.commitRepository = commitRepository;
        this.autorRepository = autorRepository;
    }

    @Override
    public ProcessarSolicitacaoView handle(ProcessarSolicitacaoCommand command) {
        var solicitacao = solicitacaoRepository.findById(command.getSolicitacaoId());

        solicitacao.iniciarAnalise();
        solicitacaoRepository.save(solicitacao);

        var diretorio = gitRepositoryCloner.clone(
                solicitacao.getRepositorioUrl(),
                solicitacao.getBranch(),
                solicitacao.getToken()
        );

        var commits = gitCommitExtractor.extrairCommitsComDiffs(
                diretorio,
                solicitacao.getBranch(),
                solicitacao.getDataInicio(),
                solicitacao.getDataFim());

        for (var commit : commits) {
            if (commitRepository.existsByHashAndRepo(commit.getHash(), solicitacao.getRepositorioUrl())) {
                continue;
            }

            commitRepository.save(commit);

            Autor autor = null;
            autor = autorRepository.buscarPorEmail(commit.getAutor().getEmail());
            if (autor == null) {
                var a = new Autor(commit.getAutor().getName(), commit.getAutor().getEmail());
                autor = autorRepository.save(a);
            }

            if (!codeAnalyzer.isValidCommit(commit)) {
                continue;
            }

            var analises = codeAnalyzer.analyze(commit);
            if (!analises.isEmpty()) {
                commit.adicionarAnalisesCodigo(analises);
            }

            commit.calcularPontuacaoFinal();
            commitRepository.save(commit);
        }

        var analiseProjeto = new AnaliseProjeto();
        analiseProjeto.consolidar(commits, solicitacao);

        solicitacao.finalizarComSucesso();
        solicitacaoRepository.save(solicitacao);

        //TODO - FICA PRA PROXIMA IMPLMENTAÇÃO
        //notificacaoService.enviarEmailConclusao(solicitacao.getProjetoUrl());

        return new ProcessarSolicitacaoView(solicitacao.getIdSolicitacaoAnalise());
    }
}