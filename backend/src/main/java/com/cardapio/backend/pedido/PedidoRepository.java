package com.cardapio.backend.pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByOrderByCriadoEmDesc();

    List<Pedido> findAllBySessaoClienteOrderByCriadoEmDesc(String sessaoCliente);
}
