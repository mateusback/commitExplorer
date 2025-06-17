package br.edu.ifpr.commitexplorer.CommitExplorer.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AnalisarRepositorioRequest {

    @Schema(description = "Token de acesso ao repositório (se necessário)", example = "ghp_ABC123...", nullable = true)
    private String accessToken;

    @Schema(description = "Lista de repositórios com URL e branch")
    private List<RepositorioAlvoRequest> repositorios;

    @Schema(description = "Data inicial para análise dos commits", example = "2024-01-01", nullable = true)
    private LocalDate startDate;

    @Schema(description = "Data final para análise dos commits", example = "2024-12-31", nullable = true)
    private LocalDate endDate;

    @Getter
    @Setter
    public static class RepositorioAlvoRequest {

        @Schema(description = "URL do repositório Git", example = "https://github.com/usuario/repositorio")
        private String repoUrl;

        @Schema(description = "Branch que será analisada", example = "main")
        private String branch;
    }
}
