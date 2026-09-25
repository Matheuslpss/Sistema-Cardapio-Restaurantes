package src.menu;

import java.util.Scanner;
import src.categoria.CategoriaService;

public class MenuCategoria {

    public void mostrarMenu() {

        System.out.println("===== CATEGORIAS =====");
        System.out.println("1 - Cadastrar categoria");
        System.out.println("2 - Listar categorias");
        System.out.println("3 - Editar categoria");
        System.out.println("4 - Excluir categoria");
        System.out.println("5 - Voltar");
    }


    public void cadastrarCategoria(
            Scanner entrada,
            CategoriaService categoriaService) {

        System.out.println("Digite o nome da categoria:");

        String nome = entrada.nextLine();

        categoriaService.cadastrarCategoria(nome);

        System.out.println("Categoria cadastrada com sucesso!");
    }


    public void listarCategorias(
            CategoriaService categoriaService) {

        categoriaService.listarCategorias();
    }


    public void editarCategoria(
            Scanner entrada,
            CategoriaService categoriaService) {

        categoriaService.listarCategorias();

        System.out.println("Qual categoria deseja editar?");
        String nomeAtual = entrada.nextLine();

        System.out.println("Digite o novo nome:");
        String novoNome = entrada.nextLine();

        categoriaService.editarCategoria(
            nomeAtual,
            novoNome
        );
    }


    public void excluirCategoria(
            Scanner entrada,
            CategoriaService categoriaService) {

        categoriaService.listarCategorias();

        System.out.println("Qual categoria deseja excluir?");
        String nome = entrada.nextLine();

        categoriaService.excluirCategoria(nome);
    }


    public void executarMenu(
            Scanner entrada,
            CategoriaService categoriaService) {

        int opcao = 0;

        while (opcao != 5) {

            mostrarMenu();

            System.out.println("Qual opção deseja?");
            opcao = entrada.nextInt();
            entrada.nextLine();

            if (opcao == 1) {

                cadastrarCategoria(
                    entrada,
                    categoriaService
                );

            } else if (opcao == 2) {

                listarCategorias(
                    categoriaService
                );

            } else if (opcao == 3) {

                editarCategoria(
                    entrada,
                    categoriaService
                );

            } else if (opcao == 4) {

                excluirCategoria(
                    entrada,
                    categoriaService
                );

            } else if (opcao == 5) {

                System.out.println("Voltando...");

            } else {

                System.out.println("Opção inválida!");
            }
        }
    }
}