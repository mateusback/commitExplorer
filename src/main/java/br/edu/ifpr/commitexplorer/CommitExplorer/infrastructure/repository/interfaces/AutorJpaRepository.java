package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutorJpaRepository extends JpaRepository<AutorEntity, Long> {

    Optional<AutorEntity> findByEmail(String email);
    Optional<AutorEntity> findByName(String name);
}
