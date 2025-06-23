package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class AnaliseProjeto {
    private Long idAnaliseProjeto;
    private LocalDateTime dataAnalise;
    private Double pontuacaoTotal;
    private Integer totalAutores;
    private Integer totalCommits;
    private Integer quantidadeCodeSmells;
    private Double complexidadeMedia;
    private Integer statusAnalise;
    private Double tempoAnalise;
    private Branch branch;
    private SolicitacaoAnalise solicitacaoAnalise;

    void vincularSolicitacao(SolicitacaoAnalise solicitacao) {
        this.solicitacaoAnalise = solicitacao;
        this.dataAnalise = LocalDateTime.now();
    }

    void atribuirDados(Integer quantidadeCodeSmells, Integer totalCommits, Integer totalAutores, Float complexidadeMedia) {
        this.quantidadeCodeSmells = quantidadeCodeSmells;
        this.totalCommits = totalCommits;
        this.totalAutores = totalAutores;
        this.complexidadeMedia = complexidadeMedia.doubleValue();
        this.pontuacaoTotal = 10.0;
    }

    public void consolidar(List<Commit> commits, SolicitacaoAnalise solicitacao) {
        int totalSmells = commits.stream()
                .flatMap(c -> c.getArquivosAlterados().stream())
                .flatMap(a -> a.getAnalisesCodigo().stream())
                .mapToInt(a -> a.getPontuacaoNegativa() != null ? 1 : 0)
                .sum();

        int totalCommits = commits.size();

        int totalAutores = (int) commits.stream()
                .map(c -> c.getAutor().getEmail())
                .distinct()
                .count();

        double mediaScore = commits.stream()
                .map(Commit::getPontuacao)
                .filter(Objects::nonNull)
                .mapToDouble(Float::doubleValue)
                .average()
                .orElse(10.0);

        AnaliseProjeto analise = new AnaliseProjeto();
        analise.vincularSolicitacao(solicitacao);
        analise.atribuirDados(
                totalSmells,
                totalCommits,
                totalAutores,
                (float) mediaScore
        );
    }

    // <editor-fold desc="Getters">
    public Long getIdAnaliseProjeto() {
        return idAnaliseProjeto;
    }
    public LocalDateTime getDataAnalise() {
        return dataAnalise;
    }
    public Double getPontuacaoTotal() {
        return pontuacaoTotal;
    }
    public Integer getTotalAutores() {
        return totalAutores;
    }
    public Integer getTotalCommits() {
        return totalCommits;
    }
    public Integer getQuantidadeCodeSmells() {
        return quantidadeCodeSmells;
    }
    public Double getComplexidadeMedia() {
        return complexidadeMedia;
    }
    public Integer getStatusAnalise() {
        return statusAnalise;
    }
    public Double getTempoAnalise() {
        return tempoAnalise;
    }
    public Branch getBranch() {
        return branch;
    }
    public SolicitacaoAnalise getSolicitacaoAnalise() {
        return solicitacaoAnalise;
    }
    // </editor-fold>
}
