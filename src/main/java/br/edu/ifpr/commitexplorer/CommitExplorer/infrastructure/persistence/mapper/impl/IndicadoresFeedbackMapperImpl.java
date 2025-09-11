package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.IndicadoresFeedback;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.IndicadoresFeedbackEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.IndicadoresFeedbackMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IndicadoresFeedbackMapperImpl implements IndicadoresFeedbackMapper {

    @Override
    public IndicadoresFeedbackEntity toEntity(IndicadoresFeedback domain) {
        if (domain == null) return null;
        return baseEntity(domain);
    }

    @Override
    public IndicadoresFeedbackEntity toEntityId(IndicadoresFeedback domain) {
        if (domain == null) return null;
        return baseEntity(domain);
    }

    @Override
    public List<IndicadoresFeedbackEntity> toEntity(List<IndicadoresFeedback> domainList) {
        if (domainList == null || domainList.isEmpty()) return new ArrayList<>();
        return domainList.stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public IndicadoresFeedback toDomain(IndicadoresFeedbackEntity entity) {
        if (entity == null) return null;
        return baseDomain(entity);
    }

    @Override
    public IndicadoresFeedback toDomainId(IndicadoresFeedbackEntity entity) {
        if (entity == null) return null;
        return baseDomain(entity);
    }

    @Override
    public List<IndicadoresFeedback> toDomain(List<IndicadoresFeedbackEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) return new ArrayList<>();
        return entityList.stream()
                .map(this::toDomain)
                .toList();
    }


    private IndicadoresFeedbackEntity baseEntity(IndicadoresFeedback d) {
        var e = new IndicadoresFeedbackEntity();
        e.setIdIndicadoresFeedback(d.getIdIndicadoresFeedback());
        e.setDiasPeriodo(d.getDiasPeriodo());
        e.setSemanasPeriodo(d.getSemanasPeriodo());

        e.setCommitsPorSemana(d.getCommitsPorSemana());
        e.setDiasAtivos(d.getDiasAtivos());
        e.setDesvioPadraoCommitsPorDia(d.getDesvioPadraoCommitsPorDia());

        e.setTotalAutores(d.getTotalAutores());
        e.setCommitsTopAutor(d.getCommitsTopAutor());
        e.setShareTopAutor(d.getShareTopAutor());

        e.setLinhasAdicionadasTotal(d.getLinhasAdicionadasTotal());
        e.setLinhasRemovidasTotal(d.getLinhasRemovidasTotal());
        e.setLinhasAlteradasTotal(d.getLinhasAlteradasTotal());
        e.setLinhasPorCommit(d.getLinhasPorCommit());

        e.setCodeSmellsTotal(d.getCodeSmellsTotal());
        e.setCodeSmellsPorKLocAlteradas(d.getCodeSmellsPorKLocAlteradas());

        e.setComplexidadeMedia(d.getComplexidadeMedia());
        e.setShareTop5Arquivos(d.getShareTop5Arquivos());

        e.setQuantidadeMerges(d.getQuantidadeMerges());
        e.setPercentualMerges(d.getPercentualMerges());

        return e;
    }

    private IndicadoresFeedback baseDomain(IndicadoresFeedbackEntity e) {
        var d = new IndicadoresFeedback();
        d.setIdIndicadoresFeedback(e.getIdIndicadoresFeedback());

        d.setDiasPeriodo(e.getDiasPeriodo());
        d.setSemanasPeriodo(e.getSemanasPeriodo());

        d.setCommitsPorSemana(e.getCommitsPorSemana());
        d.setDiasAtivos(e.getDiasAtivos());
        d.setDesvioPadraoCommitsPorDia(e.getDesvioPadraoCommitsPorDia());

        d.setTotalAutores(e.getTotalAutores());
        d.setCommitsTopAutor(e.getCommitsTopAutor());
        d.setShareTopAutor(e.getShareTopAutor());

        d.setLinhasAdicionadasTotal(e.getLinhasAdicionadasTotal());
        d.setLinhasRemovidasTotal(e.getLinhasRemovidasTotal());
        d.setLinhasAlteradasTotal(e.getLinhasAlteradasTotal());
        d.setLinhasPorCommit(e.getLinhasPorCommit());

        d.setCodeSmellsTotal(e.getCodeSmellsTotal());
        d.setCodeSmellsPorKLocAlteradas(e.getCodeSmellsPorKLocAlteradas());

        d.setComplexidadeMedia(e.getComplexidadeMedia());
        d.setShareTop5Arquivos(e.getShareTop5Arquivos());

        d.setQuantidadeMerges(e.getQuantidadeMerges());
        d.setPercentualMerges(e.getPercentualMerges());

        return d;
    }
}