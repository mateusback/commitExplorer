package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Branch {
    private Long idBranch;
    private String nome;
    private Repositorio repositorio;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAnalise;
    private List<Commit> commits;
    private List<AnaliseProjeto> analises;


    public void aplicarCommits(List<Commit> commits) {
        this.commits = commits;
    }
    public void adicionarCommit(Commit commit) {
        if (this.commits == null) {
            this.commits = new java.util.ArrayList<>();
        }
        this.commits.add(commit);
    }
    public void adicionarAnalise(AnaliseProjeto analise) {
        if (this.analises == null) {
            this.analises = new java.util.ArrayList<>();
        }
        this.analises.add(analise);
    }

    public void vincularRepositorio(Repositorio repositorio) {
        this.repositorio = repositorio;
    }

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
    public Repositorio getRepositorio() {
        return repositorio;
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

    // <editor-fold desc="Setters">
    public void setIdBranch(Long idBranch) {
        this.idBranch = idBranch;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setRepositorio(Repositorio repositorio) {
        this.repositorio = repositorio;
    }
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    public void setDataUltimaAnalise(LocalDateTime dataUltimaAnalise) {
        this.dataUltimaAnalise = dataUltimaAnalise;
    }
    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }
    public void setAnalises(List<AnaliseProjeto> analises) {
        this.analises = analises;
    }
    // </editor-fold>
}
