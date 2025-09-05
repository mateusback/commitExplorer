package br.edu.ifpr.commitexplorer.CommitExplorer.application.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.AutorResumoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.FeedbackDinamicoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.GeralAnaliseView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.FeedbackDto;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.IndicadoresAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PontuacaoAnaliseService {

    private final AvaliadorAcademicoService avaliadorAcademico;

    /**
     * Nova versão acadêmica da avaliação que considera aspectos educacionais
     */
    public FeedbackDto gerarFeedbackAcademico(List<Commit> commits,
                                              LocalDate inicio,
                                              LocalDate fim,
                                              IndicadoresAnalise indicadoresBasicos) {

        var indicadoresAcademicos = avaliadorAcademico.calcularIndicadoresAcademicos(commits, inicio, fim);

        var fb = new FeedbackDto();
        var pos = new ArrayList<String>();
        var neg = new ArrayList<String>();
        var sug = new ArrayList<String>();
        double scoreAcademico = 0.0;

        // 1. FREQUÊNCIA E CONSISTÊNCIA
        avaliarFrequenciaConsistencia(indicadoresAcademicos, pos, neg, sug);

        // 2. DISTRIBUIÇÃO DE TRABALHO
        avaliarDistribuicaoTrabalho(indicadoresAcademicos, pos, neg, sug);

        // 3. QUALIDADE DAS MENSAGENS
        avaliarQualidadeMensagens(indicadoresAcademicos, pos, neg, sug);

        // 4. VARIEDADE DE TIPOS
        avaliarVariedadeTipos(indicadoresAcademicos, pos, neg, sug);

        // 5. DISTRIBUIÇÃO TEMPORAL
        avaliarDistribuicaoTemporal(indicadoresAcademicos, pos, neg, sug);

        // 6. QUALIDADE TÉCNICA (usando indicadores básicos)
        if (indicadoresBasicos.isTemCommitsAnalisados()){
            avaliarQualidadeTecnica(indicadoresBasicos, pos, neg, sug);
            scoreAcademico = calcularScoreAcademico(indicadoresAcademicos, indicadoresBasicos, fb);
        }
        else {
            scoreAcademico = calcularScoreSemQualidadeAcademico(indicadoresAcademicos, fb);
            neg.add("Nenhum commit analisado para qualidade de código. Não foi possível avaliar este aspecto.");
        }

        char nota = notaPorScore(scoreAcademico);

        fb.setPontosPositivos(pos);
        fb.setPontosNegativos(neg);
        fb.setSugestoes(sug);
        fb.setNota(nota);
        fb.setPontuacaoGeral(scoreAcademico);
        fb.setFeedback(String.format(
                "Análise Acadêmica - Período: %s a %s | Consistência: %.1f%% | Mensagens Descritivas: %.1f%% | Variedade: %d tipos | Nota: %s (%.1f)",
                inicio, fim,
                indicadoresAcademicos.getConsistenciaTemporal() * 100,
                indicadoresAcademicos.getPctMensagensDescritivas() * 100,
                indicadoresAcademicos.getVariedadeTiposCommit(),
                nota, scoreAcademico
        ));

        return fb;
    }

    private void avaliarFrequenciaConsistencia(AvaliadorAcademicoService.IndicadoresAnaliseAcademicos ind,
                                             List<String> pos, List<String> neg, List<String> sug) {
        // Positivos
        if (ind.getConsistenciaTemporal() >= 0.6) {
            pos.add(String.format("Excelente consistência: commits em %.1f%% dos dias do período.", 
                    ind.getConsistenciaTemporal() * 100));
        }
        if (ind.getCommitsPorSemana() >= 10 && ind.getCommitsPorSemana() <= 25) {
            pos.add("Frequência adequada de commits para desenvolvimento acadêmico.");
        }
        if (ind.getMaxIntervaloSemCommits() <= 3) {
            pos.add("Boa regularidade: intervalos máximos curtos sem commits.");
        }

        // Negativos e sugestões
        if (ind.getConsistenciaTemporal() < 0.3) {
            neg.add(String.format("Baixa consistência: commits em apenas %.1f%% dos dias.", 
                    ind.getConsistenciaTemporal() * 100));
            sug.add("Estabeleça uma rotina de commits diários ou a cada 2 dias.");
        }
        if (ind.getCommitsPorSemana() < 5) {
            neg.add("Frequência muito baixa de commits para um projeto acadêmico.");
            sug.add("Divida o trabalho em pequenas entregas incrementais e commite mais frequentemente.");
        }
        if (ind.getMaxIntervaloSemCommits() > 7) {
            neg.add(String.format("Intervalo muito longo sem commits: %d dias.", ind.getMaxIntervaloSemCommits()));
            sug.add("Evite deixar o código sem versionamento por mais de uma semana.");
        }
        if (ind.getRegularidadeCommits() > 5) {
            neg.add("Grande variação no número de commits por dia (trabalho irregular).");
            sug.add("Distribua o esforço de forma mais equilibrada ao longo do tempo.");
        }
    }

    private void avaliarDistribuicaoTrabalho(AvaliadorAcademicoService.IndicadoresAnaliseAcademicos ind,
                                           List<String> pos, List<String> neg, List<String> sug) {
        if (ind.getTotalAutores() > 1) {
            if (ind.getEquilibrioDistribuicao() >= 0.7) {
                pos.add("Boa distribuição de trabalho entre os membros da equipe.");
            }
            if (ind.getConcentracaoAutor() <= 0.6) {
                pos.add("Contribuições bem distribuídas, sem concentração excessiva.");
            }

            // Negativos
            if (ind.getConcentracaoAutor() > 0.8) {
                neg.add(String.format("Um autor concentra %.1f%% dos commits.", 
                        ind.getConcentracaoAutor() * 100));
                sug.add("Distribua melhor as tarefas entre os membros da equipe.");
            }
            if (ind.getEquilibrioDistribuicao() < 0.4) {
                neg.add("Distribuição muito desigual de commits entre os autores.");
                sug.add("Organize pair programming ou revisões cruzadas para equilibrar contribuições.");
            }
        } else {
            neg.add("Projeto individual: todos os commits de um único autor.");
        }
    }

    private void avaliarQualidadeMensagens(AvaliadorAcademicoService.IndicadoresAnaliseAcademicos ind,
                                         List<String> pos, List<String> neg, List<String> sug) {
        // Positivos
        if (ind.getPctMensagensDescritivas() >= 0.7) {
            pos.add(String.format("Excelente qualidade das mensagens: %.1f%% são descritivas.", 
                    ind.getPctMensagensDescritivas() * 100));
        }
        if (ind.getPctMensagensMuitoCurtas() <= 0.1) {
            pos.add("Poucas mensagens muito curtas (demonstra cuidado na documentação).");
        }
        if (ind.getTamanhoMedioMensagem() >= 25 && ind.getTamanhoMedioMensagem() <= 80) {
            pos.add("Tamanho médio adequado das mensagens de commit.");
        }

        // Negativos
        if (ind.getPctMensagensDescritivas() < 0.4) {
            neg.add(String.format("Apenas %.1f%% das mensagens são descritivas.", 
                    ind.getPctMensagensDescritivas() * 100));
            sug.add("Use o padrão Conventional Commits: feat:, fix:, docs:, etc.");
        }
        if (ind.getPctMensagensMuitoCurtas() > 0.3) {
            neg.add(String.format("Muitas mensagens muito curtas: %.1f%%.", 
                    ind.getPctMensagensMuitoCurtas() * 100));
            sug.add("Escreva mensagens mais descritivas explicando O QUE e POR QUE mudou.");
        }
        if (ind.getPctMensagensGenericas() > 0.2) {
            neg.add(String.format("Mensagens genéricas demais: %.1f%%.", 
                    ind.getPctMensagensGenericas() * 100));
            sug.add("Evite mensagens como 'fix', 'update', 'changes'. Seja específico.");
        }
        if (ind.getTamanhoMedioMensagem() < 15) {
            neg.add("Mensagens muito curtas em média (pouco contexto).");
            sug.add("Adicione mais detalhes sobre as mudanças implementadas.");
        }
    }

    private void avaliarVariedadeTipos(AvaliadorAcademicoService.IndicadoresAnaliseAcademicos ind,
                                     List<String> pos, List<String> neg, List<String> sug) {
        // Positivos
        if (ind.getVariedadeTiposCommit() >= 5) {
            pos.add(String.format("Boa variedade de tipos de commit: %d tipos diferentes.", 
                    ind.getVariedadeTiposCommit()));
        }
        if (ind.getPctFeatures() >= 0.4 && ind.getPctFeatures() <= 0.7) {
            pos.add("Boa proporção de commits de features/funcionalidades.");
        }
        if (ind.getPctRefactoring() >= 0.1) {
            pos.add("Presença de commits de refatoração (boas práticas de manutenção).");
        }
        if (ind.getPctTestes() >= 0.1) {
            pos.add("Inclui commits específicos para testes (boa prática).");
        }
        if (ind.getPctDocumentacao() >= 0.05) {
            pos.add("Commits dedicados à documentação (demonstra cuidado com o projeto).");
        }

        // Negativos
        if (ind.getVariedadeTiposCommit() <= 2) {
            neg.add("Pouca variedade nos tipos de commit.");
            sug.add("Use diferentes tipos: feat, fix, docs, test, refactor, style, etc.");
        }
        if (ind.getPctFeatures() < 0.2) {
            neg.add("Poucos commits de novas funcionalidades.");
            sug.add("Marque adequadamente os commits que adicionam features com 'feat:'.");
        }
        if (ind.getPctRefactoring() < 0.05) {
            neg.add("Ausência de commits de refatoração.");
            sug.add("Inclua refatorações regulares para melhorar a qualidade do código.");
        }
        if (ind.getPctTestes() == 0) {
            neg.add("Nenhum commit específico para testes.");
            sug.add("Adicione commits dedicados aos testes com 'test:'.");
        }
        if (ind.getPctMerges() > 0.3) {
            neg.add("Proporção alta de merges pode indicar muitos conflitos.");
            sug.add("Sincronize branches mais frequentemente para reduzir conflitos.");
        }
    }

    private void avaliarDistribuicaoTemporal(AvaliadorAcademicoService.IndicadoresAnaliseAcademicos ind,
                                           List<String> pos, List<String> neg, List<String> sug) {
        // Positivos
        if (ind.getPctCommitsHorarioAula() >= 0.6) {
            pos.add("Boa organização: maioria dos commits em horário de aula.");
        }
        if (ind.getConcentracaoHorario() <= 0.4) {
            pos.add("Commits bem distribuídos ao longo do dia.");
        }

        // Negativos
        if (ind.getConcentracaoHorario() > 0.6) {
            neg.add(String.format("Concentração excessiva em um horário: %.1f%% dos commits.", 
                    ind.getConcentracaoHorario() * 100));
            sug.add("Distribua melhor o trabalho ao longo do dia.");
        }
        if (ind.getPctCommitsHorarioAula() < 0.3) {
            neg.add("Muitos commits fora do horário comercial (possível procrastinação).");
            sug.add("Organize melhor seu tempo, evite trabalhar apenas de madrugada.");
        }
    }

    private void avaliarQualidadeTecnica(IndicadoresAnalise ind,
                                       List<String> pos, List<String> neg, List<String> sug) {
        // Positivos técnicos
        if (ind.getSmellsPorKLocAlteradas() <= 5) {
            pos.add("Baixa densidade de problemas de qualidade de código.");
        }
        if (ind.getComplexidadeMedia() <= 10) {
            pos.add("Complexidade do código sob controle.");
        }

        // Negativos técnicos
        if (ind.getSmellsPorKLocAlteradas() > 15) {
            neg.add("Alta densidade de problemas de qualidade (code smells).");
            sug.add("Revise o código regularmente e aplique refatorações.");
        }
        if (ind.getComplexidadeMedia() > 25) {
            neg.add("Complexidade muito alta do código.");
            sug.add("Simplifique métodos e classes complexas.");
        }
    }

    private double calcularScoreAcademico(AvaliadorAcademicoService.IndicadoresAnaliseAcademicos academ,
                                        IndicadoresAnalise basicos, FeedbackDto fb) {
        double score = 0.0;
        fb.setPontuacaoFrequenciaConsistencia(scoreFrequenciaConsistencia(academ));
        fb.setPontuacaoQualidadeMensagens(scoreQualidadeMensagens(academ));
        fb.setPontuacaoVariedadeTipos(scoreVariedadeTipos(academ));
        fb.setPontuacaoDistribuicaoTrabalho(scoreDistribuicaoTrabalho(academ));
        fb.setPontuacaoDistribuicaoTemporal(scoreDistribuicaoTemporal(academ));
        fb.setPontuacaoQualidadeTecnica(scoreQualidadeTecnica(basicos));

        // 1. Frequência e Consistência (25%)
        score += 0.25 * fb.getPontuacaoFrequenciaConsistencia();

        // 2. Qualidade das Mensagens (20%)
        score += 0.20 * fb.getPontuacaoQualidadeMensagens();

        // 3. Variedade e Organização (15%)
        score += 0.15 * fb.getPontuacaoVariedadeTipos();

        // 4. Distribuição de Trabalho (15%)
        score += 0.15 * fb.getPontuacaoDistribuicaoTrabalho();

        // 5. Distribuição Temporal (10%)
        score += 0.10 * fb.getPontuacaoDistribuicaoTemporal();

        // 6. Qualidade Técnica (15%)
        score += 0.15 * fb.getPontuacaoQualidadeTecnica();

        return Math.max(0, Math.min(100, score));
    }

    private double calcularScoreSemQualidadeAcademico(AvaliadorAcademicoService.IndicadoresAnaliseAcademicos academ, FeedbackDto fb) {
        double score = 0.0;
        fb.setPontuacaoFrequenciaConsistencia(scoreFrequenciaConsistencia(academ));
        fb.setPontuacaoQualidadeMensagens(scoreQualidadeMensagens(academ));
        fb.setPontuacaoVariedadeTipos(scoreVariedadeTipos(academ));
        fb.setPontuacaoDistribuicaoTrabalho(scoreDistribuicaoTrabalho(academ));
        fb.setPontuacaoDistribuicaoTemporal(scoreDistribuicaoTemporal(academ));

        // 1. Frequência e Consistência (30%)
        score += 0.30 * fb.getPontuacaoFrequenciaConsistencia();

        // 2. Qualidade das Mensagens (25%)
        score += 0.25 * fb.getPontuacaoQualidadeMensagens();

        // 3. Variedade e Organização (20%)
        score += 0.20 * fb.getPontuacaoVariedadeTipos();

        // 4. Distribuição de Trabalho (15%)
        score += 0.15 * fb.getPontuacaoDistribuicaoTrabalho();

        // 5. Distribuição Temporal (10%)
        score += 0.10 * fb.getPontuacaoDistribuicaoTemporal();

        return Math.max(0, Math.min(100, score));
    }

    private double scoreFrequenciaConsistencia(AvaliadorAcademicoService.IndicadoresAnaliseAcademicos ind) {
        double consistencia = clamp01(ind.getConsistenciaTemporal() / 0.8) * 40;
        double frequencia = clamp01(ind.getCommitsPorSemana() / 20.0) * 35;
        double regularidade = Math.max(0, 25 - ind.getRegularidadeCommits() * 3);
        return consistencia + frequencia + regularidade;
    }

    private double scoreQualidadeMensagens(AvaliadorAcademicoService.IndicadoresAnaliseAcademicos ind) {
        double descritivas = ind.getPctMensagensDescritivas() * 50;
        double penalCurtas = Math.max(0, 25 - ind.getPctMensagensMuitoCurtas() * 100);
        double penalGenericas = Math.max(0, 25 - ind.getPctMensagensGenericas() * 100);
        return descritivas + penalCurtas + penalGenericas;
    }

    private double scoreVariedadeTipos(AvaliadorAcademicoService.IndicadoresAnaliseAcademicos ind) {
        double variedade = Math.min(50, ind.getVariedadeTiposCommit() * 8);
        double features = clamp01(ind.getPctFeatures() / 0.5) * 20;
        double refactor = Math.min(15, ind.getPctRefactoring() * 150);
        double testes = Math.min(15, ind.getPctTestes() * 150);
        return variedade + features + refactor + testes;
    }

    private double scoreDistribuicaoTrabalho(AvaliadorAcademicoService.IndicadoresAnaliseAcademicos ind) {
        if (ind.getTotalAutores() == 1) return 100;

        double equilibrio = ind.getEquilibrioDistribuicao() * 60;
        double concentracao = Math.max(0, 40 - ind.getConcentracaoAutor() * 50);
        return equilibrio + concentracao;
    }

    private double scoreDistribuicaoTemporal(AvaliadorAcademicoService.IndicadoresAnaliseAcademicos ind) {
        double horarioTrabalho = ind.getPctCommitsHorarioAula() * 60;
        double concentracao = Math.max(0, 40 - ind.getConcentracaoHorario() * 50);
        return horarioTrabalho + concentracao;
    }

    private double scoreQualidadeTecnica(IndicadoresAnalise ind) {
        double qualidade = inv(ind.getSmellsPorKLocAlteradas() / 20.0) * 50;
        double complexidade = inv(ind.getComplexidadeMedia() / 30.0) * 50;
        return qualidade + complexidade;
    }

    private double clamp01(double v) { return Math.max(0.0, Math.min(1.0, v)); }
    private double inv(double v) { return 1.0 - clamp01(v); }

    private char notaPorScore(double s) {
        if (s >= 85) return 'A';
        if (s >= 70) return 'B';
        if (s >= 55) return 'C';
        return 'D';
    }
}