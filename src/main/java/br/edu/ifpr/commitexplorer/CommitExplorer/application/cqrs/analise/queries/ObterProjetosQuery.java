package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ObterProjetosView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Query;

public class ObterProjetosQuery implements Query<ObterProjetosView> {
    private final String requestedByEmail;

    public ObterProjetosQuery(String requestedByEmail) {
        this.requestedByEmail = requestedByEmail;
    }

    public String getRequestedByEmail() {
        return requestedByEmail;
    }
}
