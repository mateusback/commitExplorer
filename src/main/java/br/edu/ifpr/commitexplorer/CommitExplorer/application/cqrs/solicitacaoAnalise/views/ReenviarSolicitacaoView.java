package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.views;

import lombok.Data;

@Data
public class ReenviarSolicitacaoView {
    private final long id;
    private final String mensagem;

    public ReenviarSolicitacaoView(long id, String mensagem) {
        this.id = id;
        this.mensagem = mensagem;
    }
}
