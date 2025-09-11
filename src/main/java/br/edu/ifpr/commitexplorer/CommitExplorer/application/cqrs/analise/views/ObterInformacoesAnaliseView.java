package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.IndicadoresAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Branch;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.FeedbackAnalise;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoFeedback;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Data
public class ObterInformacoesAnaliseView {
    private GeralAnaliseView geral;
    private FeedbackDinamicoView feedback;
    private List<AutorResumoView> autores;

    public ObterInformacoesAnaliseView() {}

    public ObterInformacoesAnaliseView(AnaliseProjeto analiseProjeto, Branch branch)
    {
        this.geral = new GeralAnaliseView(analiseProjeto);
        var feedbacks = Optional.ofNullable(analiseProjeto)
                .map(AnaliseProjeto::getFeedbacks)
                .orElse(List.of());

        var feedbackEscolhido = feedbacks.stream()
                .filter(Objects::nonNull)
                .filter(f -> f.getTipo() != null)
                .filter(f -> f.getTipo() != TipoFeedback.GERAL)
                .findFirst()
                .or(() -> feedbacks.stream()
                        .filter(Objects::nonNull)
                        .filter(f -> f.getTipo() == TipoFeedback.GERAL)
                        .findFirst()
                );

        this.feedback = feedbackEscolhido
                .map(FeedbackDinamicoView::new)
                .orElse(null);

        this.autores = feedbacks.stream()
                .filter(Objects::nonNull)
                .filter(f -> TipoFeedback.AUTOR.equals(f.getTipo()))
                .map(AutorResumoView::new)
                .toList();
    }
}
