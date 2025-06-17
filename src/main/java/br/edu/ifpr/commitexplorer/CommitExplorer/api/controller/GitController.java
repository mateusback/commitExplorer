package br.edu.ifpr.commitexplorer.CommitExplorer.api.controller;

import br.edu.ifpr.commitexplorer.CommitExplorer.api.dto.AnalisarRepositorioRequest;
import br.edu.ifpr.commitexplorer.CommitExplorer.api.mapper.AnalisarRepositorioRequestMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.AnalisarRepositorioView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator.MediatorHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.response.RepoAnalysisResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Git Controller",
        description = "Controlador responsável pelas operações relacionadas a repositórios Git, como análise de commits e branches.")
@RequestMapping("/api/v1/git")
public class GitController {

    private final MediatorHandler mediatorHandler;

    public GitController(MediatorHandler mediatorHandler) {
        this.mediatorHandler = mediatorHandler;
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
    public AnalisarRepositorioView analyzeRepository(@RequestBody AnalisarRepositorioRequest request) {

        var command = AnalisarRepositorioRequestMapper.toCommand(request);

        return mediatorHandler.enviarComando(command);
    }
}
