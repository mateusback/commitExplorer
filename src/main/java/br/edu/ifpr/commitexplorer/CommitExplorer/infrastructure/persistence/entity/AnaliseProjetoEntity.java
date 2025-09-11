package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "analise_projeto")
public class AnaliseProjetoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAnaliseProjeto;

    private LocalDateTime dataAnalise;
    private Double pontuacaoTotal;
    private Integer totalAutores;
    private Integer totalCommits;
    private Integer quantidadeCodeSmells;
    private Double complexidadeMedia;
    private Integer statusAnalise;
    private Double tempoAnalise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto", nullable = false)
    private ProjetoEntity projeto;

    @ManyToOne
    @JoinColumn(name = "id_branch")
    private BranchEntity branch;

    @OneToOne
    @JoinColumn(name = "id_solicitacao_analise")
    private SolicitacaoAnaliseEntity solicitacaoAnalise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @OneToMany(mappedBy = "analise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedbackAnaliseEntity> feedbacks;
}
