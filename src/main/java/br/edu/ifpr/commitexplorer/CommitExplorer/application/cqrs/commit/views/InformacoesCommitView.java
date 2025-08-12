package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.commit.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import lombok.Data;

import java.util.List;

@Data
public class InformacoesCommitView {
    private long id;
    private String hash;
    private boolean ehMerge;
    private String mensagem;
    private InformacoesAutorView autor;
    private String dataCommit;
    private Float pontuacao;
    private Integer complexidadeGeral;
    private int linhasAdicionadas;
    private int linhasRemovidas;
    private int totalArquivosAlterados;
    private List<InformacoesArquivoAlteradoView> arquivosAlterados;

    public InformacoesCommitView(Commit commit, int linhasAdicionadas, int linhasRemovidas) {
        this.id = commit.getIdCommit();
        this.hash = commit.getHash();
        this.ehMerge = commit.ehMerge();
        this.mensagem = commit.getMensagem();
        this.autor = new InformacoesAutorView(commit.getAutor());
        this.dataCommit = commit.getCommitDate().toString();
        this.pontuacao = commit.getPontuacao();
        this.complexidadeGeral = commit.getComplexidadeGeral();
        this.linhasAdicionadas = linhasAdicionadas;
        this.linhasRemovidas = linhasRemovidas;
        this.totalArquivosAlterados = commit.getArquivosAlterados().size();
        this.arquivosAlterados = new java.util.ArrayList<>();
        for(var arquivo : commit.getArquivosAlterados()) {
            this.arquivosAlterados.add(new InformacoesArquivoAlteradoView(arquivo));
        }
    }
}
