package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.externalService.git.results;

import java.io.File;

public record CloneResult(boolean ok, File dir, String erro) {
}
