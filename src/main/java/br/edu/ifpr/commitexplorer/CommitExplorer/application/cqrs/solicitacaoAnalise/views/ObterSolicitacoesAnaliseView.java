package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.solicitacaoAnalise.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;
import lombok.Data;

import java.util.List;

@Data
public class ObterSolicitacoesAnaliseView {

    public ObterSolicitacoesAnaliseView(List<SolicitacaoAnalise> solicitacoesAnalise) {
        this.solicitacoes = solicitacoesAnalise.stream().map(SolicitacaoView::new).toList();
    }

    public ObterSolicitacoesAnaliseView() {
        solicitacoes = List.of();
    }

    private List<SolicitacaoView> solicitacoes;
}
