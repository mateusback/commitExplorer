package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommitFileAnalysis {
    private String commitHash;
    private String filePath;
    private String author;
    private LocalDateTime date;
    private List<String> violations;
}
