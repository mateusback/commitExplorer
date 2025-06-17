package br.edu.ifpr.commitexplorer.CommitExplorer.domain.service;

import java.io.File;

public interface GitRepositoryCloner {
    File clone(String repoUrl);
}