package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Commit {
    private Long idCommit;
    private String hash;
    private String mensagem;
    private LocalDateTime commitDate;
    private Float pontuacao;
    private Integer complexidadeGeral;
    private Branch branch;
    private Autor autor;
    private List<ArquivoAlterado> arquivosAlterados;
}
