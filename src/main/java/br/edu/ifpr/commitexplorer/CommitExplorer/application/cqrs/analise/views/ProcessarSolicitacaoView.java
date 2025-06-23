package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

public class ProcessarSolicitacaoView {
    private final long solicitacaoId;

    public ProcessarSolicitacaoView(long solicitacaoId) {
        this.solicitacaoId = solicitacaoId;
    }

    public long getSolicitacaoId() {
        return solicitacaoId;
    }
}
