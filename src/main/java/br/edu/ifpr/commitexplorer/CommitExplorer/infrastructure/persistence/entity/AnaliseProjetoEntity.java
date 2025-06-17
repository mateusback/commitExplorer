package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "analise_projeto")
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

    @ManyToOne
    @JoinColumn(name = "id_branch")
    private BranchEntity branch;

    @OneToOne
    @JoinColumn(name = "id_solicitacao_analise")
    private SolicitacaoAnaliseEntity solicitacaoAnalise;
}
