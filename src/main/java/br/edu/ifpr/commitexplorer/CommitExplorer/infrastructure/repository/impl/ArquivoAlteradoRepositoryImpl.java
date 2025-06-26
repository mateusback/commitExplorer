package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.impl;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.AnaliseCodigoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.ArquivoAlteradoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.AnaliseCodigoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.ArquivoAlteradoEntity;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.AnaliseCodigoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper.ArquivoAlteradoMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.AnaliseCodigoJpaRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.repository.interfaces.ArquivoAlteradoJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ArquivoAlteradoRepositoryImpl implements ArquivoAlteradoRepository {

    private final ArquivoAlteradoJpaRepository jpaRepository;
    private final ArquivoAlteradoMapper mapper;

    public ArquivoAlteradoRepositoryImpl(
            ArquivoAlteradoJpaRepository jpaRepository,
            ArquivoAlteradoMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ArquivoAlterado> saveAll(List<ArquivoAlterado> arquivos) {
        List<ArquivoAlteradoEntity> entities = arquivos.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());

        List<ArquivoAlteradoEntity> savedEntities = jpaRepository.saveAll(entities);

        return savedEntities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
