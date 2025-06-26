package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.StatusSolicitacao;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SolicitacaoAnalise {

    private Long idSolicitacaoAnalise;
    private LocalDateTime dataSolicitacao = LocalDateTime.now();
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDateTime dataInicioAnalise;
    private LocalDateTime dataFimAnalise;
    private String repositorioUrl;
    private String branch;
    private String projetoUrl;
    private String token;
    private StatusSolicitacao status;
    private String mensagemErro;

    public SolicitacaoAnalise() {
        this.status = StatusSolicitacao.PENDENTE;
    }

    public void registrarNovaSolicitacao(String repositorioUrl, String branch, String projetoUrl, String token,
                                         LocalDate dataInicio, LocalDate dataFim) {
        this.repositorioUrl = repositorioUrl;
        this.branch = branch;
        this.dataSolicitacao = LocalDateTime.now();
        this.status = StatusSolicitacao.PENDENTE;
        this.mensagemErro = null;
        this.projetoUrl = projetoUrl;
        this.token = token;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public void iniciarAnalise() {
        this.dataInicioAnalise = LocalDateTime.now();
        this.status = StatusSolicitacao.EM_ANDAMENTO;
    }

    public void finalizarComSucesso() {
        this.dataFimAnalise = LocalDateTime.now();
        this.status = StatusSolicitacao.CONCLUIDA;
    }

    public void finalizarComErro(String erro) {
        this.dataFimAnalise = LocalDateTime.now();
        this.status = StatusSolicitacao.FALHA;
        this.mensagemErro = erro;
    }

    // <editor-fold desc="Getters">
    public Long getIdSolicitacaoAnalise() {
        return idSolicitacaoAnalise;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public LocalDateTime getDataInicioAnalise() {
        return dataInicioAnalise;
    }

    public LocalDateTime getDataFimAnalise() {
        return dataFimAnalise;
    }

    public String getRepositorioUrl() {
        return repositorioUrl;
    }

    public String getBranch() {
        return branch;
    }

    public String getProjetoUrl() {
        return projetoUrl;
    }

    public String getToken() {
        return token;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }
    // </editor-fold>

    // <editor-fold desc="Setters">
    public void setIdSolicitacaoAnalise(Long idSolicitacaoAnalise) {
        this.idSolicitacaoAnalise = idSolicitacaoAnalise;
    }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }
    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
    public void setDataInicioAnalise(LocalDateTime dataInicioAnalise) {
        this.dataInicioAnalise = dataInicioAnalise;
    }
    public void setDataFimAnalise(LocalDateTime dataFimAnalise) {
        this.dataFimAnalise = dataFimAnalise;
    }
    public void setRepositorioUrl(String repositorioUrl) {
        this.repositorioUrl = repositorioUrl;
    }
    public void setBranch(String branch) {
        this.branch = branch;
    }
    public void setProjetoUrl(String projetoUrl) {
        this.projetoUrl = projetoUrl;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setStatus(StatusSolicitacao status) {
        this.status = status;
    }
    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }
    // </editor-fold>
}
