package br.edu.ifpr.commitexplorer.CommitExplorer.application.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoCommit;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class AvaliadorAcademicoService {

    private static final Pattern PATTERN_COMMIT_DESCRITIVO = Pattern.compile(
        "^(feat|fix|docs|style|refactor|test|chore|perf|ci|build|revert)[:(.].{8,}$|.{15,}"
    );

    private static final Pattern PATTERN_COMMIT_MUITO_CURTO = Pattern.compile("^.{1,8}$");
    private static final Pattern PATTERN_COMMIT_GENERICO = Pattern.compile(
        "(?i)^(update|fix|change|add|remove|test|wip|merge|commit)\\s*\\.?$"
    );

    public IndicadoresAnaliseAcademicos calcularIndicadoresAcademicos(List<Commit> commits, 
                                                                     LocalDate inicio, 
                                                                     LocalDate fim) {
        var indicadores = new IndicadoresAnaliseAcademicos();
        
        if (commits.isEmpty()) return indicadores;

        // Cálculos básicos de tempo
        long diasPeriodo = ChronoUnit.DAYS.between(inicio, fim) + 1;
        indicadores.setDiasPeriodo(diasPeriodo);
        indicadores.setSemanasPeriodo(diasPeriodo / 7.0);

        calcularFrequenciaConsistencia(commits, indicadores, inicio, fim);

        calcularDistribuicaoTrabalho(commits, indicadores);

        calcularQualidadeMensagens(commits, indicadores);

        calcularVariedadeTipos(commits, indicadores);

        calcularDistribuicaoTemporal(commits, indicadores);

        return indicadores;
    }

    private void calcularFrequenciaConsistencia(List<Commit> commits, 
                                              IndicadoresAnaliseAcademicos indicadores,
                                              LocalDate inicio, LocalDate fim) {
        // Frequência básica
        double commitsPorSemana = commits.size() / indicadores.getSemanasPeriodo();
        indicadores.setCommitsPorSemana(commitsPorSemana);

        // Consistência - dias com commits vs total de dias
        Map<LocalDate, Long> commitsPorDia = commits.stream()
            .collect(Collectors.groupingBy(
                c -> c.getCommitDate().toLocalDate(),
                Collectors.counting()
            ));

        int diasAtivos = commitsPorDia.size();
        indicadores.setDiasAtivos(diasAtivos);
        indicadores.setConsistenciaTemporal((double) diasAtivos / indicadores.getDiasPeriodo());

        // Regularidade - desvio padrão dos commits por dia ativo
        double mediaCommitsPorDiaAtivo = commits.size() / (double) diasAtivos;
        double variancia = commitsPorDia.values().stream()
            .mapToDouble(count -> Math.pow(count - mediaCommitsPorDiaAtivo, 2))
            .average().orElse(0.0);
        indicadores.setRegularidadeCommits(Math.sqrt(variancia));

        // Intervalos máximos sem commits
        calcularIntervalosSemCommits(commitsPorDia, indicadores, inicio, fim);
    }

    private void calcularIntervalosSemCommits(Map<LocalDate, Long> commitsPorDia,
                                            IndicadoresAnaliseAcademicos indicadores,
                                            LocalDate inicio, LocalDate fim) {
        int maxDiasSemCommit = 0;
        int diasAtualSemCommit = 0;

        for (LocalDate data = inicio; !data.isAfter(fim); data = data.plusDays(1)) {
            if (commitsPorDia.containsKey(data)) {
                maxDiasSemCommit = Math.max(maxDiasSemCommit, diasAtualSemCommit);
                diasAtualSemCommit = 0;
            } else {
                diasAtualSemCommit++;
            }
        }
        maxDiasSemCommit = Math.max(maxDiasSemCommit, diasAtualSemCommit);
        indicadores.setMaxIntervaloSemCommits(maxDiasSemCommit);
    }

    private void calcularDistribuicaoTrabalho(List<Commit> commits, 
                                            IndicadoresAnaliseAcademicos indicadores) {
        Map<String, Long> commitsPorAutor = commits.stream()
            .filter(c -> c.getAutor() != null && c.getAutor().getNome() != null)
            .collect(Collectors.groupingBy(
                c -> c.getAutor().getNome(),
                Collectors.counting()
            ));

        if (!commitsPorAutor.isEmpty()) {
            long maxCommitsAutor = commitsPorAutor.values().stream()
                .mapToLong(Long::longValue)
                .max().orElse(0);
            
            indicadores.setConcentracaoAutor((double) maxCommitsAutor / commits.size());
            indicadores.setTotalAutores(commitsPorAutor.size());
            
            double equilibrioDistribuicao = calcularEquilibrio(commitsPorAutor.values());
            indicadores.setEquilibrioDistribuicao(equilibrioDistribuicao);
        }
    }

    private double calcularEquilibrio(java.util.Collection<Long> valores) {
        var listaOrdenada = valores.stream().sorted().toList();
        double soma = listaOrdenada.stream().mapToLong(Long::longValue).sum();
        
        if (soma == 0) return 1.0;
        
        double somaAcumulada = 0;
        double areaGini = 0;

        for (Long aLong : listaOrdenada) {
            somaAcumulada += aLong;
            areaGini += somaAcumulada;
        }
        
        double gini = (2 * areaGini) / (listaOrdenada.size() * soma) - (listaOrdenada.size() + 1) / (double) listaOrdenada.size();
        return 1.0 - gini;
    }

    private void calcularQualidadeMensagens(List<Commit> commits, 
                                          IndicadoresAnaliseAcademicos indicadores) {
        int totalCommits = commits.size();
        if (totalCommits == 0) return;

        long mensagensDescritivas = commits.stream()
            .map(Commit::getMensagem)
            .filter(msg -> msg != null && PATTERN_COMMIT_DESCRITIVO.matcher(msg).find())
            .count();

        long mensagensMuitoCurtas = commits.stream()
            .map(Commit::getMensagem)
            .filter(msg -> msg != null && PATTERN_COMMIT_MUITO_CURTO.matcher(msg).find())
            .count();

        long mensagensGenericas = commits.stream()
            .map(Commit::getMensagem)
            .filter(msg -> msg != null && PATTERN_COMMIT_GENERICO.matcher(msg).find())
            .count();

        indicadores.setPctMensagensDescritivas((double) mensagensDescritivas / totalCommits);
        indicadores.setPctMensagensMuitoCurtas((double) mensagensMuitoCurtas / totalCommits);
        indicadores.setPctMensagensGenericas((double) mensagensGenericas / totalCommits);

        double tamanhoMedio = commits.stream()
            .map(Commit::getMensagem)
            .filter(Objects::nonNull)
            .mapToInt(String::length)
            .average().orElse(0.0);
        indicadores.setTamanhoMedioMensagem(tamanhoMedio);
    }

    private void calcularVariedadeTipos(List<Commit> commits, 
                                      IndicadoresAnaliseAcademicos indicadores) {
        Map<TipoCommit, Long> tiposCount = commits.stream()
            .filter(c -> c.getTipo() != null)
            .collect(Collectors.groupingBy(Commit::getTipo, Collectors.counting()));

        indicadores.setVariedadeTiposCommit(tiposCount.size());
        
        long totalCommits = commits.size();
        if (totalCommits > 0) {
            indicadores.setPctFeatures((double) tiposCount.getOrDefault(TipoCommit.FEATURE, 0L) / totalCommits);
            indicadores.setPctBugFixes((double) tiposCount.getOrDefault(TipoCommit.BUGFIX, 0L) / totalCommits);
            indicadores.setPctRefactoring((double) tiposCount.getOrDefault(TipoCommit.REFACTOR, 0L) / totalCommits);
            indicadores.setPctTestes((double) tiposCount.getOrDefault(TipoCommit.TEST, 0L) / totalCommits);
            indicadores.setPctDocumentacao((double) tiposCount.getOrDefault(TipoCommit.DOCUMENTATION, 0L) / totalCommits);
            indicadores.setPctMerges((double) tiposCount.getOrDefault(TipoCommit.MERGE, 0L) / totalCommits);
        }

        // Diversidade de tipos (entropia)
        double entropia = calcularEntropia(tiposCount.values(), totalCommits);
        indicadores.setDiversidadeTipos(entropia);
    }

    private double calcularEntropia(Collection<Long> frequencias, long total) {
        if (total == 0) return 0.0;
        
        return frequencias.stream()
            .mapToDouble(freq -> {
                if (freq == 0) return 0.0;
                double p = freq / (double) total;
                return -p * Math.log(p) / Math.log(2);
            })
            .sum();
    }

    private void calcularDistribuicaoTemporal(List<Commit> commits, 
                                            IndicadoresAnaliseAcademicos indicadores) {
        Map<Integer, Long> commitsPorHora = commits.stream()
            .collect(Collectors.groupingBy(
                c -> c.getCommitDate().getHour(),
                Collectors.counting()
            ));

        if (!commitsPorHora.isEmpty()) {
            long maxCommitsHora = commitsPorHora.values().stream()
                .mapToLong(Long::longValue)
                .max().orElse(0);
            indicadores.setConcentracaoHorario((double) maxCommitsHora / commits.size());

            long commitsHorarioTrabalho = commitsPorHora.entrySet().stream()
                .filter(entry -> entry.getKey() >= 19 && entry.getKey() <= 23)
                .mapToLong(Map.Entry::getValue)
                .sum();
            indicadores.setPctCommitsHorarioAula((double) commitsHorarioTrabalho / commits.size());
        }
    }

    @Data
    public static class IndicadoresAnaliseAcademicos {
        // Tempo
        private long diasPeriodo;
        private double semanasPeriodo;

        // Frequência e Consistência
        private double commitsPorSemana;
        private int diasAtivos;
        private double consistenciaTemporal;
        private double regularidadeCommits;
        private int maxIntervaloSemCommits;

        // Distribuição de Trabalho
        private int totalAutores;
        private double concentracaoAutor;
        private double equilibrioDistribuicao;

        // Qualidade das Mensagens
        private double pctMensagensDescritivas;
        private double pctMensagensMuitoCurtas;
        private double pctMensagensGenericas;
        private double tamanhoMedioMensagem;

        // Variedade de Tipos
        private int variedadeTiposCommit;
        private double diversidadeTipos;
        private double pctFeatures;
        private double pctBugFixes;
        private double pctRefactoring;
        private double pctTestes;
        private double pctDocumentacao;
        private double pctMerges;

        // Distribuição Temporal
        private double concentracaoHorario;
        private double pctCommitsHorarioAula;
    }
}
