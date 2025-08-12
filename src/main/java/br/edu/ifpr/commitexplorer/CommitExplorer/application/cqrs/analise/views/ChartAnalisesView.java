package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import lombok.Data;

import java.util.List;

@Data
public class ChartAnalisesView {
    private List<TopArquivoChartView> topArquivos;
    private List<TipoCommitChartView> tipoCommits;
    private List<FrequenciaCommitChartView> frequenciaCommits;
    private List<DistribuicaoHorarioChartView> distribuicaoHorarios;
}
