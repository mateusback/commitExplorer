package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;

public interface AutorRepository {
    public Autor buscarPorEmail(String email);
    public Autor buscarPorId(Long id);
    public Autor save(Autor autor);
}
