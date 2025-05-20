package br.edu.ifpr.commitexplorer.CommitExplorer.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommitInfo {
    private String message;
    private String author;
    private LocalDateTime date;
    private String hash;
}