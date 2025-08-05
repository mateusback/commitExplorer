package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queryHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries.ObterAnalisesProjetoQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ObterAnalisesProjetoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ResumoAnaliseView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.QueryHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.AnaliseProjetoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.ProjetoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ObterAnalisesProjetoQueryHandler implements QueryHandler<ObterAnalisesProjetoQuery, ObterAnalisesProjetoView> {
    private final AnaliseProjetoRepository analiseProjetoRepository;
    private final ProjetoRepository projetoRepository;

    public ObterAnalisesProjetoQueryHandler(AnaliseProjetoRepository analiseProjetoRepository, ProjetoRepository projetoRepository) {
        this.analiseProjetoRepository = analiseProjetoRepository;
        this.projetoRepository = projetoRepository;
    }

    @Override
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
            int smells = analise.getQuantidadeCodeSmells() != null ? analise.getQuantidadeCodeSmells() : 0;
            int commits = analise.getTotalCommits() != null ? analise.getTotalCommits() : 0;
            int autores = analise.getTotalAutores() != null ? analise.getTotalAutores() : 0;
            double pontuacao = analise.getPontuacaoTotal() != null ? analise.getPontuacaoTotal() : 0;
            double complexidade = analise.getComplexidadeMedia() != null ? analise.getComplexidadeMedia() : 0;

            totalCodeSmells += smells;
            totalCommits += commits;
            totalAutores += autores;
            somaPontuacoes += pontuacao;
            somaComplexidade += complexidade;

            var branch = analise.getBranch();
            if (branch != null) {
                branches.add(branch.getNome());
            }

            var resumo = new ResumoAnaliseView();
            resumo.setDataAnalise(analise.getDataAnalise());
            resumo.setNomeBranch(branch != null ? branch.getNome() : null);
            resumo.setUrlRepo(branch != null && branch.getRepositorio() != null ? branch.getRepositorio().getUrlRepo() : null);
            resumo.setTotalCommits(commits);
            resumo.setTotalAutores(autores);
            resumo.setQuantidadeCodeSmells(smells);
            resumo.setComplexidadeMedia(complexidade);
            resumo.setPontuacaoTotal(pontuacao);
            resumo.setNomeProjeto(
                    analise.getSolicitacaoAnalise() != null
                            ? analise.getSolicitacaoAnalise().getProjetoUrl()
                            : null
            );

            resumoList.add(resumo);
        }

        view.setTotalAnalises(analises.size());
        view.setTotalAutores(totalAutores);
        view.setTotalCodeSmells(totalCodeSmells);
        view.setTotalCommits(totalCommits);
        view.setPontuacaoMedia((float) (somaPontuacoes / analises.size()));
        view.setBranchsAnalizadas(new ArrayList<>(branches));
        view.setAnalises(resumoList);

        return view;
    }
}