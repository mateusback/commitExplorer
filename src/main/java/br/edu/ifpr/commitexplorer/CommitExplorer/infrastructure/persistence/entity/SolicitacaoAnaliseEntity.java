package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "solicitacao_analise")
public class SolicitacaoAnaliseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitacaoAnalise;

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDateTime dataInicioAnalise;
    private LocalDateTime dataFimAnalise;

    private String repositorioUrl;
    private String branch;
    private String projetoUrl;
    private String token;

    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatusSolicitacao status;

    private String mensagemErro;
}
