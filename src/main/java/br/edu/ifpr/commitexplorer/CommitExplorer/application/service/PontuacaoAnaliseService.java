package br.edu.ifpr.commitexplorer.CommitExplorer.application.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.AutorResumoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.FeedbackDinamicoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.GeralAnaliseView;
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

    public FeedbackDinamicoView gerarFeedback(GeralAnaliseView geral,
                                              IndicadoresAnalise m,
                                              @SuppressWarnings("unused") List<AutorResumoView> autores) {

        var fb  = new FeedbackDinamicoView();
        var pos = new ArrayList<String>();
        var neg = new ArrayList<String>();
        var sug = new ArrayList<String>();

        // POSITIVOS BÁSICOS
        if (m.getCommitsPorSemana() >= 15) pos.add("Boa cadência de entregas ao longo do período.");
        if ((double)m.getDiasAtivos() / m.getDiasPeriodo() >= 0.5) pos.add("Commits distribuídos em vários dias (consistência).");
        if (m.getTotalAutores() >= 3 && m.getShareTopAutor() <= 0.5) pos.add("Colaboração saudável entre autores.");
        if (m.getSmellsPorKLocAlteradas() <= 5) pos.add("Baixa densidade de code smells vs. volume alterado.");
        if (m.getComplexidadeMedia() <= 10) pos.add("Complexidade média sob controle.");
        if (m.getShareTop5Arquivos() <= 0.4) pos.add("Alterações distribuídas (menos hotspots).");

        // NEGATIVOS + SUGESTÕES BÁSICOS
        if (m.getCommitsPorSemana() < 6) {
            neg.add("Cadência de commits baixa para o período.");
            sug.add("Quebre entregas grandes em commits menores e mais frequentes.");
        }
        double baseDia = (m.getCommitsPorSemana() / 7.0);
        if (m.getDesvioPadraoCommitsPorDia() > Math.max(1.0, baseDia * 2)) {
            neg.add("Picos concentrados de commits (rajadas) em vez de fluxo contínuo.");
            sug.add("Adote check-ins diários ou a cada 2–3 dias.");
        }
        if (m.getTotalAutores() <= 1 || m.getShareTopAutor() > 0.7) {
            neg.add("Concentração de conhecimento em poucos autores.");
            sug.add("Promova co-ownership e code reviews cruzados.");
        }
        if (m.getSmellsPorKLocAlteradas() > 12) {
            neg.add("Alta densidade de code smells por KLOC alterada.");
            sug.add("Refatore hotspots e use gates de qualidade no CI.");
        }
        if (m.getComplexidadeMedia() > 20) {
            neg.add("Complexidade média elevada nos commits.");
            sug.add("Quebre funções/classes grandes e favoreça composição.");
        }
        if (m.getShareTop5Arquivos() > 0.65) {
            neg.add("Muitas alterações concentradas em poucos arquivos (hotspots).");
            sug.add("Extrair responsabilidades para reduzir acoplamento.");
        }
        if (m.getLinhasPorCommit() < 20) {
            neg.add("Commits muito pequenos e possivelmente ruidosos.");
            sug.add("Agrupe pequenas mudanças relacionadas em um commit descritivo.");
        } else if (m.getLinhasPorCommit() > 800) {
            neg.add("Commits muito grandes; revisão e rollback ficam difíceis.");
            sug.add("Divida mudanças extensas em unidades incrementais por feature.");
        }
        if (m.getPctMerges() > 0.35) {
            neg.add("Proporção alta de merges.");
            sug.add("Sincronize branches com mais frequência e reduza tempo de PR aberto.");
        }

        double score = scoreFinal(m);
        char nota = notaPorScore(score);

        fb.setPontosPositivos(pos);
        fb.setPontosNegativos(neg);
        fb.setSugestoes(sug);
        fb.setNota(nota);
        fb.setFeedback(String.format(
                "Período: %s a %s | Cadência: %.1f commits/semana | Autores: %d | Smells/KLOC: %.2f | Complexidade: %.2f | Nota: %s (Score: %.1f).",
                geral.getDataInicio(), geral.getDataFim(),
                m.getCommitsPorSemana(), m.getTotalAutores(),
                m.getSmellsPorKLocAlteradas(), m.getComplexidadeMedia(),
                String.valueOf(nota), score
        ));
        return fb;
    }

    /**
     * Nova versão acadêmica da avaliação que considera aspectos educacionais
     */
    public FeedbackDinamicoView gerarFeedbackAcademico(List<Commit> commits, 
                                                       LocalDate inicio, 
                                                       LocalDate fim,
                                                       IndicadoresAnalise indicadoresBasicos) {
        
        var indicadoresAcademicos = avaliadorAcademico.calcularIndicadoresAcademicos(commits, inicio, fim);
        
        var fb = new FeedbackDinamicoView();
        var pos = new ArrayList<String>();
        var neg = new ArrayList<String>();
        var sug = new ArrayList<String>();

        // AVALIAÇÃO ACADÊMICA DETALHADA

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
        avaliarQualidadeTecnica(indicadoresBasicos, pos, neg, sug);

        // Cálculo da nota acadêmica
        double scoreAcademico = calcularScoreAcademico(indicadoresAcademicos, indicadoresBasicos);
        char nota = notaPorScore(scoreAcademico);

        fb.setPontosPositivos(pos);
        fb.setPontosNegativos(neg);
        fb.setSugestoes(sug);
        fb.setNota(nota);
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
                                        IndicadoresAnalise basicos) {
        double score = 0.0;

        // 1. Frequência e Consistência (25%)
        score += 0.25 * scoreFrequenciaConsistencia(academ);

        // 2. Qualidade das Mensagens (20%)
        score += 0.20 * scoreQualidadeMensagens(academ);

        // 3. Variedade e Organização (15%)
        score += 0.15 * scoreVariedadeTipos(academ);

        // 4. Distribuição de Trabalho (15%)
        score += 0.15 * scoreDistribuicaoTrabalho(academ);

        // 5. Distribuição Temporal (10%)
        score += 0.10 * scoreDistribuicaoTemporal(academ);

        // 6. Qualidade Técnica (15%)
        score += 0.15 * scoreQualidadeTecnica(basicos);

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

    // ---- Métodos auxiliares dos indicadores básicos (mantidos para compatibilidade) ----
    private double clamp01(double v) { return Math.max(0.0, Math.min(1.0, v)); }
    private double inv(double v) { return 1.0 - clamp01(v); }

    private double scoreCadencia(IndicadoresAnalise m) {
        return clamp01(m.getCommitsPorSemana() / 25.0) * 100.0;
    }

    private double scoreConsistencia(IndicadoresAnalise m) {
        double coberturaDias = clamp01((double) m.getDiasAtivos() / m.getDiasPeriodo());
        double baseDia = (m.getCommitsPorSemana() / 7.0);
        double ratio = baseDia <= 0 ? 1.0 : m.getDesvioPadraoCommitsPorDia() / Math.max(1.0, baseDia);
        double penal = clamp01(ratio / 2.0);
        return clamp01(0.7 * coberturaDias + 0.3 * (1 - penal)) * 100.0;
    }

    private double scoreColaboracao(IndicadoresAnalise m) {
        double autoresScore = clamp01(m.getTotalAutores() / 5.0);
        double concentracaoScore = 1.0 - m.getShareTopAutor();
        return (0.6 * autoresScore + 0.4 * concentracaoScore) * 100.0;
    }

    private double scoreQualidade(IndicadoresAnalise m) {
        return inv(m.getSmellsPorKLocAlteradas() / 20.0) * 100.0;
    }

    private double scoreComplexidade(IndicadoresAnalise m) {
        return inv(m.getComplexidadeMedia() / 50.0) * 100.0;
    }

    private double scoreHotspots(IndicadoresAnalise m) {
        double norm = (m.getShareTop5Arquivos() - 0.30) / (0.80 - 0.30);
        return inv(norm) * 100.0;
    }

    private double scoreChurn(IndicadoresAnalise m) {
        double x = m.getLinhasPorCommit();
        if (x <= 0) return 0;
        if (x < 20)   return (x / 20.0) * 100.0;
        if (x <= 400) return 100.0;
        return inv((x - 400.0) / 1600.0) * 100.0;
    }

    private double scoreTipos(IndicadoresAnalise m) {
        double norm = (m.getPctMerges() - 0.10) / (0.40 - 0.10);
        return inv(norm) * 100.0;
    }

    private double scoreFinal(IndicadoresAnalise m) {
        return
                0.25 * scoreCadencia(m)     +
                        0.10 * scoreConsistencia(m) +
                        0.15 * scoreColaboracao(m)  +
                        0.15 * scoreQualidade(m)    +
                        0.10 * scoreComplexidade(m) +
                        0.10 * scoreHotspots(m)     +
                        0.10 * scoreChurn(m)        +
                        0.05 * scoreTipos(m);
    }

    private char notaPorScore(double s) {
        if (s >= 85) return 'A';
        if (s >= 70) return 'B';
        if (s >= 55) return 'C';
        return 'D';
    }
}