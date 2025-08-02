package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseProjeto;
import lombok.Data;

import java.util.List;

@Data
public class ObterAnalisesView {
    private List<ResumoAnaliseView> analises;

    public ObterAnalisesView(List<AnaliseProjeto> analisesProjeto) {
        this.analises = analisesProjeto.stream().map(analise -> {
            var dto = new ResumoAnaliseView();
            dto.setDataAnalise(analise.getDataAnalise());
            dto.setTotalCommits(analise.getTotalCommits());
            dto.setTotalAutores(analise.getTotalAutores());
            dto.setQuantidadeCodeSmells(analise.getQuantidadeCodeSmells());
            dto.setComplexidadeMedia(analise.getComplexidadeMedia());
            dto.setPontuacaoTotal(analise.getPontuacaoTotal());

            if (analise.getSolicitacaoAnalise() != null) {
                dto.setNomeProjeto(analise.getSolicitacaoAnalise().getProjetoUrl());
            } else {
                dto.setNomeProjeto("Projeto não definido");
            }

            if (analise.getBranch() != null) {
                dto.setNomeBranch(analise.getBranch().getNome());

                if (analise.getBranch().getRepositorio() != null) {
                    dto.setUrlRepo(analise.getBranch().getRepositorio().getUrlRepo());
                } else {
                    dto.setUrlRepo("Repositório não definido");
                }
            } else {
                dto.setNomeBranch("Branch não definido");
                dto.setUrlRepo("Repositório não definido");
            }

            return dto;
        }).toList();
    }
}
