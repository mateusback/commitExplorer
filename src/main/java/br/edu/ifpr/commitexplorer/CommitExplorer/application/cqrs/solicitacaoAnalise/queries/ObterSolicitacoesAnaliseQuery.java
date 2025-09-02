package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.queries;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.views.ObterSolicitacoesAnaliseView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Query;

public record ObterSolicitacoesAnaliseQuery(String requestedByEmail) implements Query<ObterSolicitacoesAnaliseView>{ }