package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;

public interface CommitRepository {
    boolean existsByHashAndRepo(String hash, String repoUrl);

    void save(Commit commit);

    Commit findByHashAndRepo(String hash, String repoUrl);
}
