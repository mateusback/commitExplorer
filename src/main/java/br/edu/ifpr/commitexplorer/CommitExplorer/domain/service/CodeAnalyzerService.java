package br.edu.ifpr.commitexplorer.CommitExplorer.domain.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;

import java.util.List;

public interface CodeAnalyzerService {
    List<AnaliseCodigo> analyze(Commit commit);
    boolean isValidCommit(Commit commit);
}
