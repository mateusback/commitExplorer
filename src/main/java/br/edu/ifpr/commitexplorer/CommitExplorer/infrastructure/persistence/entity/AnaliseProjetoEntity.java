package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AnaliseProjetoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Integer quantidadeConquistas;

    @ManyToOne
    @JoinColumn(name = "branch")
    private BranchEntity branch;
}
