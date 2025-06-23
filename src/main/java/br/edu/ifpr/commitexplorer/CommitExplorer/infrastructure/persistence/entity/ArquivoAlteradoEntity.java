package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoAcao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "arquivo_alterado")
public class ArquivoAlteradoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArquivoAlterado;

    @Enumerated(EnumType.STRING)
    private TipoAcao flgTipoAcao;

    private Integer qtdLinhasAdicionadas;
    private Integer qtdLinhasRemovidas;
    private String nomeArquivo;

    @Lob
    private String conteudoAntes;

    @Lob
    private String conteudoDepois;

    @ManyToOne
    @JoinColumn(name = "id_commit")
    private CommitEntity commit;

    @OneToMany(mappedBy = "arquivoAlterado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnaliseCodigoEntity> analisesCodigo;
}