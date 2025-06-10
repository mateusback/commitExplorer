package br.edu.ifpr.commitexplorer.CommitExplorer.model.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ConquistaProjeto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codConquistaProjeto;

    private String titulo;
    private String descricao;
    private String tipo;
    private String formaDesbloqueio;

    @OneToMany(mappedBy = "conquistaProjeto")
    private List<ConquistaDesbloqueadaProjeto> desbloqueios;
}