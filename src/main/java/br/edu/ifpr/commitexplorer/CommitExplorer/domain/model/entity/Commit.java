package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Commit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codCommit;

    private String hash;
    private String mensagem;
    private LocalDateTime commitDate;
    private Float pontuacao;
    private Integer complexidadeGeral;

    @ManyToOne
    @JoinColumn(name = "codBranch")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "codAutor")
    private Autor autor;

    @OneToMany(mappedBy = "commit")
    private List<ArquivoAlterado> arquivosAlterados;
}
