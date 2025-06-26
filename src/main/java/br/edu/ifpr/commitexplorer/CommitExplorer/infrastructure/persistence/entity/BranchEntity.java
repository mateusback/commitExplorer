package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "branch")
public class BranchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBranch;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_repositorio")
    private RepositorioEntity repositorio;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAnalise;

    @OneToMany(mappedBy = "branch")
    private List<CommitEntity> commits;

    @OneToMany(mappedBy = "branch")
    private List<AnaliseProjetoEntity> analises;
}
