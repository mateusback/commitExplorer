package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
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
    @JoinColumn(name = "idBranch")
    private BranchEntity branch;

    @ManyToOne
    @JoinColumn(name = "idAutor")
    private AutorEntity autor;

    @OneToMany(mappedBy = "commit")
    private List<ArquivoAlteradoEntity> arquivosAlterados;
}
