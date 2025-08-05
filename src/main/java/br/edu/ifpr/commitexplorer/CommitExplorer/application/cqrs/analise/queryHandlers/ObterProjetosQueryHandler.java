package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queryHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries.ObterProjetosQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ObterProjetosView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.QueryHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.ProjetoRepository;
import org.springframework.stereotype.Component;

@Component
public class ObterProjetosQueryHandler implements QueryHandler<ObterProjetosQuery, ObterProjetosView> {

    private final ProjetoRepository repository;

    public ObterProjetosQueryHandler(ProjetoRepository repository) {
        this.repository = repository;
    }

    @Override
    public ObterProjetosView handle(ObterProjetosQuery command) {
        var projetos = repository.findAll();
        return new ObterProjetosView(projetos);
    }
}
