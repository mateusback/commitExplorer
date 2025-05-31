package br.edu.ifpr.commitexplorer.CommitExplorer.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.model.request.RepoAnalysisRequest;
import br.edu.ifpr.commitexplorer.CommitExplorer.model.response.AnalysisInfo;
import br.edu.ifpr.commitexplorer.CommitExplorer.model.response.CommitInfo;
import br.edu.ifpr.commitexplorer.CommitExplorer.model.response.RepoAnalysisResponse;
import br.edu.ifpr.commitexplorer.CommitExplorer.util.PMDExecutor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class GitAnalyzerService {

    public RepoAnalysisResponse analyzeRepository(RepoAnalysisRequest request) {
        long start = System.nanoTime();
        List<AnalysisInfo> analysisResults = new ArrayList<>();
        List<CommitInfo> commitInfos = new ArrayList<>();
        Map<String, Integer> authorsCount = new HashMap<>();
        int totalIssues = 0;

        try {
            File tempDir = Files.createTempDirectory("repo").toFile();
            Git git = Git.cloneRepository()
                    .setURI(request.getRepoUrl())
                    .setDirectory(tempDir)
                    .call();

            Repository repo = git.getRepository();
            Iterable<RevCommit> commits = git.log().call();
            List<String> commitHashes = new ArrayList<>();

            RevWalk revWalk = new RevWalk(repo);
            DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
            diffFormatter.setRepository(repo);
            diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
            diffFormatter.setDetectRenames(true);

            for (RevCommit commit : commits) {
                RevCommit parent = commit.getParentCount() > 0
                        ? revWalk.parseCommit(commit.getParent(0).getId())
                        : null;

                List<DiffEntry> diffs = parent != null
                        ? diffFormatter.scan(parent.getTree(), commit.getTree())
                        : diffFormatter.scan(new EmptyTreeIterator(), new CanonicalTreeParser(null, repo.newObjectReader(), commit.getTree()));

                String hash = commit.getName();
                String author = commit.getAuthorIdent().getName();
                String message = commit.getShortMessage();
                Date date = commit.getAuthorIdent().getWhen();

                commitHashes.add(hash);
                authorsCount.put(author, authorsCount.getOrDefault(author, 0) + 1);

                commitInfos.add(new CommitInfo(
                        message,
                        author,
                        date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        hash
                ));

                for (DiffEntry diff : diffs) {
                    if (diff.getChangeType() == DiffEntry.ChangeType.DELETE) continue;
                    if (!diff.getNewPath().endsWith(".java")) continue;

                    try {
                        ObjectId objectId = diff.getNewId().toObjectId();
                        ObjectLoader loader = repo.open(objectId);
                        String content = new String(loader.getBytes(), StandardCharsets.UTF_8);

                        var report = PMDExecutor.analyzeFile(diff.getNewPath(), content);

                        var violations = report.getViolations();
                        List<String> descriptions = violations.stream()
                                .map(v -> v.getRule().getName() + ": " + v.getDescription())
                                .toList();

                        totalIssues += descriptions.size();

                        analysisResults.add(new AnalysisInfo(
                                diff.getNewPath(),
                                descriptions.size(),
                                descriptions,
                                "High", // Pode customizar severidade se tiver lógica
                                1.0 - (descriptions.size() / 10.0)
                        ));
                    } catch (Exception e) {
                        log.warn("Erro ao analisar arquivo {} no commit {}", diff.getNewPath(), hash, e);
                    }
                }
            }

            revWalk.close();
            diffFormatter.close();

            return new RepoAnalysisResponse(
                    request.getBranch(),
                    commitHashes.size(),
                    totalIssues,
                    authorsCount.size(),
                    calcularMediaScore(analysisResults),
                    commitInfos,
                    analysisResults,
                    authorsCount
            );

        } catch (Exception e) {
            log.error("Erro ao analisar repositório", e);
            throw new RuntimeException("Falha ao analisar repositório", e);
        } finally {
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            log.info("Tempo de execução da análise do repositório: {} ms", durationMs);
        }
    }

    private double calcularMediaScore(List<AnalysisInfo> analysis) {
        if (analysis.isEmpty()) return 1.0;
        return analysis.stream()
                .mapToDouble(AnalysisInfo::getSemanticScore)
                .average()
                .orElse(1.0);
    }
}