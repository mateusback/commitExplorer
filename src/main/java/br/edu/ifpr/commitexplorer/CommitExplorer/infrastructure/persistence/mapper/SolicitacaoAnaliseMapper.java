package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.SolicitacaoAnaliseEntity;

public interface SolicitacaoAnaliseMapper {
    SolicitacaoAnaliseEntity toEntity(SolicitacaoAnalise domain);
    SolicitacaoAnalise toDomain(SolicitacaoAnaliseEntity entity);
}