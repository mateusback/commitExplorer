package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.SolicitacaoAnaliseEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.SolicitacaoAnaliseMapper;
import org.springframework.stereotype.Service;

@Service
public class SolicitacaoAnaliseMapperImpl implements SolicitacaoAnaliseMapper {
    @Override
    public SolicitacaoAnaliseEntity toEntity(SolicitacaoAnalise domain) {
        var entity = new SolicitacaoAnaliseEntity();
        entity.setIdSolicitacaoAnalise(domain.getIdSolicitacaoAnalise());
        entity.setDataSolicitacao(domain.getDataSolicitacao());
        entity.setDataFim(domain.getDataFim());
        entity.setDataInicio(domain.getDataInicio());
        entity.setStatus(domain.getStatus());
        entity.setMensagemErro(domain.getMensagemErro());
        entity.setDataInicioAnalise(domain.getDataInicioAnalise());
        entity.setDataFimAnalise(domain.getDataFimAnalise());
        entity.setRepositorioUrl(domain.getRepositorioUrl());
        entity.setBranch(domain.getBranch());
        entity.setProjetoUrl(domain.getProjetoUrl());
        entity.setToken(domain.getToken());
        entity.setNomeProjeto(domain.getNomeProjeto());
        entity.setUsuario(domain.getUsuario());

        return entity;
    }

    @Override
    public SolicitacaoAnalise toDomain(SolicitacaoAnaliseEntity entity) {
        if (entity == null) {
            return null;
        }

        var solicitacaoAnalise = new SolicitacaoAnalise();
        solicitacaoAnalise.setIdSolicitacaoAnalise(entity.getIdSolicitacaoAnalise());
        solicitacaoAnalise.setDataSolicitacao(entity.getDataSolicitacao());
        solicitacaoAnalise.setDataFim(entity.getDataFim());
        solicitacaoAnalise.setDataInicio(entity.getDataInicio());
        solicitacaoAnalise.setStatus(entity.getStatus());
        solicitacaoAnalise.setMensagemErro(entity.getMensagemErro());
        solicitacaoAnalise.setDataInicioAnalise(entity.getDataInicioAnalise());
        solicitacaoAnalise.setDataFimAnalise(entity.getDataFimAnalise());
        solicitacaoAnalise.setRepositorioUrl(entity.getRepositorioUrl());
        solicitacaoAnalise.setBranch(entity.getBranch());
        solicitacaoAnalise.setProjetoUrl(entity.getProjetoUrl());
        solicitacaoAnalise.setToken(entity.getToken());
        solicitacaoAnalise.setNomeProjeto(entity.getNomeProjeto());
        solicitacaoAnalise.setUsuario(entity.getUsuario());

        return solicitacaoAnalise;
    }

}
