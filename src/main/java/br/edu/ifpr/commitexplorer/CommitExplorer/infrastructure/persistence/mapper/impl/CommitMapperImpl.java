package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.CommitEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.ArquivoAlteradoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AutorMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.CommitMapper;
import org.springframework.stereotype.Service;

@Service
public class CommitMapperImpl implements CommitMapper {

    private final ArquivoAlteradoMapper arquivoAlteradoMapper;
    private final AutorMapper autorMapper;

    public CommitMapperImpl(ArquivoAlteradoMapper arquivoAlteradoMapper, AutorMapper autorMapper) {
        this.arquivoAlteradoMapper = arquivoAlteradoMapper;
        this.autorMapper = autorMapper;
    }

    @Override
    public CommitEntity toEntity(Commit domain) {
        var entity = baseEntity(domain);
        if (domain.getArquivosAlterados() != null) {
            entity.setArquivosAlterados(
                domain.getArquivosAlterados().stream()
                    .map(arquivoAlteradoMapper::toEntityId)
                    .toList()
            );
        }
        if (domain.getAutor() != null) {
            entity.setAutor(autorMapper.toEntityId(domain.getAutor()));
        }
        return entity;
    }

    @Override
    public CommitEntity toEntityId(Commit domain) {
        return baseEntity(domain);
    }

    @Override
    public Commit toDomain(CommitEntity entity) {
        var domain = baseDomain(entity);
        if (entity.getArquivosAlterados() != null) {
            domain.setArquivosAlterados(
                entity.getArquivosAlterados().stream()
                    .map(arquivoAlteradoMapper::toDomainId)
                    .toList()
            );
        }
        if (entity.getAutor() != null) {
            domain.setAutor(autorMapper.toDomainId(entity.getAutor()));
        }
        return domain;
    }

    @Override
    public Commit toDomainId(CommitEntity entity) {
        return baseDomain(entity);
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
