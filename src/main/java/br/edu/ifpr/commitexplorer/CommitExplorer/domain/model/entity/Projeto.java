package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Projeto {
    private Long idProjeto;
    private String nome;
    private String urlRepo;
    private LocalDateTime dataCriacao;
    private List<Branch> branches;

    // <editor-fold desc="Getters">
    public Long getIdProjeto() {
        return idProjeto;
    }
    public String getNome() {
        return nome;
    }
    public String getUrlRepo() {
        return urlRepo;
    }
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    public List<Branch> getBranches() {
        return branches;
    }
    // </editor-fold>
}