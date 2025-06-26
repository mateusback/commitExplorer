package br.edu.ifpr.commitexplorer.CommitExplorer.application.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.ProcessarSolicitacaoCommand;

public interface SolicitacaoAnaliseExecutor {
    void processar(ProcessarSolicitacaoCommand command);
}
