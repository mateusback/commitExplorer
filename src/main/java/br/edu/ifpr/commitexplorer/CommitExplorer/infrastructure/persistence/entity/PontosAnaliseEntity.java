package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.enums.TipoPontoAnalise;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "pontos_analise")
public class PontosAnaliseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPontoAnalise;

    @ManyToOne
    @JoinColumn(name = "feedback_analise_id")
    private FeedbackAnaliseEntity feedbackAnalise;

    @Enumerated(EnumType.STRING)
    private TipoPontoAnalise tipo;

    @Column(columnDefinition = "TEXT")
    private String descriacao;
}
