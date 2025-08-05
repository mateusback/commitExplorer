package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import lombok.Data;

@Data
public class AutorView {
    private long id;
    private String nome;
    private String email;
    private Integer totalCommits;
    private Integer totalLinhasAdicionadas;
    private Integer totalLinhasRemovidas;

    public AutorView(long id, String nome, String email, Integer totalCommits, Integer totalLinhasAdicionadas, Integer totalLinhasRemovidas) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.totalCommits = totalCommits;
        this.totalLinhasAdicionadas = totalLinhasAdicionadas;
        this.totalLinhasRemovidas = totalLinhasRemovidas;
    }
}
