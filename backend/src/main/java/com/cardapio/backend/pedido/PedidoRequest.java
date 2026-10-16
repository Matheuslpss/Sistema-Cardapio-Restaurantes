package com.cardapio.backend.pedido;

import java.util.List;

public record PedidoRequest(Integer mesa, String cliente, String sessaoCliente, List<ItemPedidoRequest> itens) {
}
