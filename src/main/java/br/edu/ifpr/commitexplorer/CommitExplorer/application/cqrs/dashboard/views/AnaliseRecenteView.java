package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnaliseRecenteView {
    private Long id;
    private String nomeProjeto;
    private String urlRepositorio;
    private String status;
    private Double pontuacao;
    private Integer totalCommits;
    private LocalDateTime dataCriacao;
    private String nomeUsuario;
}

