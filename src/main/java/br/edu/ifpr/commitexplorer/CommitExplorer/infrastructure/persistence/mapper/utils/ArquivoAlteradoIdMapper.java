package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.utils;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ArquivoAlteradoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ArquivoAlteradoIdMapper {

    @Named("somenteId")
    default ArquivoAlteradoEntity toEntitySomenteId(ArquivoAlterado domain) {
        if (domain == null || domain.getIdArquivoAlterado() == null) {
            return null;
        }
        var entity = new ArquivoAlteradoEntity();
        entity.setIdArquivoAlterado(domain.getIdArquivoAlterado());
        return entity;
    }
}
