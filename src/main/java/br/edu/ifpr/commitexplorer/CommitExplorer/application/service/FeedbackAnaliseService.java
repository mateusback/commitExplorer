package br.edu.ifpr.commitexplorer.CommitExplorer.application.service;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;

public interface FeedbackAnaliseService {
    void calcularFeedback(AnaliseProjeto analise);
}
