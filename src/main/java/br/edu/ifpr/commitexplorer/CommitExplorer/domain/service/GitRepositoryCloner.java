package br.edu.ifpr.commitexplorer.CommitExplorer.domain.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.externalService.git.results.CloneResult;

public interface GitRepositoryCloner {
    CloneResult clone(SolicitacaoAnalise solicitacaoAnalise);
}