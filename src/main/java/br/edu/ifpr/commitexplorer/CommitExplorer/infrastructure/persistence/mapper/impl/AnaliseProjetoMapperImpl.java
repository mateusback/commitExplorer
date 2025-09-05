package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseProjetoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnaliseProjetoMapperImpl implements AnaliseProjetoMapper {

    private final ProjetoMapper projetoMapper;
    private final BranchMapper branchMapper;
    private final SolicitacaoAnaliseMapper solicitacaoAnaliseMapper;
    private final FeedbackAnaliseMapper feedbackAnaliseMapper;

    public AnaliseProjetoMapperImpl(@Lazy ProjetoMapper projetoMapper,
                                    @Lazy BranchMapper branchMapper,
                                    SolicitacaoAnaliseMapper solicitacaoAnaliseMapper,
                                    @Lazy FeedbackAnaliseMapper feedbackAnaliseMapper) {
        this.projetoMapper = projetoMapper;
        this.branchMapper = branchMapper;
        this.solicitacaoAnaliseMapper = solicitacaoAnaliseMapper;
        this.feedbackAnaliseMapper = feedbackAnaliseMapper;
    }

    @Override
    public AnaliseProjetoEntity toEntity(AnaliseProjeto domain) {
        var entity = baseEntity(domain);
        if (domain.getProjeto() != null) {
            entity.setProjeto(projetoMapper.toEntityId(domain.getProjeto()));
        }
        if (domain.getBranch() != null) {
            entity.setBranch(branchMapper.toEntityId(domain.getBranch()));
        }
        if (domain.getSolicitacaoAnalise() != null) {
            entity.setSolicitacaoAnalise(solicitacaoAnaliseMapper.toEntity(domain.getSolicitacaoAnalise()));
        }
        if (domain.getFeedbacks() != null) {
            entity.setFeedbacks(feedbackAnaliseMapper.toEntity(domain.getFeedbacks()));
        }
        return entity;
    }

    public AnaliseProjetoEntity toEntityId(AnaliseProjeto domain) {
        return baseEntity(domain);
    }

    @Override
    public List<AnaliseProjetoEntity> toEntity(List<AnaliseProjeto> domainList) {
        if( domainList == null || domainList.isEmpty()) {
            return List.of();
        }
        return domainList.stream()
                .map(this::toEntityId)
                .toList();
    }

    @Override
    public AnaliseProjeto toDomain(AnaliseProjetoEntity entity) {
        var domain = baseDomain(entity);
        if (entity.getProjeto() != null) {
            domain.setProjeto(projetoMapper.toDomainId(entity.getProjeto()));
        }
        if (entity.getBranch() != null) {
            domain.setBranch(branchMapper.toDomainId(entity.getBranch()));
        }
        if (entity.getSolicitacaoAnalise() != null) {
            domain.setSolicitacaoAnalise(solicitacaoAnaliseMapper.toDomain(entity.getSolicitacaoAnalise()));
        }
        if (entity.getFeedbacks() != null) {
            domain.setFeedbacks(feedbackAnaliseMapper.toDomain(entity.getFeedbacks()));
        }
        return domain;
    }

    public AnaliseProjeto toDomainId(AnaliseProjetoEntity entity) {
        return baseDomain(entity);
    }

    @Override
    public List<AnaliseProjeto> toDomain(List<AnaliseProjetoEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return List.of();
        }
        return entityList.stream()
                .map(this::toDomainId)
                .toList();
    }

    private AnaliseProjetoEntity baseEntity(AnaliseProjeto domain) {
        var entity = new AnaliseProjetoEntity();
        entity.setIdAnaliseProjeto(domain.getIdAnaliseProjeto());
        entity.setDataAnalise(domain.getDataAnalise());
        entity.setStatusAnalise(domain.getStatusAnalise());
        entity.setComplexidadeMedia(domain.getComplexidadeMedia());
        entity.setQuantidadeCodeSmells(domain.getQuantidadeCodeSmells());
        entity.setPontuacaoTotal(domain.getPontuacaoTotal());
        entity.setTotalCommits(domain.getTotalCommits());
        entity.setTotalAutores(domain.getTotalAutores());
        entity.setTempoAnalise(domain.getTempoAnalise());
        entity.setUsuario(domain.getUsuario());
        return entity;
    }

    private AnaliseProjeto baseDomain(AnaliseProjetoEntity entity) {
        var domain = new AnaliseProjeto();
        domain.setIdAnaliseProjeto(entity.getIdAnaliseProjeto());
        domain.setDataAnalise(entity.getDataAnalise());
        domain.setStatusAnalise(entity.getStatusAnalise());
        domain.setComplexidadeMedia(entity.getComplexidadeMedia());
        domain.setQuantidadeCodeSmells(entity.getQuantidadeCodeSmells());
        domain.setPontuacaoTotal(entity.getPontuacaoTotal());
        domain.setTotalCommits(entity.getTotalCommits());
        domain.setTotalAutores(entity.getTotalAutores());
        domain.setTempoAnalise(entity.getTempoAnalise());
        domain.setUsuario(entity.getUsuario());
        return domain;
    }

}
