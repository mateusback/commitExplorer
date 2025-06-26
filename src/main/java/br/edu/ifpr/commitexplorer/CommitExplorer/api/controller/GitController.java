package br.edu.ifpr.commitexplorer.CommitExplorer.api.controller;

import br.edu.ifpr.commitexplorer.CommitExplorer.api.dto.AnalisarRepositorioRequest;
import br.edu.ifpr.commitexplorer.CommitExplorer.api.mapper.AnalisarRepositorioRequestMapper;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries.ObterAnalisesQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.AnalisarRepositorioView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ObterAnalisesView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator.MediatorHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnalisarRepositorioView.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping("/analyze")
    public AnalisarRepositorioView analyzeRepository(@RequestBody AnalisarRepositorioRequest request) {

        var command = AnalisarRepositorioRequestMapper.toCommand(request);

        return mediatorHandler.enviarComando(command);
    }

    @Operation(summary = "Obter análises de repositórios",
            description = "Recupera todas as análises de repositórios realizadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Análises recuperadas com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ObterAnalisesView.class))),
            @ApiResponse(responseCode = "404", description = "Nenhuma análise encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/analysis")
    public ObterAnalisesView getAnalysis() {
        return mediatorHandler.enviarConsulta(new ObterAnalisesQuery());
    }
}
