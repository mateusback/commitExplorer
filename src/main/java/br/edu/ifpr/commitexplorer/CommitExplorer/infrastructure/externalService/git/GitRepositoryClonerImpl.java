package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.externalService.git;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.GitRepositoryCloner;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

@Service
public class GitRepositoryClonerImpl implements GitRepositoryCloner {

    public File clone(String repoUrl, String branch, String token) {
        try {
            File tempDir = Files.createTempDirectory("repo").toFile();

            var cloneCommand = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setBranch(branch)
                    .setDirectory(tempDir);

            if (token != null && !token.isBlank()) {
                cloneCommand.setCredentialsProvider(
                        new UsernamePasswordCredentialsProvider("git", token)
                );
            }

            cloneCommand.call();
            return tempDir;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao clonar repositório", e);
        }
    }
}