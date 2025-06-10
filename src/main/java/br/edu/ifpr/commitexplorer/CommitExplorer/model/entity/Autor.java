package br.edu.ifpr.commitexplorer.CommitExplorer.model.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codAutor;

    private String name;
    private String email;

    @OneToMany(mappedBy = "autor")
    private List<Commit> commits;

    @OneToMany(mappedBy = "autor")
    private List<ConquistaDesbloqueadaAutor> conquistasDesbloqueadas;
}