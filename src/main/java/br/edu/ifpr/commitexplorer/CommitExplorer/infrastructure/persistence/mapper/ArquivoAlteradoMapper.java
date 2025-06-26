package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ArquivoAlteradoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {
        AnaliseCodigoMapper.class
})
public interface ArquivoAlteradoMapper {
    @Mapping(target = "analisesCodigo", qualifiedByName = "semArquivo")
    ArquivoAlteradoEntity toEntity(ArquivoAlterado domain);

    @Mapping(target = "commit", ignore = true)
    ArquivoAlterado toDomain(ArquivoAlteradoEntity entity);
}
