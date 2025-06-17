package br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity;

import jakarta.persistence.*;
import java.util.List;

public class ArquivoAlterado {
    private Long idArquivoAlterado;
    private Integer flgTipoAcao;
    private Integer qtdLinhasAdicionadas;
    private Integer qtdLinhasRemovidas;
    private String nomeArquivo;

    private Commit commit;
    private List<AnaliseCodigo> analisesCodigo;
}
