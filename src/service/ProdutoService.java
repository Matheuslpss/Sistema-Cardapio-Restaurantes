package src.service;

import java.util.ArrayList;

import src.categoria.Categoria;
import src.model.Produto;

public class ProdutoService {

    private ArrayList<Produto> produtos = new ArrayList<>();


    public void cadastrarProduto(
            String nome,
            double preco,
            String descricao,
            Categoria categoria) {

        Produto produto =
            new Produto(nome, preco, descricao, categoria);

        produtos.add(produto);
    }


    public void listarProdutos() {

        for (Produto produto : produtos) {

            System.out.println(
                produto.getNome()
                + " - R$ " + produto.getPreco()
                + " - " + produto.getDescricao()
                + " - Categoria: "
                + produto.getCategoria().getNome()
            );
        }
    }

    public void listarProdutosPorCategoria (Categoria categoria){
        if (categoria == null){
            return;
        }
        for (Produto produto : produtos){
            
            if (produto.getCategoria() !=null 
                    && produto.getCategoria().getNome()
                        .equalsIgnoreCase(categoria.getNome())){

                System.out.println(
                    produto.getNome()
                    + " - R$ " + produto.getPreco()
                    + " - " + produto.getDescricao()
            );
        }
    }
}



    public void editarProduto(
            String nomeAtual,
            String novoNome,
            double novoPreco,
            String novaDescricao,
            Categoria novaCategoria) {

        for (Produto produto : produtos) {

            if (produto.getNome().equalsIgnoreCase(nomeAtual)) {

                produto.setNome(novoNome);
                produto.setPreco(novoPreco);
                produto.setDescricao(novaDescricao);
                produto.setCategoria(novaCategoria);
            }
        }
    }


    public void excluirProduto(String nome) {

        for (int i = 0; i < produtos.size(); i++) {

            Produto produto = produtos.get(i);

            if (produto.getNome().equalsIgnoreCase(nome)) {

                produtos.remove(i);
                break;
            }
        }
    }
}