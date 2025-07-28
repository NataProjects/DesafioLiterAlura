package br.com.livros.CatalogoLivrosApplication;

import br.com.livros.CatalogoLivrosApplication.service.LivroService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleApp implements CommandLineRunner {
    private final LivroService livroService;

    public ConsoleApp(LivroService livroService) {
        this.livroService = livroService;
    }

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);
        int opcao = -1;

        do {
            try {
                System.out.println("\n===== 📚 Catálogo de Livros =====");
                System.out.println("1️⃣  - Buscar livro por título");
                System.out.println("2️⃣  - Listar livros registrados");
                System.out.println("3️⃣  - Listar livros por autor");
                System.out.println("4️⃣  - Listar autores vivos em determinado ano");
                System.out.println("5️⃣  - Listar livros por idioma");
                System.out.println("0️⃣  - Sair");
                System.out.print("👉 Escolha uma opção: ");
                opcao = Integer.parseInt(sc.nextLine());

                switch (opcao) {
                    case 1 -> {
                        System.out.print("🔍 Digite o título do livro: ");
                        String titulo = sc.nextLine();
                        try {
                            livroService.buscarELocalizarLivro(titulo);
                            //System.out.println("✅ Livro adicionado com sucesso!\n");
                        } catch (Exception e) {
                            System.out.println("❌ Erro: " + e.getMessage());
                        }
                    }
                    case 2 -> livroService.listarTodos().forEach(l ->
                            System.out.println("📖 " + l.getTitulo() + " - " + l.getAutor().getNome()));
                    case 3 -> {
                        System.out.print("👤 Nome do autor: ");
                        String nome = sc.nextLine();
                        livroService.listarPorAutor(nome).forEach(l ->
                                System.out.println("📖 " + l.getTitulo()));
                    }
                    case 4 -> {
                        System.out.print("📅 Ano: ");
                        int ano = Integer.parseInt(sc.nextLine());
                        livroService.listarAutoresVivosNoAno(ano).forEach(l ->
                                System.out.println("📖 " + l.getTitulo() + " - " + l.getAutor().getNome()));
                    }
                    case 5 -> {
                        System.out.print("🌐 Idioma (ex: en, pt, fr): ");
                        String idioma = sc.nextLine();
                        livroService.listarPorIdioma(idioma).forEach(l ->
                                System.out.println("📖 " + l.getTitulo()));
                    }
                    case 0 -> {
                        System.out.println("👋 Saindo...");
                        sc.close();
                        System.exit(0);
                    }
                    default -> System.out.println("⚠️ Opção inválida");
                }

            } catch (NumberFormatException e) {
                System.out.println("⚠️ Entrada inválida! Digite um número.");
            } catch (Exception e) {
                System.out.println("❌ Erro inesperado: " + e.getMessage());
            }

        } while (opcao != 0);
    }
}