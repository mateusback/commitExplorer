package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.CommitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitJpaRepository  extends JpaRepository<CommitEntity, Long> {
    boolean existsByHashAndBranch_Repositorio_UrlRepo(String hash, String repositorioUrl);
    CommitEntity findByHashAndBranch_Repositorio_UrlRepo(String hash, String urlRepo);
}