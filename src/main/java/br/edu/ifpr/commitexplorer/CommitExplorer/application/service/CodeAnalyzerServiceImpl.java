package br.edu.ifpr.commitexplorer.CommitExplorer.application.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.interfaces.CodeAnalyzerService;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.util.PMDExecutor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.response.AnalysisInfo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CodeAnalyzerServiceImpl implements CodeAnalyzerService {

    @Override
    public List<AnalysisInfo> analyze(DiffEntry diff, RevCommit commit, Repository repo) {
        try {
            if (diff.getChangeType() == DiffEntry.ChangeType.DELETE || !diff.getNewPath().endsWith(".java")) {
                return Collections.emptyList();
            }

            ObjectId objectId = diff.getNewId().toObjectId();
            ObjectLoader loader = repo.open(objectId);
            String content = new String(loader.getBytes(), StandardCharsets.UTF_8);

            var report = PMDExecutor.analyzeFile(diff.getNewPath(), content);
            var violations = report.getViolations();

            List<String> descriptions = violations.stream()
                    .map(v -> v.getRule().getName() + ": " + v.getDescription())
                    .collect(Collectors.toList());

            return List.of(new AnalysisInfo(
                    diff.getNewPath(),
                    descriptions.size(),
                    descriptions,
                    "High",
                    1.0 - (descriptions.size() / 10.0)
            ));

        } catch (Exception e) {
            log.warn("Erro ao analisar arquivo {} no commit {}", diff.getNewPath(), commit.getName(), e);
            return Collections.emptyList();
        }
    }
}
