package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.queryHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.queries.ObterDashboardQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.views.*;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.QueryHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.StatusSolicitacao;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.AnaliseProjetoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.ProjetoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.SolicitacaoAnaliseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ObterDashboardQueryHandler implements QueryHandler<ObterDashboardQuery, ObterDashboardView> {

    private final ProjetoRepository projetoRepository;
    private final AnaliseProjetoRepository analiseProjetoRepository;
    private final SolicitacaoAnaliseRepository solicitacaoAnaliseRepository;

    public ObterDashboardQueryHandler(ProjetoRepository projetoRepository,
                                      AnaliseProjetoRepository analiseProjetoRepository,
                                      SolicitacaoAnaliseRepository solicitacaoAnaliseRepository) {
        this.projetoRepository = projetoRepository;
        this.analiseProjetoRepository = analiseProjetoRepository;
        this.solicitacaoAnaliseRepository = solicitacaoAnaliseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ObterDashboardView handle(ObterDashboardQuery query) {
        log.info("Obtendo dashboard para usuarioId={}, ehProfessor={}", query.getUsuarioId(), query.isEhProfessor());

        var projetos = projetoRepository.findAllByOwnerId(query.getUsuarioId());
        var todasAnalises = analiseProjetoRepository.findAll();
        var solicitacoesDoUsuario = solicitacaoAnaliseRepository.obterSolicitacoesAnalisePorSolicitante(query.getUsuarioId());

        var analisesDoUsuario = todasAnalises.stream()
                .filter(a -> a.getUsuario() != null && a.getUsuario().getId().equals(query.getUsuarioId()))
                .collect(Collectors.toList());

        var view = new ObterDashboardView();
        view.setEstatisticas(montarEstatisticas(projetos, analisesDoUsuario, solicitacoesDoUsuario));
        view.setAnalisesRecentes(montarAnalisesRecentes(analisesDoUsuario));
        view.setTopProjetos(montarTopProjetos(projetos, analisesDoUsuario));
        view.setAtividadeTendencia(montarAtividadeTendencia(analisesDoUsuario));

        if (query.isEhProfessor()) {
            view.setVisaoGeralAlunos(montarVisaoGeralAlunos(todasAnalises));
        }

        return view;
    }

    private EstatisticasView montarEstatisticas(List<Projeto> projetos,
                                                List<AnaliseProjeto> analises,
                                                List<br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise> solicitacoes) {

        int totalProjetos = projetos.size();
        int totalAnalises = analises.size();

        long analisesEmAndamento = solicitacoes.stream()
                .filter(s -> s.getStatus() == StatusSolicitacao.EM_ANDAMENTO)
                .count();

        long analisesFalhadas = solicitacoes.stream()
                .filter(s -> s.getStatus() == StatusSolicitacao.FALHA)
                .count();

        long analisesCompletas = solicitacoes.stream()
                .filter(s -> s.getStatus() == StatusSolicitacao.CONCLUIDA)
                .count();

        int totalCommits = analises.stream()
                .mapToInt(a -> valorPadrao(a.getTotalCommits()))
                .sum();

        int totalAutores = (int) analises.stream()
                .mapToInt(a -> valorPadrao(a.getTotalAutores()))
                .distinct()
                .count();

        double pontuacaoMedia = analises.stream()
                .filter(a -> a.getPontuacaoTotal() != null)
                .mapToDouble(AnaliseProjeto::getPontuacaoTotal)
                .average()
                .orElse(0.0);

        int totalCodeSmells = analises.stream()
                .mapToInt(a -> valorPadrao(a.getQuantidadeCodeSmells()))
                .sum();

        return new EstatisticasView(
                totalProjetos,
                totalAnalises,
                (int) analisesEmAndamento,
                (int) analisesFalhadas,
                (int) analisesCompletas,
                totalCommits,
                totalAutores,
                arredondar(pontuacaoMedia),
                totalCodeSmells
        );
    }

    private List<AnaliseRecenteView> montarAnalisesRecentes(List<AnaliseProjeto> analises) {
        return analises.stream()
                .sorted((a1, a2) -> {
                    LocalDateTime d1 = a1.getDataAnalise() != null ? a1.getDataAnalise() : LocalDateTime.MIN;
                    LocalDateTime d2 = a2.getDataAnalise() != null ? a2.getDataAnalise() : LocalDateTime.MIN;
                    return d2.compareTo(d1);
                })
                .limit(5)
                .map(this::converterParaAnaliseRecenteView)
                .collect(Collectors.toList());
    }

    private AnaliseRecenteView converterParaAnaliseRecenteView(AnaliseProjeto analise) {
        var view = new AnaliseRecenteView();
        view.setId(analise.getIdAnaliseProjeto());
        view.setNomeProjeto(analise.getProjeto() != null ? analise.getProjeto().getNome() : "Desconhecido");
        view.setUrlRepositorio(analise.getSolicitacaoAnalise() != null ?
                analise.getSolicitacaoAnalise().getRepositorioUrl() : "");
        view.setStatus(analise.getSolicitacaoAnalise() != null ?
                analise.getSolicitacaoAnalise().getStatus().name() : "DESCONHECIDO");
        view.setPontuacao(analise.getPontuacaoTotal());
        view.setTotalCommits(analise.getTotalCommits());
        view.setDataCriacao(analise.getDataAnalise());
        view.setNomeUsuario(analise.getUsuario() != null ? analise.getUsuario().getName() : "Desconhecido");
        return view;
    }

    private List<ProjetoPorPontuacaoView> montarTopProjetos(List<Projeto> projetos, List<AnaliseProjeto> analises) {
        Map<Long, List<AnaliseProjeto>> analisesPorProjeto = analises.stream()
                .filter(a -> a.getProjeto() != null)
                .collect(Collectors.groupingBy(a -> a.getProjeto().getIdProjeto()));

        return projetos.stream()
                .map(projeto -> {
                    List<AnaliseProjeto> analisesProj = analisesPorProjeto.getOrDefault(
                            projeto.getIdProjeto(),
                            Collections.emptyList()
                    );

                    double pontuacaoMedia = analisesProj.stream()
                            .filter(a -> a.getPontuacaoTotal() != null)
                            .mapToDouble(AnaliseProjeto::getPontuacaoTotal)
                            .average()
                            .orElse(0.0);

                    LocalDateTime ultimaAnalise = analisesProj.stream()
                            .map(AnaliseProjeto::getDataAnalise)
                            .filter(Objects::nonNull)
                            .max(LocalDateTime::compareTo)
                            .orElse(null);

                    var view = new ProjetoPorPontuacaoView();
                    view.setId(projeto.getIdProjeto());
                    view.setNome(projeto.getNome());
                    view.setTotalAnalises(analisesProj.size());
                    view.setPontuacaoMedia(arredondar(pontuacaoMedia));
                    view.setDataUltimaAnalise(ultimaAnalise);
                    return view;
                })
                .filter(p -> p.getTotalAnalises() > 0)
                .sorted((p1, p2) -> Double.compare(p2.getPontuacaoMedia(), p1.getPontuacaoMedia()))
                .limit(5)
                .collect(Collectors.toList());
    }

    private AtividadeTendenciaView montarAtividadeTendencia(List<AnaliseProjeto> analises) {
        LocalDate hoje = LocalDate.now();
        List<LocalDate> datas = new ArrayList<>();
        List<Integer> commits = new ArrayList<>();
        List<Integer> analisesLista = new ArrayList<>();

        for (int i = 29; i >= 0; i--) {
            LocalDate data = hoje.minusDays(i);
            datas.add(data);

            long analisesNoDia = analises.stream()
                    .filter(a -> a.getDataAnalise() != null)
                    .filter(a -> a.getDataAnalise().toLocalDate().equals(data))
                    .count();
            analisesLista.add((int) analisesNoDia);

            int commitsNoDia = analises.stream()
                    .filter(a -> a.getDataAnalise() != null)
                    .filter(a -> a.getDataAnalise().toLocalDate().equals(data))
                    .mapToInt(a -> valorPadrao(a.getTotalCommits()))
                    .sum();
            commits.add(commitsNoDia);
        }

        return new AtividadeTendenciaView(datas, commits, analisesLista);
    }

    private VisaoGeralAlunosView montarVisaoGeralAlunos(List<AnaliseProjeto> todasAnalises) {
        Map<Long, List<AnaliseProjeto>> analisesPorUsuario = todasAnalises.stream()
                .filter(a -> a.getUsuario() != null)
                .collect(Collectors.groupingBy(a -> a.getUsuario().getId()));

        int totalAlunos = analisesPorUsuario.size();

        LocalDateTime dataLimite = LocalDateTime.now().minusDays(30);
        long alunosAtivos = todasAnalises.stream()
                .filter(a -> a.getUsuario() != null && a.getDataAnalise() != null)
                .filter(a -> a.getDataAnalise().isAfter(dataLimite))
                .map(a -> a.getUsuario().getId())
                .distinct()
                .count();

        long alunosComProjetos = analisesPorUsuario.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .count();

        double pontuacaoMediaAlunos = todasAnalises.stream()
                .filter(a -> a.getPontuacaoTotal() != null)
                .mapToDouble(AnaliseProjeto::getPontuacaoTotal)
                .average()
                .orElse(0.0);

        List<AlunoView> topAlunos = analisesPorUsuario.entrySet().stream()
                .map(entry -> montarAlunoView(entry.getKey(), entry.getValue()))
                .sorted((a1, a2) -> Double.compare(a2.getPontuacaoMedia(), a1.getPontuacaoMedia()))
                .limit(5)
                .collect(Collectors.toList());

        var view = new VisaoGeralAlunosView();
        view.setTotalAlunos(totalAlunos);
        view.setAlunosAtivos((int) alunosAtivos);
        view.setAlunosComProjetos((int) alunosComProjetos);
        view.setPontuacaoMediaAlunos(arredondar(pontuacaoMediaAlunos));
        view.setTopAlunos(topAlunos);
        return view;
    }

    private AlunoView montarAlunoView(Long usuarioId, List<AnaliseProjeto> analisesUsuario) {
        int totalProjetos = (int) analisesUsuario.stream()
                .filter(a -> a.getProjeto() != null)
                .map(a -> a.getProjeto().getIdProjeto())
                .distinct()
                .count();

        double pontuacaoMedia = analisesUsuario.stream()
                .filter(a -> a.getPontuacaoTotal() != null)
                .mapToDouble(AnaliseProjeto::getPontuacaoTotal)
                .average()
                .orElse(0.0);

        String nomeUsuario = analisesUsuario.stream()
                .filter(a -> a.getUsuario() != null)
                .map(a -> a.getUsuario().getName())
                .findFirst()
                .orElse("Desconhecido");

        var view = new AlunoView();
        view.setId(usuarioId);
        view.setNome(nomeUsuario);
        view.setTotalProjetos(totalProjetos);
        view.setPontuacaoMedia(arredondar(pontuacaoMedia));
        return view;
    }

    private int valorPadrao(Integer valor) {
        return valor != null ? valor : 0;
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
