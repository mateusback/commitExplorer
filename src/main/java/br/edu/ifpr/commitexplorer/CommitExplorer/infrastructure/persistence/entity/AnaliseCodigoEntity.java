package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "analise_codigo")
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
