package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoView {
    private Long id;
    private String nome;
    private Integer totalProjetos;
    private Double pontuacaoMedia;
}

