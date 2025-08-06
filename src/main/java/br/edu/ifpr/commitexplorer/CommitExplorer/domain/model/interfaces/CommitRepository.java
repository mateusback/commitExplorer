package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;

import java.util.List;

public interface CommitRepository {
    boolean existsByHashAndRepo(String hash, String repoUrl);

    Commit save(Commit commit);

    Commit update(Commit commit);

    Commit findByHashAndRepo(String hash, String repoUrl);

    Commit obterInformacoesCommit(Long idCommit);
}
