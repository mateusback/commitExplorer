package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.commit.queryHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.commit.queries.ObterInformacoesCommitQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.commit.views.InformacoesCommitView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.QueryHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.CommitRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ObterInformacoesCommitQueryHandler implements QueryHandler<ObterInformacoesCommitQuery, InformacoesCommitView> {

    private final CommitRepository commitRepository;

    public ObterInformacoesCommitQueryHandler(CommitRepository commitRepository) {
        this.commitRepository = commitRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public InformacoesCommitView handle(ObterInformacoesCommitQuery query) {

        var commit = commitRepository.obterInformacoesCommit(query.getIdCommit());

        return new InformacoesCommitView(commit,
                getTotalLinhasAdicionadas(commit),
                getTotalLinhasRemovidas(commit));
    }


    public int getTotalLinhasAdicionadas(Commit commit) {
        int total = 0;
        for(var arquivo : commit.getArquivosAlterados()){
            total += arquivo.getQtdLinhasAdicionadas() != null ? arquivo.getQtdLinhasAdicionadas() : 0;
        }

        return total;
    }

    public int getTotalLinhasRemovidas(Commit commit) {
        int total = 0;
        for(var arquivo : commit.getArquivosAlterados()){
            total += arquivo.getQtdLinhasRemovidas() != null ? arquivo.getQtdLinhasRemovidas() : 0;
        }

        return total;
    }
}
