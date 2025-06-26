package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.RepositorioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioJpaRepository extends JpaRepository<RepositorioEntity, Long> {
}
