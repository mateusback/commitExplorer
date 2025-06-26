package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Branch;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.BranchRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.BranchMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.BranchJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class BranchRepositoryImpl implements BranchRepository {
    private final BranchJpaRepository autorJpaRepository;
    private final BranchMapper autorMapper;

    public BranchRepositoryImpl(BranchJpaRepository autorJpaRepository, BranchMapper autorMapper) {
        this.autorJpaRepository = autorJpaRepository;
        this.autorMapper = autorMapper;
    }

    @Override
    public Branch save(Branch branch) {
        var entity = autorMapper.toEntity(branch);
        var saved = autorJpaRepository.save(entity);
        return autorMapper.toDomain(saved);
    }

}
