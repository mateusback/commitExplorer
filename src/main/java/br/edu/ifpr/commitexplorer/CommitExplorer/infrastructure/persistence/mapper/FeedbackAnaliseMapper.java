package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.FeedbackAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.FeedbackAnaliseEntity;

import java.util.List;

public interface FeedbackAnaliseMapper {

    FeedbackAnaliseEntity toEntity(FeedbackAnalise domain);

    /**
     * Converte um objeto de domínio {@link FeedbackAnalise} para uma instância de {@link FeedbackAnaliseEntity}
     * sem associar a lista de commits.
     * <p>
     * Útil para operações que não requerem commits e para evitar problemas de recursão infinita.
     *
     * @param domain o objeto de domínio a ser convertido
     * @return a entidade correspondente sem associação de commits
     */
    FeedbackAnaliseEntity toEntityId(FeedbackAnalise domain);

    List<FeedbackAnaliseEntity> toEntity(List<FeedbackAnalise> domainList);

    FeedbackAnalise toDomain(FeedbackAnaliseEntity entity);

    /**
     * Converte uma entidade {@link FeedbackAnaliseEntity} para um objeto de domínio {@link FeedbackAnalise}
     * sem associar a lista de commits.
     * <p>
     * Útil para operações que não requerem commits e para evitar problemas de recursão infinita.
     *
     * @param entity a entidade a ser convertida
     * @return o objeto de domínio correspondente sem associação de commits
     */
    FeedbackAnalise toDomainId(FeedbackAnaliseEntity entity);

    List<FeedbackAnalise> toDomain(List<FeedbackAnaliseEntity> entityList);
}
