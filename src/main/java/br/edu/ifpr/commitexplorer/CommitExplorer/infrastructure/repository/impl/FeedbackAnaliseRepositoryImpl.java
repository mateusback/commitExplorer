package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.FeedbackAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.FeedbackAnaliseRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.FeedbackAnaliseMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.FeedbackAnaliseJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedbackAnaliseRepositoryImpl implements FeedbackAnaliseRepository {
    private final FeedbackAnaliseJpaRepository jpaRepository;
    private final FeedbackAnaliseMapper mapper;

    public FeedbackAnaliseRepositoryImpl(
            FeedbackAnaliseJpaRepository jpaRepository,
            FeedbackAnaliseMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    public FeedbackAnalise save(FeedbackAnalise feedbackAnalise) {
        var entity = mapper.toEntity(feedbackAnalise);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public List<FeedbackAnalise> obterPorIdAnalise(Long analiseId) {
        var entities = jpaRepository.findByAnalise_IdAnaliseProjeto(analiseId);
        return mapper.toDomain(entities);
    }
}
