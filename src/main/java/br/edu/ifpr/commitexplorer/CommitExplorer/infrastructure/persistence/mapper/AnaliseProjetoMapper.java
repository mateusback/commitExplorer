package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseCodigoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseProjetoEntity;

import java.util.List;

public interface AnaliseProjetoMapper {
    AnaliseProjetoEntity toEntity(AnaliseProjeto domain);

    /**
     * Converte um objeto de domínio {@link AnaliseCodigo} para uma instância de {@link AnaliseCodigoEntity}
     * sem associar a lista de commits.
     * <p>
     * Útil para operações que não requerem commits e para evitar problemas de recursão infinita.
     *
     * @param domain o objeto de domínio a ser convertido
     * @return a entidade correspondente sem associação de commits
     */
    AnaliseProjetoEntity toEntityId(AnaliseProjeto domain);

    List<AnaliseProjetoEntity> toEntity(List<AnaliseProjeto> domainList);

    AnaliseProjeto toDomain(AnaliseProjetoEntity entity);

    /**
     * Converte uma entidade {@link AnaliseProjetoEntity} para um objeto de domínio {@link AnaliseProjeto}
     * sem associar a lista de análises de código.
     * <p>
     * Útil para operações que não requerem análises de código e para evitar problemas de recursão infinita.
     *
     * @param entity a entidade a ser convertida
     * @return o objeto de domínio correspondente sem associação de análises de código
     */
    AnaliseProjeto toDomainId(AnaliseProjetoEntity entity);

    List<AnaliseProjeto> toDomain(List<AnaliseProjetoEntity> entityList);
}
