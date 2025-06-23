package br.edu.ifpr.commitexplorer.CommitExplorer.domain.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.response.AnalysisInfo;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;

public interface CodeAnalyzerService {
    List<AnaliseCodigo> analyze(Commit commit);
    boolean isValidCommit(Commit commit);
}
