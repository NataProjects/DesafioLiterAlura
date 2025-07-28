package br.com.livros.CatalogoLivrosApplication.service;

import br.com.livros.CatalogoLivrosApplication.client.GutendexClient;
import br.com.livros.CatalogoLivrosApplication.dto.LivroDTO;
import br.com.livros.CatalogoLivrosApplication.exception.LivroNaoEncontradoException;
import br.com.livros.CatalogoLivrosApplication.model.Autor;
import br.com.livros.CatalogoLivrosApplication.model.Livro;
import br.com.livros.CatalogoLivrosApplication.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
public class LivroService {
    private final LivroRepository livroRepository;
    private final GutendexClient gutendexClient;

    public LivroService(LivroRepository livroRepository, GutendexClient gutendexClient) {
        this.livroRepository = livroRepository;
        this.gutendexClient = gutendexClient;
    }

    public void buscarELocalizarLivro(String tituloDigitado) {
        List<LivroDTO> livros = gutendexClient.buscarLivros(tituloDigitado);

        String tituloNormalizado = normalizar(tituloDigitado);

        Optional<LivroDTO> livroEncontrado = livros.stream()
                .filter(l -> normalizar(l.titulo()).contains(tituloNormalizado))
                .findFirst();

        if (livroEncontrado.isEmpty()) {
            throw new LivroNaoEncontradoException("Livro não encontrado na API Gutendex");
        }

        LivroDTO dto = livroEncontrado.get();

        // Verifica se já existe livro com o mesmo título e autor
        Optional<Livro> livroExistente = livroRepository
                .findByTituloIgnoreCaseAndAutor_NomeIgnoreCase(dto.titulo(), dto.autorNome());

        if (livroExistente.isPresent()) {
            System.out.println("📚 Livro já cadastrado no banco de dados.");
            return;
        }

        Autor autor = new Autor();
        autor.setNome(dto.autorNome());
        autor.setAnoNascimento(dto.nascimento());
        autor.setAnoFalecimento(dto.falecimento());

        Livro livro = new Livro();
        livro.setTitulo(dto.titulo());
        livro.setIdioma(dto.idioma());
        livro.setAutor(autor);

        livroRepository.save(livro);
        System.out.println("✅ Livro salvo com sucesso!");
    }

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public List<Livro> listarPorAutor(String nome) {
        return livroRepository.findByAutor_NomeIgnoreCase(nome);
    }

    public List<Livro> listarPorIdioma(String idioma) {
        return livroRepository.findByIdiomaIgnoreCase(idioma);
    }

    public List<Livro> listarAutoresVivosNoAno(Integer ano) {
        return livroRepository.findByAutor_AnoFalecimentoAfterOrAutor_AnoFalecimentoIsNull(ano);
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase();
    }
}