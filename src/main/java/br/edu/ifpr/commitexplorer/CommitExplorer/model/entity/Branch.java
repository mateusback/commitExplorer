package br.edu.ifpr.commitexplorer.CommitExplorer.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codBranch;

    @ManyToOne
    @JoinColumn(name = "codProjeto")
    private Projeto projeto;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAnalise;

    @OneToMany(mappedBy = "branch")
    private List<Commit> commits;

    @OneToMany(mappedBy = "branch")
    private List<AnaliseProjeto> analises;
}
