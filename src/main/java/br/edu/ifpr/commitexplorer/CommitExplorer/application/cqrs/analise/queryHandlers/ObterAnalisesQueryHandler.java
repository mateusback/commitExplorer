package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queryHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries.ObterAnalisesQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ObterAnalisesView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.QueryHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.AnaliseProjetoRepository;

public class ObterAnalisesQueryHandler implements QueryHandler<ObterAnalisesQuery, ObterAnalisesView> {

    private final AnaliseProjetoRepository analiseProjetoRepository;

    public ObterAnalisesQueryHandler(AnaliseProjetoRepository analiseProjetoRepository) {
        this.analiseProjetoRepository = analiseProjetoRepository;
    }

    @Override
    public ObterAnalisesView handle(ObterAnalisesQuery command) {
        var analises = analiseProjetoRepository.findAll();
        return new ObterAnalisesView(analises);
    }
}
