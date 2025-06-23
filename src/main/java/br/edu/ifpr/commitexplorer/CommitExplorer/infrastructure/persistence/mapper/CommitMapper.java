package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.CommitEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        BranchMapper.class,
        ArquivoAlteradoMapper.class,
})
public interface CommitMapper {
    Commit toDomain(CommitEntity entity);
    CommitEntity toEntity(Commit domain);
}
