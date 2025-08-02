package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ProjetoEntity;

import java.util.List;

public interface ProjetoMapper {
    ProjetoEntity toEntity(Projeto domain);

    /**
     * Converte um objeto de domínio {@link Projeto} para uma instância de {@link ProjetoEntity}
     * sem associar a lista de commits.
     * <p>
     * Útil para operações que não requerem commits e para evitar problemas de recursão infinita.
     *
     * @param domain o objeto de domínio a ser convertido
     * @return a entidade correspondente sem associação de commits
     */
    ProjetoEntity toEntityId(Projeto domain);

    List<ProjetoEntity> toEntity(List<Projeto> domainList);

    Projeto toDomain(ProjetoEntity entity);

    /**
     * Converte uma entidade {@link ProjetoEntity} para um objeto de domínio {@link Projeto}
     * sem associar a lista de commits.
     * <p>
     * Útil para operações que não requerem commits e para evitar problemas de recursão infinita.
     *
     * @param entity a entidade a ser convertida
     * @return o objeto de domínio correspondente sem associação de commits
     */
    Projeto toDomainId(ProjetoEntity entity);

    List<Projeto> toDomain(List<ProjetoEntity> entityList);
}
