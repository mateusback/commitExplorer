package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Repositorio;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ProjetoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.RepositorioEntity;

import java.util.List;

public interface RepositorioMapper {
    RepositorioEntity toEntity(Repositorio domain);

    /**
     * Converte um objeto de domínio {@link Repositorio} para uma instância de {@link RepositorioEntity}
     * sem associar a lista de projetos.
     * <p>
     * Útil para operações que não requerem projetos e para evitar problemas de recursão infinita.
     *
     * @param domain o objeto de domínio a ser convertido
     * @return a entidade correspondente sem associação de projetos
     */
    RepositorioEntity toEntityId(Repositorio domain);

    List<RepositorioEntity> toEntity(List<Repositorio> domain);

    Repositorio toDomain(RepositorioEntity entity);

    /**
     * Converte uma entidade {@link RepositorioEntity} para um objeto de domínio {@link Repositorio}
     * sem associar a lista de projetos.
     * <p>
     * Útil para operações que não requerem projetos e para evitar problemas de recursão infinita.
     *
     * @param entity a entidade a ser convertida
     * @return o objeto de domínio correspondente sem associação de projetos
     */
    Repositorio toDomainId(RepositorioEntity entity);

    List<Repositorio> toDomain(List<RepositorioEntity> entityList);
}
