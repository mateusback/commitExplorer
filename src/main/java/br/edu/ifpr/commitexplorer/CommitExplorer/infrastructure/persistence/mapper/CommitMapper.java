package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.CommitEntity;

import java.util.List;

public interface CommitMapper {
    CommitEntity toEntity(Commit domain);

    /**
     * Converte um objeto de domínio {@link Commit} para uma instância de {@link CommitEntity}
     * sem associar a lista de branches.
     * <p>
     * Útil para operações que não requerem branches e para evitar problemas de recursão infinita.
     *
     * @param domain o objeto de domínio a ser convertido
     * @return a entidade correspondente sem associação de branches
     */
    CommitEntity toEntityId(Commit domain);

    List<CommitEntity> toEntity(List<Commit> domainList);

    Commit toDomain(CommitEntity entity);

    /**
     * Converte uma entidade {@link CommitEntity} para um objeto de domínio {@link Commit}
     * sem associar a lista de branches.
     * <p>
     * Útil para operações que não requerem branches e para evitar problemas de recursão infinita.
     *
     * @param entity a entidade a ser convertida
     * @return o objeto de domínio correspondente sem associação de branches
     */
    Commit toDomainId(CommitEntity entity);

    List<Commit> toDomain(List<CommitEntity> entityList);
}
