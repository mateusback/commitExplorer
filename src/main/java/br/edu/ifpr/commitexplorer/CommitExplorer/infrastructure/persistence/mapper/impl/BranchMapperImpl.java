package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Branch;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.BranchEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AnaliseCodigoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AnaliseProjetoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.BranchMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.CommitMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class BranchMapperImpl implements BranchMapper {

    private final AnaliseProjetoMapper analiseProjetoMapper;
    private final CommitMapper commitMapper;
    private final AnaliseCodigoMapper analiseCodigoMapper;

    public BranchMapperImpl(
            @Lazy AnaliseProjetoMapper analiseProjetoMapper,
            CommitMapper commitMapper,
            AnaliseCodigoMapper analiseCodigoMapper
    ) {
        this.analiseProjetoMapper = analiseProjetoMapper;
        this.commitMapper = commitMapper;
        this.analiseCodigoMapper = analiseCodigoMapper;
    }

    @Override
    public BranchEntity toEntity(Branch domain) {
        var entity = baseEntity(domain);
        if (domain.getAnalises() != null) {
            entity.setAnalises(
                    domain.getAnalises().stream()
                            .map(analiseProjetoMapper::toEntity)
                            .toList()
            );
        }
        if (domain.getCommits() != null) {
            entity.setCommits(
                    domain.getCommits().stream()
                            .map(commitMapper::toEntityId)
                            .toList()
            );
        }
//        todo -fazer mapper do repositorio
//        if (domain.getRepositorio())
        return entity;
    }

    @Override
    public BranchEntity toEntityId(Branch domain) {
        return baseEntity(domain);
    }

    @Override
    public Branch toDomain(BranchEntity entity) {
        var domain = baseDomain(entity);
        if (entity.getAnalises() != null) {
            domain.setAnalises(
                    entity.getAnalises().stream()
                            .map(analiseProjetoMapper::toDomain)
                            .toList()
            );
        }
        if (entity.getCommits() != null) {
            domain.setCommits(
                    entity.getCommits().stream()
                            .map(commitMapper::toDomainId)
                            .toList()
            );
        }
        return domain;
    }

    @Override
    public Branch toDomainId(BranchEntity entity) {
        return baseDomain(entity);
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
