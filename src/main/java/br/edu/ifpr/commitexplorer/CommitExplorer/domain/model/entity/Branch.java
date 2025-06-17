package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Branch {
    private Long idBranch;
    private Projeto projeto;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAnalise;
    private List<Commit> commits;
    private List<AnaliseProjeto> analises;
}
