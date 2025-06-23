package br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Command;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.CommandHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Query;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.QueryHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator.MediatorHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MediatorHandlerImpl implements MediatorHandler {

    private final Map<Class<?>, CommandHandler<?, ?>> commandHandlers = new HashMap<>();
    private final Map<Class<?>, QueryHandler<?, ?>> queryHandlers = new HashMap<>();

    public <T extends CommandHandler<?, ?>> void registerCommandHandler(Class<?> commandClass, T handler) {
        commandHandlers.put(commandClass, handler);
    }

    public <T extends QueryHandler<?, ?>> void registerQueryHandler(Class<?> queryClass, T handler) {
        queryHandlers.put(queryClass, handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TResponse> TResponse enviarComando(Command<TResponse> comando) {
        var handler = (CommandHandler<Command<TResponse>, TResponse>) commandHandlers.get(comando.getClass());
        if (handler == null)
            throw new IllegalArgumentException("Handler não encontrado para: " + comando.getClass().getName());
        return handler.handle(comando);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TResponse> TResponse enviarConsulta(Query<TResponse> consulta) {
        var handler = (QueryHandler<Query<TResponse>, TResponse>) queryHandlers.get(consulta.getClass());
        if (handler == null)
            throw new IllegalArgumentException("Handler não encontrado para: " + consulta.getClass().getName());
        return handler.handle(consulta);
    }
}