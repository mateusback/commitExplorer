package br.edu.ifpr.commitexplorer.CommitExplorer.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ConquistaDesbloqueadaProjeto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codConquistaDesbloqueadaProjeto;

    private LocalDateTime dtaDesbloqueio;

    @ManyToOne
    @JoinColumn(name = "codConquistaProjeto")
    private ConquistaProjeto conquistaProjeto;

    @ManyToOne
    @JoinColumn(name = "codProjeto")
    private Projeto projeto;
}
