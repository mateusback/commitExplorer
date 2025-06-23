package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views.AnalisarRepositorioView;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.Command;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class AnalisarRepositorioCommand implements Command<AnalisarRepositorioView> {

    private final List<RepositorioAlvo> repositorios;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String accessToken;
    private final String projectUrl;

    public AnalisarRepositorioCommand(List<RepositorioAlvo> repositorios, LocalDate startDate, LocalDate endDate, String accessToken, String projectUrl) {
        this.repositorios = repositorios;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accessToken = accessToken;
        this.projectUrl = projectUrl;
    }

}