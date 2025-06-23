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

    public void registrarCommit(String mensagem, String hash, LocalDateTime commitDate, List<ArquivoAlterado> arquivosAlterados) {
        this.mensagem = mensagem;
        this.hash = hash;
        this.commitDate = commitDate;
        this.arquivosAlterados = arquivosAlterados;
    }

    public void atribuirAutor(Autor autor) {
        this.autor = autor;
    }

    public void atribuirBranch(Branch branch) {
        this.branch = branch;
    }

    public void adicionarAnalisesCodigo(List<AnaliseCodigo> analises) {
        if (this.arquivosAlterados == null || analises == null) return;

        for (var analise : analises) {
            for (var arquivo : this.arquivosAlterados) {
                if (arquivo.getNomeArquivo().equals(analise.getArquivoAlterado().getNomeArquivo())) {
                    arquivo.adicionarAnalise(analise);
                }
            }
        }
    }

    public void calcularPontuacaoFinal() {
        if (this.arquivosAlterados == null || this.arquivosAlterados.isEmpty()) {
            this.pontuacao = 10.0f;
            return;
        }

        var todasAnalises = this.arquivosAlterados.stream()
                .flatMap(a -> a.getAnalisesCodigo().stream())
                .toList();

        if (todasAnalises.isEmpty()) {
            this.pontuacao = 10.0f;
            return;
        }

        int penalidadeTotal = todasAnalises.stream()
                .mapToInt(a -> a.getPontuacaoNegativa() != null ? a.getPontuacaoNegativa() : 0)
                .sum();

        float scoreFinal = 10.0f - (penalidadeTotal / 10.0f);
        this.pontuacao = Math.max(scoreFinal, 0.0f);
    }

    // <editor-fold desc="Getters">
    public Long getIdCommit() {
        return idCommit;
    }
    public String getHash() {
        return hash;
    }
    public String getMensagem() {
        return mensagem;
    }
    public LocalDateTime getCommitDate() {
        return commitDate;
    }
    public Float getPontuacao() {
        return pontuacao;
    }
    public Integer getComplexidadeGeral() {
        return complexidadeGeral;
    }
    public Branch getBranch() {
        return branch;
    }
    public Autor getAutor() {
        return autor;
    }
    public List<ArquivoAlterado> getArquivosAlterados() {
        return arquivosAlterados;
    }
    // </editor-fold>
}