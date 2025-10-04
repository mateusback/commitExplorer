package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commandHandlers;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.AnalisarRepositorioCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.ProcessarSolicitacaoCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.AnalisarRepositorioView;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.service.SolicitacaoAnaliseExecutor;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.repository.UserRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.CommandHandler;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.security.EncryptionService;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.ProjetoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.SolicitacaoAnaliseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class AnalisarRepositorioCommandHandler implements CommandHandler<AnalisarRepositorioCommand, AnalisarRepositorioView> {

    private final EncryptionService encryptionService;
    private final SolicitacaoAnaliseRepository solicitacaoAnaliseRepository;
    private final SolicitacaoAnaliseExecutor solicitacaoAnaliseExecutor;
    private final UserRepository userRepository;
    private final ProjetoRepository projetoRepository;

    public AnalisarRepositorioCommandHandler(
            EncryptionService encryptionService,
            SolicitacaoAnaliseRepository solicitacaoAnaliseRepository,
            SolicitacaoAnaliseExecutor solicitacaoAnaliseExecutor,
            UserRepository userRepository,
            ProjetoRepository projetoRepository
    ) {
        this.encryptionService = encryptionService;
        this.solicitacaoAnaliseRepository = solicitacaoAnaliseRepository;
        this.solicitacaoAnaliseExecutor = solicitacaoAnaliseExecutor;
        this.userRepository = userRepository;
        this.projetoRepository = projetoRepository;
    }

    @Override
    public AnalisarRepositorioView handle(AnalisarRepositorioCommand command) {
        var repositoriosParaAnalisar = 0;
        log.info("Nova solicitação de análise de repositórios recebida");

        var usuario = userRepository.findByEmail(command.getRequestedByEmail()).orElseThrow(() -> {
            log.error("Usuário com email {} não encontrado", command.getRequestedByEmail());
            return new IllegalArgumentException("Usuário não encontrado");
        });

        var projeto = new Projeto(command.getProjectName(), command.getProjectUrl());
        projeto.definirUsuario(usuario);

        var projetoSalvo = projetoRepository.save(projeto);

        for (var repositorio : command.getRepositorios()) {
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
            solicitacao.setNomeProjeto(command.getProjectName());
            solicitacao.setUsuario(usuario);
            var entity = solicitacaoAnaliseRepository.save(solicitacao);
            var processarCommand = new ProcessarSolicitacaoCommand(entity.getIdSolicitacaoAnalise(), projetoSalvo.getIdProjeto());
            log.info("Enviando comando para processar solicitação de análise: {}", entity.getIdSolicitacaoAnalise());
            solicitacaoAnaliseExecutor.processar(processarCommand);
        }

        log.info("Total de repositórios para análise: {}", repositoriosParaAnalisar);
        return new AnalisarRepositorioView(
                "Solicitação de análise de repositórios recebida com sucesso.",
                repositoriosParaAnalisar
        );
    }
}