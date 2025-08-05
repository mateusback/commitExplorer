package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ObterAnalisesProjetoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Query;
import lombok.Getter;

@Getter
public class ObterAnalisesProjetoQuery implements Query<ObterAnalisesProjetoView> {
    private final long projetoId;

    public ObterAnalisesProjetoQuery(long projetoId) {
        this.projetoId = projetoId;
    }
}
