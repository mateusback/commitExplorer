package br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator;

import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.CommandHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.QueryHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator.impl.MediatorHandlerImpl;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MediatorRegistry {
    public MediatorRegistry(
            MediatorHandlerImpl mediator,
            List<CommandHandler<?, ?>> commandHandlers,
            List<QueryHandler<?, ?>> queryHandlers
    ) {
        for (CommandHandler<?, ?> handler : commandHandlers) {
            Class<?> commandType = ResolvableType.forClass(handler.getClass())
                    .as(CommandHandler.class)
                    .getGeneric(0)
                    .resolve();
            if (commandType != null) {
                mediator.registerCommandHandler(commandType, handler);
            }
        }

        for (QueryHandler<?, ?> handler : queryHandlers) {
            Class<?> queryType = ResolvableType.forClass(handler.getClass())
                    .as(QueryHandler.class)
                    .getGeneric(0)
                    .resolve();
            if (queryType != null) {
                mediator.registerQueryHandler(queryType, handler);
            }
        }
    }
}