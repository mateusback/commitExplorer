package br.edu.ifpr.commitexplorer.CommitExplorer.presentation.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.presentation.dto.AnalisarRepositorioRequest;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.AnalisarRepositorioCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.RepositorioAlvo;

import java.util.List;
import java.util.stream.Collectors;

public class AnalisarRepositorioRequestMapper {

    public static AnalisarRepositorioCommand toCommand(AnalisarRepositorioRequest request, String email) {
        List<RepositorioAlvo> repositorios = request.getRepositories()
                .stream()
                .map(dto -> new RepositorioAlvo(dto.getUrl(), dto.getBranch()))
                .collect(Collectors.toList());

        return new AnalisarRepositorioCommand(
                repositorios,
                request.getStartDate(),
                request.getEndDate(),
                request.getGithubToken(),
                request.getProjectUrl(),
                request.getProjectName(),
                email
        );
    }
}
