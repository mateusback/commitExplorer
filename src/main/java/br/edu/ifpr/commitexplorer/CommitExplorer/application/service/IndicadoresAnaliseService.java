package br.edu.ifpr.commitexplorer.CommitExplorer.application.service;


import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.IndicadoresAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoAnalise;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class IndicadoresAnaliseService {

    public IndicadoresAnalise calcular(List<Commit> commits, LocalDate ini, LocalDate fim, boolean temCommitsAnalisados) {
        var m = new IndicadoresAnalise();

        m.setDiasPeriodo(Math.max(1, ChronoUnit.DAYS.between(ini, fim) + 1));
        m.setSemanasPeriodo(Math.max(1.0, m.getDiasPeriodo() / 7.0));

        // Commits por dia
        Map<LocalDate, Long> porDia = commits.stream()
                .collect(Collectors.groupingBy(c -> c.getCommitDate().toLocalDate(), TreeMap::new, Collectors.counting()));
        m.setDiasAtivos(porDia.size());
        m.setCommitsPorSemana(commits.size() / m.getSemanasPeriodo());
        m.setDesvioPadraoCommitsPorDia(calcStdDev(porDia.values()));

        // Colaboração
        Map<Long, Long> porAutor = commits.stream()
                .filter(c -> c.getAutor() != null && c.getAutor().getIdAutor() != null)
                .collect(Collectors.groupingBy(c -> c.getAutor().getIdAutor(), Collectors.counting()));
        m.setTotalAutores(porAutor.size());
        int top = porAutor.values().stream().mapToInt(Long::intValue).max().orElse(0);
        m.setCommitsTopAutor(top);
        m.setShareTopAutor(commits.isEmpty() ? 0.0 : (double) top / commits.size());

        // Volume / churn / hotspots / merges
        long add = 0, rem = 0;
        Map<String, Integer> linhasPorArquivo = new HashMap<>();
        int merges = 0;

        for (var c : commits) {
            if (c.getTipo() != null && c.getTipo().toString().toLowerCase().contains("merge")) merges++;
            for (var a : safeList(c.getArquivosAlterados())) {
                int la = safe(a.getQtdLinhasAdicionadas());
                int lr = safe(a.getQtdLinhasRemovidas());
                add += la; rem += lr;
                String nome = a.obterNomeArquivoFinal();
                if (nome != null && !nome.contains("mvnw")) {
                    linhasPorArquivo.merge(nome, la + lr, Integer::sum);
                }
            }
        }
        m.setQtdMerges(merges);
        m.setPctMerges(commits.isEmpty() ? 0.0 : (double) merges / commits.size());

        m.setLinhasAdicionadasTotal(add);
        m.setLinhasRemovidasTotal(rem);
        m.setLinhasAlteradasTotal(add + rem);
        m.setLinhasPorCommit(commits.isEmpty() ? 0.0 : (double) m.getLinhasAlteradasTotal() / commits.size());

        // Qualidade
        int smells = commits.stream()
                .flatMap(c -> safeList(c.getArquivosAlterados()).stream())
                .flatMap(a -> safeList(a.getAnalisesCodigo()).stream())
                .map(x -> x != null ? x.getTipo() : null)
                .mapToInt(t -> t == TipoAnalise.SMELL ? 1 : 0)
                .sum();
        m.setSmellsTotal(smells);
        m.setSmellsPorKLocAlteradas(m.getLinhasAlteradasTotal() == 0 ? 0.0 : (smells * 1000.0) / m.getLinhasAlteradasTotal());

        // Hotspots: share Top 5
        var top5 = linhasPorArquivo.values().stream()
                .sorted(Comparator.reverseOrder())
                .limit(5)
                .mapToInt(Integer::intValue)
                .sum();
        int totalPorArquivo = linhasPorArquivo.values().stream().mapToInt(Integer::intValue).sum();
        m.setShareTop5Arquivos(totalPorArquivo == 0 ? 0.0 : (double) top5 / totalPorArquivo);

        // Complexidade (usa campo do Commit)
        if (temCommitsAnalisados) {
            m.setTemCommitsAnalisados(true);
        } else {
            m.setTemCommitsAnalisados(false);
            return m;
        }

        double complexidadeMedia = commits.stream()
                .map(Commit::getComplexidadeGeral)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average().orElse(0.0);
        m.setComplexidadeMedia(complexidadeMedia);

        return m;
    }

    private static double calcStdDev(Collection<Long> values) {
        if (values.isEmpty()) return 0.0;
        double mean = values.stream().mapToDouble(Long::doubleValue).average().orElse(0.0);
        double var = values.stream().mapToDouble(v -> Math.pow(v - mean, 2)).sum() / values.size();
        return Math.sqrt(var);
    }

    private static <T> List<T> safeList(List<T> list) { return list != null ? list : Collections.emptyList(); }
    private static int safe(Integer v) { return v != null ? v : 0; }
}