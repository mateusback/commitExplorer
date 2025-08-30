package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.model.User;
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
    private String projetoUrl;
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnaliseProjetoEntity> analises;

    @OneToMany(mappedBy = "projeto")
    private List<RepositorioEntity> repositorios;
}
