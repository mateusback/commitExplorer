package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commandHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.AnalisarRepositorioCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.AnalisarRepositorioView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.interfaces.CodeAnalyzerService;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.CommandHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.GitRepositoryCloner;
import org.springframework.stereotype.Component;

@Component
public class AnalisarRepositorioCommandHandler implements CommandHandler<AnalisarRepositorioCommand, AnalisarRepositorioView> {

    private final CodeAnalyzerService codeAnalyzer;
    private final GitRepositoryCloner cloner;

    public AnalisarRepositorioCommandHandler(
            CodeAnalyzerService gitAnalyzerService,
            GitRepositoryCloner cloner) {
        this.codeAnalyzer = gitAnalyzerService;
        this.cloner = cloner;
    }

    @Override
    public AnalisarRepositorioView handle(AnalisarRepositorioCommand command){

        return new AnalisarRepositorioView();
    }
}
