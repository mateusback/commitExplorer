package br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos;

import lombok.Data;

import java.util.List;

@Data
public class FeedbackDto {
    List<String> pontosPositivos;
    List<String> pontosNegativos;
    List<String> sugestoes;
    String feedback;
    char nota;
    double pontuacaoGeral;
    double pontuacaoFrequenciaConsistencia;
    double pontuacaoQualidadeMensagens;
    double pontuacaoVariedadeTipos;
    double pontuacaoDistribuicaoTrabalho;
    double pontuacaoDistribuicaoTemporal;
    Double pontuacaoQualidadeTecnica;
}
