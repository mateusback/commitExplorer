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
}