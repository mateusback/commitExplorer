package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.externalService.git;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Branch;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoAcao;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.service.GitCommitExtractor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GitCommitExtractorImpl implements GitCommitExtractor {

    @Override
    public List<Commit> extrairCommitsComDiffs(File repositorioClonado, String branch, LocalDate dataInicio, LocalDate dataFim) {
        List<Commit> commits = new ArrayList<>();

        try (Git git = Git.open(repositorioClonado)) {
            Repository repository = git.getRepository();
            ObjectId branchHead = repository.resolve("refs/heads/" + branch);

            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit headCommit = revWalk.parseCommit(branchHead);
                revWalk.markStart(headCommit);

                RevCommit previous = null;
                for (RevCommit current : revWalk) {
                    if (previous == null) {
                        previous = current;
                        continue;
                    }

                    List<ArquivoAlterado> arquivos = extrairDiffs(repository, previous, current);

                    LocalDateTime dataCommit = current.getAuthorIdent()
                            .getWhenAsInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();

                    Commit novoCommit = new Commit();
                    novoCommit.registrarCommit(
                            current.getFullMessage(),
                            current.getName(),
                            dataCommit,
                            arquivos
                    );

                    Autor autor = new Autor(current.getAuthorIdent().getName(), current.getAuthorIdent().getEmailAddress());
                    novoCommit.atribuirAutor(autor);

                    Branch b = new Branch(branch);
                    novoCommit.atribuirBranch(b);

                    commits.add(novoCommit);
                    previous = current;
                }

                revWalk.dispose();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair commits: " + e.getMessage(), e);
        }

        return commits;
    }


    private List<ArquivoAlterado> extrairDiffs(Repository repository, RevCommit oldCommit, RevCommit newCommit) throws Exception {
        List<ArquivoAlterado> arquivos = new ArrayList<>();

        try (ObjectReader reader = repository.newObjectReader()) {
            CanonicalTreeParser oldTree = new CanonicalTreeParser();
            oldTree.reset(reader, oldCommit.getTree());

            CanonicalTreeParser newTree = new CanonicalTreeParser();
            newTree.reset(reader, newCommit.getTree());

            try (Git git = new Git(repository)) {
                List<DiffEntry> diffs = git.diff()
                        .setOldTree(oldTree)
                        .setNewTree(newTree)
                        .call();

                DiffFormatter formatter = new DiffFormatter(new ByteArrayOutputStream());
                formatter.setRepository(repository);
                formatter.setDiffComparator(RawTextComparator.DEFAULT);
                formatter.setDetectRenames(true);

                for (DiffEntry diff : diffs) {
                    ArquivoAlterado arquivo = new ArquivoAlterado();
                    arquivo.atribuirNomeArquivo(diff.getNewPath());


                    String conteudoAntes = null;
                    String conteudoDepois = null;

                    if (diff.getChangeType() != DiffEntry.ChangeType.ADD) {
                        ObjectId oldId = diff.getOldId().toObjectId();
                        ObjectLoader oldLoader = repository.open(oldId);
                        conteudoAntes = new String(oldLoader.getBytes(), StandardCharsets.UTF_8);
                    }

                    if (diff.getChangeType() != DiffEntry.ChangeType.DELETE) {
                        ObjectId newId = diff.getNewId().toObjectId();
                        ObjectLoader newLoader = repository.open(newId);
                        conteudoDepois = new String(newLoader.getBytes(), StandardCharsets.UTF_8);
                    }

                    String patch = getPatchAsString(repository, diff);
                    arquivo.adicionarAlteracoes(
                            mapChangeType(diff.getChangeType()),
                            conteudoAntes,
                            conteudoDepois,
                            contarRemovidas(patch),
                            contarAdicionadas(patch)
                    );

                    arquivos.add(arquivo);
                }
            }
        }

        return arquivos;
    }

    private TipoAcao mapChangeType(DiffEntry.ChangeType changeType) {
        return switch (changeType) {
            case ADD -> TipoAcao.ADICIONADO;
            case MODIFY -> TipoAcao.MODIFICADO;
            case DELETE -> TipoAcao.REMOVIDO;
            case RENAME -> TipoAcao.RENOMEADO;
            case COPY -> TipoAcao.COPIADO;
            default -> throw new IllegalArgumentException("Tipo de alteração desconhecido: " + changeType);
        };
    }

    private String getPatchAsString(Repository repo, DiffEntry diff) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (DiffFormatter formatter = new DiffFormatter(out)) {
            formatter.setRepository(repo);
            formatter.format(diff);
            return out.toString(StandardCharsets.UTF_8);
        }
    }

    private int contarAdicionadas(String patch) {
        return (int) Arrays.stream(patch.split("\n"))
                .filter(l -> l.startsWith("+") && !l.startsWith("+++"))
                .count();
    }

    private int contarRemovidas(String patch) {
        return (int) Arrays.stream(patch.split("\n"))
                .filter(l -> l.startsWith("-") && !l.startsWith("---"))
                .count();
    }

}
