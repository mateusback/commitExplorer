package br.edu.ifpr.commitexplorer.CommitExplorer.presentation.controller;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.queries.ObterDashboardQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.views.ObterDashboardView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.BaseResponse;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.ResponseBuilder;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.model.User;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator.MediatorHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Dashboard Controller",
        description = "Controlador responsável por fornecer dados estatísticos e visão geral do sistema.")
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final MediatorHandler mediatorHandler;

    public DashboardController(MediatorHandler mediatorHandler) {
        this.mediatorHandler = mediatorHandler;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<ObterDashboardView>> obterDashboard(
            @AuthenticationPrincipal User usuario,
            @RequestHeader(name = "X-Professor", defaultValue = "false") boolean ehProfessor) {

        log.info("Requisição de dashboard recebida para usuarioId={}, ehProfessor={}",
                usuario != null ? usuario.getId() : "null", ehProfessor);

        if (usuario == null) {
            log.warn("Usuário não autenticado tentando acessar dashboard");
            return ResponseEntity.status(401).body(ResponseBuilder.unauthorized());
        }

        var query = new ObterDashboardQuery(usuario.getId(), ehProfessor);
        var view = mediatorHandler.enviarConsulta(query);

        return ResponseEntity.ok(ResponseBuilder.success(view, "Dashboard obtido com sucesso"));
    }
}
