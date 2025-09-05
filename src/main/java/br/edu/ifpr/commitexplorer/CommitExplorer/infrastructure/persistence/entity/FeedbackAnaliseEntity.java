package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoFeedback;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "feedback_analise")
public class FeedbackAnaliseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idFeedbackAnalise;

    @ManyToOne(optional = false)
    @JoinColumn(name = "analise_id")
    private AnaliseProjetoEntity analise;

    @Enumerated(EnumType.STRING)
    private TipoFeedback tipo;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private AutorEntity autor;

    private LocalDateTime periodoInicio;
    private LocalDateTime periodoFim;

    private double pontuacaoGeral;
    private double pontuacaoFrequenciaConsistencia;
    private double pontuacaoQualidadeMensagens;
    private double pontuacaoVariedadeTipos;
    private double pontuacaoDistribuicaoTrabalho;
    private double pontuacaoDistribuicaoTemporal;
    private double pontuacaoQualidadeTecnica;

    private char conceito;
    private LocalDateTime dataCriacao;

    @Column(columnDefinition = "TEXT")
    private String resumo;

    @OneToMany(mappedBy = "feedbackAnalise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PontosAnaliseEntity> pontosPositivos;
    @OneToMany(mappedBy = "feedbackAnalise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PontosAnaliseEntity> pontosNegativos;
    @OneToMany(mappedBy = "feedbackAnalise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PontosAnaliseEntity> sugestoesMelhoria;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "id_indicadores_feedback", nullable = false, unique = true)
    private IndicadoresFeedbackEntity indicadores;
}
