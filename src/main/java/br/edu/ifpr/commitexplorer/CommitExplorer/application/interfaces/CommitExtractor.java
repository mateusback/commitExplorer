package br.edu.ifpr.commitexplorer.CommitExplorer.application.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.response.CommitInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public interface CommitExtractor {

    public List<CommitInfo> extractCommits(Git git, Repository repo) throws Exception;
}
