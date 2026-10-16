package com.cardapio.backend.pedido;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cardapio.backend.produto.Produto;
import com.cardapio.backend.produto.ProdutoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    public Pedido criar(PedidoRequest dados) {
        if (dados == null || dados.itens() == null || dados.itens().isEmpty()) {
            throw new IllegalArgumentException("O pedido precisa ter pelo menos um produto.");
        }

        Pedido pedido = new Pedido();
        if (dados.mesa() == null || dados.mesa() <= 0) {
            throw new IllegalArgumentException("Mesa invÃ¡lida.");
        }
        if (dados.sessaoCliente() == null || dados.sessaoCliente().isBlank()) {
            throw new IllegalArgumentException("SessÃ£o do cliente invÃ¡lida.");
        }

        pedido.setMesa(dados.mesa());
        pedido.setCliente(dados.cliente() == null || dados.cliente().isBlank() ? "Cliente" : dados.cliente().trim());
        pedido.setSessaoCliente(dados.sessaoCliente().trim());

        double total = 0;
        for (ItemPedidoRequest item : dados.itens()) {
            if (item.produtoId() == null || item.quantidade() <= 0) {
                throw new IllegalArgumentException("Produto ou quantidade inválida.");
            }

            Produto produto = produtoRepository.findById(item.produtoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

            PedidoItem pedidoItem = new PedidoItem(
                    produto.getId(), produto.getNome(), produto.getPreco(), item.quantidade());
            pedido.adicionarItem(pedidoItem);
            total += pedidoItem.getSubtotal();
        }

        pedido.setTotal(total);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAllByOrderByCriadoEmDesc();
    }

    public List<Pedido> listarPorSessao(String sessaoCliente) {
        if (sessaoCliente == null || sessaoCliente.isBlank()) {
            throw new IllegalArgumentException("SessÃ£o do cliente invÃ¡lida.");
        }

        return pedidoRepository.findAllBySessaoClienteOrderByCriadoEmDesc(sessaoCliente);
    }

    public Pedido buscar(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado."));
    }

    public Pedido atualizarStatus(Long id, StatusPedido status) {
        if (status == null) {
            throw new IllegalArgumentException("Status inválido.");
        }

        Pedido pedido = buscar(id);
        pedido.setStatus(status);
        return pedidoRepository.save(pedido);
    }
}
