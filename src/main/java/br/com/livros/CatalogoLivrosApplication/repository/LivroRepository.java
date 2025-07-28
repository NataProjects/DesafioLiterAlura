package br.com.livros.CatalogoLivrosApplication.repository;

import br.com.livros.CatalogoLivrosApplication.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByAutor_NomeIgnoreCase(String nome);
    List<Livro> findByIdiomaIgnoreCase(String idioma);
    List<Livro> findByAutor_AnoFalecimentoAfterOrAutor_AnoFalecimentoIsNull(Integer ano);
    Optional<Livro> findByTituloIgnoreCaseAndAutor_NomeIgnoreCase(String titulo, String autorNome);
}
