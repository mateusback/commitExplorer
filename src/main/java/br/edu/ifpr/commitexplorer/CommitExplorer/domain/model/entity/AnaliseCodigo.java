package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoAnalise;

public class AnaliseCodigo {
    private Long idAnaliseCodigo;
    private String descricaoSmell;
    private Integer severidade;
    private Integer pontuacaoNegativa;
    private TipoAnalise tipo;
    private ArquivoAlterado arquivoAlterado;

    public void registrarAnaliseBoa(ArquivoAlterado arquivoAlterado) {
        this.descricaoSmell = "Nenhum problema encontrado";
        this.severidade = 0;
        this.pontuacaoNegativa = 0;
        this.tipo = TipoAnalise.INFO;
        this.arquivoAlterado = arquivoAlterado;
    }

    public void registrarAnaliseRuim(String descricaoSmell, Integer severidade, Integer pontuacaoNegativa, ArquivoAlterado arquivoAlterado) {
        this.descricaoSmell = descricaoSmell;
        this.severidade = severidade;
        this.pontuacaoNegativa = pontuacaoNegativa;
        this.tipo = TipoAnalise.SMELL;
        this.arquivoAlterado = arquivoAlterado;
    }

    // <editor-fold desc="Getters">
    public Long getIdAnaliseCodigo() {
        return idAnaliseCodigo;
    }
    public String getDescricaoSmell() {
        return descricaoSmell;
    }
    public Integer getSeveridade() {
        return severidade;
    }
    public Integer getPontuacaoNegativa() {
        return pontuacaoNegativa;
    }
    public TipoAnalise getTipo() {
        return tipo;
    }
    public ArquivoAlterado getArquivoAlterado() {
        return arquivoAlterado;
    }
    // </editor-fold>

    // <editor-fold desc="Setters">
    public void setIdAnaliseCodigo(Long idAnaliseCodigo) {
        this.idAnaliseCodigo = idAnaliseCodigo;
    }
    public void setDescricaoSmell(String descricaoSmell) {
        this.descricaoSmell = descricaoSmell;
    }
    public void setSeveridade(Integer severidade) {
        this.severidade = severidade;
    }
    public void setPontuacaoNegativa(Integer pontuacaoNegativa) {
        this.pontuacaoNegativa = pontuacaoNegativa;
    }
    public void setTipo(TipoAnalise tipo) {
        this.tipo = tipo;
    }
    public void setArquivoAlterado(ArquivoAlterado arquivoAlterado) {
        this.arquivoAlterado = arquivoAlterado;
    }
    // </editor-fold>
}