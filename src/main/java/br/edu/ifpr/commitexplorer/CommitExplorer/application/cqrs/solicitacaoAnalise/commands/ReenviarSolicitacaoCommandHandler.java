package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.commands;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.ProcessarSolicitacaoCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.commandHandlers.ReenviarSolicitacaoCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.views.ReenviarSolicitacaoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.service.SolicitacaoAnaliseExecutor;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.CommandHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.StatusSolicitacao;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.SolicitacaoAnaliseRepository;
import org.springframework.stereotype.Component;

@Component
public class ReenviarSolicitacaoCommandHandler implements CommandHandler<ReenviarSolicitacaoCommand, ReenviarSolicitacaoView> {
    private final SolicitacaoAnaliseRepository solicitacaoAnaliseRepository;
    private final SolicitacaoAnaliseExecutor solicitacaoAnaliseExecutor;

    public ReenviarSolicitacaoCommandHandler(
            SolicitacaoAnaliseRepository solicitacaoAnaliseRepository,
            SolicitacaoAnaliseExecutor solicitacaoAnaliseExecutor) {
        this.solicitacaoAnaliseRepository = solicitacaoAnaliseRepository;
        this.solicitacaoAnaliseExecutor = solicitacaoAnaliseExecutor;
    }

    @Override
    public ReenviarSolicitacaoView handle(ReenviarSolicitacaoCommand command) {
        var solicitacao = solicitacaoAnaliseRepository.findById(command.id());

        if (solicitacao.getStatus() == StatusSolicitacao.CONCLUIDA)
            return new ReenviarSolicitacaoView(solicitacao.getIdSolicitacaoAnalise(), "A solicitação já foi concluída e não pode ser reenviada.");

        solicitacao.redefinirParaPendente();
        solicitacaoAnaliseRepository.save(solicitacao);

        var comando = new ProcessarSolicitacaoCommand(solicitacao.getIdSolicitacaoAnalise());

        solicitacaoAnaliseExecutor.processar(comando);

        return new ReenviarSolicitacaoView(solicitacao.getIdSolicitacaoAnalise(), "Solicitação reenviada com sucesso.");
    }
}
