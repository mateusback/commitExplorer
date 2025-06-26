package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import java.util.List;

public class Repositorio {
    private Long idRepositorio;
    private String nome;
    private String urlRepo;
    private Projeto projeto;
    private List<Branch> branches;

    public Repositorio(String nome, String urlRepo, Projeto projeto) {
        this.nome = nome;
        this.urlRepo = urlRepo;
        this.projeto = projeto;
    }

    public void atribuirProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public void adicionarBranch(Branch branch) {
        if (this.branches == null) {
            this.branches = new java.util.ArrayList<>();
        }
        this.branches.add(branch);
        branch.setRepositorio(this);
    }

    // <editor-fold desc="Getters">
    public Long getIdRepositorio() {
        return idRepositorio;
    }
    public String getNome() {
        return nome;
    }
    public String getUrlRepo() {
        return urlRepo;
    }
    public Projeto getProjeto() {
        return projeto;
    }
    public List<Branch> getBranches() {
        return branches;
    }
    // </editor-fold>

    // <editor-fold desc="Setters">
    public void setIdRepositorio(Long idRepositorio) {
        this.idRepositorio = idRepositorio;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setUrlRepo(String urlRepo) {
        this.urlRepo = urlRepo;
    }
    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }
    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }
    // </editor-fold>
}