package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.CommitEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.ArquivoAlteradoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AutorMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.BranchMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.CommitMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommitMapperImpl implements CommitMapper {

    private final ArquivoAlteradoMapper arquivoAlteradoMapper;
    private final AutorMapper autorMapper;
    private final BranchMapper branchMapper;

    public CommitMapperImpl(
            ArquivoAlteradoMapper arquivoAlteradoMapper,
            AutorMapper autorMapper,
            @Lazy BranchMapper branchMapper)
    {
        this.arquivoAlteradoMapper = arquivoAlteradoMapper;
        this.autorMapper = autorMapper;
        this.branchMapper = branchMapper;
    }

    @Override
    public CommitEntity toEntity(Commit domain) {
        var entity = baseEntity(domain);
        if (domain.getArquivosAlterados() != null) {
            entity.setArquivosAlterados(arquivoAlteradoMapper.toEntity(domain.getArquivosAlterados()));
        }
        if (domain.getAutor() != null) {
            entity.setAutor(autorMapper.toEntityId(domain.getAutor()));
        }
        if (domain.getBranch() != null) {
            entity.setBranch(branchMapper.toEntityId(domain.getBranch()));
        }
        return entity;
    }

    @Override
    public CommitEntity toEntityId(Commit domain) {
        return baseEntity(domain);
    }

    @Override
    public List<CommitEntity> toEntity(List<Commit> domainList) {
        if (domainList == null || domainList.isEmpty()) {
            return new ArrayList<>();
        }
        return domainList.stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public Commit toDomain(CommitEntity entity) {
        var domain = baseDomain(entity);
        if (entity.getArquivosAlterados() != null) {
            domain.setArquivosAlterados(arquivoAlteradoMapper.toDomain(entity.getArquivosAlterados()));
        }
        if (entity.getAutor() != null) {
            domain.setAutor(autorMapper.toDomainId(entity.getAutor()));
        }
        if (entity.getBranch() != null) {
            domain.setBranch(branchMapper.toDomainId(entity.getBranch()));
        }
        return domain;
    }

    @Override
    public Commit toDomainId(CommitEntity entity) {
        return baseDomain(entity);
    }

    @Override
    public List<Commit> toDomain(List<CommitEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return entityList.stream()
                .map(this::toDomain)
                .toList();
    }


    private CommitEntity baseEntity(Commit domain) {
        var entity = new CommitEntity();
        entity.setIdCommit(domain.getIdCommit());
        entity.setHash(domain.getHash());
        entity.setCommitDate(domain.getCommitDate());
        entity.setComplexidadeGeral(domain.getComplexidadeGeral());
        entity.setMensagem(domain.getMensagem());
        entity.setPontuacao(domain.getPontuacao());
        return entity;
    }

    private Commit baseDomain(CommitEntity entity) {
        var domain = new Commit();
        domain.setIdCommit(entity.getIdCommit());
        domain.setHash(entity.getHash());
        domain.setCommitDate(entity.getCommitDate());
        domain.setComplexidadeGeral(entity.getComplexidadeGeral());
        domain.setMensagem(entity.getMensagem());
        domain.setPontuacao(entity.getPontuacao());
        return domain;
    }
}
