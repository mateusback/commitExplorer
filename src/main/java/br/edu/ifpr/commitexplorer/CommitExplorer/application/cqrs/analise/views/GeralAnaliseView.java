package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.util.DateUtils;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;

@Data
public class GeralAnaliseView {
    private long id;
    private String dataAnalise;
    private String nomeProjeto;
    private String urlRepositorio;
    private String branch;
    private String dataInicio;
    private String dataFim;
    private double pontuacaoTotal;
    private int totalAutores;
    private int totalCommits;
    private int quantidadeCodeSmells;
    private double complexidadeMedia;
    private String statusAnalise;
    private double tempoAnalise;
    private ChartAnalisesView charts;
    private List<CommitView> commits;

    public GeralAnaliseView() {}

    public GeralAnaliseView(AnaliseProjeto analise){
        this.id = analise.getIdAnaliseProjeto();
        this.dataAnalise = DateUtils.formatOrNull(analise.getDataAnalise());
        this.nomeProjeto = analise.getProjeto().getNome();
        this.urlRepositorio = analise.getProjeto().getProjetoUrl();
        this.branch = analise.getBranch().getNome();
        this.dataInicio = DateUtils.formatOrNull(analise.getSolicitacaoAnalise().getDataInicio());
        this.dataFim = DateUtils.formatOrNull(analise.getSolicitacaoAnalise().getDataFim());
        this.pontuacaoTotal = analise.getPontuacaoTotal();
        this.totalAutores = analise.getTotalAutores();
        this.totalCommits = analise.getTotalCommits();
        this.quantidadeCodeSmells = analise.getQuantidadeCodeSmells();
        this.complexidadeMedia = analise.getComplexidadeMedia();
        this.statusAnalise = analise.getStatusAnalise() == 1 ? "Concluída" : "Em andamento";
        this.tempoAnalise = analise.getTempoAnalise();

        this.charts = ChartAnalisesView.of(analise.getBranch().getCommits());

        if (this.totalCommits == 0) {
            this.commits = new ArrayList<>();
            return;
        }

        this.commits = analise.getBranch().getCommits().stream()
                .map(CommitView::from)
                .toList();
    }
}