package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "solicitacao_analise")
public class SolicitacaoAnaliseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitacaoAnalise;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSolicitacao = new Date();

    @OneToOne(mappedBy = "solicitacaoAnalise")
    private AnaliseProjetoEntity analiseProjeto;
}
