package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseProjetoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnaliseProjetoJpaRepository extends JpaRepository<AnaliseProjetoEntity, Long> {
    List<AnaliseProjetoEntity> findByProjeto_IdProjeto(Long projetoId);
}