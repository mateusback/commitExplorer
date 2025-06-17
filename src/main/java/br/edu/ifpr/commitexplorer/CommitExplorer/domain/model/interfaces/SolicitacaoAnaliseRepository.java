package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;

import java.time.LocalDate;

public interface SolicitacaoAnaliseRepository {
    void save(SolicitacaoAnalise solicitacaoAnalise);
    boolean existsInDateRange(LocalDate startDate, LocalDate endDate);
}

