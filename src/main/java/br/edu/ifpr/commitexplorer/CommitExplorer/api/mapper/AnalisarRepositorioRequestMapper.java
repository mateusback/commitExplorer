package br.edu.ifpr.commitexplorer.CommitExplorer.api.mapper;

import br.edu.ifpr.commitexplorer.CommitExplorer.api.dto.AnalisarRepositorioRequest;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.AnalisarRepositorioCommand;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.commands.RepositorioAlvo;

import java.util.List;
import java.util.stream.Collectors;

public class AnalisarRepositorioRequestMapper {

    //TODO - verificar se é necessário o uso do MapStruct para este mapper
    public static AnalisarRepositorioCommand toCommand(AnalisarRepositorioRequest request) {
        List<RepositorioAlvo> repositorios = request.getRepositorios()
                .stream()
                .map(dto -> new RepositorioAlvo(dto.getRepoUrl(), dto.getBranch()))
                .collect(Collectors.toList());

        return new AnalisarRepositorioCommand(
                repositorios,
                request.getStartDate(),
                request.getEndDate(),
                request.getAccessToken(),
                request.getProjectUrl()
        );
    }
}
