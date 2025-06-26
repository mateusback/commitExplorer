package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Repositorio;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.RepositorioRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.RepositorioEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.RepositorioMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.RepositorioJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class RepositorioRepositoryImpl implements RepositorioRepository {
    private final RepositorioJpaRepository jpaRepository;
    private final RepositorioMapper mapper;

    public RepositorioRepositoryImpl(RepositorioJpaRepository jpaRepository, RepositorioMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    @Override
    public Repositorio save(Repositorio repositorio) {
        RepositorioEntity entity = mapper.toEntity(repositorio);
        RepositorioEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
