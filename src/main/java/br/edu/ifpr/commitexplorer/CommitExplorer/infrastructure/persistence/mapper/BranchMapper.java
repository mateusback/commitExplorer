package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Branch;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.BranchEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        ArquivoAlteradoMapper.class,
        AnaliseProjetoMapper.class
})
public interface BranchMapper {
    Branch toDomain(BranchEntity entity);
    BranchEntity toEntity(Branch domain);
}
