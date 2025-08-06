package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.commit.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoAcao;
import lombok.Data;

import java.util.List;

@Data
public class InformacoesArquivoAlteradoView {
    private Long id;
    private TipoAcao flgTipoAcao;
    private Integer qtdLinhasAdicionadas;
    private Integer qtdLinhasRemovidas;
    private String nomeArquivo;
    private String conteudoAntes;
    private String conteudoDepois;
    private List<InformacoesAnaliseCodigoView> analisesCodigos;

    public InformacoesArquivoAlteradoView(ArquivoAlterado arquivoAlterado) {
        this.id = arquivoAlterado.getIdArquivoAlterado();
        this.flgTipoAcao = arquivoAlterado.getFlgTipoAcao();
        this.qtdLinhasAdicionadas = arquivoAlterado.getQtdLinhasAdicionadas();
        this.qtdLinhasRemovidas = arquivoAlterado.getQtdLinhasRemovidas();
        this.nomeArquivo = arquivoAlterado.getNomeArquivo();
        this.conteudoAntes = arquivoAlterado.getConteudoAntes();
        this.conteudoDepois = arquivoAlterado.getConteudoDepois();
        this.analisesCodigos = new java.util.ArrayList<>();
        for (var analise : arquivoAlterado.getAnalisesCodigo()) {
            this.analisesCodigos.add(new InformacoesAnaliseCodigoView(analise));
        }
    }
}
