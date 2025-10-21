package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObterDashboardView {
    private EstatisticasView estatisticas;
    private List<AnaliseRecenteView> analisesRecentes;
    private List<ProjetoPorPontuacaoView> topProjetos;
    private AtividadeTendenciaView atividadeTendencia;
    private VisaoGeralAlunosView visaoGeralAlunos;
}

