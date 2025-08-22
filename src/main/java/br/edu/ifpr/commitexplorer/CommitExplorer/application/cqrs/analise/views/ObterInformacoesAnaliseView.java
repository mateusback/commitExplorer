package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ObterInformacoesAnaliseView {
    private GeralAnaliseView geral;
    private FeedbackDinamicoView feedback;
    private List<AutorResumoView> autores;
    private Map<Long, AutorResumoView> porAutor;
}
