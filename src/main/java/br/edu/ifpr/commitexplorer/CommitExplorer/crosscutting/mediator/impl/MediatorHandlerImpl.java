package br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Command;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.CommandHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Query;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.QueryHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator.MediatorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MediatorHandlerImpl implements MediatorHandler {

    private final Map<Class<?>, CommandHandler<?, ?>> commandHandlers = new HashMap<>();
    private final Map<Class<?>, QueryHandler<?, ?>> queryHandlers = new HashMap<>();

    @Autowired
    public MediatorHandlerImpl(
            List<CommandHandler<?, ?>> commandHandlersList,
            List<QueryHandler<?, ?>> queryHandlersList
    ) {
        for (CommandHandler<?, ?> handler : commandHandlersList) {
            Class<?> commandType = ResolvableType.forClass(handler.getClass())
                    .as(CommandHandler.class)
                    .getGeneric(0)
                    .resolve();

            if (commandType != null) {
                commandHandlers.put(commandType, handler);
            }
        }

        for (QueryHandler<?, ?> handler : queryHandlersList) {
            Class<?> queryType = ResolvableType.forClass(handler.getClass())
                    .as(QueryHandler.class)
                    .getGeneric(0)
                    .resolve();

            if (queryType != null) {
                queryHandlers.put(queryType, handler);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TResponse> TResponse enviarComando(Command<TResponse> comando) {
        CommandHandler<Command<TResponse>, TResponse> handler =
                (CommandHandler<Command<TResponse>, TResponse>) commandHandlers.get(comando.getClass());

        if (handler == null) {
            throw new IllegalArgumentException("Handler não encontrado para o comando: " + comando.getClass().getName());
        }

        return handler.handle(comando);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TResponse> TResponse enviarConsulta(Query<TResponse> consulta) {
        QueryHandler<Query<TResponse>, TResponse> handler =
                (QueryHandler<Query<TResponse>, TResponse>) queryHandlers.get(consulta.getClass());

        if (handler == null) {
            throw new IllegalArgumentException("Handler não encontrado para a consulta: " + consulta.getClass().getName());
        }

        return handler.handle(consulta);
    }
}
