package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.externalService.pmd;

import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.util.CyclomaticComplexityCalculator;
import br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.util.PMDExecutor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.AnaliseCodigo;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.interfaces.AnaliseCodigoRepository;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.CodeAnalyzerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CodeAnalyzerServiceImpl implements CodeAnalyzerService {

    private final AnaliseCodigoRepository analiseCodigoRepository;

    public CodeAnalyzerServiceImpl(AnaliseCodigoRepository analiseCodigoRepository) {
        this.analiseCodigoRepository = analiseCodigoRepository;
    }

    @Override
    public List<AnaliseCodigo> analyze(Commit commit) {
        if (commit.getArquivosAlterados() == null || commit.getArquivosAlterados().isEmpty()) {
            return Collections.emptyList();
        }

        List<AnaliseCodigo> resultado = new ArrayList<>();
        List<Float> pontuacoesArquivos = new ArrayList<>();
        List<Double> complexidadesArquivos = new ArrayList<>();

        for (ArquivoAlterado arquivo : commit.getArquivosAlterados()) {
            String nome = arquivo.getNomeArquivo();
            String content = arquivo.getConteudoDepois();

            if (nome == null || !nome.endsWith(".java") || content == null) continue;

            float pontuacaoArquivo = 100f;

            try {
                var report = PMDExecutor.analyzeFile(nome, content);
                var violations = report.getViolations();

                var complexityResult = CyclomaticComplexityCalculator.calculateComplexity(nome, content);
                double complexidadeMedia = complexityResult.getAverageComplexity();
                complexidadesArquivos.add(complexidadeMedia);

                if (violations.isEmpty()) {
                    AnaliseCodigo analiseOk = new AnaliseCodigo();
                    analiseOk.registrarAnaliseBoa(arquivo);
                    var analiseOkSalva = analiseCodigoRepository.save(analiseOk);
                    resultado.add(analiseOkSalva);
                    pontuacoesArquivos.add(pontuacaoArquivo);
                    continue;
                }

                for (var violation : violations) {
                    int prioridade = violation.getRule().getPriority().getPriority();
                    int peso = calcularPesoPorPrioridade(prioridade);
                    int linha = violation.getBeginLine();

                    AnaliseCodigo analise = new AnaliseCodigo();
                    analise.registrarAnaliseRuim(
                            violation.getRule().getName() + ": " + violation.getDescription(),
                            prioridade,
                            peso,
                            linha,
                            arquivo
                    );
                    var analiseSalva = analiseCodigoRepository.save(analise);
                    resultado.add(analiseSalva);
                    arquivo.adicionarAnalise(analiseSalva);
                    pontuacaoArquivo -= peso;
                }

                if (pontuacaoArquivo < 0) pontuacaoArquivo = 0;
                pontuacoesArquivos.add(pontuacaoArquivo);

            } catch (Exception e) {
                log.warn("Erro ao analisar arquivo {} no commit {}", nome, commit.getHash(), e);
                complexidadesArquivos.add(1.0);
            }
        }

        if (!pontuacoesArquivos.isEmpty()) {
            float media = (float) pontuacoesArquivos.stream().mapToDouble(Float::doubleValue).average().orElse(100.0);
            commit.setPontuacao(media);
        }

        if (!complexidadesArquivos.isEmpty()) {
            double complexidadeMedia = complexidadesArquivos.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(1.0);
            
            int complexidadeGeral = (int) Math.round(complexidadeMedia);
            commit.setComplexidadeGeral(complexidadeGeral);
            
            log.debug("Complexidade ciclomática calculada para commit {}: {}", commit.getHash(), complexidadeGeral);
        } else {
            commit.setComplexidadeGeral(1);
        }

        return resultado;
    }

    private int calcularPesoPorPrioridade(int prioridade) {
        return switch (prioridade) {
            case 1 -> 10;
            case 2 -> 7;
            case 3 -> 5;
            case 4 -> 3;
            case 5 -> 1;
            default -> 5;
        };
    }

    @Override
    public boolean isValidCommit(Commit commit) {
        return commit.getArquivosAlterados().stream()
                .anyMatch(a -> a.getNomeArquivo() != null && a.getNomeArquivo().endsWith(".java"));
    }
}
