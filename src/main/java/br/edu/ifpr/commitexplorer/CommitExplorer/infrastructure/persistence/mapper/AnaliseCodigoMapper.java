package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseCodigoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnaliseCodigoMapper {
    AnaliseCodigo toDomain(AnaliseCodigoEntity entity);
    AnaliseCodigoEntity toEntity(AnaliseCodigo domain);
}
