package br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs;

public interface CommandHandler<C extends Command<TResponse>, TResponse> {
    TResponse handle(C command);
}
