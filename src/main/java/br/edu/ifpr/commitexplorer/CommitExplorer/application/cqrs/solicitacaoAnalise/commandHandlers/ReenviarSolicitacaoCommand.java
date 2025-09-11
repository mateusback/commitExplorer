package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.commandHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.views.ReenviarSolicitacaoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Command;

public record ReenviarSolicitacaoCommand(long id) implements Command<ReenviarSolicitacaoView> { }