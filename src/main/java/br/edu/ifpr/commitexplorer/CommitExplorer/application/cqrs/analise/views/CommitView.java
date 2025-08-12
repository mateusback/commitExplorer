package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.analise.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.ArquivoAlterado;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;
import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Commit;
import lombok.Data;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class CommitView {
    private long id;
    private String hash;
    private boolean ehMerge;
    private String mensagem;
    private String autor;
    private String dataCommit;
    private Float pontuacao;
    private Integer complexidadeGeral;
    private int linhasAdicionadas;
    private int linhasRemovidas;
    private int totalArquivosAlterados;
    private String tipo;

    public static CommitView from(Commit c) {
        return from(c, ZoneId.systemDefault(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static CommitView from(Commit c, ZoneId zone, DateTimeFormatter fmt) {
        CommitView v = new CommitView();
        if (c == null) return v;

        v.setId(Optional.ofNullable(c.getIdCommit()).orElse(0L));
        v.setHash(shortHash(c.getHash(), 7));
        v.setEhMerge(c.ehMerge());
        v.setMensagem(Optional.ofNullable(c.getMensagem()).orElse(""));

        Autor a = c.getAutor();
        v.setAutor(a != null
                ? Optional.ofNullable(a.getNome()).orElse(Optional.ofNullable(a.getEmail()).orElse("Desconhecido"))
                : "Desconhecido");

        if (c.getCommitDate() != null) {
            v.setDataCommit(c.getCommitDate().atZone(zone).toLocalDateTime().format(fmt));
        } else {
            v.setDataCommit("");
        }

        v.setPontuacao(c.getPontuacao());
        v.setComplexidadeGeral(c.getComplexidadeGeral());
        v.setTipo(c.getTipo().name());

        List<ArquivoAlterado> arquivos = safeList(c.getArquivosAlterados());
        v.setLinhasAdicionadas(arquivos.stream()
                .map(ArquivoAlterado::getQtdLinhasAdicionadas)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum());

        v.setLinhasRemovidas(arquivos.stream()
                .map(ArquivoAlterado::getQtdLinhasRemovidas)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum());

        v.setTotalArquivosAlterados((int) arquivos.stream()
                .map(ArquivoAlterado::getNomeArquivo)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .size());

        return v;
    }

    public static List<CommitView> fromList(List<Commit> commits) {
        if (commits == null) return List.of();
        return commits.stream()
                .sorted(Comparator.comparing(Commit::getCommitDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .map(CommitView::from)
                .toList();
    }

    private static String shortHash(String hash, int len) {
        if (hash == null) return "";
        return hash.length() <= len ? hash : hash.substring(0, len);
    }

    private static <T> List<T> safeList(List<T> list) {
        return list != null ? list : Collections.emptyList();
    }
}
