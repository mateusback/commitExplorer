package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoCommit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "commit")
public class CommitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCommit;

    private String hash;
    @Column(columnDefinition = "TEXT")
    private String mensagem;
    private LocalDateTime commitDate;
    private Float pontuacao;
    private Integer complexidadeGeral;
    private boolean ehMerge;
    private boolean analisado;

    @Enumerated(EnumType.STRING)
    private TipoCommit tipo;

    @ManyToOne
    @JoinColumn(name = "id_branch")
    private BranchEntity branch;

    @ManyToOne
    @JoinColumn(name = "id_autor")
    private AutorEntity autor;

    @OneToMany(mappedBy = "commit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArquivoAlteradoEntity> arquivosAlterados;
}
