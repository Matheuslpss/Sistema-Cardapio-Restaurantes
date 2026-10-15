package com.cardapio.backend.categoria;

import com.cardapio.backend.produto.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public Categoria salvar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria atualizar(Long id, Categoria dados) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        categoria.setNome(dados.getNome());
        return categoriaRepository.save(categoria);
    }

    public void excluir(Long id) {
        if (produtoRepository.existsByCategoriaId(id)) {
            throw new IllegalStateException("Não é possível excluir uma categoria que possui produtos.");
        }

        categoriaRepository.deleteById(id);
    }

}
