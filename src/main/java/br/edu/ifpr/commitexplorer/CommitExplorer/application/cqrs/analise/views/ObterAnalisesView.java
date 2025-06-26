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
            dto.setNomeProjeto(analise.getSolicitacaoAnalise().getProjetoUrl());
            dto.setNomeBranch(analise.getBranch().getNome());
            dto.setUrlRepo(analise.getBranch().getRepositorio().getUrlRepo());
            dto.setTotalCommits(analise.getTotalCommits());
            dto.setTotalAutores(analise.getTotalAutores());
            dto.setQuantidadeCodeSmells(analise.getQuantidadeCodeSmells());
            dto.setComplexidadeMedia(analise.getComplexidadeMedia());
            dto.setPontuacaoTotal(analise.getPontuacaoTotal());
            return dto;
        }).toList();
    }
}
