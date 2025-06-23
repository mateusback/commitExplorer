package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AutorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        CommitMapper.class
})
public interface AutorMapper {
    Autor toDomain(AutorEntity entity);
    AutorEntity toEntity(Autor domain);
}
