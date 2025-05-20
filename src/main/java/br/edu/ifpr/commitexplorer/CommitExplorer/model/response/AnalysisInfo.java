package br.edu.ifpr.commitexplorer.CommitExplorer.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AnalysisInfo {
    private String commitMessage;
    private int issuesFound;
    private List<String> issueDescriptions;
    private String severity;
    private double semanticScore;
}
