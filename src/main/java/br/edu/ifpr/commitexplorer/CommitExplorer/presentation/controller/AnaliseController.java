package br.edu.ifpr.commitexplorer.CommitExplorer.presentation.controller;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries.ObterInformacoesAnaliseQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.queries.ObterSolicitacoesAnaliseQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.BaseResponse;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.ResponseBuilder;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator.MediatorHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Analise Controller",
        description = "Controlador responsável por análises de commits.")
@RequestMapping("/api/v1/analysis")
public class AnaliseController {

    private final MediatorHandler mediatorHandler;

    public AnaliseController(MediatorHandler mediatorHandler) {
        this.mediatorHandler = mediatorHandler;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<Object>> fetchInfosAnalises(@PathVariable("id") Long id) {
        var command = new ObterInformacoesAnaliseQuery(id);
        var view = mediatorHandler.enviarConsulta(command);
        return ResponseEntity.ok(ResponseBuilder.success(view, "Informações da análise obtidas com sucesso"));
    }

    @GetMapping(value = "/solicitacoes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<Object>> fetchStatusSolicitacoes(@AuthenticationPrincipal UserDetails me) {
        var command = new ObterSolicitacoesAnaliseQuery(me.getUsername());
        var view = mediatorHandler.enviarConsulta(command);
        return ResponseEntity.ok(ResponseBuilder.success(view, "Status das solicitações obtido com sucesso"));
    }
}
