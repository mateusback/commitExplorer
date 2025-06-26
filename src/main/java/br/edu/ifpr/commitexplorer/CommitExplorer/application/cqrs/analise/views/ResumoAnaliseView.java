package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResumoAnaliseView {
    private String nomeProjeto;
    private String urlRepo;
    private String nomeBranch;
    private LocalDateTime dataAnalise;
    private Integer totalCommits;
    private Integer totalAutores;
    private Integer quantidadeCodeSmells;
    private Double complexidadeMedia;
    private Double pontuacaoTotal;
}
