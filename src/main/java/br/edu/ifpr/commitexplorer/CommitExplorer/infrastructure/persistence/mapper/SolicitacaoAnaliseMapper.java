package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.SolicitacaoAnaliseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolicitacaoAnaliseMapper {
    SolicitacaoAnalise toDomain(SolicitacaoAnaliseEntity entity);
    SolicitacaoAnaliseEntity toEntity(SolicitacaoAnalise domain);
}