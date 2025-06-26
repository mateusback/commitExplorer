package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;

import java.util.List;

public interface ArquivoAlteradoRepository {
    List<ArquivoAlterado> saveAll(List<ArquivoAlterado> arquivosAlterados);
}
