package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.SolicitacaoAnaliseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SolicitacaoAnaliseJpaRepository extends JpaRepository<SolicitacaoAnaliseEntity, Long> {
    boolean existsByRepositorioUrlAndBranchAndDataSolicitacaoAfter(
            String repositorioUrl, String branch, LocalDateTime dataSolicitacao);

}