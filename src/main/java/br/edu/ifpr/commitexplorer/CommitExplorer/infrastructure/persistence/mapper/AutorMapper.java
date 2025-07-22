package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AutorEntity;

public interface AutorMapper {
    AutorEntity toEntity(Autor domain);

    /**
     * Converte um objeto de domínio {@link Autor} para uma instância de {@link AutorEntity}
     * sem associar a lista de commits.
     * <p>
     * Útil para operações que não requerem commits e para evitar problemas de recursão infinita.
     *
     * @param domain o objeto de domínio a ser convertido
     * @return a entidade correspondente sem associação de commits
     */
    AutorEntity toEntityId(Autor domain);

    Autor toDomain(AutorEntity entity);

    /**
     * Converte uma entidade {@link AutorEntity} para um objeto de domínio {@link Autor}
     * sem associar a lista de commits.
     * <p>
     * Útil para operações que não requerem commits e para evitar problemas de recursão infinita.
     *
     * @param entity a entidade a ser convertida
     * @return o objeto de domínio correspondente sem associação de commits
     */
    Autor toDomainId(AutorEntity entity);
}
