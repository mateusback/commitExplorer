package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Repositorio;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.RepositorioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        BranchMapper.class,
        CommitMapper.class,
        ArquivoAlteradoMapper.class,
        AnaliseProjetoMapper.class
})
public interface RepositorioMapper {
    Repositorio toDomain(RepositorioEntity entity);
    RepositorioEntity toEntity(Repositorio domain);
}
