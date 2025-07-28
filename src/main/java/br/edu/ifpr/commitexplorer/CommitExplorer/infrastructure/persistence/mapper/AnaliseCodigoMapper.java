package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseCodigoEntity;

public interface AnaliseCodigoMapper {
    AnaliseCodigoEntity toEntity(AnaliseCodigo domain);

    /**
     * Converte um objeto de domínio {@link AnaliseCodigo} para uma instância de {@link AnaliseCodigoEntity}
     * sem associar a lista de commits.
     * <p>
     * Útil para operações que não requerem commits e para evitar problemas de recursão infinita.
     *
     * @param domain o objeto de domínio a ser convertido
     * @return a entidade correspondente sem associação de commits
     */
    AnaliseCodigoEntity toEntityId(AnaliseCodigo domain);

    AnaliseCodigo toDomain(AnaliseCodigoEntity entity);

    /**
     * Converte uma entidade {@link AnaliseCodigoEntity} para um objeto de domínio {@link AnaliseCodigo}
     * sem associar a lista de commits.
     * <p>
     * Útil para operações que não requerem commits e para evitar problemas de recursão infinita.
     *
     * @param entity a entidade a ser convertida
     * @return o objeto de domínio correspondente sem associação de commits
     */
    AnaliseCodigo toDomainId(AnaliseCodigoEntity entity);
}
