package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoPorPontuacaoView {
    private Long id;
    private String nome;
    private Integer totalAnalises;
    private Double pontuacaoMedia;
    private LocalDateTime dataUltimaAnalise;
}

