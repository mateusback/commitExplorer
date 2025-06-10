package br.edu.ifpr.commitexplorer.CommitExplorer.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ConquistaDesbloqueadaAutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codConquistaDesbloqueadaAutor;

    private LocalDateTime dtaDesbloqueio;

    @ManyToOne
    @JoinColumn(name = "codConquistaAutor")
    private ConquistaAutor conquistaAutor;

    @ManyToOne
    @JoinColumn(name = "codAutor")
    private Autor autor;
}