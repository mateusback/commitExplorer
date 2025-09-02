package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.externalService.git;

import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.security.EncryptionService;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.SolicitacaoAnaliseRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.GitRepositoryCloner;
import br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.externalService.git.results.CloneResult;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

@Slf4j
@Service
public class GitRepositoryClonerImpl implements GitRepositoryCloner {

    private final EncryptionService encryptionService;
    private final SolicitacaoAnaliseRepository solicitacaoAnaliseRepository;

    public GitRepositoryClonerImpl(EncryptionService encryptionService, SolicitacaoAnaliseRepository solicitacaoAnaliseRepository) {
        this.encryptionService = encryptionService;
        this.solicitacaoAnaliseRepository = solicitacaoAnaliseRepository;
    }

    public CloneResult clone(SolicitacaoAnalise solicitacaoAnalise) {
        String repoUrl = solicitacaoAnalise.getRepositorioUrl();
        String branch = solicitacaoAnalise.getBranch();
        String token = solicitacaoAnalise.getToken();
        try {
            File tempDir = Files.createTempDirectory("repo").toFile();

            var cloneCommand = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setBranch(branch)
                    .setDirectory(tempDir);

            if (token != null && !token.isBlank()) {
                cloneCommand.setCredentialsProvider(
                        new UsernamePasswordCredentialsProvider("git", encryptionService.decrypt(token))
                );
            }

            cloneCommand.call();
            return new CloneResult(true, tempDir, null);
        } catch (Exception e) {
            return new CloneResult(false, null, e.getMessage());
        }
    }
}