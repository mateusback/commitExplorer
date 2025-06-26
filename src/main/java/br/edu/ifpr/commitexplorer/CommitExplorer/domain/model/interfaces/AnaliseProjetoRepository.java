package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;

import java.util.List;

public interface AnaliseProjetoRepository {
    AnaliseProjeto save(AnaliseProjeto analiseProjeto);
    AnaliseProjeto findById(Long id);
    List<AnaliseProjeto> findAll();
}
