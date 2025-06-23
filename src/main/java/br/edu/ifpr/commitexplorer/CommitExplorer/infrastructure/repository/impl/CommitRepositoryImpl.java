package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.CommitRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.CommitEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.CommitMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.CommitJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CommitRepositoryImpl implements CommitRepository {

    private final CommitJpaRepository jpaRepository;
    private final CommitMapper mapper;

    public CommitRepositoryImpl(CommitJpaRepository jpaRepository, CommitMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByHashAndRepo(String hash, String repoUrl) {
        return jpaRepository.existsByHashAndBranch_Projeto_UrlRepo(hash, repoUrl);
    }

    @Override
    public void save(Commit commit) {
        CommitEntity entity = mapper.toEntity(commit);
        jpaRepository.save(entity);
    }

    @Override
    public Commit findByHashAndRepo(String hash, String repoUrl) {
        CommitEntity entity = jpaRepository.findByHashAndBranch_Projeto_UrlRepo(hash, repoUrl);
        if (entity == null) {
            throw new IllegalArgumentException("Commit não encontrado");
        }
        return mapper.toDomain(entity);
    }
}
