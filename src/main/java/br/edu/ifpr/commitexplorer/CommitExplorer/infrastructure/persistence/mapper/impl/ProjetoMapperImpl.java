package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ProjetoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AnaliseProjetoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.ProjetoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.RepositorioMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjetoMapperImpl implements ProjetoMapper {

    private final RepositorioMapper repositorioMapper;
    private final AnaliseProjetoMapper analiseProjetoMapper;

    public ProjetoMapperImpl(@Lazy RepositorioMapper repositorioMapper,
                             @Lazy AnaliseProjetoMapper analiseProjetoMapper) {
        this.repositorioMapper = repositorioMapper;
        this.analiseProjetoMapper = analiseProjetoMapper;
    }

    @Override
    public ProjetoEntity toEntity(Projeto domain) {
        var entity = baseEntity(domain);
        if (domain.getRepositorios() != null) {
            entity.setRepositorios(repositorioMapper.toEntity(domain.getRepositorios()));
        }
        if (domain.getAnalises() != null) {
            entity.setAnalises(analiseProjetoMapper.toEntity(domain.getAnalises()));
        }
        return entity;
    }

    @Override
    public ProjetoEntity toEntityId(Projeto domain) {
        return baseEntity(domain);
    }

    @Override
    public List<ProjetoEntity> toEntity(List<Projeto> domainList) {
        if (domainList == null || domainList.isEmpty()) {
            return new ArrayList<>();
        }
        return domainList.stream()
                .map(this::toEntityId)
                .toList();
    }

    @Override
    public Projeto toDomain(ProjetoEntity entity) {
        var domain = baseDomain(entity);
        if (entity.getRepositorios() != null) {
            domain.setRepositorios(repositorioMapper.toDomain(entity.getRepositorios()));
        }
        if (entity.getAnalises() != null) {
            domain.setAnalises(analiseProjetoMapper.toDomain(entity.getAnalises()));
        }
        return domain;
    }

    @Override
    public Projeto toDomainId(ProjetoEntity entity) {
        return baseDomain(entity);
    }

    @Override
    public List<Projeto> toDomain(List<ProjetoEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return entityList.stream()
                .map(this::toDomainId)
                .toList();
    }


    private ProjetoEntity baseEntity(Projeto domain) {
        var entity = new ProjetoEntity();
        entity.setIdProjeto(domain.getIdProjeto());
        entity.setNome(domain.getNome());
        entity.setDataCriacao(domain.getDataCriacao());
        entity.setProjetoUrl(domain.getProjetoUrl());
        entity.setUsuario(domain.getUsuario());
        return entity;
    }

    private Projeto baseDomain(ProjetoEntity entity) {
        var domain = new Projeto(entity.getNome(), entity.getProjetoUrl());
        domain.setIdProjeto(entity.getIdProjeto());
        domain.setDataCriacao(entity.getDataCriacao());
        domain.setUsuario(entity.getUsuario());
        return domain;
    }
}
