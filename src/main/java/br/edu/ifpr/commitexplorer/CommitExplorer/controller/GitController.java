package br.edu.ifpr.commitexplorer.CommitExplorer.controller;

import br.edu.ifpr.commitexplorer.CommitExplorer.base.BaseController;
import br.edu.ifpr.commitexplorer.CommitExplorer.model.request.RepoAnalysisRequest;
import br.edu.ifpr.commitexplorer.CommitExplorer.model.response.RepoAnalysisResponse;
import br.edu.ifpr.commitexplorer.CommitExplorer.service.GitAnalyzerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Git Controller",
        description = "Controlador responsável pelas operações relacionadas a repositórios Git, como análise de commits e branches.")
@RequestMapping("/api/v1/git")
public class GitController extends BaseController {

    private final GitAnalyzerService gitAnalyzerService;

    public GitController(GitAnalyzerService gitAnalyzerService) {
        this.gitAnalyzerService = gitAnalyzerService;
    }

    @Operation(summary = "Analisar repositório Git",
            description = "Executa a análise de commits em um repositório Git específico dentro de um intervalo de tempo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Análise concluída com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RepoAnalysisResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping("/analyze")
    public ResponseEntity<RepoAnalysisResponse> analyzeRepository(@RequestBody RepoAnalysisRequest request) {
        RepoAnalysisResponse response = gitAnalyzerService.analyzeRepository(request);
        return ResponseEntity.ok(response);
    }
}
