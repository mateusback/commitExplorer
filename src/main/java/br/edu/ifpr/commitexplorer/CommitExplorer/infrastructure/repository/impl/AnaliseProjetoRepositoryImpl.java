package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.AnaliseProjetoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AnaliseProjetoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.AnaliseProjetoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnaliseProjetoRepositoryImpl implements AnaliseProjetoRepository {
    private final AnaliseProjetoJpaRepository analiseProjetoJpaRepository;
    private final AnaliseProjetoMapper analiseProjetoMapper;

    public AnaliseProjetoRepositoryImpl(AnaliseProjetoJpaRepository analiseProjetoJpaRepository,
                                        AnaliseProjetoMapper analiseProjetoMapper) {
        this.analiseProjetoJpaRepository = analiseProjetoJpaRepository;
        this.analiseProjetoMapper = analiseProjetoMapper;
    }


    @Override
    public AnaliseProjeto save(AnaliseProjeto analiseProjeto) {
        var entity = analiseProjetoMapper.toEntity(analiseProjeto);
        var saved = analiseProjetoJpaRepository.save(entity);
        return analiseProjetoMapper.toDomain(saved);
    }

    @Override
    public AnaliseProjeto findById(Long id) {
        return analiseProjetoJpaRepository.findById(id)
                .map(analiseProjetoMapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<AnaliseProjeto> findAll() {
        return analiseProjetoJpaRepository.findAll()
                .stream()
                .map(analiseProjetoMapper::toDomain)
                .toList();
    }
}
