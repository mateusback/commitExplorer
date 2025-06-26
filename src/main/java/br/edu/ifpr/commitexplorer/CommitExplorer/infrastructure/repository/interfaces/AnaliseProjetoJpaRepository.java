package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseProjetoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnaliseProjetoJpaRepository extends JpaRepository<AnaliseProjetoEntity, Long> {
}
