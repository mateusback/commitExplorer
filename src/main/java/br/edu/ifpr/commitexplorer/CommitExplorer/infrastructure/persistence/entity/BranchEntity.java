package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.persistence.entity;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Projeto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "branch")
public class BranchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBranch;

    @ManyToOne
    @JoinColumn(name = "idProjeto")
    private ProjetoEntity projeto;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAnalise;

    @OneToMany(mappedBy = "branch")
    private List<CommitEntity> commits;

    @OneToMany(mappedBy = "branch")
    private List<AnaliseProjetoEntity> analises;
}
