    package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

    import lombok.AllArgsConstructor;
    import lombok.Data;

    @Data
    @AllArgsConstructor
    public class DistribuicaoHorarioChartView {
        private String horario;
        private int totalCommits;
    }
