package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ArquivoAlteradoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        AnaliseCodigoMapper.class
})
public interface ArquivoAlteradoMapper {
    ArquivoAlterado toDomain(ArquivoAlteradoEntity entity);
    ArquivoAlteradoEntity toEntity(ArquivoAlterado domain);
}
