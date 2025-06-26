package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ArquivoAlteradoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArquivoAlteradoJpaRepository extends JpaRepository<ArquivoAlteradoEntity, Long> {
}
