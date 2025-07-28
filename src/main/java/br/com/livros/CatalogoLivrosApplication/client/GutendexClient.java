package br.com.livros.CatalogoLivrosApplication.client;

import br.com.livros.CatalogoLivrosApplication.dto.LivroDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GutendexClient {
    private final RestTemplate restTemplate;

    public GutendexClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<LivroDTO> buscarLivros(String titulo) {
        String tituloFormatado = titulo.trim().replace(" ", "+");

        String url = UriComponentsBuilder.fromHttpUrl("https://gutendex.com/books")
                .queryParam("search", tituloFormatado)
                .toUriString(); // sem encode() aqui

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        System.out.println("URL gerada: " + url);
        System.out.println("Resposta da API: " + response);

        if (response == null || ((Number) response.get("count")).intValue() == 0) return List.of();

        List<Map<String, Object>> resultados = (List<Map<String, Object>>) response.get("results");
        List<LivroDTO> livros = new ArrayList<>();

        for (Map<String, Object> livro : resultados) {
            String title = (String) livro.get("title");
            List<String> idiomas = (List<String>) livro.get("languages");
            String idioma = idiomas != null && !idiomas.isEmpty() ? idiomas.get(0) : "desconhecido";

            List<Map<String, Object>> autores = (List<Map<String, Object>>) livro.get("authors");
            if (autores == null || autores.isEmpty()) continue;

            Map<String, Object> autor = autores.get(0);
            String nome = (String) autor.get("name");
            Integer nascimento = (Integer) autor.get("birth_year");
            Integer falecimento = (Integer) autor.get("death_year");

            livros.add(new LivroDTO(title, idioma, nome, nascimento, falecimento));
        }

        return livros;
    }
}
