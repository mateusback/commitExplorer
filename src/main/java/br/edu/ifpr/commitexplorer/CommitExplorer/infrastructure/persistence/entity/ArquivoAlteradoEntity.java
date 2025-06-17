package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ArquivoAlteradoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArquivoAlterado;

    private Integer flgTipoAcao;
    private Integer qtdLinhasAdicionadas;
    private Integer qtdLinhasRemovidas;
    private String nomeArquivo;

    @ManyToOne
    @JoinColumn(name = "idCommit")
    private CommitEntity commit;

    @OneToMany(mappedBy = "arquivoAlterado")
    private List<AnaliseCodigoEntity> analisesCodigo;
}
