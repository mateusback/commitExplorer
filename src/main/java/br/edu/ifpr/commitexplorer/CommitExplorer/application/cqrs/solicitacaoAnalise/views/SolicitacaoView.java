package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.StatusSolicitacao;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SolicitacaoView {
    public SolicitacaoView(SolicitacaoAnalise sa) {
        nomeProjeto = sa.getNomeProjeto();
        dataSolicitacao = sa.getDataSolicitacao();
        dataInicio = sa.getDataInicio();
        dataFim = sa.getDataFim();
        repositorioUrl = sa.getRepositorioUrl();
        branch = sa.getBranch();
        projetoUrl = sa.getProjetoUrl();
        status = sa.getStatus();
        mensagemErro = sa.getMensagemErro();
    }

    private String nomeProjeto;
    private LocalDateTime dataSolicitacao = LocalDateTime.now();
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String repositorioUrl;
    private String branch;
    private String projetoUrl;
    private StatusSolicitacao status;
    private String mensagemErro;
}
