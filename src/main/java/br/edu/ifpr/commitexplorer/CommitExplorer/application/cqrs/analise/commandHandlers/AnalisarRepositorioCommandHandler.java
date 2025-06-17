package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commandHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.AnalisarRepositorioCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.AnalisarRepositorioView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.interfaces.CodeAnalyzerService;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.CommandHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.SolicitacaoAnaliseRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.GitRepositoryCloner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AnalisarRepositorioCommandHandler implements CommandHandler<AnalisarRepositorioCommand, AnalisarRepositorioView> {

    private final CodeAnalyzerService codeAnalyzer;
    private final GitRepositoryCloner cloner;
    private final SolicitacaoAnaliseRepository solicitacaoAnaliseRepository;

    public AnalisarRepositorioCommandHandler(
            CodeAnalyzerService gitAnalyzerService,
            GitRepositoryCloner cloner,
            SolicitacaoAnaliseRepository solicitacaoAnaliseRepository
    ) {
        this.codeAnalyzer = gitAnalyzerService;
        this.cloner = cloner;
        this.solicitacaoAnaliseRepository = solicitacaoAnaliseRepository;
    }

    @Override
    public AnalisarRepositorioView handle(AnalisarRepositorioCommand command){

        LocalDate start = LocalDate.of(1990, 1, 20);
        LocalDate end = LocalDate.now();

        var exists = solicitacaoAnaliseRepository.existsInDateRange(start, end);

        return new AnalisarRepositorioView();
    }
}
