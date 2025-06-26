package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ProjetoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoJpaRepository  extends JpaRepository<ProjetoEntity, Long> {
}
