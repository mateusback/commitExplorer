package br.edu.ifpr.commitexplorer.CommitExplorer.infrastructure.externalService.git;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Branch;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.enums.TipoAcao;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.service.GitCommitExtractor;
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

                for (RevCommit commit : revWalk) {
                    LocalDateTime dataCommit = commit.getAuthorIdent()
                            .getWhenAsInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();

                    LocalDate dataCommitLocal = dataCommit.toLocalDate();
                    if (dataCommitLocal.isBefore(dataInicio) || dataCommitLocal.isAfter(dataFim)) {
                        continue;
                    }

                    boolean isMergeCommit = commit.getParentCount() > 1;
                    List<ArquivoAlterado> arquivos = new ArrayList<>();

                    if (!isMergeCommit && commit.getParentCount() > 0) {
                        RevCommit parent = commit.getParent(0);
                        arquivos = extrairDiffs(repository, parent, commit);
                    }
                    processarArquivosAlterados(arquivos);
                    Commit novoCommit = new Commit();
                    novoCommit.registrarCommit(
                            commit.getFullMessage(),
                            commit.getName(),
                            dataCommit,
                            arquivos
                    );

                    novoCommit.marcarComoMerge(isMergeCommit);

                    Autor autor = new Autor(commit.getAuthorIdent().getName(), commit.getAuthorIdent().getEmailAddress());
                    novoCommit.atribuirAutor(autor);

                    Branch b = new Branch(branch);
                    novoCommit.atribuirBranch(b);

                    commits.add(novoCommit);
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
                        byte[] bytes = oldLoader.getBytes();
                        conteudoAntes = limparConteudo(new String(bytes, StandardCharsets.UTF_8));
                    }

                    if (diff.getChangeType() != DiffEntry.ChangeType.DELETE) {
                        ObjectId newId = diff.getNewId().toObjectId();
                        ObjectLoader newLoader = repository.open(newId);
                        byte[] bytes = newLoader.getBytes();
                        conteudoDepois = limparConteudo(new String(bytes, StandardCharsets.UTF_8));
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

    private void processarArquivosAlterados(List<ArquivoAlterado> arquivos) {
        if(arquivos == null || arquivos.isEmpty()) return;
        for (var arquivo : arquivos) {
            if (arquivo.getConteudoAntes() == null) {
                arquivo.setConteudoAntes("");
            }
            if (arquivo.getConteudoDepois() == null) {
                arquivo.setConteudoDepois("");
            }
            if (arquivo.getFlgTipoAcao() == TipoAcao.REMOVIDO) {
                arquivo.setConteudoDepois("");
            } else if (arquivo.getFlgTipoAcao() == TipoAcao.ADICIONADO) {
                arquivo.setConteudoAntes("");
            }
        }
    }

    private String limparConteudo(String conteudo) {
        if (conteudo == null) return null;

        String limpo = conteudo.replace("\u0000", "");

        int limite = 500_000; // ~500 KB
        return limpo.length() > limite ? limpo.substring(0, limite) : limpo;
    }

}
