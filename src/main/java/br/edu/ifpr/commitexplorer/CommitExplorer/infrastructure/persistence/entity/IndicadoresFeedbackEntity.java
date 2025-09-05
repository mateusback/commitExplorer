package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "indicadores_feedback")
public class IndicadoresFeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idIndicadoresFeedback;

    private long diasPeriodo;
    private double semanasPeriodo;

    private double commitsPorSemana;
    private int diasAtivos;
    private double desvioPadraoCommitsPorDia;

    private int totalAutores;
    private int commitsTopAutor;
    private double shareTopAutor;

    private long linhasAdicionadasTotal;
    private long linhasRemovidasTotal;
    private long linhasAlteradasTotal;
    private double linhasPorCommit;

    private int codeSmellsTotal;
    private double codeSmellsPorKLocAlteradas;

    private double complexidadeMedia;
    private double shareTop5Arquivos;

    private int quantidadeMerges;
    private double percentualMerges;
}
