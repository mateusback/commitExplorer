package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.FeedbackAnalise;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AutorResumoView {
    private Long idAutor;
    private String nome;
    private String email;

    private int totalCommits;
    private int linhasAdicionadas;
    private int linhasRemovidas;
    private int quantidadeCodeSmells;
    private double complexidadeMedia;

    private ChartAnalisesView charts;
    private List<CommitView> commits;
    private FeedbackDinamicoView feedback;

    public AutorResumoView(FeedbackAnalise feed){
        this.idAutor = feed.getAutor().getIdAutor();
        this.nome = feed.getAutor().getNome();
        this.email = feed.getAutor().getEmail();

        this.totalCommits = feed.getAutor().getCommits().size();
        this.linhasAdicionadas = feed.getIndicadores().getLinhasAdicionadasTotal().intValue();
        this.linhasRemovidas = feed.getIndicadores().getLinhasRemovidasTotal().intValue();
        this.quantidadeCodeSmells = feed.getIndicadores().getCodeSmellsTotal();
        this.complexidadeMedia = feed.getIndicadores().getComplexidadeMedia().intValue();

        this.feedback = new FeedbackDinamicoView(feed);
        this.charts = ChartAnalisesView.of(feed.getAutor().getCommits());

        this.commits = feed.getAutor().getCommits()
                .stream()
                .map(CommitView::from)
                .toList();
    }

}
