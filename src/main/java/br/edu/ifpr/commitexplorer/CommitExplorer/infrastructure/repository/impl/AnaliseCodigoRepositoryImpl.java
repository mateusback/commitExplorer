package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.AnaliseCodigoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseCodigoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AnaliseCodigoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.AnaliseCodigoJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AnaliseCodigoRepositoryImpl implements AnaliseCodigoRepository {

    private final AnaliseCodigoJpaRepository jpaRepository;
    private final AnaliseCodigoMapper mapper;

    public AnaliseCodigoRepositoryImpl(
            AnaliseCodigoJpaRepository jpaRepository,
            AnaliseCodigoMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public List<AnaliseCodigo> saveAll(List<AnaliseCodigo> analises) {
        List<AnaliseCodigoEntity> entities = analises.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());

        List<AnaliseCodigoEntity> savedEntities = jpaRepository.saveAll(entities);

        return savedEntities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AnaliseCodigo save(AnaliseCodigo analiseCodigo) {
        AnaliseCodigoEntity entity = mapper.toEntity(analiseCodigo);
        AnaliseCodigoEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
