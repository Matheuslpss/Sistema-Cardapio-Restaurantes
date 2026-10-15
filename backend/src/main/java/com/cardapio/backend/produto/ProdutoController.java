

package com.cardapio.backend.produto;

import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import com.cardapio.backend.categoria.Categoria;
import com.cardapio.backend.categoria.CategoriaRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final CategoriaRepository categoriaRepository;
    private static final Path PASTA_UPLOADS = Paths.get("uploads");

    public ProdutoController(ProdutoService produtoService, CategoriaRepository categoriaRepository) {
        this.produtoService = produtoService;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping("/produtos")
    public List<Produto> listar() {
        return produtoService.listar();
    }

    @PostMapping(value = "/produtos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Produto salvarComImagem(
            @RequestParam("nome") String nome,
            @RequestParam("descricao") String descricao,
            @RequestParam("preco") double preco,
            @RequestParam("categoriaId") Long categoriaId,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) throws IOException {

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        produto.setCategoria(categoria);

        if (imagem != null && !imagem.isEmpty()) {
            Files.createDirectories(PASTA_UPLOADS);
            String nomeOriginal = imagem.getOriginalFilename() == null ? "imagem" : imagem.getOriginalFilename();
            String extensao = "";
            int ponto = nomeOriginal.lastIndexOf('.');
            if (ponto >= 0) {
                extensao = nomeOriginal.substring(ponto).toLowerCase();
            }

            String nomeArquivo = UUID.randomUUID() + extensao;
            Files.copy(imagem.getInputStream(), PASTA_UPLOADS.resolve(nomeArquivo));
            produto.setImagem("/imagens/" + nomeArquivo);
        }

        return produtoService.salvar(produto);
    }

    @PutMapping("/produtos/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        return produtoService.atualizar(id, produto);
    }

    @PutMapping(value = "/produtos/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Produto atualizarComImagem(
            @PathVariable Long id,
            @RequestParam("nome") String nome,
            @RequestParam("descricao") String descricao,
            @RequestParam("preco") double preco,
            @RequestParam("categoriaId") Long categoriaId,
            @RequestParam("removerImagem") boolean removerImagem,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) throws IOException {

        Produto dados = new Produto();
        dados.setNome(nome);
        dados.setDescricao(descricao);
        dados.setPreco(preco);

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        dados.setCategoria(categoria);

        if (removerImagem) {
            dados.setImagem("");
        } else if (imagem != null && !imagem.isEmpty()) {
            Files.createDirectories(PASTA_UPLOADS);
            String nomeOriginal = imagem.getOriginalFilename() == null ? "imagem" : imagem.getOriginalFilename();
            String extensao = "";
            int ponto = nomeOriginal.lastIndexOf('.');
            if (ponto >= 0) {
                extensao = nomeOriginal.substring(ponto).toLowerCase();
            }

            String nomeArquivo = UUID.randomUUID() + extensao;
            Files.copy(imagem.getInputStream(), PASTA_UPLOADS.resolve(nomeArquivo));
            dados.setImagem("/imagens/" + nomeArquivo);
        }

        return produtoService.atualizar(id, dados);
    }

    @DeleteMapping("/produtos/{id}")
    public void excluir(@PathVariable Long id) {
        produtoService.excluir(id);
    }
}
