package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.model.User;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoAnalise;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class AnaliseProjeto {
    private Long idAnaliseProjeto;
    private LocalDateTime dataAnalise;
    private Double pontuacaoTotal;
    private Integer totalAutores;
    private Integer totalCommits;
    private Integer quantidadeCodeSmells;
    private Double complexidadeMedia;
    private Integer statusAnalise;
    private Double tempoAnalise;
    private Projeto projeto;
    private Branch branch;
    private SolicitacaoAnalise solicitacaoAnalise;
    private User usuario;
    private List<FeedbackAnalise> feedbacks;

    void vincularSolicitacao(SolicitacaoAnalise solicitacao) {
        this.solicitacaoAnalise = solicitacao;
        this.dataAnalise = LocalDateTime.now();
    }

    void atribuirDados(Integer quantidadeCodeSmells, Integer totalCommits, Integer totalAutores, Float complexidadeMedia) {
        this.quantidadeCodeSmells = quantidadeCodeSmells;
        this.totalCommits = totalCommits;
        this.totalAutores = totalAutores;
        this.complexidadeMedia = complexidadeMedia.doubleValue();
        this.pontuacaoTotal = 10.0;
    }

    public void consolidar(List<Commit> commits, SolicitacaoAnalise solicitacao, Projeto projeto) {
        this.projeto = projeto;
        this.vincularSolicitacao(solicitacao);
        this.statusAnalise = 1;
        this.totalCommits = commits.size();
        this.totalAutores = calcularTotalAutores(commits);
        this.quantidadeCodeSmells = calcularTotalCodeSmells(commits);
        this.complexidadeMedia = calcularMediaComplexidade(commits);
        this.pontuacaoTotal = calcularPontuacaoTotal(commits);
    }

    public void definirTempoAnalise(LocalDateTime inicio, LocalDateTime fim) {
        this.tempoAnalise = (java.time.Duration.between(inicio, fim).toMillis()) / 1000.0;
    }

    private int calcularTotalAutores(List<Commit> commits) {
        return (int) commits.stream()
                .map(c -> c.getAutor().getEmail())
                .filter(Objects::nonNull)
                .distinct()
                .count();
    }

    private int calcularTotalCodeSmells(List<Commit> commits) {
        return (int) commits.stream()
                .flatMap(c -> c.getArquivosAlterados().stream())
                .flatMap(a -> a.getAnalisesCodigo().stream())
                .filter(analise -> analise.getTipo() == TipoAnalise.SMELL)
                .count();
    }

    private double calcularMediaComplexidade(List<Commit> commits) {
        return commits.stream()
                .map(Commit::getComplexidadeGeral)
                .filter(Objects::nonNull)
                .mapToDouble(Integer::doubleValue)
                .average()
                .orElse(1.0);
    }

    private double calcularPontuacaoTotal(List<Commit> commits) {
        return commits.stream()
                .map(Commit::getPontuacao)
                .filter(Objects::nonNull)
                .mapToDouble(Float::doubleValue)
                .average()
                .orElse(100.0);
    }

    // <editor-fold desc="Getters">
    public Long getIdAnaliseProjeto() {
        return idAnaliseProjeto;
    }
    public LocalDateTime getDataAnalise() {
        return dataAnalise;
    }
    public Double getPontuacaoTotal() {
        return pontuacaoTotal;
    }
    public Integer getTotalAutores() {
        return totalAutores;
    }
    public Integer getTotalCommits() {
        return totalCommits;
    }
    public Integer getQuantidadeCodeSmells() {
        return quantidadeCodeSmells;
    }
    public Double getComplexidadeMedia() {
        return complexidadeMedia;
    }
    public Integer getStatusAnalise() {
        return statusAnalise;
    }
    public Double getTempoAnalise() {
        return tempoAnalise;
    }
    public Branch getBranch() {
        return branch;
    }
    public Projeto getProjeto() {
        return projeto;
    }
    public SolicitacaoAnalise getSolicitacaoAnalise() {
        return solicitacaoAnalise;
    }
    public User getUsuario() {
        return usuario;
    }
    public List<FeedbackAnalise> getFeedbacks() {
        return feedbacks;
    }
    // </editor-fold>

    // <editor-fold desc="Setters">
    public void setIdAnaliseProjeto(Long idAnaliseProjeto) {
        this.idAnaliseProjeto = idAnaliseProjeto;
    }
    public void setDataAnalise(LocalDateTime dataAnalise) {
        this.dataAnalise = dataAnalise;
    }
    public void setPontuacaoTotal(Double pontuacaoTotal) {
        this.pontuacaoTotal = pontuacaoTotal;
    }
    public void setTotalAutores(Integer totalAutores) {
        this.totalAutores = totalAutores;
    }
    public void setTotalCommits(Integer totalCommits) {
        this.totalCommits = totalCommits;
    }
    public void setQuantidadeCodeSmells(Integer quantidadeCodeSmells) {
        this.quantidadeCodeSmells = quantidadeCodeSmells;
    }
    public void setComplexidadeMedia(Double complexidadeMedia) {
        this.complexidadeMedia = complexidadeMedia;
    }
    public void setStatusAnalise(Integer statusAnalise) {
        this.statusAnalise = statusAnalise;
    }
    public void setTempoAnalise(Double tempoAnalise) {
        this.tempoAnalise = tempoAnalise;
    }
    public void setBranch(Branch branch) {
        this.branch = branch;
    }
    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }
    public void setSolicitacaoAnalise(SolicitacaoAnalise solicitacaoAnalise) {
        this.solicitacaoAnalise = solicitacaoAnalise;
    }
    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
    public void setFeedbacks(List<FeedbackAnalise> feedbacks) {
        this.feedbacks = feedbacks;
    }
    // </editor-fold>
}
