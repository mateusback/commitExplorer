package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.queries;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.views.ObterDashboardView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Query;
import lombok.Getter;

@Getter
public class ObterDashboardQuery implements Query<ObterDashboardView> {
    private final Long usuarioId;
    private final boolean ehProfessor;

    public ObterDashboardQuery(Long usuarioId, boolean ehProfessor) {
        this.usuarioId = usuarioId;
        this.ehProfessor = ehProfessor;
    }
}

