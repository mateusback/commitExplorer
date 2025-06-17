package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "solicitacoes_analise")
public class SolicitacaoAnaliseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitacaoAnalise;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSolicitacao = new Date();

    @OneToOne(mappedBy = "solicitacaoAnalise")
    private AnaliseProjetoEntity analiseProjeto;

    public Long getIdSolicitacaoAnalise() {
        return idSolicitacaoAnalise;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public AnaliseProjetoEntity getAnaliseProjeto() {
        return analiseProjeto;
    }
}
