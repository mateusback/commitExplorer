package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import java.time.LocalDateTime;

public class AnaliseProjeto {
    private Long idAnaliseProjeto;
    private LocalDateTime dataAnalise;
    private Double pontuacaoTotal;
    private Integer totalAutores;
    private Integer totalCommits;
    private LocalDateTime periodoAnalisadoInicio;
    private LocalDateTime periodoAnalisadoFim;
    private Integer quantidadeCodeSmells;
    private Double complexidadeMedia;
    private Integer statusAnalise;
    private Double tempoAnalise;
    private Branch branch;
    private SolicitacaoAnalise solicitacaoAnalise;
}
