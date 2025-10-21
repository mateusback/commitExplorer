package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Branch;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.BranchRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.BranchMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.BranchJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BranchRepositoryImpl implements BranchRepository {
    private final BranchJpaRepository autorJpaRepository;
    private final BranchMapper autorMapper;

    public BranchRepositoryImpl(BranchJpaRepository autorJpaRepository, BranchMapper autorMapper) {
        this.autorJpaRepository = autorJpaRepository;
        this.autorMapper = autorMapper;
    }

    @Override
    @Transactional
    public Branch save(Branch branch) {
        var entity = autorMapper.toEntity(branch);
        var saved = autorJpaRepository.save(entity);
        return autorMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public Branch findById(long id) {
        var optionalEntity = autorJpaRepository.findByIdBranch(id);
        var entity = optionalEntity.orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));
        return autorMapper.toDomain(entity);
    }

}
