package br.edu.ifpr.commitexplorer.CommitExplorer.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.model.request.RepoAnalysisRequest;
import br.edu.ifpr.commitexplorer.CommitExplorer.model.response.AnalysisInfo;
import br.edu.ifpr.commitexplorer.CommitExplorer.model.response.CommitInfo;
import br.edu.ifpr.commitexplorer.CommitExplorer.model.response.RepoAnalysisResponse;
import br.edu.ifpr.commitexplorer.CommitExplorer.util.PMDExecutor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.io.File;
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

            Iterable<RevCommit> commits = git.log().call();
            List<String> commitHashes = new ArrayList<>();

            for (RevCommit commit : commits) {
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

                String filePath = "Exemplo.java";
                String content = """
                        public class Exemplo {
                            public Exemplo() {}
                        }
                        """;

                var report = PMDExecutor.analyzeFile(filePath, content);

                var violations = report.getViolations();
                List<String> descriptions = violations.stream()
                        .map(v -> v.getRule().getName() + ": " + v.getDescription())
                        .toList();

                totalIssues += descriptions.size();

                analysisResults.add(new AnalysisInfo(
                        message,
                        descriptions.size(),
                        descriptions,
                        "High",
                        1.0 - (descriptions.size() / 10.0)
                ));
            }

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