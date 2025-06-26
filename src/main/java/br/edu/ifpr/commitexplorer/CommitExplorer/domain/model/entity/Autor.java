package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import java.util.List;

public class Autor {
    private Long idAutor;
    private String name;
    private String email;
    private List<Commit> commits;

    public Autor(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void adicionarCommit(Commit commit) {
        if (this.commits == null) {
            this.commits = new java.util.ArrayList<>();
        }
        this.commits.add(commit);
    }

    // <editor-fold desc="Getters">
    public Long getIdAutor() {
        return idAutor;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public List<Commit> getCommits() {
        return commits;
    }
    // </editor-fold>

    // <editor-fold desc="Setters">
    public void setIdAutor(Long idAutor) {
        this.idAutor = idAutor;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }
    // </editor-fold>
}