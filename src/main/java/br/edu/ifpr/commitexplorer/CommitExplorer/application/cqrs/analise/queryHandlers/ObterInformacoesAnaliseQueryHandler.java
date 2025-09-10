package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queryHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries.ObterInformacoesAnaliseQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.*;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.QueryHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.*;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoFeedback;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ObterInformacoesAnaliseQueryHandler
        implements QueryHandler<ObterInformacoesAnaliseQuery, ObterInformacoesAnaliseView> {

    private final AnaliseProjetoRepository analiseProjetoRepository;
    private final BranchRepository branchRepository;

    @Override
    @Transactional(readOnly = true)
    public ObterInformacoesAnaliseView handle(ObterInformacoesAnaliseQuery query) {
        var analise = analiseProjetoRepository.findById(query.getId());
        if (analise == null) return new ObterInformacoesAnaliseView();

        var branch = branchRepository.findById(analise.getBranch().getIdBranch());
        if (branch == null) return new ObterInformacoesAnaliseView();

        List<Commit> commitsMutaveis = new ArrayList<>(branch.getCommits());
        filtrarCommitsParaPeriodo(commitsMutaveis, analise.getSolicitacaoAnalise());

        atribuirCommitsParaAutores(commitsMutaveis, analise.getFeedbacks().stream()
                .filter(f -> f.getTipo() == TipoFeedback.AUTOR)
                .map(FeedbackAnalise::getAutor)
                .toList());

        branch.setCommits(commitsMutaveis);
        analise.setBranch(branch);

        var view = new ObterInformacoesAnaliseView(analise, branch);

        return view;
    }

    private void atribuirCommitsParaAutores (List<Commit> commits, List<Autor> autores) {
        var mapaAutores = new HashMap<String, Autor>();
        for (var autor : autores) {
            mapaAutores.put(autor.getEmail(), autor);
            autor.setCommits(new ArrayList<>());
        }

        for (var commit : commits) {
            var autor = mapaAutores.get(commit.getAutor().getEmail());
            if (autor != null) {
                autor.getCommits().add(commit);
            }
        }
    }

    private void filtrarCommitsParaPeriodo(List<Commit> commits, SolicitacaoAnalise solicitacao) {
        var dataInicio = solicitacao.getDataInicio().atStartOfDay();
        var dataFim = solicitacao.getDataFim().atTime(23, 59, 59);
        commits.removeIf(c -> c.getCommitDate().isBefore(dataInicio) ||
                c.getCommitDate().isAfter(dataFim));
    }

}
