package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.externalService;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.GitRepositoryCloner;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

@Service
public class GitRepositoryClonerImpl implements GitRepositoryCloner {

    public File clone(String repoUrl) {
        try {
            File tempDir = Files.createTempDirectory("repo").toFile();
            Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(tempDir)
                    .call();
            return tempDir;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao clonar repositório", e);
        }
    }
}