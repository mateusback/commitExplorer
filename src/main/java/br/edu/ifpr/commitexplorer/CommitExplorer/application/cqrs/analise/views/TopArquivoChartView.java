package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopArquivoChartView {
    private int rank;
    private String nomeArquivo;
    private int totalAlteracoes;
}
