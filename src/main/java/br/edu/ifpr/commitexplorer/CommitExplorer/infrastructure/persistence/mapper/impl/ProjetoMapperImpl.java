package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ProjetoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.ProjetoMapper;
import org.springframework.stereotype.Service;

@Service
public class ProjetoMapperImpl implements ProjetoMapper {

    @Override
    public ProjetoEntity toEntity(Projeto domain) {
        var entity = baseEntity(domain);
        //entity.setRepositorios();
        return entity;
    }

    @Override
    public ProjetoEntity toEntityId(Projeto domain) {
        var entity = baseEntity(domain);
        return entity;
    }

    @Override
    public Projeto toDomain(ProjetoEntity entity) {
        var domain = baseDomain(entity);
        //domain.setRepositorios();
        return domain;
    }

    @Override
    public Projeto toDomainId(ProjetoEntity entity) {
        return baseDomain(entity);
    }


    private ProjetoEntity baseEntity(Projeto domain) {
        var entity = new ProjetoEntity();
        entity.setIdProjeto(domain.getIdProjeto());
        entity.setNome(domain.getNome());
        entity.setDataCriacao(domain.getDataCriacao());
        entity.setProjetoUrl(domain.getProjetoUrl());
        return entity;
    }

    private Projeto baseDomain(ProjetoEntity entity) {
        var domain = new Projeto(entity.getNome(), entity.getProjetoUrl());
        domain.setIdProjeto(entity.getIdProjeto());
        domain.setDataCriacao(entity.getDataCriacao());
        return domain;
    }
}
