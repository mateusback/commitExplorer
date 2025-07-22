package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseCodigoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ArquivoAlteradoEntity;

public interface ArquivoAlteradoMapper {
    ArquivoAlteradoEntity toEntity(ArquivoAlterado domain);

    /**
     * Converte um objeto de domínio {@link ArquivoAlterado} para uma instância de {@link ArquivoAlteradoEntity}
     * sem associar a lista de commits.
     * <p>
     * Útil para operações que não requerem commits e para evitar problemas de recursão infinita.
     *
     * @param domain o objeto de domínio a ser convertido
     * @return a entidade correspondente sem associação de commits
     */
    ArquivoAlteradoEntity toEntityId(ArquivoAlterado domain);

    ArquivoAlterado toDomain(ArquivoAlteradoEntity entity);

    /**
     * Converte uma entidade {@link ArquivoAlteradoEntity} para um objeto de domínio {@link ArquivoAlterado}
     * sem associar a lista de commits.
     * <p>
     * Útil para operações que não requerem commits e para evitar problemas de recursão infinita.
     *
     * @param entity a entidade a ser convertida
     * @return o objeto de domínio correspondente sem associação de commits
     */
    ArquivoAlterado toDomainId(ArquivoAlteradoEntity entity);
}
