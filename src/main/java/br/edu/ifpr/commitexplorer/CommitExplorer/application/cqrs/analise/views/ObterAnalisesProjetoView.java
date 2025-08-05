package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import lombok.Data;

import java.util.List;

@Data
public class ObterAnalisesProjetoView {
    private int totalAnalises;
    private int totalAutores;
    private List<String> branchsAnalizadas;
    private int totalCodeSmells;
    private int totalCommits;
    private float pontuacaoMedia;
    private List<ResumoAnaliseView> analises;
    private List<AutorView> autores;
    private List<CommitView> commits;
}
