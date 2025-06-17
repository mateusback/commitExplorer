package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class CommitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCommit;

    private String hash;
    private String mensagem;
    private LocalDateTime commitDate;
    private Float pontuacao;
    private Integer complexidadeGeral;

    @ManyToOne
    @JoinColumn(name = "codBranch")
    private BranchEntity branch;

    @ManyToOne
    @JoinColumn(name = "codAutor")
    private AutorEntity autor;

    @OneToMany(mappedBy = "commit")
    private List<ArquivoAlteradoEntity> arquivosAlterados;
}
