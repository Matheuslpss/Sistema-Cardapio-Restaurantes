package src.categoria;

import java.util.ArrayList;

public class CategoriaService {

    private ArrayList<Categoria> categorias = new ArrayList<>();


    public void cadastrarCategoria(String nome) {

        Categoria categoria = new Categoria(nome);

        categorias.add(categoria);
    }


    public void listarCategorias() {

        for (Categoria categoria : categorias) {

            System.out.println(categoria.getNome());
        }
    }


    public Categoria buscarCategoria(String nome) {

        for (Categoria categoria : categorias) {

            if (categoria.getNome().equalsIgnoreCase(nome)) {

                return categoria;
            }
        }

        return null;
    }

    public ArrayList<Categoria> getCategorias() {

        return new ArrayList<>(categorias);
    }


    public void editarCategoria(String nomeAtual, String novoNome) {

        for (Categoria categoria : categorias) {

            if (categoria.getNome().equalsIgnoreCase(nomeAtual)) {

                categoria.setNome(novoNome);
            }
        }
    }


    public void excluirCategoria(String nome) {

        for (int i = 0; i < categorias.size(); i++) {

            Categoria categoria = categorias.get(i);

            if (categoria.getNome().equalsIgnoreCase(nome)) {

                categorias.remove(i);
                break;
            }
        }
    }
}