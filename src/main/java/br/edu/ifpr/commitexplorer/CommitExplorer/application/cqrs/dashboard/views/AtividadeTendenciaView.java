package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.dashboard.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtividadeTendenciaView {
    private List<LocalDate> datas;
    private List<Integer> commits;
    private List<Integer> analises;
}

