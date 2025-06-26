package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseCodigoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.utils.ArquivoAlteradoIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses =
        ArquivoAlteradoIdMapper.class
)
public interface AnaliseCodigoMapper {
    @Named("comArquivo")
    @Mapping(target = "arquivoAlterado", qualifiedByName = "somenteId")
    AnaliseCodigoEntity toEntityComArquivo(AnaliseCodigo domain);

    @Named("semArquivo")
    @Mapping(target = "arquivoAlterado", ignore = true)
    AnaliseCodigoEntity toEntitySemArquivo(AnaliseCodigo domain);

    AnaliseCodigo toDomain(AnaliseCodigoEntity entity);
}
