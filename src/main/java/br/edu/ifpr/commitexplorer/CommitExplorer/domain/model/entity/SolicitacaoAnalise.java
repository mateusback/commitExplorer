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
}
