package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;

public interface ProjetoRepository {
    Projeto save(Projeto projeto);
}
