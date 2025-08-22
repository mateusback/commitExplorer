package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queryHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries.ObterInformacoesAnaliseQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.*;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.service.IndicadoresAnaliseService;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.service.PontuacaoAnaliseService;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.QueryHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.util.DateUtils;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.AnaliseProjetoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ObterInformacoesAnaliseQueryHandler
        implements QueryHandler<ObterInformacoesAnaliseQuery, ObterInformacoesAnaliseView> {

    private final AnaliseProjetoRepository analiseProjetoRepository;
    private final BranchRepository branchRepository;
    private final IndicadoresAnaliseService indicadoresAnaliseService;
    private final PontuacaoAnaliseService pontuacaoAnaliseService;

    @Override
    @Transactional(readOnly = true)
    public ObterInformacoesAnaliseView handle(ObterInformacoesAnaliseQuery command) {
        var analise = analiseProjetoRepository.findById(command.getId());
        var branch = branchRepository.findById(analise.getBranch().getIdBranch());
        analise.setBranch(branch);

        var commitsNoPeriodo = filtrarCommitsNoPeriodo(analise);

        var geral = montarGeral(analise, commitsNoPeriodo);

        Map<Long, List<Commit>> porAutor = commitsNoPeriodo.stream()
                .filter(c -> c.getAutor() != null && c.getAutor().getIdAutor() != null)
                .collect(Collectors.groupingBy(c -> c.getAutor().getIdAutor()));

        var autoresViews = porAutor.entrySet().stream()
                .map(e -> montarAutorResumo(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingInt(AutorResumoView::getTotalCommits).reversed())
                .toList();

        var porAutorView = autoresViews.stream()
                .collect(Collectors.toMap(AutorResumoView::getIdAutor, Function.identity(), (a,b)->a, LinkedHashMap::new));


        var inicio = analise.getSolicitacaoAnalise().getDataInicio();
        var fim    = analise.getSolicitacaoAnalise().getDataFim();

        var indicadores = indicadoresAnaliseService.calcular(commitsNoPeriodo, inicio, fim);
        var feedback    = pontuacaoAnaliseService.gerarFeedback(geral, indicadores, autoresViews);

        var out = new ObterInformacoesAnaliseView();
        out.setGeral(geral);
        out.setAutores(autoresViews);
        out.setPorAutor(porAutorView);
        out.setFeedback(feedback);
        return out;
    }

    private List<Commit> filtrarCommitsNoPeriodo(AnaliseProjeto analise) {
        var inicio = analise.getSolicitacaoAnalise().getDataInicio().atStartOfDay();
        var fim = analise.getSolicitacaoAnalise().getDataFim().atTime(LocalTime.MAX);
        return analise.getBranch().getCommits().stream()
                .filter(c -> {
                    var dt = c.getCommitDate();
                    return (dt.isEqual(inicio) || dt.isAfter(inicio)) && (dt.isEqual(fim) || dt.isBefore(fim));
                })
                .toList();
    }

    private GeralAnaliseView montarGeral(AnaliseProjeto analise, List<Commit> commits) {
        var v = new GeralAnaliseView();
        v.setId(analise.getIdAnaliseProjeto());
        v.setDataAnalise(analise.getDataAnalise() != null ? analise.getDataAnalise().toString() : null);
        v.setPontuacaoTotal(Optional.ofNullable(analise.getPontuacaoTotal()).orElse(0.0));
        v.setStatusAnalise(String.valueOf(Optional.ofNullable(analise.getStatusAnalise()).orElse(0)));
        v.setTempoAnalise(Optional.ofNullable(analise.getTempoAnalise()).orElse(0.0));
        v.setNomeProjeto(analise.getProjeto().getNome());
        v.setUrlRepositorio(analise.getSolicitacaoAnalise().getRepositorioUrl());
        v.setBranch(analise.getBranch().getNome());
        v.setDataInicio(DateUtils.formatOrNull(analise.getSolicitacaoAnalise().getDataInicio()));
        v.setDataFim(DateUtils.formatOrNull(analise.getSolicitacaoAnalise().getDataFim()));

        v.setTotalCommits(commits.size());
        v.setTotalAutores((int) commits.stream()
                .map(Commit::getAutor)
                .filter(Objects::nonNull)
                .map(Autor::getIdAutor)
                .distinct().count());

        v.setQuantidadeCodeSmells(contarSmells(commits));
        v.setComplexidadeMedia(mediaComplexidade(commits));

        v.setCharts(List.of(construirCharts(commits)));
        v.setCommits(mapUltimosCommits(commits));
        return v;
    }

    private AutorResumoView montarAutorResumo(Long idAutor, List<Commit> commitsAutor) {
        var primeiro = commitsAutor.get(0);
        var autor = primeiro.getAutor();

        var v = new AutorResumoView();
        v.setIdAutor(idAutor);
        v.setNome(autor != null ? autor.getNome() : null);
        v.setEmail(autor != null ? autor.getEmail() : null);

        v.setTotalCommits(commitsAutor.size());
        v.setLinhasAdicionadas(soma(commitsAutor, ArquivoAlterado::getQtdLinhasAdicionadas));
        v.setLinhasRemovidas(soma(commitsAutor, ArquivoAlterado::getQtdLinhasRemovidas));
        v.setQuantidadeCodeSmells(contarSmells(commitsAutor));
        v.setComplexidadeMedia(mediaComplexidade(commitsAutor));

        v.setCharts(List.of(construirCharts(commitsAutor)));
        v.setCommits(mapUltimosCommits(commitsAutor));
        return v;
    }

    private int soma(List<Commit> commits, ToInt<ArquivoAlterado> getter) {
        return commits.stream()
                .flatMap(c -> safeList(c.getArquivosAlterados()).stream())
                .mapToInt(a -> getter.get(a) != null ? getter.get(a) : 0)
                .sum();
    }

    private int contarSmells(List<Commit> commits) {
        return commits.stream()
                .flatMap(c -> safeList(c.getArquivosAlterados()).stream())
                .flatMap(a -> safeList(a.getAnalisesCodigo()).stream())
                .filter(analise -> analise != null && analise.getTipo().equals(TipoAnalise.SMELL))
                .mapToInt(x -> 1)
                .sum();
    }

    private double mediaComplexidade(List<Commit> commits) {
        return commits.stream()
                .map(Commit::getComplexidadeGeral)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average().orElse(0.0);
    }

    private List<CommitView> mapUltimosCommits(List<Commit> commits) {
        return commits.stream()
                .sorted(Comparator.comparing(Commit::getCommitDate).reversed())
                .map(CommitView::from)
                .toList();
    }


    private ChartAnalisesView construirCharts(List<Commit> commits) {
        var chart = new ChartAnalisesView();
        chart.setFrequenciaCommits(chartFreqPorDia(commits));
        chart.setDistribuicaoHorarios(chartPorHora(commits));
        chart.setTopArquivos(chartTopArquivos(commits, 6));
        chart.setTipoCommits(chartTiposCommit(commits));
        return chart;
    }

    private List<FrequenciaCommitChartView> chartFreqPorDia(List<Commit> commits) {
        Map<LocalDate, Long> porDia = commits.stream()
                .collect(Collectors.groupingBy(c -> c.getCommitDate().toLocalDate(),
                        TreeMap::new, Collectors.counting()));

        return porDia.entrySet().stream()
                .map(e -> new FrequenciaCommitChartView(e.getKey().toString(), e.getValue()))
                .toList();
    }

    private List<DistribuicaoHorarioChartView> chartPorHora(List<Commit> commits) {
        var buckets = new int[24];
        commits.forEach(c -> buckets[c.getCommitDate().getHour()]++);
        var out = new ArrayList<DistribuicaoHorarioChartView>(24);
        for (int h = 0; h < 24; h++) {
            out.add(new DistribuicaoHorarioChartView(String.format("%02d:00", h), buckets[h]));
        }
        return out;
    }

    private List<TopArquivoChartView> chartTopArquivos(List<Commit> commits, int topN) {
        Map<String, Integer> porArquivo = commits.stream()
                .flatMap(c -> safeList(c.getArquivosAlterados()).stream())
                .filter(a -> a.obterNomeArquivoFinal() != null && !a.obterNomeArquivoFinal().contains("mvnw"))
                .collect(Collectors.groupingBy(
                        ArquivoAlterado::obterNomeArquivoFinal,
                        Collectors.summingInt(a ->
                                safe(a.getQtdLinhasAdicionadas()) + safe(a.getQtdLinhasRemovidas())
                        )
                ));

        var ordenado = porArquivo.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(topN)
                .toList();

        var out = new ArrayList<TopArquivoChartView>(ordenado.size());
        int rank = 1;
        for (var e : ordenado) {
            out.add(new TopArquivoChartView(rank++, e.getKey(), e.getValue()));
        }
        return out;
    }

    private List<TipoCommitChartView> chartTiposCommit(List<Commit> commits) {
        Map<String, Integer> porTipo = commits.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getTipo() != null ? c.getTipo().toString() : "Outro",
                        TreeMap::new,
                        Collectors.summingInt(x -> 1)
                ));

        var ordenado = porTipo.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .toList();

        var out = new ArrayList<TipoCommitChartView>(ordenado.size());
        int rank = 1;
        for (var e : ordenado) {
            out.add(new TipoCommitChartView(rank++, e.getKey(), e.getValue()));
        }
        return out;
    }

    private static <T> List<T> safeList(List<T> list) {
        return list != null ? list : Collections.emptyList();
    }
    private static int safe(Integer v) { return v != null ? v : 0; }

    @FunctionalInterface
    private interface ToInt<T> { Integer get(T t); }
}
