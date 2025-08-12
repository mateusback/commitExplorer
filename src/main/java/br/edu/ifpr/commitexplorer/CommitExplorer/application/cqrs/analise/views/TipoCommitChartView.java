package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TipoCommitChartView {
    private int rank;
    private String tipoCommit;
    private int quantidadeCommits;
}
