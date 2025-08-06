package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.commit.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import lombok.Data;

@Data
public class InformacoesAnaliseCodigoView {
    private long id;
    private String descricaoSmell;
    private Integer severidade;
    private Integer pontuacaoNegativa;
    private String tipo;

    public InformacoesAnaliseCodigoView(AnaliseCodigo analise){
        this.id = analise.getIdAnaliseCodigo();
        this.descricaoSmell = analise.getDescricaoSmell();
        this.severidade = analise.getSeveridade();
        this.pontuacaoNegativa = analise.getPontuacaoNegativa();
        this.tipo = analise.getTipo().name();
    }
}
