package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queryHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries.ObterProjetosQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ObterProjetosView;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.repository.UserRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.QueryHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.ProjetoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ObterProjetosQueryHandler implements QueryHandler<ObterProjetosQuery, ObterProjetosView> {

    private final ProjetoRepository repository;
    private final UserRepository userRepository;

    public ObterProjetosQueryHandler(ProjetoRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ObterProjetosView handle(ObterProjetosQuery command) {
        var usuario = userRepository.findByEmail(command.getRequestedByEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        var projetos = repository.findAllByOwnerId(usuario.getId());
        return new ObterProjetosView(projetos);
    }
}
