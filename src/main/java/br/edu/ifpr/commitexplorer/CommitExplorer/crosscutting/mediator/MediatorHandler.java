package br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator;

import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Command;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Query;

public interface MediatorHandler {
    <TResponse> TResponse enviarComando(Command<TResponse> comando);
    <TResponse> TResponse enviarConsulta(Query<TResponse> consulta);
}
