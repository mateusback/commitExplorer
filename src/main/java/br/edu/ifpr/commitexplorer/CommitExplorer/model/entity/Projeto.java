package br.edu.ifpr.commitexplorer.CommitExplorer.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Projeto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codProjeto;
    private String nome;
    private String urlRepo;
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "projeto")
    private List<Branch> branches;

    @OneToMany(mappedBy = "projeto")
    private List<ConquistaDesbloqueadaProjeto> conquistasDesbloqueadas;

    // getters e setters
}