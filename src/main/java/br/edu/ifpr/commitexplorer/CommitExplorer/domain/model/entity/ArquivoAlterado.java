package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoAcao;

import java.util.ArrayList;
import java.util.List;

public class ArquivoAlterado {
    private Long idArquivoAlterado;
    private TipoAcao flgTipoAcao;
    private Integer qtdLinhasAdicionadas;
    private Integer qtdLinhasRemovidas;
    private String nomeArquivo;
    private Commit commit;
    private String conteudoAntes;
    private String conteudoDepois;
    private List<AnaliseCodigo> analisesCodigo = new ArrayList<>();

    public void atribuirNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public void adicionarAlteracoes(TipoAcao flgTipoAcao, String conteudoAntes, String conteudoDepois, Integer qtdLinhasAdicionadas, Integer qtdLinhasRemovidas) {
        this.flgTipoAcao = flgTipoAcao;
        this.conteudoAntes = conteudoAntes;
        this.conteudoDepois = conteudoDepois;
        this.qtdLinhasAdicionadas = qtdLinhasAdicionadas;
        this.qtdLinhasRemovidas = qtdLinhasRemovidas;
    }

    public void adicionarAnalise(AnaliseCodigo analise) {
        if (this.analisesCodigo == null) {
            this.analisesCodigo = new java.util.ArrayList<>();
        }
        this.analisesCodigo.add(analise);
    }

    public void atribuirCommit(Commit commit) {
        this.commit = commit;
    }

    // <editor-fold desc="Getters">
    public Long getIdArquivoAlterado() {
        return idArquivoAlterado;
    }
    public TipoAcao getFlgTipoAcao() {
        return flgTipoAcao;
    }
    public Integer getQtdLinhasAdicionadas() {
        return qtdLinhasAdicionadas;
    }
    public Integer getQtdLinhasRemovidas() {
        return qtdLinhasRemovidas;
    }
    public String getNomeArquivo() {
        return nomeArquivo;
    }
    public Commit getCommit() {
        return commit;
    }
    public String getConteudoAntes() {
        return conteudoAntes;
    }
    public String getConteudoDepois() {
        return conteudoDepois;
    }
    public List<AnaliseCodigo> getAnalisesCodigo() {
        return analisesCodigo;
    }
    // </editor-fold>

    // <editor-fold desc="Setters">
    public void setIdArquivoAlterado(Long idArquivoAlterado) {
        this.idArquivoAlterado = idArquivoAlterado;
    }
    public void setFlgTipoAcao(TipoAcao flgTipoAcao) {
        this.flgTipoAcao = flgTipoAcao;
    }
    public void setQtdLinhasAdicionadas(Integer qtdLinhasAdicionadas) {
        this.qtdLinhasAdicionadas = qtdLinhasAdicionadas;
    }
    public void setQtdLinhasRemovidas(Integer qtdLinhasRemovidas) {
        this.qtdLinhasRemovidas = qtdLinhasRemovidas;
    }
    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
    public void setCommit(Commit commit) {
        this.commit = commit;
    }
    public void setConteudoAntes(String conteudoAntes) {
        this.conteudoAntes = conteudoAntes;
    }
    public void setConteudoDepois(String conteudoDepois) {
        this.conteudoDepois = conteudoDepois;
    }
    public void setAnalisesCodigo(List<AnaliseCodigo> analisesCodigo) {
        this.analisesCodigo = analisesCodigo;
    }
    // </editor-fold>
}
