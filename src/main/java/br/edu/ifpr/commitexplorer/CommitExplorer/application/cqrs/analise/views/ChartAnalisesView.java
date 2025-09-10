package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.service.ChartAnalisesAssembler;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Branch;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ChartAnalisesView {
    private List<TopArquivoChartView> topArquivos;
    private List<TipoCommitChartView> tipoCommits;
    private List<FrequenciaCommitChartView> frequenciaCommits;
    private List<DistribuicaoHorarioChartView> distribuicaoHorarios;

    public static ChartAnalisesView of(List<Commit> commits) {
        return ChartAnalisesAssembler.from(commits);
    }

    public static ChartAnalisesView of(List<Commit> commits, int topNArquivos) {
        return ChartAnalisesAssembler.from(commits, topNArquivos);
    }
}
