package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.FeedbackAnalise;
import lombok.Data;

import java.util.List;

@Data
public class FeedbackDinamicoView{
    List<String> pontosPositivos;
    List<String> pontosNegativos;
    List<String> sugestoes;
    double pontuacaoGeral;
    char conceito;
    double pontuacaoFrequenciaConsistencia;
    double pontuacaoQualidadeMensagens;
    double pontuacaoVariedadeTipos;
    double pontuacaoDistribuicaoTrabalho;
    double pontuacaoDistribuicaoTemporal;
    Double pontuacaoQualidadeTecnica;
    String resumo;

    public FeedbackDinamicoView() {}

    public FeedbackDinamicoView(FeedbackAnalise feed){
        this.pontosNegativos = feed.getPontosNegativos();
        this.pontosPositivos = feed.getPontosPositivos();
        this.sugestoes = feed.getSugestoesMelhoria();
        this.pontuacaoGeral = feed.getPontuacaoGeral();
        this.conceito = feed.getConceito();
        this.pontuacaoFrequenciaConsistencia = feed.getPontuacaoFrequenciaConsistencia();
        this.pontuacaoQualidadeMensagens = feed.getPontuacaoQualidadeMensagens();
        this.pontuacaoVariedadeTipos = feed.getPontuacaoVariedadeTipos();
        this.pontuacaoDistribuicaoTrabalho = feed.getPontuacaoDistribuicaoTrabalho();
        this.pontuacaoDistribuicaoTemporal = feed.getPontuacaoDistribuicaoTemporal();
        this.pontuacaoQualidadeTecnica = feed.getPontuacaoQualidadeTecnica();
        this.resumo = feed.getResumo();
    }
}
