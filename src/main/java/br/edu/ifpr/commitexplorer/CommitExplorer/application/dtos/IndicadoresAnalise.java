package br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos;

import lombok.Data;

@Data
public class IndicadoresAnalise {
    // Período
    private long diasPeriodo;
    private double semanasPeriodo;

    // Cadência/Consistência
    private double commitsPorSemana;
    private int diasAtivos;
    private double desvioPadraoCommitsPorDia;

    // Colaboração
    private int totalAutores;
    private int commitsTopAutor;
    private double shareTopAutor;

    // Volume / Churn
    private long linhasAdicionadasTotal;
    private long linhasRemovidasTotal;
    private long linhasAlteradasTotal;
    private double linhasPorCommit;

    // Qualidade
    private int smellsTotal;
    private double smellsPorKLocAlteradas;

    // Complexidade
    private double complexidadeMedia;

    // Hotspots
    private double shareTop5Arquivos;

    // Tipos
    private int qtdMerges;
    private double pctMerges;
}
