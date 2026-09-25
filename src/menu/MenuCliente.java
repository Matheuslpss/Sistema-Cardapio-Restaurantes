package src.menu;

import java.util.Scanner;

import src.categoria.CategoriaService;
import src.service.ProdutoService;

public class MenuCliente {

    public void mostrarMenu() {
        System.out.println("===== CARDÁPIO =====");
        System.out.println("1 - Ver produtos");
        System.out.println("2 - Voltar");
    }

    public void mostrarCardapio(ProdutoService produtoService) {
        System.out.println("===== CARDÁPIO =====");

        produtoService.listarProdutos();
    }

    public void executarMenu(
            Scanner entrada,
            ProdutoService produtoService,
            CategoriaService categoriaService) {

        int opcao = 0;

        while (opcao != 2) {

            mostrarMenu();

            System.out.println("Escolha uma opção:");
            opcao = entrada.nextInt();
            entrada.nextLine();

            if (opcao == 1) {

                mostrarCardapio(produtoService);

            } else if (opcao == 2) {

                System.out.println("Voltando...");

            } else {

                System.out.println("Opção inválida!");
            }
        }
    }
}
