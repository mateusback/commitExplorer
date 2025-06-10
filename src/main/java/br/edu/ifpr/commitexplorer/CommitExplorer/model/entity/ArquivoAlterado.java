package br.edu.ifpr.commitexplorer.CommitExplorer.model.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ArquivoAlterado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codArquivoAlterado;

    private Integer flgTipoAcao;
    private Integer qtdLinhasAdicionadas;
    private Integer qtdLinhasRemovidas;
    private String nomeArquivo;

    @ManyToOne
    @JoinColumn(name = "codCommit")
    private Commit commit;

    @OneToMany(mappedBy = "arquivoAlterado")
    private List<AnaliseCodigo> analisesCodigo;
}
