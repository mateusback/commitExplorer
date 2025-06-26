package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;

import java.util.List;

public interface AnaliseCodigoRepository {
    List<AnaliseCodigo> saveAll(List<AnaliseCodigo> analises);
}
