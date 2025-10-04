package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.IndicadoresFeedback;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.IndicadoresFeedbackRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.IndicadoresFeedbackMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.IndicadoresFeedbackJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class IndicadoresFeedbackRepositoryImpl implements IndicadoresFeedbackRepository {
    private final IndicadoresFeedbackJpaRepository jpaRepository;
    private final IndicadoresFeedbackMapper mapper;

    public IndicadoresFeedbackRepositoryImpl(
            IndicadoresFeedbackJpaRepository jpaRepository,
            IndicadoresFeedbackMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public IndicadoresFeedback save(IndicadoresFeedback indicadoresFeedback) {
        var entity = mapper.toEntity(indicadoresFeedback);
        return mapper.toDomain(jpaRepository.save(entity));
    }
}