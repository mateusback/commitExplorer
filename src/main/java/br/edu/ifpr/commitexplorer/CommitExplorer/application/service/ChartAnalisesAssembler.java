package br.edu.ifpr.commitexplorer.CommitExplorer.application.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.*;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public final class ChartAnalisesAssembler {

    private static final int DEFAULT_TOP_ARQUIVOS = 6;

    private ChartAnalisesAssembler() { }

    public static ChartAnalisesView from(List<Commit> commits) {
        return from(commits, DEFAULT_TOP_ARQUIVOS);
    }

    public static ChartAnalisesView from(List<Commit> commits, int topNArquivos) {
        var safeCommits = safeList(commits);

        if (safeCommits.isEmpty()) {
            return new ChartAnalisesView(
                    List.of(),
                    List.of(),
                    List.of(),
                    horasZeradas()
            );
        }

        var freq = chartFreqPorDia(safeCommits);
        var hora = chartPorHora(safeCommits);
        var top  = chartTopArquivos(safeCommits, topNArquivos);
        var tipo = chartTiposCommit(safeCommits);

        return new ChartAnalisesView(top, tipo, freq, hora);
    }


    private static List<FrequenciaCommitChartView> chartFreqPorDia(List<Commit> commits) {
        Map<LocalDate, Long> porDia = commits.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getCommitDate().toLocalDate(),
                        TreeMap::new,
                        Collectors.counting()
                ));

        return porDia.entrySet().stream()
                .map(e -> new FrequenciaCommitChartView(e.getKey().toString(), e.getValue()))
                .toList();
    }

    private static List<DistribuicaoHorarioChartView> chartPorHora(List<Commit> commits) {
        var buckets = new int[24];
        commits.forEach(c -> buckets[c.getCommitDate().getHour()]++);
        var out = new ArrayList<DistribuicaoHorarioChartView>(24);
        for (int h = 0; h < 24; h++) {
            out.add(new DistribuicaoHorarioChartView(String.format("%02d:00", h), buckets[h]));
        }
        return out;
    }

    private static List<TopArquivoChartView> chartTopArquivos(List<Commit> commits, int topN) {
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

    private static List<TipoCommitChartView> chartTiposCommit(List<Commit> commits) {
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

    private static List<DistribuicaoHorarioChartView> horasZeradas() {
        var out = new ArrayList<DistribuicaoHorarioChartView>(24);
        for (int h = 0; h < 24; h++) {
            out.add(new DistribuicaoHorarioChartView(String.format("%02d:00", h), 0));
        }
        return out;
    }

    private static <T> List<T> safeList(List<T> list) {
        return list == null ? List.of() : list;
    }

    private static int safe(Integer n) {
        return n == null ? 0 : n;
    }
}