package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

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
    private Branch branch;
    private SolicitacaoAnalise solicitacaoAnalise;

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

    public void consolidar(List<Commit> commits, SolicitacaoAnalise solicitacao) {
        this.vincularSolicitacao(solicitacao);

        this.totalCommits = commits.size();
        this.totalAutores = calcularTotalAutores(commits);
        this.quantidadeCodeSmells = calcularTotalCodeSmells(commits);
        this.complexidadeMedia = calcularMediaComplexidade(commits);
        this.pontuacaoTotal = calcularPontuacaoTotal(commits);
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
                .map(Commit::getPontuacao)
                .filter(Objects::nonNull)
                .mapToDouble(Float::doubleValue)
                .average()
                .orElse(10.0);
    }

    private double calcularPontuacaoTotal(List<Commit> commits) {
        return calcularMediaComplexidade(commits);
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
    public SolicitacaoAnalise getSolicitacaoAnalise() {
        return solicitacaoAnalise;
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
    public void setSolicitacaoAnalise(SolicitacaoAnalise solicitacaoAnalise) {
        this.solicitacaoAnalise = solicitacaoAnalise;
    }
    // </editor-fold>
}
