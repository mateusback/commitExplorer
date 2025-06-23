package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.SolicitacaoAnaliseRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.SolicitacaoAnaliseEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.SolicitacaoAnaliseMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.SolicitacaoAnaliseJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class SolicitacaoAnaliseRepositoryImpl implements SolicitacaoAnaliseRepository {

    private final SolicitacaoAnaliseJpaRepository jpaRepository;
    private final SolicitacaoAnaliseMapper mapper;

    public SolicitacaoAnaliseRepositoryImpl(
            SolicitacaoAnaliseJpaRepository jpaRepository,
            SolicitacaoAnaliseMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public SolicitacaoAnalise save(SolicitacaoAnalise solicitacaoAnalise) {
        SolicitacaoAnaliseEntity entity = mapper.toEntity(solicitacaoAnalise);
        SolicitacaoAnaliseEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public SolicitacaoAnalise findById(Long idSolicitacaoAnalise) {
        return jpaRepository.findById(idSolicitacaoAnalise)
                .map(mapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação de análise não encontrada com ID: " + idSolicitacaoAnalise));
    }

    @Override
    public boolean existsRecentByRepositorioUrlAndBranch(String repositorioUrl, String branch, LocalDateTime threshold) {
        return jpaRepository.existsByRepositorioUrlAndBranchAndDataSolicitacaoAfter(
                repositorioUrl, branch, threshold
        );
    }
}