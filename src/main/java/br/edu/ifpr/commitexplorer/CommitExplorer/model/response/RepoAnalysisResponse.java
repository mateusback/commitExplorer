package br.edu.ifpr.commitexplorer.CommitExplorer.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class RepoAnalysisResponse {
    private String branchAnalysed;
    private int totalCommits;
    private int totalIssues;
    private int totalAuthors;
    private double averageScore;

    private List<CommitInfo> commits;
    private List<AnalysisInfo> analysis;
    private Map<String, Integer> commitAuthorsCount;
}
