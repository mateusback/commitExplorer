package br.edu.ifpr.commitexplorer.CommitExplorer.authentication.dto;

import java.util.Set;

public record CurrentUserResponse(Long id, String name, String email, Set<String> roles) {}

