package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.IndicadoresFeedback;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.IndicadoresFeedbackEntity;

import java.util.List;

public interface IndicadoresFeedbackMapper {

    IndicadoresFeedbackEntity toEntity(IndicadoresFeedback domain);

    /**
     * Converte um objeto de domínio {@link IndicadoresFeedback} para uma instância de {@link IndicadoresFeedbackEntity}
     * sem associar a lista de commits.
     * <p>
     * Útil para operações que não requerem commits e para evitar problemas de recursão infinita.
     *
     * @param domain o objeto de domínio a ser convertido
     * @return a entidade correspondente sem associação de commits
     */
    IndicadoresFeedbackEntity toEntityId(IndicadoresFeedback domain);

    List<IndicadoresFeedbackEntity> toEntity(List<IndicadoresFeedback> domainList);

    IndicadoresFeedback toDomain(IndicadoresFeedbackEntity entity);

    /**
     * Converte uma entidade {@link IndicadoresFeedbackEntity} para um objeto de domínio {@link IndicadoresFeedback}
     * sem associar a lista de commits.
     * <p>
     * Útil para operações que não requerem commits e para evitar problemas de recursão infinita.
     *
     * @param entity a entidade a ser convertida
     * @return o objeto de domínio correspondente sem associação de commits
     */
    IndicadoresFeedback toDomainId(IndicadoresFeedbackEntity entity);

    List<IndicadoresFeedback> toDomain(List<IndicadoresFeedbackEntity> entityList);
}