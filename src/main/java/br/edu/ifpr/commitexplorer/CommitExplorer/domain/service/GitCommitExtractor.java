package br.edu.ifpr.commitexplorer.CommitExplorer.domain.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public interface GitCommitExtractor {
    List<Commit> extrairCommitsComDiffs(File repositorioClonado, String branch, LocalDate dataInicio, LocalDate dataFim);

}