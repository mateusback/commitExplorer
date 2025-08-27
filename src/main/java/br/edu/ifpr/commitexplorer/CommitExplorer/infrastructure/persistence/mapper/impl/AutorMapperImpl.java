package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AutorEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AutorMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.CommitMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AutorMapperImpl implements AutorMapper {

    private final CommitMapper commitMapper;

    public AutorMapperImpl(@Lazy CommitMapper commitMapper) {
        this.commitMapper = commitMapper;
    }

    @Override
    public AutorEntity toEntity(Autor domain) {
        var entity = baseEntity(domain);
        if (domain.getCommits() != null) {
            entity.setCommits(commitMapper.toEntity(domain.getCommits()));
        }
        return entity;
    }

    @Override
    public AutorEntity toEntityId(Autor domain) {
        return baseEntity(domain);
    }

    @Override
    public List<AutorEntity> toEntity(List<Autor> domainList) {
        if (domainList == null || domainList.isEmpty()) {
            return new ArrayList<>();
        }
        return domainList.stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public Autor toDomain(AutorEntity entity) {
        //TODO - DAR UMA OLAHDA NESSE CARA AQUI;
        var domain = baseDomain(entity);
        if (entity.getCommits() != null) {
            domain.setCommits(commitMapper.toDomain(entity.getCommits()));
        } else{
            domain.setCommits(new ArrayList<>());
        }
        return domain;
    }

    @Override
    public Autor toDomainId(AutorEntity entity) {
        return baseDomain(entity);
    }

    @Override
    public List<Autor> toDomain(List<AutorEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return entityList.stream()
                .map(this::toDomain)
                .toList();
    }


    private AutorEntity baseEntity(Autor domain) {
        var entity = new AutorEntity();
        entity.setIdAutor(domain.getIdAutor());
        entity.setEmail(domain.getEmail());
        entity.setNome(domain.getNome());
        return entity;
    }

    private Autor baseDomain(AutorEntity entity) {
        var domain = new Autor(entity.getNome(), entity.getEmail());
        domain.setIdAutor(entity.getIdAutor());
        return domain;
    }
}
