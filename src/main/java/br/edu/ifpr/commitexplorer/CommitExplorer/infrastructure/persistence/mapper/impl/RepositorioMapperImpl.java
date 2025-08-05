package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Repositorio;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.RepositorioEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.BranchMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.ProjetoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.RepositorioMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            entity.setBranches(branchMapper.toEntity(domain.getBranches()));
        }
        return entity;
    }

    @Override
    public RepositorioEntity toEntityId(Repositorio domain) {
        return baseEntity(domain);
    }

    @Override
    public List<RepositorioEntity> toEntity(List<Repositorio> domain) {
        if (domain == null || domain.isEmpty()) {
            return new ArrayList<>();
        }
        return domain.stream()
                .map(this::toEntityId)
                .toList();
    }

    @Override
    public Repositorio toDomain(RepositorioEntity entity) {
        var domain = baseDomain(entity);
        if (entity.getProjeto() != null) {
            domain.setProjeto(projetoMapper.toDomainId(entity.getProjeto()));
        }
        if (entity.getBranches() != null) {
            domain.setBranches(branchMapper.toDomain(entity.getBranches()));
        }

        return domain;
    }

    @Override
    public Repositorio toDomainId(RepositorioEntity entity) {
        return baseDomain(entity);
    }

    @Override
    public List<Repositorio> toDomain(List<RepositorioEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return entityList.stream()
                .map(this::toDomainId)
                .toList();
    }

    private RepositorioEntity baseEntity(Repositorio domain) {
        var entity = new RepositorioEntity();
        entity.setIdRepositorio(domain.getIdRepositorio());
        entity.setNome(domain.getNome());
        entity.setUrlRepo(domain.getUrlRepo());
        return entity;
    }

    private Repositorio baseDomain(RepositorioEntity entity) {
        var domain = new Repositorio(entity.getNome(), entity.getUrlRepo(), projetoMapper.toDomainId(entity.getProjeto()));
        domain.setIdRepositorio(entity.getIdRepositorio());
        return domain;
    }
}
