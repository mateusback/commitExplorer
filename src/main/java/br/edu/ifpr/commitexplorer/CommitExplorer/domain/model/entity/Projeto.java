package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Projeto {
    private Long idProjeto;
    private String nome;
    private String urlRepo;
    private LocalDateTime dataCriacao;
    private List<Branch> branches;
}