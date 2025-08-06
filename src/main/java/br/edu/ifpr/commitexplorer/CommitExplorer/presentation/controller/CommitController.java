package br.edu.ifpr.commitexplorer.CommitExplorer.presentation.controller;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.commit.queries.ObterInformacoesCommitQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.commit.views.InformacoesCommitView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator.MediatorHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Commit Controller",
        description = "Controlardor responsável por itens de commits.")
@RequestMapping("/api/v1/commits")
public class CommitController {

    private final MediatorHandler mediatorHandler;

    public CommitController(MediatorHandler mediatorHandler) {
        this.mediatorHandler = mediatorHandler;
    }

    @Operation(summary = "Buscar informações de um commit",
            description = "Busca os arquivos alterados e informações gerais de um commit.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informações do commit recuperadas com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InformacoesCommitView.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InformacoesCommitView> fetchComitInfo(@PathVariable("id") Long id) {
        var command = new ObterInformacoesCommitQuery(id);
        var view = mediatorHandler.enviarConsulta(command);
        return ResponseEntity.ok(view);
    }
}
