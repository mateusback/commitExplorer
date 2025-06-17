package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseCodigoEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AnaliseCodigoMapper {

    @Mappings({
            @Mapping(source = "idAnaliseCodigo", target = "id"),
            @Mapping(source = "arquivoAlterado.idArquivoAlterado", target = "idArquivoAlterado")
    })
    AnaliseCodigo toDomain(AnaliseCodigoEntity entity);

    @InheritInverseConfiguration
    @Mapping(target = "arquivoAlterado", ignore = true)
    AnaliseCodigoEntity toEntity(AnaliseCodigo domain);
}
