package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Repositorio;

public interface RepositorioRepository {
    Repositorio save(Repositorio repositorio);
}
