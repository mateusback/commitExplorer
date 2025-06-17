package br.edu.ifpr.commitexplorer.CommitExplorer.application.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.interfaces.CommitExtractor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.response.CommitInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommitExtractorImpl implements CommitExtractor {

    public List<CommitInfo> extractCommits(Git git, Repository repo) throws Exception {
        List<CommitInfo> commits = new ArrayList<>();
        Iterable<RevCommit> log = git.log().call();

        for (RevCommit commit : log) {
            commits.add(new CommitInfo(
                    commit.getShortMessage(),
                    commit.getAuthorIdent().getName(),
                    commit.getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    commit.getName()
            ));
        }
        return commits;
    }
}
