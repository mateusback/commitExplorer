package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchJpaRepository extends JpaRepository<BranchEntity, Long> {

}