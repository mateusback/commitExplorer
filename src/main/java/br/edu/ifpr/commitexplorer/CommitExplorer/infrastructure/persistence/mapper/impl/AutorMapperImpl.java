package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AutorEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AutorMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.CommitMapper;
import org.springframework.stereotype.Service;

@Service
public class AutorMapperImpl implements AutorMapper {

    private final CommitMapper commitMapper;

    public AutorMapperImpl(CommitMapper commitMapper) {
        this.commitMapper = commitMapper;
    }

    @Override
    public AutorEntity toEntity(Autor domain) {
        var entity = baseEntity(domain);
        // todo - trocar para um mapper de lista
        if (domain.getCommits() != null) {
            entity.setCommits(domain.getCommits().stream()
                    .map(commitMapper::toEntityId)
                    .toList());
        }
        return entity;
    }

    @Override
    public AutorEntity toEntityId(Autor domain) {
        return baseEntity(domain);
    }

    @Override
    public Autor toDomain(AutorEntity entity) {
        var domain = baseDomain(entity);
        // todo - trocar para um mapper de lista
        if (entity.getCommits() != null) {
            domain.setCommits(entity.getCommits().stream()
                    .map(commitMapper::toDomainId)
                    .toList());
        }
        return domain;
    }

    @Override
    public Autor toDomainId(AutorEntity entity) {
        return baseDomain(entity);
    }


    private AutorEntity baseEntity(Autor domain) {
        var entity = new AutorEntity();
        entity.setIdAutor(domain.getIdAutor());
        entity.setEmail(domain.getEmail());
        entity.setName(domain.getName());
        return entity;
    }

    private Autor baseDomain(AutorEntity entity) {
        var domain = new Autor(entity.getName(), entity.getEmail());
        domain.setIdAutor(entity.getIdAutor());
        return domain;
    }
}
