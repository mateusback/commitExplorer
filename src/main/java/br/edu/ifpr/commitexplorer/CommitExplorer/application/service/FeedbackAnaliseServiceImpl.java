package br.edu.ifpr.commitexplorer.CommitExplorer.application.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.FeedbackDto;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.IndicadoresAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.*;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoFeedback;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.AnaliseProjetoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.FeedbackAnaliseRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.IndicadoresFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackAnaliseServiceImpl implements FeedbackAnaliseService {

    private final IndicadoresAnaliseService indicadoresService;
    private final AnaliseProjetoRepository analiseRepo;
    private final PontuacaoAnaliseService pontuacaoService;
    private final FeedbackAnaliseRepository feedbackRepo;
    private final IndicadoresFeedbackRepository indicadoresFeedbackRepo;

    @Override
    @Transactional
    public void calcularFeedback(AnaliseProjeto analise) {
        var inicio = analise.getSolicitacaoAnalise().getDataInicio();
        var fim = analise.getSolicitacaoAnalise().getDataFim();
        var commits = filtrarCommitsNoPeriodo(analise, inicio, fim);
        var temCommitsAnalisados = commits.stream().anyMatch(Commit::isAnalisado);

        var indBasicos = indicadoresService.calcular(commits, inicio, fim, temCommitsAnalisados);
        var fbGeral = pontuacaoService.gerarFeedbackAcademico(commits, inicio, fim, indBasicos);
        salvarFeedback(analise, null, TipoFeedback.GERAL, inicio, fim, fbGeral, indBasicos);
        analise.setPontuacaoTotal(fbGeral.getPontuacaoGeral());
        analiseRepo.save(analise);

        Map<Long, List<Commit>> porAutor = commits.stream()
                .filter(c -> c.getAutor() != null && c.getAutor().getIdAutor() != null)
                .collect(Collectors.groupingBy(c -> c.getAutor().getIdAutor()));

        porAutor.forEach((idAutor, commitsAutor) -> {
            var autorTemCommitsAnalisados = commitsAutor.stream().anyMatch(Commit::isAnalisado);
            var indAutor = indicadoresService.calcular(commitsAutor, inicio, fim, autorTemCommitsAnalisados);
            var fbAutor = pontuacaoService.gerarFeedbackAcademico(commitsAutor, inicio, fim, indAutor);
            var autor = commitsAutor.get(0).getAutor();
            salvarFeedback(analise, autor, TipoFeedback.AUTOR, inicio, fim, fbAutor, indAutor);
        });
    }

    private void salvarFeedback(AnaliseProjeto analise,
                                Autor autorOrNull,
                                TipoFeedback tipo,
                                LocalDate inicio, LocalDate fim,
                                FeedbackDto fb,
                                IndicadoresAnalise indicadores) {

        var f = new FeedbackAnalise();
        f.setAnaliseProjeto(analise);
        f.setTipo(tipo);
        f.setAutor(autorOrNull);

        f.setPeriodoInicio(inicio.atStartOfDay());
        f.setPeriodoFim(fim.atTime(LocalTime.MAX));
        f.setPontuacaoFrequenciaConsistencia(fb.getPontuacaoFrequenciaConsistencia());
        f.setPontuacaoDistribuicaoTrabalho(fb.getPontuacaoDistribuicaoTrabalho());
        f.setPontuacaoDistribuicaoTemporal(fb.getPontuacaoDistribuicaoTemporal());
        f.setPontuacaoVariedadeTipos(fb.getPontuacaoVariedadeTipos());
        f.setPontuacaoQualidadeMensagens(fb.getPontuacaoQualidadeMensagens());
        f.setPontuacaoQualidadeTecnica(fb.getPontuacaoQualidadeTecnica());
        f.setConceito(fb.getNota());
        f.setPontuacaoGeral(fb.getPontuacaoGeral());
        f.setResumo(fb.getFeedback());

        f.setPontosPositivos(safe(fb.getPontosPositivos()));
        f.setPontosNegativos(safe(fb.getPontosNegativos()));
        f.setSugestoesMelhoria(safe(fb.getSugestoes()));

        f.setDataCriacao(LocalDateTime.now());
        var indicadoresFeedback = new IndicadoresFeedback(indicadores);
        f.setIndicadores(indicadoresFeedback);

        feedbackRepo.save(f);
    }

    private List<String> safe(List<String> xs) {
        return xs == null ? List.of() : xs;
    }
    
    private List<Commit> filtrarCommitsNoPeriodo(AnaliseProjeto a, LocalDate i, LocalDate f) {
        var start = i.atStartOfDay();
        var end = f.atTime(LocalTime.MAX);
        return Optional.ofNullable(a.getBranch())
                .map(Branch::getCommits).orElseGet(List::of)
                .stream()
                .filter(c -> {
                    var dt = c.getCommitDate();
                    return (dt.isEqual(start) || dt.isAfter(start)) && (dt.isEqual(end) || dt.isBefore(end));
                })
                .toList();
    }

}
