package src.menu;

import java.util.Scanner;
import src.categoria.CategoriaService;
import src.service.ProdutoService;

public class MenuAdmin {

    public void mostrarMenu() {

        System.out.println("===== MENU ADMIN =====");
        System.out.println("1 - Gerenciar categorias");
        System.out.println("2 - Gerenciar produtos");
        System.out.println("3 - Voltar");
    }


    public void executarMenu(
            Scanner entrada,
            ProdutoService produtoService,
            CategoriaService categoriaService) {

        MenuCategoria menuCategoria =
            new MenuCategoria();

        MenuProduto menuProduto =
            new MenuProduto();


        int opcao = 0;

        while (opcao != 3) {

            mostrarMenu();

            System.out.println("Qual opção deseja?");
            opcao = entrada.nextInt();
            entrada.nextLine();


            if (opcao == 1) {

                menuCategoria.executarMenu(
                    entrada,
                    categoriaService
                );

            } else if (opcao == 2) {

                menuProduto.executarMenu(
                    entrada,
                    produtoService,
                    categoriaService
                );

            } else if (opcao == 3) {

                System.out.println("Voltando...");

            } else {

                System.out.println("Opção inválida!");
            }
        }
    }
}