package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisaoGeralAlunosView {
    private int totalAlunos;
    private int alunosAtivos;
    private int alunosComProjetos;
    private double pontuacaoMediaAlunos;
    private List<AlunoView> topAlunos;
}

