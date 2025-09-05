package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.FeedbackAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.FeedbackAnaliseEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.PontosAnaliseEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.enums.TipoPontoAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AnaliseProjetoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AutorMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.FeedbackAnaliseMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.IndicadoresFeedbackMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackAnaliseMapperImpl implements FeedbackAnaliseMapper {

    private final AnaliseProjetoMapper analiseProjetoMapper;
    private final AutorMapper autorMapper;
    private final IndicadoresFeedbackMapper indicadoresMapper;

    public FeedbackAnaliseMapperImpl(
            @Lazy AnaliseProjetoMapper analiseProjetoMapper,
            @Lazy AutorMapper autorMapper,
            IndicadoresFeedbackMapper indicadoresMapper

    ) {
        this.analiseProjetoMapper = analiseProjetoMapper;
        this.autorMapper = autorMapper;
        this.indicadoresMapper = indicadoresMapper;
    }

    @Override
    public FeedbackAnaliseEntity toEntity(FeedbackAnalise domain) {
        var entity = baseEntity(domain);

        if (domain.getAnaliseProjeto() != null) {
            entity.setAnalise(analiseProjetoMapper.toEntityId(domain.getAnaliseProjeto()));
        }
        if (domain.getAutor() != null) {
            entity.setAutor(autorMapper.toEntityId(domain.getAutor()));
        }
        if (domain.getIndicadores() != null) {
            entity.setIndicadores(indicadoresMapper.toEntity(domain.getIndicadores()));
        }

        entity.setPontosPositivos(mapStringsToPontos(entity, domain.getPontosPositivos(), TipoPontoAnalise.POSITIVO));
        entity.setPontosNegativos(mapStringsToPontos(entity, domain.getPontosNegativos(), TipoPontoAnalise.NEGATIVO));
        entity.setSugestoesMelhoria(mapStringsToPontos(entity, domain.getSugestoesMelhoria(), TipoPontoAnalise.SUGESTAO_MELHORIA));

        return entity;
    }

    @Override
    public FeedbackAnaliseEntity toEntityId(FeedbackAnalise domain) {
        var entity = baseEntity(domain);
        if (domain.getAnaliseProjeto() != null) {
            entity.setAnalise(analiseProjetoMapper.toEntityId(domain.getAnaliseProjeto()));
        }
        if (domain.getAutor() != null) {
            entity.setAutor(autorMapper.toEntityId(domain.getAutor()));
        }
        if (domain.getIndicadores() != null) {
            entity.setIndicadores(indicadoresMapper.toEntity(domain.getIndicadores()));
        }
        return entity;
    }

    @Override
    public List<FeedbackAnaliseEntity> toEntity(List<FeedbackAnalise> domainList) {
        if (domainList == null || domainList.isEmpty()) {
            return new ArrayList<>();
        }
        return domainList.stream()
                .map(this::toEntityId)
                .toList();
    }

    @Override
    public FeedbackAnalise toDomain(FeedbackAnaliseEntity entity) {
        var domain = baseDomain(entity);

        if (entity.getAnalise() != null) {
            domain.setAnaliseProjeto(analiseProjetoMapper.toDomainId(entity.getAnalise()));
        }
        if (entity.getAutor() != null) {
            domain.setAutor(autorMapper.toDomainId(entity.getAutor()));
        }
        if (entity.getIndicadores() != null) {
            domain.setIndicadores(indicadoresMapper.toDomain(entity.getIndicadores()));
        }

        domain.setPontosPositivos(mapPontosToStrings(entity.getPontosPositivos(), TipoPontoAnalise.POSITIVO));
        domain.setPontosNegativos(mapPontosToStrings(entity.getPontosNegativos(), TipoPontoAnalise.NEGATIVO));
        domain.setSugestoesMelhoria(mapPontosToStrings(entity.getSugestoesMelhoria(), TipoPontoAnalise.SUGESTAO_MELHORIA));

        return domain;
    }

    @Override
    public FeedbackAnalise toDomainId(FeedbackAnaliseEntity entity) {
        return baseDomain(entity);
    }

    @Override
    public List<FeedbackAnalise> toDomain(List<FeedbackAnaliseEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return entityList.stream()
                .map(this::toDomainId)
                .toList();
    }

    private FeedbackAnaliseEntity baseEntity(FeedbackAnalise domain) {
        var entity = new FeedbackAnaliseEntity();
        if (domain == null) return entity;

        entity.setIdFeedbackAnalise(domain.getIdFeedbackAnalise());
        entity.setTipo(domain.getTipo());
        entity.setPeriodoInicio(domain.getPeriodoInicio());
        entity.setPeriodoFim(domain.getPeriodoFim());
        entity.setPontuacaoGeral(domain.getPontuacaoGeral());
        entity.setPontuacaoFrequenciaConsistencia(domain.getPontuacaoFrequenciaConsistencia());
        entity.setPontuacaoDistribuicaoTrabalho(domain.getPontuacaoDistribuicaoTrabalho());
        entity.setPontuacaoDistribuicaoTemporal(domain.getPontuacaoDistribuicaoTemporal());
        entity.setPontuacaoVariedadeTipos(domain.getPontuacaoVariedadeTipos());
        entity.setPontuacaoQualidadeMensagens(domain.getPontuacaoQualidadeMensagens());
        entity.setPontuacaoQualidadeTecnica(domain.getPontuacaoQualidadeTecnica());
        entity.setConceito(domain.getConceito());
        entity.setResumo(domain.getResumo());
        entity.setDataCriacao(domain.getDataCriacao());

        return entity;
    }

    private FeedbackAnalise baseDomain(FeedbackAnaliseEntity entity) {
        var domain = new FeedbackAnalise();
        if (entity == null) return domain;

        domain.setIdFeedbackAnalise(entity.getIdFeedbackAnalise());
        domain.setTipo(entity.getTipo());
        domain.setPeriodoInicio(entity.getPeriodoInicio());
        domain.setPeriodoFim(entity.getPeriodoFim());
        domain.setPontuacaoGeral(entity.getPontuacaoGeral());
        domain.setPontuacaoFrequenciaConsistencia(entity.getPontuacaoFrequenciaConsistencia());
        domain.setPontuacaoDistribuicaoTrabalho(entity.getPontuacaoDistribuicaoTrabalho());
        domain.setPontuacaoDistribuicaoTemporal(entity.getPontuacaoDistribuicaoTemporal());
        domain.setPontuacaoVariedadeTipos(entity.getPontuacaoVariedadeTipos());
        domain.setPontuacaoQualidadeMensagens(entity.getPontuacaoQualidadeMensagens());
        domain.setPontuacaoQualidadeTecnica(entity.getPontuacaoQualidadeTecnica());
        domain.setConceito(entity.getConceito());
        domain.setResumo(entity.getResumo());
        domain.setDataCriacao(entity.getDataCriacao());

        return domain;
    }

    private List<PontosAnaliseEntity> mapStringsToPontos(
            FeedbackAnaliseEntity parent,
            List<String> textos,
            TipoPontoAnalise tipo
    ) {
        if (textos == null || textos.isEmpty()) return new ArrayList<>();
        var out = new ArrayList<PontosAnaliseEntity>(textos.size());
        for (String t : textos) {
            if (t == null || t.isBlank()) continue;
            var p = new PontosAnaliseEntity();
            p.setFeedbackAnalise(parent);
            p.setTipo(tipo);
            p.setDescriacao(t);
            out.add(p);
        }
        return out;
    }

    private List<String> mapPontosToStrings(List<PontosAnaliseEntity> pontos, TipoPontoAnalise tipo) {
        if (pontos == null || pontos.isEmpty()) return new ArrayList<>();
        var out = new ArrayList<String>();
        for (var p : pontos) {
            if (p == null) continue;
            if (p.getTipo() == tipo && p.getDescriacao() != null) {
                out.add(p.getDescriacao());
            }
        }
        return out;
    }
}