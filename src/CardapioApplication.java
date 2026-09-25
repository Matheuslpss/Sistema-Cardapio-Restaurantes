package src;

import java.util.Scanner;

import src.categoria.CategoriaService;
import src.menu.MenuAdmin;
import src.menu.MenuCliente;
import src.service.ProdutoService;

public class CardapioApplication {

    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);

        ProdutoService produtoService = new ProdutoService();

        CategoriaService categoriaService = new CategoriaService();

        MenuAdmin menuAdmin = new MenuAdmin();

        MenuCliente menuCliente = new MenuCliente();

        int opcao = 0;

        while (opcao != 3) {

            System.out.println("===== SISTEMA CARDÁPIO =====");
            System.out.println("1 - Cliente");
            System.out.println("2 - Administrador");
            System.out.println("3 - Sair");

            System.out.println("Qual opção deseja?");
            opcao = entrada.nextInt();
            entrada.nextLine();

            if (opcao == 1) {

                menuCliente.executarMenu(
                    entrada,
                    produtoService,
                    categoriaService
                );

            } else if (opcao == 2) {

                menuAdmin.executarMenu(
                    entrada,
                    produtoService,
                    categoriaService
                );

            } else if (opcao == 3) {

                System.out.println("Sistema encerrado.");

            } else {

                System.out.println("Opção inválida!");
            }
        }

        entrada.close();
    }
}