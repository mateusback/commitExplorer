package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import lombok.Data;

@Data
public class CommitView {
    private long id;
    private String hash;
    private boolean ehMerge;
    private String mensagem;
    private String autor;
    private String dataCommit;
    private Float pontuacao;
    private Integer complexidadeGeral;
    private int linhasAdicionadas;
    private int linhasRemovidas;
    private int totalArquivosAlterados;
}
