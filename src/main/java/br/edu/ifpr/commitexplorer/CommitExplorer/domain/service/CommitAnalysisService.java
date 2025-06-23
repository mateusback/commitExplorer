package br.edu.ifpr.commitexplorer.CommitExplorer.domain.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.response.AnalysisInfo;

import java.util.List;

public interface CommitAnalysisService {
    void adicionarCodeSmellsAoCommit(Commit commit, List<AnalysisInfo> smells);
}
