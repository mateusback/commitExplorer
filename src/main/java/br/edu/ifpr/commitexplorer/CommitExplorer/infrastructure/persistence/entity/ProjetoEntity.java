package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "projeto")
public class ProjetoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProjeto;
    private String nome;
    private LocalDateTime dataCriacao;
    private String projetoUrl;

    @OneToMany(mappedBy = "projeto")
    private List<RepositorioEntity> repositorios;
}
