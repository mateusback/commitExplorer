package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

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
    private String mensagem;
    private LocalDateTime commitDate;
    private Float pontuacao;
    private Integer complexidadeGeral;

    @ManyToOne
    @JoinColumn(name = "id_branch")
    private BranchEntity branch;

    @ManyToOne
    @JoinColumn(name = "id_autor")
    private AutorEntity autor;

    @OneToMany(mappedBy = "commit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArquivoAlteradoEntity> arquivosAlterados;
}
