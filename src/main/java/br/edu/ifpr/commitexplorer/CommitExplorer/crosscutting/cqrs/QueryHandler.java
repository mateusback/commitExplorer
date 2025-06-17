package br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs;

public interface QueryHandler<C extends Query<TResponse>, TResponse> {
    TResponse handle(C command);
}
