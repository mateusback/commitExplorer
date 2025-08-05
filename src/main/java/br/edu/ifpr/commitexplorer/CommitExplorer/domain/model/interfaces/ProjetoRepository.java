package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;

import java.util.List;

public interface ProjetoRepository {
    Projeto save(Projeto projeto);
    List<Projeto> findAll();
}
