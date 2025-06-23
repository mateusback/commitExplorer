package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.SolicitacaoAnalise;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SolicitacaoAnaliseRepository {
    SolicitacaoAnalise save(SolicitacaoAnalise solicitacaoAnalise);
    SolicitacaoAnalise findById(Long idSolicitacaoAnalise);
    boolean existsRecentByRepositorioUrlAndBranch(String repositorioUrl, String branch, LocalDateTime threshold);
}

