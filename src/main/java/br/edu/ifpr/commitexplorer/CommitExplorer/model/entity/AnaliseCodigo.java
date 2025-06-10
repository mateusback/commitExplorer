package br.edu.ifpr.commitexplorer.CommitExplorer.model.entity;

import jakarta.persistence.*;

@Entity
public class AnaliseCodigo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codAnaliseCodigo;

    private String descricaoSmell;
    private Integer severidade;
    private Integer pontuacaoNegativa;

    @ManyToOne
    @JoinColumn(name = "codArquivoAlterado")
    private ArquivoAlterado arquivoAlterado;
}