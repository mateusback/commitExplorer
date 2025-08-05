package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.cqrs.views.View;
import lombok.Getter;

@Getter
public class AnalisarRepositorioView extends View {
    private final String message;
    private final int repositoriosParaAnalisar;

    public AnalisarRepositorioView(String message, int repositoriosParaAnalisar) {
        this.message = message;
        this.repositoriosParaAnalisar = repositoriosParaAnalisar;
    }
}
