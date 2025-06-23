package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SolicitacaoAnaliseRepository {
    void save(SolicitacaoAnalise solicitacaoAnalise);
    boolean existsRecentByRepositorioUrlAndBranch(String repositorioUrl, String branch, LocalDateTime threshold);
}

