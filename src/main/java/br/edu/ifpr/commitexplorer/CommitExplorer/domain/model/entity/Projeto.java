package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Projeto {
    private Long idProjeto;
    private String nome;
    private String projetoUrl;
    private LocalDateTime dataCriacao;
    private List<Repositorio> repositorios;
    private List<AnaliseProjeto> analises;
    private User usuario;

    public Projeto(String nome, String projetoUrl) {
        this.nome = nome;
        this.projetoUrl = projetoUrl;
        this.dataCriacao = LocalDateTime.now();
    }

    public void inserirRepositorio(Repositorio repositorio) {
        if (this.repositorios == null) {
            this.repositorios = new java.util.ArrayList<>();
        }
        this.repositorios.add(repositorio);
        repositorio.setProjeto(this);
    }

    public void adicionarAnalise(AnaliseProjeto analise) {
        if (this.analises == null) {
            this.analises = new ArrayList<>();
        }
        this.analises.add(analise);
        analise.setProjeto(this);
    }

    public void definirUsuario(User usuario) {
        this.usuario = usuario;
    }


    // <editor-fold desc="Getters">
    public Long getIdProjeto() {
        return idProjeto;
    }
    public String getNome() {
        return nome;
    }
    public String getProjetoUrl() {
        return projetoUrl;
    }
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    public List<Repositorio> getRepositorios() {
        return repositorios;
    }
    public List<AnaliseProjeto> getAnalises() {
        return analises;
    }
    public User getUsuario() {
        return usuario;
    }
    // </editor-fold>

    // <editor-fold desc="Setters">
    public void setIdProjeto(Long idProjeto) {
        this.idProjeto = idProjeto;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setProjetoUrl(String projetoUrl) {
        this.projetoUrl = projetoUrl;
    }
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    public void setRepositorios(List<Repositorio> repositorios) {
        this.repositorios = repositorios;
    }
    public void setAnalises(List<AnaliseProjeto> analises) {
        this.analises = analises;
    }
    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
    // </editor-fold>
}