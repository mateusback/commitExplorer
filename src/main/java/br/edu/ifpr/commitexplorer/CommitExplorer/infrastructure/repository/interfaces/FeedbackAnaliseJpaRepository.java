package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.FeedbackAnaliseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackAnaliseJpaRepository extends JpaRepository<FeedbackAnaliseEntity, Long> {
    List<FeedbackAnaliseEntity> findByAnalise_IdAnaliseProjeto(Long analiseId);
}
