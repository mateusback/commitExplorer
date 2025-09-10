package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoFeedback;

import java.time.LocalDateTime;
import java.util.List;

public class FeedbackAnalise {
    private long idFeedbackAnalise;
    private AnaliseProjeto analiseProjeto;
    private TipoFeedback tipo;
    private Autor autor;
    private LocalDateTime periodoInicio;
    private LocalDateTime periodoFim;
    private double pontuacaoGeral;
    private double pontuacaoFrequenciaConsistencia;
    private double pontuacaoQualidadeMensagens;
    private double pontuacaoVariedadeTipos;
    private double pontuacaoDistribuicaoTrabalho;
    private double pontuacaoDistribuicaoTemporal;
    private Double pontuacaoQualidadeTecnica;
    private char conceito;
    private String resumo;
    private List<String> pontosPositivos;
    private List<String> pontosNegativos;
    private List<String> sugestoesMelhoria;
    private LocalDateTime dataCriacao;
    private IndicadoresFeedback indicadores;

    public FeedbackAnalise() {
    }

    // <editor-fold desc="Getters">
    public long getIdFeedbackAnalise() {
        return idFeedbackAnalise;
    }
    public AnaliseProjeto getAnaliseProjeto() {
        return analiseProjeto;
    }
    public TipoFeedback getTipo() {
        return tipo;
    }
    public Autor getAutor() {
        return autor;
    }
    public LocalDateTime getPeriodoInicio() {
        return periodoInicio;
    }
    public LocalDateTime getPeriodoFim() {
        return periodoFim;
    }
    public double getPontuacaoGeral() {
        return pontuacaoGeral;
    }
    public double getPontuacaoFrequenciaConsistencia() {
        return pontuacaoFrequenciaConsistencia;
    }
    public double getPontuacaoQualidadeMensagens() {
        return pontuacaoQualidadeMensagens;
    }
    public double getPontuacaoVariedadeTipos() {
        return pontuacaoVariedadeTipos;
    }
    public double getPontuacaoDistribuicaoTrabalho() {
        return pontuacaoDistribuicaoTrabalho;
    }
    public double getPontuacaoDistribuicaoTemporal() {
        return pontuacaoDistribuicaoTemporal;
    }
    public Double getPontuacaoQualidadeTecnica() {
        return pontuacaoQualidadeTecnica;
    }
    public char getConceito() {
        return conceito;
    }
    public String getResumo() {
        return resumo;
    }
    public List<String> getPontosPositivos() {
        return pontosPositivos;
    }
    public List<String> getPontosNegativos() {
        return pontosNegativos;
    }
    public List<String> getSugestoesMelhoria() {
        return sugestoesMelhoria;
    }
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    public IndicadoresFeedback getIndicadores() {
        return indicadores;
    }
    // </editor-fold>

    // <editor-fold desc="Setters">
    public void setIdFeedbackAnalise(long idFeedbackAnalise) {
        this.idFeedbackAnalise = idFeedbackAnalise;
    }
    public void setAnaliseProjeto(AnaliseProjeto analiseProjeto) {
        this.analiseProjeto = analiseProjeto;
    }
    public void setTipo(TipoFeedback tipo) {
        this.tipo = tipo;
    }
    public void setAutor(Autor autor) {
        this.autor = autor;
    }
    public void setPeriodoInicio(LocalDateTime periodoInicio) {
        this.periodoInicio = periodoInicio;
    }
    public void setPeriodoFim(LocalDateTime periodoFim) {
        this.periodoFim = periodoFim;
    }
    public void setPontuacaoGeral(double pontuacaoGeral) {
        this.pontuacaoGeral = pontuacaoGeral;
    }
    public void setPontuacaoFrequenciaConsistencia(double pontuacaoFrequenciaConsistencia) {
        this.pontuacaoFrequenciaConsistencia = pontuacaoFrequenciaConsistencia;
    }
    public void setPontuacaoQualidadeMensagens(double pontuacaoQualidadeMensagens) {
        this.pontuacaoQualidadeMensagens = pontuacaoQualidadeMensagens;
    }
    public void setPontuacaoVariedadeTipos(double pontuacaoVariedadeTipos) {
        this.pontuacaoVariedadeTipos = pontuacaoVariedadeTipos;
    }
    public void setPontuacaoDistribuicaoTrabalho(double pontuacaoDistribuicaoTrabalho) {
        this.pontuacaoDistribuicaoTrabalho = pontuacaoDistribuicaoTrabalho;
    }
    public void setPontuacaoDistribuicaoTemporal(double pontuacaoDistribuicaoTemporal) {
        this.pontuacaoDistribuicaoTemporal = pontuacaoDistribuicaoTemporal;
    }
    public void setPontuacaoQualidadeTecnica(Double pontuacaoQualidadeTecnica) {
        this.pontuacaoQualidadeTecnica = pontuacaoQualidadeTecnica;
    }
    public void setConceito(char conceito) {
        this.conceito = conceito;
    }
    public void setResumo(String resumo) {
        this.resumo = resumo;
    }
    public void setPontosPositivos(List<String> pontosPositivos) {
        this.pontosPositivos = pontosPositivos;
    }
    public void setPontosNegativos(List<String> pontosNegativos) {
        this.pontosNegativos = pontosNegativos;
    }
    public void setSugestoesMelhoria(List<String> sugestoesMelhoria) {
        this.sugestoesMelhoria = sugestoesMelhoria;
    }
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    public void setIndicadores(IndicadoresFeedback indicadores) {
        this.indicadores = indicadores;
    }
    // </editor-fold>
}
