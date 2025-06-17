package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import java.util.List;

public class Autor {
    private Long idAutor;
    private String name;
    private String email;
    private List<Commit> commits;
}