package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseProjetoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnaliseProjetoMapper {
    AnaliseProjeto toDomain(AnaliseProjetoEntity entity);
    AnaliseProjetoEntity toEntity(AnaliseProjeto domain);
}
