package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.externalService.git;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.service.GitCommitExtractor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Branch;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoAcao;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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

                for (RevCommit rc : revWalk) {
                    LocalDateTime dataCommit = rc.getAuthorIdent().getWhenAsInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDateTime();

                    LocalDate d = dataCommit.toLocalDate();
                    if (d.isBefore(dataInicio) || d.isAfter(dataFim)) continue;

                    boolean isMerge = rc.getParentCount() > 1;

                    List<ArquivoAlterado> arquivos = switch (rc.getParentCount()) {
                        case 0 -> scanArquivos(repository, new EmptyTreeIterator(), createTreeParser(repository, rc));
                        default -> isMerge
                                ? List.of()
                                : scanArquivos(repository, createTreeParser(repository, rc.getParent(0)), createTreeParser(repository, rc));
                    };

                    Commit c = new Commit();
                    c.registrarCommit(rc.getFullMessage(), rc.getName(), dataCommit, arquivos);
                    c.marcarComoMerge(isMerge);
                    c.atribuirAutor(new Autor(rc.getAuthorIdent().getName(), rc.getAuthorIdent().getEmailAddress()));
                    c.atribuirBranch(new Branch(branch));

                    commits.add(c);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair commits: " + e.getMessage(), e);
        }

        return commits;
    }

    private CanonicalTreeParser createTreeParser(Repository repo, RevCommit commit) throws Exception {
        try (ObjectReader reader = repo.newObjectReader()) {
            CanonicalTreeParser tree = new CanonicalTreeParser();
            tree.reset(reader, commit.getTree());
            return tree;
        }
    }

    private List<ArquivoAlterado> scanArquivos(Repository repo,
                                               org.eclipse.jgit.treewalk.AbstractTreeIterator oldTree,
                                               org.eclipse.jgit.treewalk.AbstractTreeIterator newTree) throws Exception {
        List<ArquivoAlterado> arquivos = new ArrayList<>();

        try (DiffFormatter fmt = new DiffFormatter(DisabledOutputStream.INSTANCE)) {
            fmt.setRepository(repo);
            fmt.setDiffComparator(RawTextComparator.DEFAULT);
            fmt.setDetectRenames(true);

            List<DiffEntry> diffs = fmt.scan(oldTree, newTree);

            ObjectId zero = ObjectId.zeroId();

            for (DiffEntry diff : diffs) {
                String nomeArquivo = (diff.getChangeType() == DiffEntry.ChangeType.DELETE)
                        ? diff.getOldPath() : diff.getNewPath();

                String antes = readBlob(repo, diff.getOldId().toObjectId(), zero);
                String depois = readBlob(repo, diff.getNewId().toObjectId(), zero);

                int add = 0, del = 0;
                var edits = fmt.toFileHeader(diff).toEditList();
                for (var e : edits) {
                    del += (e.getEndA() - e.getBeginA());
                    add += (e.getEndB() - e.getBeginB());
                }

                switch (diff.getChangeType()) {
                    case ADD -> { del = 0; if (antes == null) antes = ""; }
                    case DELETE -> { add = 0; if (depois == null) depois = ""; }
                    default -> {}
                }

                ArquivoAlterado arq = new ArquivoAlterado();
                arq.atribuirNomeArquivo(nomeArquivo);
                arq.adicionarAlteracoes(
                        mapChangeType(diff.getChangeType()),
                        safe(antes),
                        safe(depois),
                        del,
                        add
                );

                arquivos.add(arq);
            }
        }

        return arquivos;
    }

    private String readBlob(Repository repo, ObjectId id, ObjectId zero) throws Exception {
        if (id == null || id.equals(zero)) return null;
        byte[] bytes = repo.open(id).getBytes();
        return limparConteudo(new String(bytes, StandardCharsets.UTF_8));
    }

    private static String safe(String s) { return s == null ? "" : s; }

    private TipoAcao mapChangeType(DiffEntry.ChangeType t) {
        return switch (t) {
            case ADD -> TipoAcao.ADICIONADO;
            case MODIFY -> TipoAcao.MODIFICADO;
            case DELETE -> TipoAcao.REMOVIDO;
            case RENAME -> TipoAcao.RENOMEADO;
            case COPY -> TipoAcao.COPIADO;
        };
    }

    private String limparConteudo(String conteudo) {
        if (conteudo == null) return null;
        String limpo = conteudo.replace("\u0000", "");
        int limite = 500_000; // ~500 KB
        return limpo.length() > limite ? limpo.substring(0, limite) : limpo;
    }
}
