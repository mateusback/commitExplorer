package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
public class AnaliseCodigoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAnaliseCodigo;

    private String descricaoSmell;
    private Integer severidade;
    private Integer pontuacaoNegativa;

    @ManyToOne
    @JoinColumn(name = "idArquivoAlterado")
    private ArquivoAlteradoEntity arquivoAlterado;
}
