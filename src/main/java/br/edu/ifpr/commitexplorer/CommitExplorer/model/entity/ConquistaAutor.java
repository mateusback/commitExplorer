package br.edu.ifpr.commitexplorer.CommitExplorer.model.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ConquistaAutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codConquistaAutor;

    private String titulo;
    private String descricao;
    private String tipo;
    private String formaDesbloqueio;

    @OneToMany(mappedBy = "conquistaAutor")
    private List<ConquistaDesbloqueadaAutor> desbloqueios;
}