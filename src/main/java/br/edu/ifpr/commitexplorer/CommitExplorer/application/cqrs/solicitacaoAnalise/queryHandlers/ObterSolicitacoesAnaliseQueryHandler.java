package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.queryHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.queries.ObterSolicitacoesAnaliseQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.views.ObterSolicitacoesAnaliseView;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.repository.UserRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.QueryHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.SolicitacaoAnaliseRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ObterSolicitacoesAnaliseQueryHandler implements QueryHandler<ObterSolicitacoesAnaliseQuery, ObterSolicitacoesAnaliseView> {
    private final SolicitacaoAnaliseRepository solicitacaoAnaliseRepository;
    private final UserRepository userRepository;

    public ObterSolicitacoesAnaliseQueryHandler(
            SolicitacaoAnaliseRepository solicitacaoAnaliseRepository,
            UserRepository userRepository) {
        this.solicitacaoAnaliseRepository = solicitacaoAnaliseRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ObterSolicitacoesAnaliseView handle(ObterSolicitacoesAnaliseQuery command) {
        var usuario = userRepository.findByEmail(command.requestedByEmail());
        if (usuario.isEmpty()) {
            return new ObterSolicitacoesAnaliseView();
        }
        var solicitacoes = solicitacaoAnaliseRepository.obterSolicitacoesAnalisePorSolicitante(usuario.get().getId());

        return new ObterSolicitacoesAnaliseView(solicitacoes);
    }
}
