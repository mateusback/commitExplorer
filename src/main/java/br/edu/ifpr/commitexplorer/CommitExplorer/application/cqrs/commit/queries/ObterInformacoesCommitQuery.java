package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.commit.queries;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.commit.views.InformacoesCommitView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObterInformacoesCommitQuery implements Query<InformacoesCommitView> {

    private final long idCommit;

}
