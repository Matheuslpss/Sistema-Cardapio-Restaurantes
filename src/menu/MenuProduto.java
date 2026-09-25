package src.menu;

import java.util.Scanner;
import src.categoria.Categoria;
import src.categoria.CategoriaService;
import src.service.ProdutoService;

public class MenuProduto {

    public void mostrarMenu() {

        System.out.println("===== PRODUTOS =====");
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Listar produtos");
        System.out.println("3 - Editar produto");
        System.out.println("4 - Excluir produto");
        System.out.println("5 - Voltar");
    }


    public void cadastrarProduto(
            Scanner entrada,
            ProdutoService produtoService,
            CategoriaService categoriaService) {

        System.out.println("Qual o nome do produto?");
        String nome = entrada.nextLine();

        System.out.println("Qual o valor do produto?");
        double preco = entrada.nextDouble();
        entrada.nextLine();

        System.out.println("Qual a descrição do produto?");
        String descricao = entrada.nextLine();


        System.out.println("Categorias disponíveis:");

        categoriaService.listarCategorias();

        System.out.println("Qual a categoria do produto?");
        String nomeCategoria = entrada.nextLine();


        Categoria categoria =
            categoriaService.buscarCategoria(nomeCategoria);


        if (categoria != null) {

            produtoService.cadastrarProduto(
                nome,
                preco,
                descricao,
                categoria
            );

            System.out.println(
                "Produto cadastrado com sucesso!"
            );

        } else {

            System.out.println(
                "Categoria não encontrada!"
            );
        }
    }


    public void listarProdutos(
            ProdutoService produtoService) {

        produtoService.listarProdutos();
    }


    public void editarProduto(
            Scanner entrada,
            ProdutoService produtoService,
            CategoriaService categoriaService) {

        produtoService.listarProdutos();

        System.out.println("Qual produto deseja editar?");
        String nomeAtual = entrada.nextLine();

        System.out.println("Digite o novo nome:");
        String novoNome = entrada.nextLine();

        System.out.println("Digite o novo preço:");
        double novoPreco = entrada.nextDouble();
        entrada.nextLine();

        System.out.println("Digite a nova descrição:");
        String novaDescricao = entrada.nextLine();


        System.out.println("Categorias disponíveis:");

        categoriaService.listarCategorias();

        System.out.println("Digite a nova categoria:");
        String nomeCategoria = entrada.nextLine();


        Categoria novaCategoria =
            categoriaService.buscarCategoria(nomeCategoria);


        if (novaCategoria != null) {

            produtoService.editarProduto(
                nomeAtual,
                novoNome,
                novoPreco,
                novaDescricao,
                novaCategoria
            );

            System.out.println(
                "Produto editado com sucesso!"
            );

        } else {

            System.out.println(
                "Categoria não encontrada!"
            );
        }
    }


    public void excluirProduto(
            Scanner entrada,
            ProdutoService produtoService) {

        produtoService.listarProdutos();

        System.out.println("Qual produto deseja excluir?");
        String nome = entrada.nextLine();

        produtoService.excluirProduto(nome);
    }


    public void executarMenu(
            Scanner entrada,
            ProdutoService produtoService,
            CategoriaService categoriaService) {

        int opcao = 0;

        while (opcao != 5) {

            mostrarMenu();

            System.out.println("Qual opção deseja?");
            opcao = entrada.nextInt();
            entrada.nextLine();


            if (opcao == 1) {

                cadastrarProduto(
                    entrada,
                    produtoService,
                    categoriaService
                );

            } else if (opcao == 2) {

                listarProdutos(
                    produtoService
                );

            } else if (opcao == 3) {

                editarProduto(
                    entrada,
                    produtoService,
                    categoriaService
                );

            } else if (opcao == 4) {

                excluirProduto(
                    entrada,
                    produtoService
                );

            } else if (opcao == 5) {

                System.out.println("Voltando...");

            } else {

                System.out.println("Opção inválida!");
            }
        }
    }
}