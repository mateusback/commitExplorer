package br.edu.ifpr.commitexplorer.CommitExplorer.presentation.controller;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.BaseResponse;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.ResponseBuilder;
import br.edu.ifpr.commitexplorer.CommitExplorer.presentation.dto.AnalisarRepositorioRequest;
import br.edu.ifpr.commitexplorer.CommitExplorer.presentation.mapper.AnalisarRepositorioRequestMapper;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping(value = "/analyze", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<AnalisarRepositorioView>> analyzeRepository(
            @AuthenticationPrincipal UserDetails me,
            @RequestBody AnalisarRepositorioRequest request) {

        String email = me.getUsername();

        var command = AnalisarRepositorioRequestMapper.toCommand(request, email);

        var result = mediatorHandler.enviarComando(command);

        return ResponseEntity.ok(ResponseBuilder.success(result, "Repositório enviado para análise com sucesso"));
    }

    @Operation(summary = "Obter análises de repositórios",
            description = "Recupera todas as análises de repositórios realizadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Análises recuperadas com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Nenhuma análise encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/analysis")
    public ResponseEntity<BaseResponse<ObterAnalisesView>> getAnalysis() {
        var result = mediatorHandler.enviarConsulta(new ObterAnalisesQuery());
        return ResponseEntity.ok(ResponseBuilder.success(result, "Análises obtidas com sucesso"));
    }
}
