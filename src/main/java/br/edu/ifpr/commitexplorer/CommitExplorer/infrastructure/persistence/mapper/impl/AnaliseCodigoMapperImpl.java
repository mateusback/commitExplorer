package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseCodigoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AnaliseCodigoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.ArquivoAlteradoMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnaliseCodigoMapperImpl implements AnaliseCodigoMapper {

    private final ArquivoAlteradoMapper arquivoAlteradoMapper;

    public AnaliseCodigoMapperImpl(ArquivoAlteradoMapper arquivoAlteradoMapper) {
        this.arquivoAlteradoMapper = arquivoAlteradoMapper;
    }

    @Override
    public AnaliseCodigoEntity toEntity(AnaliseCodigo d) {
        var entity = baseEntity(d);

        var arquivoalterado = d.getArquivoAlterado();
        if (arquivoalterado != null) {
            entity.setArquivoAlterado(arquivoAlteradoMapper.toEntityId(arquivoalterado));
        }

        return entity;
    }

    @Override
    public AnaliseCodigoEntity toEntityId(AnaliseCodigo d) {
        return baseEntity(d);
    }

    @Override
    public List<AnaliseCodigoEntity> toEntity(List<AnaliseCodigo> domainList) {
        if (domainList == null || domainList.isEmpty()) {
            return new ArrayList<>();
        }
        return domainList.stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public AnaliseCodigo toDomain(AnaliseCodigoEntity entity) {
        var domain = baseDomain(entity);

        var arquivoAlterado = entity.getArquivoAlterado();
        if (arquivoAlterado != null) {
            domain.setArquivoAlterado(arquivoAlteradoMapper.toDomainId(arquivoAlterado));
        }

        return domain;
    }

    @Override
    public AnaliseCodigo toDomainId(AnaliseCodigoEntity entity) {
        return baseDomain(entity);
    }

    @Override
    public List<AnaliseCodigo> toDomain(List<AnaliseCodigoEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return entityList.stream()
                .map(this::toDomain)
                .toList();
    }

    private AnaliseCodigoEntity baseEntity(AnaliseCodigo domain) {
        var entity = new AnaliseCodigoEntity();
        entity.setIdAnaliseCodigo(domain.getIdAnaliseCodigo());
        entity.setTipo(domain.getTipo());
        entity.setSeveridade(domain.getSeveridade());
        entity.setDescricaoSmell(domain.getDescricaoSmell());
        entity.setPontuacaoNegativa(domain.getPontuacaoNegativa());
        entity.setLinha(domain.getLinha());
        return entity;
    }

    private AnaliseCodigo baseDomain(AnaliseCodigoEntity entity) {
        var domain = new AnaliseCodigo();
        domain.setIdAnaliseCodigo(entity.getIdAnaliseCodigo());
        domain.setTipo(entity.getTipo());
        domain.setSeveridade(entity.getSeveridade());
        domain.setDescricaoSmell(entity.getDescricaoSmell());
        domain.setPontuacaoNegativa(entity.getPontuacaoNegativa());
        domain.setLinha(entity.getLinha());
        return domain;
    }
}
