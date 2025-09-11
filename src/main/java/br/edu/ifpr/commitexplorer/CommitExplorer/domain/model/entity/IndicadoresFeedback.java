package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.IndicadoresAnalise;

public class IndicadoresFeedback {
    private long idIndicadoresFeedback;
    private long diasPeriodo;
    private double semanasPeriodo;

    private double commitsPorSemana;
    private int diasAtivos;
    private double desvioPadraoCommitsPorDia;

    private int totalAutores;
    private int commitsTopAutor;
    private double shareTopAutor;

    private long linhasAdicionadasTotal;
    private long linhasRemovidasTotal;
    private long linhasAlteradasTotal;
    private double linhasPorCommit;

    private int codeSmellsTotal;
    private double codeSmellsPorKLocAlteradas;

    private double complexidadeMedia;
    private double shareTop5Arquivos;

    private int quantidadeMerges;
    private double percentualMerges;


    public IndicadoresFeedback() {
    }

    public IndicadoresFeedback(IndicadoresAnalise dto){
        this.diasPeriodo = dto.getDiasPeriodo();
        this.semanasPeriodo = dto.getSemanasPeriodo();
        this.commitsPorSemana = dto.getCommitsPorSemana();
        this.diasAtivos = dto.getDiasAtivos();
        this.desvioPadraoCommitsPorDia = dto.getDesvioPadraoCommitsPorDia();
        this.totalAutores = dto.getTotalAutores();
        this.commitsTopAutor = dto.getCommitsTopAutor();
        this.shareTopAutor = dto.getShareTopAutor();
        this.linhasAdicionadasTotal = dto.getLinhasAdicionadasTotal();
        this.linhasRemovidasTotal = dto.getLinhasRemovidasTotal();
        this.linhasAlteradasTotal = dto.getLinhasAlteradasTotal();
        this.linhasPorCommit = dto.getLinhasPorCommit();
        this.codeSmellsTotal = dto.getSmellsTotal();
        this.codeSmellsPorKLocAlteradas = dto.getSmellsPorKLocAlteradas();
        this.complexidadeMedia = dto.getComplexidadeMedia();
        this.shareTop5Arquivos = dto.getShareTop5Arquivos();
        this.quantidadeMerges = dto.getQtdMerges();
        this.percentualMerges = dto.getPctMerges();
    }

    // <editor-fold desc="Getters">
    public Long getDiasPeriodo() {
        return diasPeriodo;
    }
    public Double getSemanasPeriodo() {
        return semanasPeriodo;
    }
    public Double getCommitsPorSemana() {
        return commitsPorSemana;
    }
    public Integer getDiasAtivos() {
        return diasAtivos;
    }
    public Double getDesvioPadraoCommitsPorDia() {
        return desvioPadraoCommitsPorDia;
    }
    public Integer getTotalAutores() {
        return totalAutores;
    }
    public Integer getCommitsTopAutor() {
        return commitsTopAutor;
    }
    public Double getShareTopAutor() {
        return shareTopAutor;
    }
    public Long getLinhasAdicionadasTotal() {
        return linhasAdicionadasTotal;
    }
    public Long getLinhasRemovidasTotal() {
        return linhasRemovidasTotal;
    }
    public Long getLinhasAlteradasTotal() {
        return linhasAlteradasTotal;
    }
    public Double getLinhasPorCommit() {
        return linhasPorCommit;
    }
    public Integer getCodeSmellsTotal() {
        return codeSmellsTotal;
    }
    public Double getCodeSmellsPorKLocAlteradas() {
        return codeSmellsPorKLocAlteradas;
    }
    public Double getComplexidadeMedia() {
        return complexidadeMedia;
    }
    public Double getShareTop5Arquivos() {
        return shareTop5Arquivos;
    }
    public Integer getQuantidadeMerges() {
        return quantidadeMerges;
    }
    public Double getPercentualMerges() {
        return percentualMerges;
    }
    public long getIdIndicadoresFeedback() {
        return idIndicadoresFeedback;
    }
    // </editor-fold>

    // <editor-fold desc="Setters">
    public void setDiasPeriodo(Long diasPeriodo) {
        this.diasPeriodo = diasPeriodo;
    }
    public void setSemanasPeriodo(Double semanasPeriodo) {
        this.semanasPeriodo = semanasPeriodo;
    }
    public void setCommitsPorSemana(Double commitsPorSemana) {
        this.commitsPorSemana = commitsPorSemana;
    }
    public void setDiasAtivos(Integer diasAtivos) {
        this.diasAtivos = diasAtivos;
    }
    public void setDesvioPadraoCommitsPorDia(Double desvioPadraoCommitsPorDia) {
        this.desvioPadraoCommitsPorDia = desvioPadraoCommitsPorDia;
    }
    public void setTotalAutores(Integer totalAutores) {
        this.totalAutores = totalAutores;
    }
    public void setCommitsTopAutor(Integer commitsTopAutor) {
        this.commitsTopAutor = commitsTopAutor;
    }
    public void setShareTopAutor(Double shareTopAutor) {
        this.shareTopAutor = shareTopAutor;
    }
    public void setLinhasAdicionadasTotal(Long linhasAdicionadasTotal) {
        this.linhasAdicionadasTotal = linhasAdicionadasTotal;
    }
    public void setLinhasRemovidasTotal(Long linhasRemovidasTotal) {
        this.linhasRemovidasTotal = linhasRemovidasTotal;
    }
    public void setLinhasAlteradasTotal(Long linhasAlteradasTotal) {
        this.linhasAlteradasTotal = linhasAlteradasTotal;
    }
    public void setLinhasPorCommit(Double linhasPorCommit) {
        this.linhasPorCommit = linhasPorCommit;
    }
    public void setCodeSmellsTotal(Integer codeSmellsTotal) {
        this.codeSmellsTotal = codeSmellsTotal;
    }
    public void setCodeSmellsPorKLocAlteradas(Double codeSmellsPorKLocAlteradas) {
        this.codeSmellsPorKLocAlteradas = codeSmellsPorKLocAlteradas;
    }
    public void setComplexidadeMedia(Double complexidadeMedia) {
        this.complexidadeMedia = complexidadeMedia;
    }
    public void setShareTop5Arquivos(Double shareTop5Arquivos) {
        this.shareTop5Arquivos = shareTop5Arquivos;
    }
    public void setQuantidadeMerges(Integer quantidadeMerges) {
        this.quantidadeMerges = quantidadeMerges;
    }
    public void setPercentualMerges(Double percentualMerges) {
        this.percentualMerges = percentualMerges;
    }
    public void setIdIndicadoresFeedback(long idIndicadoresFeedback) {
        this.idIndicadoresFeedback = idIndicadoresFeedback;
    }
    // </editor-fold>
}