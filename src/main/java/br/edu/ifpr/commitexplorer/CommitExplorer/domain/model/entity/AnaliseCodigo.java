package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

public class AnaliseCodigo {
    private Long idAnaliseCodigo;
    private String descricaoSmell;
    private Integer severidade;
    private Integer pontuacaoNegativa;

    //TODO - AJUSTAR DEPOIS
    private long idArquivoAlterado;

    public Long getIdAnaliseCodigo() {
        return idAnaliseCodigo;
    }

    public void setIdAnaliseCodigo(Long idAnaliseCodigo) {
        this.idAnaliseCodigo = idAnaliseCodigo;
    }

    public String getDescricaoSmell() {
        return descricaoSmell;
    }

    public void setDescricaoSmell(String descricaoSmell) {
        this.descricaoSmell = descricaoSmell;
    }

    public Integer getSeveridade() {
        return severidade;
    }

    public void setSeveridade(Integer severidade) {
        this.severidade = severidade;
    }

    public Integer getPontuacaoNegativa() {
        return pontuacaoNegativa;
    }

    public void setPontuacaoNegativa(Integer pontuacaoNegativa) {
        this.pontuacaoNegativa = pontuacaoNegativa;
    }

    public long getIdArquivoAlterado() {
        return idArquivoAlterado;
    }

    public void setIdArquivoAlterado(long idArquivoAlterado) {
        this.idArquivoAlterado = idArquivoAlterado;
    }
}