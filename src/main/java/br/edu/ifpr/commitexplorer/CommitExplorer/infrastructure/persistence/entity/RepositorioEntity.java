package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "repositorio")
public class RepositorioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRepositorio;
    private String nome;
    private String urlRepo;

    @ManyToOne
    @JoinColumn(name = "id_projeto", nullable = false)
    private ProjetoEntity projeto;

    @OneToMany(mappedBy = "repositorio")
    private List<BranchEntity> branches;
}
