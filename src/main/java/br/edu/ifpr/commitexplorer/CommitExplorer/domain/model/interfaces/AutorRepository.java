package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;

public interface AutorRepository {
    Autor buscarPorEmail(String email);
    Autor buscarPorId(Long id);
    Autor save(Autor autor);
    Autor buscarOuCriarPorEmail(String nome, String email);
}
