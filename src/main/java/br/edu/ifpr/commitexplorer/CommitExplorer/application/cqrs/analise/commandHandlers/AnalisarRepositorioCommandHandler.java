package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commandHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.AnalisarRepositorioCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.ProcessarSolicitacaoCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.AnalisarRepositorioView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.service.SolicitacaoAnaliseExecutor;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.CommandHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.mediator.MediatorHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.security.EncryptionService;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.SolicitacaoAnaliseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
public class AnalisarRepositorioCommandHandler implements CommandHandler<AnalisarRepositorioCommand, AnalisarRepositorioView> {

    private final EncryptionService encryptionService;
    private final SolicitacaoAnaliseRepository solicitacaoAnaliseRepository;
    private final SolicitacaoAnaliseExecutor solicitacaoAnaliseExecutor;

    public AnalisarRepositorioCommandHandler(
            EncryptionService encryptionService,
            SolicitacaoAnaliseRepository solicitacaoAnaliseRepository,
            SolicitacaoAnaliseExecutor solicitacaoAnaliseExecutor
    ) {
        this.encryptionService = encryptionService;
        this.solicitacaoAnaliseRepository = solicitacaoAnaliseRepository;
        this.solicitacaoAnaliseExecutor = solicitacaoAnaliseExecutor;
    }

    @Override
    public AnalisarRepositorioView handle(AnalisarRepositorioCommand command) {
        var repositoriosParaAnalisar = 0;
        log.info("Nova solicitação de análise de repositórios recebida");

        for (var repositorio : command.getRepositorios()) {
            var analiseJaRealizada = solicitacaoAnaliseRepository.existsRecentByRepositorioUrlAndBranch(
                    repositorio.getRepoUrl(),
                    repositorio.getBranch(),
                    LocalDateTime.now().minusHours(4)
            );

            if (analiseJaRealizada)
                continue;

            repositoriosParaAnalisar++;

            var encryptedToken = encryptionService.encrypt(command.getAccessToken());
            var dataInicio = command.getStartDate() != null ? command.getStartDate() : LocalDate.now().minusMonths(3);
            var dataFim = command.getEndDate() != null ? command.getEndDate() : LocalDate.now();
            var solicitacao = new SolicitacaoAnalise();
            solicitacao.registrarNovaSolicitacao(
                    repositorio.getRepoUrl(),
                    repositorio.getBranch(),
                    command.getProjectUrl(),
                    encryptedToken,
                    dataInicio,
                    dataFim
            );

            var entity = solicitacaoAnaliseRepository.save(solicitacao);
            var processarCommand = new ProcessarSolicitacaoCommand(entity.getIdSolicitacaoAnalise());
            log.info("Enviando comando para processar solicitação de análise: {}", entity.getIdSolicitacaoAnalise());
            solicitacaoAnaliseExecutor.processar(processarCommand);
        }

        log.info("Total de repositórios para análise: {}", repositoriosParaAnalisar);
        return new AnalisarRepositorioView();
    }
}