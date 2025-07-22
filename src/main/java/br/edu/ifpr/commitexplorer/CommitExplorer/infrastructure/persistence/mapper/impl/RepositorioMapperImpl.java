package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Repositorio;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.RepositorioEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.BranchMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.ProjetoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.RepositorioMapper;
import org.springframework.stereotype.Service;

@Service
public class RepositorioMapperImpl implements RepositorioMapper {

    private final ProjetoMapper projetoMapper;
    private final BranchMapper branchMapper;

    public RepositorioMapperImpl(ProjetoMapper projetoMapper, BranchMapper branchMapper) {
        this.projetoMapper = projetoMapper;
        this.branchMapper = branchMapper;
    }

    @Override
    public RepositorioEntity toEntity(Repositorio domain) {
        var entity = baseEntity(domain);
        if (domain.getProjeto() != null) {
            entity.setProjeto(projetoMapper.toEntityId(domain.getProjeto()));
        }
        if (domain.getBranches() != null) {
            entity.setBranches(
                domain.getBranches().stream()
                    .map(branchMapper::toEntityId)
                    .toList()
            );
        }
        return entity;
    }

    @Override
    public RepositorioEntity toEntityId(Repositorio domain) {
        return baseEntity(domain);
    }

    @Override
    public Repositorio toDomain(RepositorioEntity entity) {
        var domain = baseDomain(entity);
        if (entity.getBranches() != null) {
            domain.setBranches(
                entity.getBranches().stream()
                    .map(branchMapper::toDomainId)
                    .toList()
            );
        }
        return domain;
    }

    @Override
    public Repositorio toDomainId(RepositorioEntity entity) {
        return baseDomain(entity);
    }

    private RepositorioEntity baseEntity(Repositorio domain) {
        var entity = new RepositorioEntity();
        entity.setIdRepositorio(domain.getIdRepositorio());
        entity.setNome(domain.getNome());
        entity.setUrlRepo(domain.getUrlRepo());
        return entity;
    }

    private Repositorio baseDomain(RepositorioEntity entity) {
        var domain = new Repositorio(entity.getNome(), entity.getUrlRepo(), projetoMapper.toDomain(entity.getProjeto()));
        domain.setIdRepositorio(entity.getIdRepositorio());
        return domain;
    }
}
