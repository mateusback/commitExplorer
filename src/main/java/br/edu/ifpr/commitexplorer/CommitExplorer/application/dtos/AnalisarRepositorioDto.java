package br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos;

import java.time.LocalDate;

public class AnalisarRepositorioDto {
    public String accessToken;
    public String repoUrl;
    public String branch;
    public LocalDate startDate;
    public LocalDate endDate;
}
