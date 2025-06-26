package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.ProjetoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ProjetoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.ProjetoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.ProjetoJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class ProjetoRepositoryImpl implements ProjetoRepository {
    private final ProjetoJpaRepository jpaRepository;
    private final ProjetoMapper mapper;

    public ProjetoRepositoryImpl(ProjetoJpaRepository jpaRepository, ProjetoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    @Override
    public Projeto save(Projeto repositorio) {
        ProjetoEntity entity = mapper.toEntity(repositorio);
        ProjetoEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}