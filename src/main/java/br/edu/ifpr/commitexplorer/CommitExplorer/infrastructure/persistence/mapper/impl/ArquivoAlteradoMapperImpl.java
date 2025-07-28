package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ArquivoAlteradoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AnaliseCodigoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.ArquivoAlteradoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.CommitMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ArquivoAlteradoMapperImpl implements ArquivoAlteradoMapper {

    private final AnaliseCodigoMapper analiseCodigoMapper;
    private final CommitMapper commitMapper;

    public ArquivoAlteradoMapperImpl(
            @Lazy AnaliseCodigoMapper analiseCodigoMapper,
            @Lazy CommitMapper commitMapper
    ) {
        this.analiseCodigoMapper = analiseCodigoMapper;
        this.commitMapper = commitMapper;
    }

    @Override
    public ArquivoAlteradoEntity toEntity(ArquivoAlterado domain) {
        var entity = baseEntity(domain);
        //todo - fazer um mapper de lista
//        if (domain.getAnalisesCodigo() != null) {
//            entity.setAnalisesCodigo(analiseCodigoMapper.toEntityId(domain.getAnalisesCodigo()));
//        }

        if (domain.getCommit() != null) {
            entity.setCommit(commitMapper.toEntityId(domain.getCommit()));
        }

        return entity;
    }

    @Override
    public ArquivoAlteradoEntity toEntityId(ArquivoAlterado domain) {
        return baseEntity(domain);
    }

    @Override
    public ArquivoAlterado toDomain(ArquivoAlteradoEntity entity) {
        var domain = baseDomain(entity);
        if (entity.getCommit() != null) {
            domain.setCommit(commitMapper.toDomainId(entity.getCommit()));
        }
//        if (domain.getAnalisesCodigo() != null) {
//            entity.setAnalisesCodigo(analiseCodigoMapper.toEntityId(domain.getAnalisesCodigo()));
//        }
        return domain;
    }

    @Override
    public ArquivoAlterado toDomainId(ArquivoAlteradoEntity entity) {
        return baseDomain(entity);
    }


    private ArquivoAlteradoEntity baseEntity(ArquivoAlterado domain) {
        var entity = new ArquivoAlteradoEntity();
        entity.setIdArquivoAlterado(domain.getIdArquivoAlterado());
        entity.setNomeArquivo(domain.getNomeArquivo());
        entity.setConteudoAntes(domain.getConteudoAntes());
        entity.setConteudoDepois(domain.getConteudoDepois());
        entity.setFlgTipoAcao(domain.getFlgTipoAcao());
        entity.setQtdLinhasAdicionadas(domain.getQtdLinhasAdicionadas());
        entity.setQtdLinhasRemovidas(domain.getQtdLinhasRemovidas());
        return entity;
    }

    private ArquivoAlterado baseDomain(ArquivoAlteradoEntity entity) {
        var domain = new ArquivoAlterado();
        domain.setIdArquivoAlterado(entity.getIdArquivoAlterado());
        domain.setNomeArquivo(entity.getNomeArquivo());
        domain.setConteudoAntes(entity.getConteudoAntes());
        domain.setConteudoDepois(entity.getConteudoDepois());
        domain.setFlgTipoAcao(entity.getFlgTipoAcao());
        domain.setQtdLinhasAdicionadas(entity.getQtdLinhasAdicionadas());
        domain.setQtdLinhasRemovidas(entity.getQtdLinhasRemovidas());
        return domain;
    }
}
