package br.edu.ifpr.commitexplorer.CommitExplorer.presentation.controller;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.queries.ObterInformacoesAnaliseQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.ObterInformacoesAnaliseView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.commandHandlers.ReenviarSolicitacaoCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.queries.ObterSolicitacoesAnaliseQuery;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.views.ObterSolicitacoesAnaliseView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.BaseResponse;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.ResponseBuilder;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator.MediatorHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.AnaliseProjetoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Analise Controller",
        description = "Controlador responsável por  solicitações de análises e análises de commits.")
@RequestMapping("/api/v1/analysis")
public class AnaliseController {

    private final MediatorHandler mediatorHandler;
    private final AnaliseProjetoRepository analiseRepository;

    public AnaliseController(MediatorHandler mediatorHandler, AnaliseProjetoRepository analiseRepository) {
        this.mediatorHandler = mediatorHandler;
        this.analiseRepository = analiseRepository;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<ObterInformacoesAnaliseView>> fetchInfosAnalises(@PathVariable("id") Long id,
                                                                                        @AuthenticationPrincipal UserDetails me) {
        var query = new ObterInformacoesAnaliseQuery(id);
        var view = mediatorHandler.enviarConsulta(query);

        boolean isProfessor = me != null && me.getAuthorities() != null && me.getAuthorities().stream()
                .anyMatch(a -> "PROFESSOR".equals(a.getAuthority()));
        if (!isProfessor && view != null && view.getFeedback() != null) {
            view.getFeedback().setConceito(null);
            view.getAutores().forEach(autor -> autor.getFeedback().setConceito(null));
        }

        return ResponseEntity.ok(ResponseBuilder.success(view, "Informações da análise obtidas com sucesso"));
    }

    @GetMapping(value = "/solicitacoes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<ObterSolicitacoesAnaliseView>> fetchStatusSolicitacoes(@AuthenticationPrincipal UserDetails me) {
        var query = new ObterSolicitacoesAnaliseQuery(me.getUsername());
        var view = mediatorHandler.enviarConsulta(query);
        return ResponseEntity.ok(ResponseBuilder.success(view, "Status das solicitações obtido com sucesso"));
    }

    @PatchMapping(value = "/{id}/reenviar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<Object>> retryAnalise(@PathVariable("id") Long id) {
        var command = new ReenviarSolicitacaoCommand(id);
        var view = mediatorHandler.enviarComando(command);
        return ResponseEntity.ok(ResponseBuilder.success(view.getMensagem()));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<BaseResponse<Object>> deleteAnalise(@PathVariable("id") Long id,
                                                              @AuthenticationPrincipal UserDetails me) {
        var analise = analiseRepository.findById(id);
        if (analise == null) {
            return ResponseEntity.ok(ResponseBuilder.success("Análise não encontrada ou já removida"));
        }
        var projeto = analise.getProjeto();
        boolean isOwner = me != null && projeto != null && projeto.getUsuario() != null &&
                projeto.getUsuario().getEmail().equalsIgnoreCase(me.getUsername());
        boolean hasPriv = me != null && me.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ADMIN") || a.equals("PROFESSOR"));
        if (!isOwner && !hasPriv) {
            return ResponseEntity.status(403).body(ResponseBuilder.forbidden());
        }
        analiseRepository.softDelete(id);
        return ResponseEntity.ok(ResponseBuilder.success("Análise removida"));
    }
}
