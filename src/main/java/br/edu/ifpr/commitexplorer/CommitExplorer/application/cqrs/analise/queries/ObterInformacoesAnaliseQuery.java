package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ObterInformacoesAnaliseView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Query;

public class ObterInformacoesAnaliseQuery implements Query<ObterInformacoesAnaliseView> {

    private final Long id;

    public ObterInformacoesAnaliseQuery(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
