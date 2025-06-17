package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class BranchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBranch;

    @ManyToOne
    @JoinColumn(name = "codProjeto")
    private Projeto projeto;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAnalise;

    @OneToMany(mappedBy = "branch")
    private List<CommitEntity> commits;

    @OneToMany(mappedBy = "branch")
    private List<AnaliseProjetoEntity> analises;
}
