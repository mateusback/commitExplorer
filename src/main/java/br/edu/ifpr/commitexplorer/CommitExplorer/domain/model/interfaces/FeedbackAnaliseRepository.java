package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.FeedbackAnalise;

import java.util.List;

public interface FeedbackAnaliseRepository {
    FeedbackAnalise save(FeedbackAnalise f);
    List<FeedbackAnalise> obterPorIdAnalise(Long analiseId);
}
