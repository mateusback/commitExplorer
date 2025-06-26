package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity.SolicitacaoAnaliseEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SolicitacaoAnaliseMapperTest {

    @Autowired
    private SolicitacaoAnaliseMapper mapper;

    @Test
    void deveMapearDomainParaEntityCorretamente() {
        // Arrange
        SolicitacaoAnalise domain = new SolicitacaoAnalise();
        domain.registrarNovaSolicitacao(
                "https://github.com/test/repo",
                "main",
                "https://projeto.com",
                "secrettoken",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 31)
        );

        // Act
        SolicitacaoAnaliseEntity entity = mapper.toEntity(domain);

        // Assert
        assertEquals(domain.getRepositorioUrl(), entity.getRepositorioUrl());
        assertEquals(domain.getBranch(), entity.getBranch());
        assertEquals(domain.getProjetoUrl(), entity.getProjetoUrl());
        assertEquals(domain.getToken(), entity.getToken());
        assertEquals(domain.getDataInicio(), entity.getDataInicio());
        assertEquals(domain.getDataFim(), entity.getDataFim());
        assertEquals(domain.getStatus(), entity.getStatus());
    }

    @Test
    void deveMapearEntityParaDomainCorretamente() {
        // Arrange
        SolicitacaoAnaliseEntity entity = new SolicitacaoAnaliseEntity();
        entity.setRepositorioUrl("https://github.com/test/repo");
        entity.setBranch("main");
        entity.setProjetoUrl("https://projeto.com");
        entity.setToken("secrettoken");
        entity.setDataInicio(LocalDate.of(2024, 1, 1));
        entity.setDataFim(LocalDate.of(2024, 1, 31));
        entity.setStatus(br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.StatusSolicitacao.PENDENTE);

        // Act
        SolicitacaoAnalise domain = mapper.toDomain(entity);

        // Assert
        assertEquals(entity.getRepositorioUrl(), domain.getRepositorioUrl());
        assertEquals(entity.getBranch(), domain.getBranch());
        assertEquals(entity.getProjetoUrl(), domain.getProjetoUrl());
        assertEquals(entity.getToken(), domain.getToken());
        assertEquals(entity.getDataInicio(), domain.getDataInicio());
        assertEquals(entity.getDataFim(), domain.getDataFim());
        assertEquals(entity.getStatus(), domain.getStatus());
    }
}
