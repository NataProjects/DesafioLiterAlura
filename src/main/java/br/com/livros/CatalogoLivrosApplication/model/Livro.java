package br.com.livros.CatalogoLivrosApplication.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idioma;

    @ManyToOne(cascade = CascadeType.ALL)
    private Autor autor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return """
            ----- LIVRO -----
            Título: %s
            Autor: %s
            Idioma: %s
            -----------------
            """.formatted(titulo, autor != null ? autor.getNome() : "Desconhecido", idioma);
    }
}