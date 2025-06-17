package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands;

public class  RepositorioAlvo{
    public String repoUrl;
    public String branch;

    public RepositorioAlvo(String repoUrl, String branch) {
        this.repoUrl = repoUrl;
        this.branch = branch;
    }
}
