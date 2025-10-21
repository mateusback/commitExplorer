package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticasView {
    private int totalProjetos;
    private int totalAnalises;
    private int analisesEmAndamento;
    private int analisesFalhadas;
    private int analisesCompletas;
    private int totalCommits;
    private int totalAutores;
    private double pontuacaoMedia;
    private int totalCodeSmells;
}

