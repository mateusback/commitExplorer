package br.edu.ifpr.commitexplorer.CommitExplorer.presentation.controller;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries.ObterAnalisesProjetoQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries.ObterProjetosQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ObterAnalisesProjetoView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ObterAnalisesView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ObterProjetosView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.BaseResponse;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.ResponseBuilder;
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
@Tag(name = "Project Controller",
        description = "Controlador responsável pelas operações relacionadas a projetos.")
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final MediatorHandler mediatorHandler;

    public ProjectController(MediatorHandler mediatorHandler) {
        this.mediatorHandler = mediatorHandler;
    }


    @Operation(summary = "Obter lista de Projetos",
            description = "Recupera todos os projetos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projetos recuperados com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ObterAnalisesView.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<ObterProjetosView>> getProjects(@AuthenticationPrincipal UserDetails me) {
        var view = mediatorHandler.enviarConsulta(new ObterProjetosQuery(me.getUsername()));
        return ResponseEntity.ok(ResponseBuilder
                .success(view, "Projetos obtidos com sucesso"));
    }

    @GetMapping(value = "/analysis/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<ObterAnalisesProjetoView>> getProjectAnalysis(@PathVariable("id") String projectId) {
        if (projectId == null || projectId.isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseBuilder
                    .error("ID do projeto é obrigatório"));
        }

        var id = Long.parseLong(projectId);
        var view = mediatorHandler.enviarConsulta(new ObterAnalisesProjetoQuery(id));
        return ResponseEntity.ok(ResponseBuilder
                .success(view, "Análises do projeto obtidas com sucesso"));
    }

}
