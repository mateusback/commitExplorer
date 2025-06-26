package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseCodigoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnaliseCodigoJpaRepository extends JpaRepository<AnaliseCodigoEntity, Long> {
}
