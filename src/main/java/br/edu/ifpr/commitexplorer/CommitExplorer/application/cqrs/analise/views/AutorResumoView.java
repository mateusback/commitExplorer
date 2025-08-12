package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import lombok.Data;

import java.util.List;

@Data
public class AutorResumoView {
    private Long idAutor;
    private String nome;
    private String email;

    private int totalCommits;
    private int linhasAdicionadas;
    private int linhasRemovidas;
    private int quantidadeCodeSmells;
    private double complexidadeMedia;

    private List<ChartAnalisesView> charts;
    private List<CommitView> commits;
}
