

package com.cardapio.backend.produto;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/produtos")
    public List<Produto> listar() {
        return produtoService.listar();
    }

    @PostMapping("/produtos")
    public Produto salvar(@RequestBody Produto produto) {
        return produtoService.salvar(produto);
    }
}
