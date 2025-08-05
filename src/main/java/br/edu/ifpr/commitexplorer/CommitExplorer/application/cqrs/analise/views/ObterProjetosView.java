package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;
import lombok.Getter;

import java.util.List;

@Getter
public class ObterProjetosView {
    List<Projeto> projetos;

    public ObterProjetosView(List<Projeto> projetos) {
        this.projetos = projetos;
    }
}
