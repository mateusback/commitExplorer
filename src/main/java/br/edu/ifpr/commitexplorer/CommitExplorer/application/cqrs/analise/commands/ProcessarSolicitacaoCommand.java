package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ProcessarSolicitacaoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Command;
import lombok.Getter;

@Getter
public class ProcessarSolicitacaoCommand implements Command<ProcessarSolicitacaoView> {
    private final long solicitacaoId;
    private final long projetoId;


    public ProcessarSolicitacaoCommand(long solicitacaoId, long projetoId) {
        this.solicitacaoId = solicitacaoId;
        this.projetoId = projetoId;
    }
}
