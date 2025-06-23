package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ProjetoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        BranchMapper.class,
        CommitMapper.class,
        ArquivoAlteradoMapper.class,
        AnaliseProjetoMapper.class
})
public interface ProjetoMapper {
    Projeto toDomain(ProjetoEntity entity);
    ProjetoEntity toEntity(Projeto domain);
}
