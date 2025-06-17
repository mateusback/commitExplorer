package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.SolicitacaoAnaliseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SolicitacaoAnaliseJpaRepository extends JpaRepository<SolicitacaoAnaliseEntity, Long> {
    boolean existsByDataSolicitacaoBetween(Date startDate, Date endDate);
}