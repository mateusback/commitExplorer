package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.externalService.pmd;

import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.util.PMDExecutor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.response.AnalysisInfo;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.CodeAnalyzerService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CodeAnalyzerServiceImpl implements CodeAnalyzerService {
    @Override
    public List<AnaliseCodigo> analyze(Commit commit) {
        if (commit.getArquivosAlterados() == null || commit.getArquivosAlterados().isEmpty()) {
            return Collections.emptyList();
        }

        List<AnaliseCodigo> resultado = new ArrayList<>();

        for (ArquivoAlterado arquivo : commit.getArquivosAlterados()) {
            String nome = arquivo.getNomeArquivo();
            String content = arquivo.getConteudoDepois();

            if (nome == null || !nome.endsWith(".java") || content == null) continue;

            try {
                var report = PMDExecutor.analyzeFile(nome, content);
                var violations = report.getViolations();

                if (violations.isEmpty()) {
                    AnaliseCodigo analiseOk = new AnaliseCodigo();
                    analiseOk.registrarAnaliseBoa(arquivo);
                    resultado.add(analiseOk);
                    continue;
                }

                for (var violation : violations) {
                    AnaliseCodigo analise = new AnaliseCodigo();
                    analise.registrarAnaliseRuim(
                            violation.getRule().getName() + ": " + violation.getDescription(),
                            violation.getRule().getPriority().getPriority(),
                            violation.getRule().getPriority().getPriority(),
                            arquivo
                    );
                    resultado.add(analise);
                }

            } catch (Exception e) {
                log.warn("Erro ao analisar arquivo {} no commit {}", nome, commit.getHash(), e);
            }
        }

        return resultado;
    }

    @Override
    public boolean isValidCommit(Commit commit) {
        return commit.getArquivosAlterados().stream()
                .anyMatch(a -> a.getNomeArquivo() != null && a.getNomeArquivo().endsWith(".java"));
    }
}
