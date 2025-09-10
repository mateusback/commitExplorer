package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.AutorRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AutorEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AutorMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.AutorJpaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class AutorRepositoryImpl implements AutorRepository {
    private final AutorJpaRepository autorJpaRepository;
    private final AutorMapper autorMapper;

    public AutorRepositoryImpl(AutorJpaRepository autorJpaRepository, AutorMapper autorMapper) {
        this.autorJpaRepository = autorJpaRepository;
        this.autorMapper = autorMapper;
    }

    @Override
    public Autor buscarPorEmail(String email) {
        AutorEntity entity = autorJpaRepository.findByEmail(email).orElse(null);
        return entity != null ? autorMapper.toDomain(entity) : null;
    }

    @Override
    public Autor buscarPorId(Long id) {
        AutorEntity entity = autorJpaRepository.findById(id).orElse(null);
        return entity != null ? autorMapper.toDomain(entity) : null;
    }

    @Override
    public Autor save(Autor autor) {
        AutorEntity entity = autorMapper.toEntity(autor);
        AutorEntity saved = autorJpaRepository.save(entity);
        return autorMapper.toDomain(saved);
    }

    @Override
    public Autor buscarOuCriarPorEmail(String nome, String email) {
        AutorEntity entity = autorJpaRepository.findByEmail(email).orElse(null);
        if (entity != null) {
            return autorMapper.toDomain(entity);
        }
        try {
            AutorEntity novoAutor = new AutorEntity();
            novoAutor.setNome(nome);
            novoAutor.setEmail(email);
            AutorEntity salvo = autorJpaRepository.save(novoAutor);
            return autorMapper.toDomain(salvo);
        } catch (DataIntegrityViolationException e) {
            AutorEntity existente = autorJpaRepository.findByEmail(email).orElse(null);
            return existente != null ? autorMapper.toDomain(existente) : null;
        }
    }
}
