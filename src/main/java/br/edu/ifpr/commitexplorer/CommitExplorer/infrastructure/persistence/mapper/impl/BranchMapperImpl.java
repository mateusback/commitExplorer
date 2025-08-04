package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Branch;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.BranchEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BranchMapperImpl implements BranchMapper {

    private final AnaliseProjetoMapper analiseProjetoMapper;
    private final CommitMapper commitMapper;
    private final AnaliseCodigoMapper analiseCodigoMapper;
    private final RepositorioMapper repositorioMapper;

    public BranchMapperImpl(
            @Lazy AnaliseProjetoMapper analiseProjetoMapper,
            CommitMapper commitMapper,
            AnaliseCodigoMapper analiseCodigoMapper,
            @Lazy RepositorioMapper repositorioMapper
    ) {
        this.analiseProjetoMapper = analiseProjetoMapper;
        this.commitMapper = commitMapper;
        this.analiseCodigoMapper = analiseCodigoMapper;
        this.repositorioMapper = repositorioMapper;
    }

    @Override
    public BranchEntity toEntity(Branch domain) {
        var entity = baseEntity(domain);
        if (domain.getAnalises() != null) {
            entity.setAnalises(analiseProjetoMapper.toEntity(domain.getAnalises()));
        }
        if (domain.getCommits() != null) {
            entity.setCommits(commitMapper.toEntity(domain.getCommits()));
        }
        entity.setRepositorio(repositorioMapper.toEntity(domain.getRepositorio()));
        return entity;
    }

    @Override
    public BranchEntity toEntityId(Branch domain) {
        return baseEntity(domain);
    }

    @Override
    public List<BranchEntity> toEntity(List<Branch> domainList) {
        if (domainList == null || domainList.isEmpty()) {
            return new ArrayList<>();
        }
        return domainList.stream()
                .map(this::toEntityId)
                .toList();
    }

    @Override
    public Branch toDomain(BranchEntity entity) {
        var domain = baseDomain(entity);
        if (entity.getAnalises() != null) {
            domain.setAnalises(analiseProjetoMapper.toDomain(entity.getAnalises()));
        }
        if (entity.getCommits() != null) {
            domain.aplicarCommits(commitMapper.toDomain(entity.getCommits()));
        }
        domain.setRepositorio(repositorioMapper.toDomain(entity.getRepositorio()));
        return domain;
    }

    @Override
    public Branch toDomainId(BranchEntity entity) {
        return baseDomain(entity);
    }

    @Override
    public List<Branch> toDomain(List<BranchEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return entityList.stream()
                .map(this::toDomainId)
                .toList();
    }


    private BranchEntity baseEntity(Branch domain) {
        var entity = new BranchEntity();
        entity.setIdBranch(domain.getIdBranch());
        entity.setNome(domain.getNome());
        entity.setDataCriacao(domain.getDataCriacao());
        entity.setDataUltimaAnalise(domain.getDataUltimaAnalise());
        return entity;
    }

    private Branch baseDomain(BranchEntity entity) {
        var domain = new Branch(entity.getNome());
        domain.setIdBranch(entity.getIdBranch());
        domain.setDataCriacao(entity.getDataCriacao());
        domain.setDataUltimaAnalise(entity.getDataUltimaAnalise());
        return domain;
    }
}
