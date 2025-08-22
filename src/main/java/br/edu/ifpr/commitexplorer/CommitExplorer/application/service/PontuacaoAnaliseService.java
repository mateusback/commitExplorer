package br.edu.ifpr.commitexplorer.CommitExplorer.application.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.AutorResumoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.FeedbackDinamicoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.GeralAnaliseView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.IndicadoresAnalise;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PontuacaoAnaliseService {

    public FeedbackDinamicoView gerarFeedback(GeralAnaliseView geral,
                                              IndicadoresAnalise m,
                                              List<AutorResumoView> autores) {

        var fb  = new FeedbackDinamicoView();
        var pos = new ArrayList<String>();
        var neg = new ArrayList<String>();
        var sug = new ArrayList<String>();

        // POSITIVOS
        if (m.getCommitsPorSemana() >= 15) pos.add("Boa cadência de entregas ao longo do período.");
        if ((double)m.getDiasAtivos() / m.getDiasPeriodo() >= 0.5) pos.add("Commits distribuídos em vários dias (consistência).");
        if (m.getTotalAutores() >= 3 && m.getShareTopAutor() <= 0.5) pos.add("Colaboração saudável entre autores.");
        if (m.getSmellsPorKLocAlteradas() <= 5) pos.add("Baixa densidade de code smells vs. volume alterado.");
        if (m.getComplexidadeMedia() <= 10) pos.add("Complexidade média sob controle.");
        if (m.getShareTop5Arquivos() <= 0.4) pos.add("Alterações distribuídas (menos hotspots).");

        // NEGATIVOS + SUGESTÕES
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

    // ---- Scoring ----
    private double clamp01(double v) { return Math.max(0.0, Math.min(1.0, v)); }
    private double inv(double v) { return 1.0 - clamp01(v); }

    private double scoreCadencia(IndicadoresAnalise m) {
        return clamp01(m.getCommitsPorSemana() / 25.0) * 100.0; // meta ~25/semana
    }

    private double scoreConsistencia(IndicadoresAnalise m) {
        double coberturaDias = clamp01((double) m.getDiasAtivos() / m.getDiasPeriodo());
        double baseDia = (m.getCommitsPorSemana() / 7.0);
        double ratio = baseDia <= 0 ? 1.0 : m.getDesvioPadraoCommitsPorDia() / Math.max(1.0, baseDia);
        double penal = clamp01(ratio / 2.0);
        return clamp01(0.7 * coberturaDias + 0.3 * (1 - penal)) * 100.0;
    }

    private double scoreColaboracao(IndicadoresAnalise m) {
        double autoresScore = clamp01((double) m.getTotalAutores() / 5.0);
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