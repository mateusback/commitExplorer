package br.edu.ifpr.commitexplorer.CommitExplorer.application.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.response.AnalysisInfo;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;

public interface CodeAnalyzerService {
    List<AnalysisInfo> analyze(DiffEntry diff, RevCommit commit, Repository repo);
}