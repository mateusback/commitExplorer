package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoAnalise;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "analise_codigo")
public class AnaliseCodigoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAnaliseCodigo;

    private String descricaoSmell;
    private Integer severidade;
    private Integer pontuacaoNegativa;

    @Enumerated(EnumType.STRING)
    private TipoAnalise tipo;

    @ManyToOne
    @JoinColumn(name = "id_arquivo_alterado")
    private ArquivoAlteradoEntity arquivoAlterado;
}
