package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Branch {
    private Long idBranch;
    private String nome;
    private Projeto projeto;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAnalise;
    private List<Commit> commits;
    private List<AnaliseProjeto> analises;

    public Branch(String nome) {
        this.nome = nome;
    }

    // <editor-fold desc="Getters">
    public Long getIdBranch() {
        return idBranch;
    }

    public String getNome() {
        return nome;
    }
    public Projeto getProjeto() {
        return projeto;
    }
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    public LocalDateTime getDataUltimaAnalise() {
        return dataUltimaAnalise;
    }
    public List<Commit> getCommits() {
        return commits;
    }
    public List<AnaliseProjeto> getAnalises() {
        return analises;
    }
    // </editor-fold>
}
