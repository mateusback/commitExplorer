package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands;

import lombok.Getter;

@Getter
public class  RepositorioAlvo{
    public String repoUrl;
    public String branch;

    public RepositorioAlvo(String repoUrl, String branch) {
        this.repoUrl = repoUrl;
        this.branch = branch;
    }
}
