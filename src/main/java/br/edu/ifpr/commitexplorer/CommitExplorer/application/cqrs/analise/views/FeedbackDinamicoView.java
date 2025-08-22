package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import lombok.Data;

import java.util.List;

@Data
public class FeedbackDinamicoView{
    List<String> pontosPositivos;
    List<String> pontosNegativos;
    List<String> sugestoes;
    String feedback;
    char nota;
}
