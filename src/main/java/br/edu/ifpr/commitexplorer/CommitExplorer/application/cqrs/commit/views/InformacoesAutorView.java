package br.edu.ifpr.commitexplorer.CommitExplorer.application.cqrs.commit.views;

import br.edu.ifpr.commitexplorer.CommitExplorer.domain.model.entity.Autor;
import lombok.Data;

@Data
public class InformacoesAutorView {
    private long id;
    private String nome;
    private String email;

    public InformacoesAutorView(Autor autor) {
        this.id = autor.getIdAutor();
        this.nome = autor.getNome();
        this.email = autor.getEmail();
    }
}
