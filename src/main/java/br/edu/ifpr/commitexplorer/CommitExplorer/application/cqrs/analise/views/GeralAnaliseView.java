package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import lombok.Data;

import java.util.List;

@Data
public class GeralAnaliseView {
    private long id;
    private String dataAnalise;
    private String nomeProjeto;
    private String urlRepositorio;
    private String branch;
    private String dataInicio;
    private String dataFim;
    private double pontuacaoTotal;
    private int totalAutores;
    private int totalCommits;
    private int quantidadeCodeSmells;
    private double complexidadeMedia;
    private String statusAnalise;
    private double tempoAnalise;
    private List<ChartAnalisesView> charts;
    private List<CommitView> commits;
}