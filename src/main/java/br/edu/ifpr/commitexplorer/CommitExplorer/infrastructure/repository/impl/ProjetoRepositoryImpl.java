package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.ProjetoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ProjetoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.ProjetoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.ProjetoJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ProjetoRepositoryImpl implements ProjetoRepository {
    private final ProjetoJpaRepository jpaRepository;
    private final ProjetoMapper mapper;

    public ProjetoRepositoryImpl(ProjetoJpaRepository jpaRepository, ProjetoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Projeto save(Projeto repositorio) {
        ProjetoEntity entity = mapper.toEntity(repositorio);
        ProjetoEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional
    public List<Projeto> findAll() {
        return jpaRepository.findAllByDeletadoFalse().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public Projeto findById(Long id) {
        return jpaRepository.findByIdProjetoAndDeletadoFalse(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado com o ID: " + id));
    }

    @Override
    @Transactional
    public List<Projeto> findAllByOwnerId(Long ownerId) {
        return jpaRepository.findAllByUsuarioIdAndDeletadoFalse(ownerId)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setDeletado(true);
            jpaRepository.save(entity);
        });
    }
}