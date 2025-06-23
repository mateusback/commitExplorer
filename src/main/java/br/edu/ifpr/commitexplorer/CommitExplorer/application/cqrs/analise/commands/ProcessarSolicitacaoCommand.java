package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ProcessarSolicitacaoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Command;
import lombok.Getter;

@Getter
public class ProcessarSolicitacaoCommand implements Command<ProcessarSolicitacaoView> {
    private final long solicitacaoId;

    public ProcessarSolicitacaoCommand(long solicitacaoId) {
        this.solicitacaoId = solicitacaoId;
    }

    public long getSolicitacaoId() {
        return solicitacaoId;
    }
}
