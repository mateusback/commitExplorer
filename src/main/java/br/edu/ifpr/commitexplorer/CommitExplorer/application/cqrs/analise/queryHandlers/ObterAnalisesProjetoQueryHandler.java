package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queryHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries.ObterAnalisesProjetoQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.AutorView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.CommitView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ObterAnalisesProjetoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ResumoAnaliseView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.QueryHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.AnaliseProjetoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.BranchRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.RepositorioRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class ObterAnalisesProjetoQueryHandler implements QueryHandler<ObterAnalisesProjetoQuery, ObterAnalisesProjetoView> {
    private final AnaliseProjetoRepository analiseProjetoRepository;
    private final BranchRepository branchRepository;

    public ObterAnalisesProjetoQueryHandler(AnaliseProjetoRepository analiseProjetoRepository,
                                            BranchRepository branchRepository) {
        this.analiseProjetoRepository = analiseProjetoRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ObterAnalisesProjetoView handle(ObterAnalisesProjetoQuery command) {
        var analises = analiseProjetoRepository.findByProjetoId(command.getProjetoId());
        return montarResponse(analises);
    }

    private ObterAnalisesProjetoView montarResponse(List<AnaliseProjeto> analises) {
        var view = new ObterAnalisesProjetoView();

        if (analises == null || analises.isEmpty()) return view;

        int totalCodeSmells = 0;
        int totalCommits = 0;
        int totalAutores = 0;
        double somaPontuacoes = 0.0;
        double somaComplexidade = 0.0;
        Set<String> branches = new HashSet<>();
        List<ResumoAnaliseView> resumoList = new ArrayList<>();

        for (AnaliseProjeto analise : analises) {
            int smells = defaultInt(analise.getQuantidadeCodeSmells());
            int commits = defaultInt(analise.getTotalCommits());
            int autores = defaultInt(analise.getTotalAutores());
            double pontuacao = defaultDouble(analise.getPontuacaoTotal());
            double complexidade = defaultDouble(analise.getComplexidadeMedia());

            totalCodeSmells += smells;
            totalCommits += commits;
            totalAutores += autores;
            somaPontuacoes += pontuacao;
            somaComplexidade += complexidade;

            var branch = branchRepository.findById(analise.getBranch().getIdBranch());
            analise.setBranch(branch);
            if (branch != null) branches.add(branch.getNome());

            resumoList.add(montarResumoAnalise(analise));
        }

        view.setTotalAnalises(analises.size());
        view.setTotalAutores(totalAutores);
        view.setTotalCodeSmells(totalCodeSmells);
        view.setTotalCommits(totalCommits);
        view.setPontuacaoMedia((float) (somaPontuacoes / analises.size()));
        view.setBranchsAnalizadas(new ArrayList<>(branches));
        view.setAnalises(resumoList);
        view.setAutores(agruparAutores(analises));
        view.setCommits(agruparCommits(analises));

        return view;
    }

    private ResumoAnaliseView montarResumoAnalise(AnaliseProjeto analise) {
        var resumo = new ResumoAnaliseView();
        var branch = analise.getBranch();
        var solicitacao = analise.getSolicitacaoAnalise();
        var repositorioUrl = branch != null ? branch.getRepositorio().getUrlRepo() : null;

        resumo.setDataAnalise(analise.getDataAnalise());
        resumo.setDataInicio(solicitacao.getDataInicioAnalise().toString());
        resumo.setDataFim(solicitacao.getDataFim().toString());
        resumo.setNomeBranch(branch != null ? branch.getNome() : null);
        resumo.setUrlRepo(repositorioUrl);
        resumo.setTotalCommits(defaultInt(analise.getTotalCommits()));
        resumo.setTotalAutores(defaultInt(analise.getTotalAutores()));
        resumo.setQuantidadeCodeSmells(defaultInt(analise.getQuantidadeCodeSmells()));
        resumo.setComplexidadeMedia(defaultDouble(analise.getComplexidadeMedia()));
        resumo.setPontuacaoTotal(defaultDouble(analise.getPontuacaoTotal()));
        resumo.setNomeProjeto(analise.getSolicitacaoAnalise() != null
                ? analise.getSolicitacaoAnalise().getProjetoUrl()
                : null);
        resumo.setId(analise.getIdAnaliseProjeto());

        return resumo;
    }

    private List<AutorView> agruparAutores(List<AnaliseProjeto> analises) {
        Map<String, AutorView> autoresMap = new HashMap<>();

        for (var analise : analises) {
            analise.setBranch(branchRepository.findById(analise.getBranch().getIdBranch()));

            var branch = analise.getBranch();
            if (branch == null || branch.getCommits() == null) continue;

            for (var commit : branch.getCommits()) {
                var autor = commit.getAutor();
                if (autor == null || autor.getEmail() == null) continue;

                var email = autor.getEmail();
                var autorView = autoresMap.getOrDefault(email, new AutorView(
                        autor.getIdAutor(),
                        autor.getNome(),
                        email,
                        0, 0, 0
                ));

                autorView.setTotalCommits(autorView.getTotalCommits() + 1);

                if (commit.getArquivosAlterados() != null) {
                    for (var arquivo : commit.getArquivosAlterados()) {
                        autorView.setTotalLinhasAdicionadas(
                                autorView.getTotalLinhasAdicionadas() + defaultInt(arquivo.getQtdLinhasAdicionadas())
                        );
                        autorView.setTotalLinhasRemovidas(
                                autorView.getTotalLinhasRemovidas() + defaultInt(arquivo.getQtdLinhasRemovidas())
                        );
                    }
                }

                autoresMap.put(email, autorView);
            }
        }

        return new ArrayList<>(autoresMap.values());
    }

    private List<CommitView> agruparCommits(List<AnaliseProjeto> analises) {
        List<CommitView> commitViews = new ArrayList<>();

        for (var analise : analises) {
            analise.setBranch(branchRepository.findById(analise.getBranch().getIdBranch()));

            var branch = analise.getBranch();
            if (branch == null || branch.getCommits() == null) continue;

            for (var commit : branch.getCommits()) {
                var commitView = new CommitView();
                commitView.setId(commit.getIdCommit());
                commitView.setHash(commit.getHash());
                commitView.setMensagem(commit.getMensagem());
                commitView.setAutor(commit.getAutor() != null ? commit.getAutor().getNome() : null);
                commitView.setDataCommit(commit.getCommitDate() != null ? commit.getCommitDate().toString() : null);
                commitView.setPontuacao(commit.getPontuacao());
                commitView.setComplexidadeGeral(commit.getComplexidadeGeral());
                commitView.setEhMerge(commit.ehMerge());

                int adicionadas = 0;
                int removidas = 0;

                if (commit.getArquivosAlterados() != null) {
                    for (var arquivo : commit.getArquivosAlterados()) {
                        adicionadas += defaultInt(arquivo.getQtdLinhasAdicionadas());
                        removidas += defaultInt(arquivo.getQtdLinhasRemovidas());
                    }
                }

                commitView.setLinhasAdicionadas(adicionadas);
                commitView.setLinhasRemovidas(removidas);
                commitView.setTotalArquivosAlterados(commit.getArquivosAlterados() != null ? commit.getArquivosAlterados().size() : 0);
                commitViews.add(commitView);
            }
        }

        return commitViews;
    }

    private int defaultInt(Integer value) {
        return value != null ? value : 0;
    }

    private double defaultDouble(Double value) {
        return value != null ? value : 0.0;
    }
}