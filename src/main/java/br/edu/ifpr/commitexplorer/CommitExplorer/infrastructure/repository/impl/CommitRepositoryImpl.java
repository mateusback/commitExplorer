package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.CommitRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.CommitEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.BranchMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.CommitMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.BranchJpaRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.CommitJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CommitRepositoryImpl implements CommitRepository {

    private final CommitJpaRepository jpaRepository;
    private final CommitMapper mapper;

    public CommitRepositoryImpl(CommitJpaRepository jpaRepository, BranchJpaRepository branchJpaRepository, CommitMapper mapper, BranchMapper branchMapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByHashAndRepo(String hash, String repoUrl) {
        return jpaRepository.existsByHashAndBranch_Repositorio_UrlRepo(hash, repoUrl);
    }

    @Override
    public Commit save(Commit commit) {
        CommitEntity entity = mapper.toEntity(commit);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Commit update(Commit commit) {
        CommitEntity entity = mapper.toEntity(commit);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Commit findByHashAndRepo(String hash, String repoUrl) {
        CommitEntity entity = jpaRepository.findByHashAndBranch_Repositorio_UrlRepo(hash, repoUrl);
        if (entity == null) {
            throw new IllegalArgumentException("Commit não encontrado");
        }
        return mapper.toDomain(entity);
    }
}
