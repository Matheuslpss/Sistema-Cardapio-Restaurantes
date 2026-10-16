package com.cardapio.backend.pedido;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedido_itens")
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long produtoId;
    private String nomeProduto;
    private double precoUnitario;
    private int quantidade;
    private double subtotal;

    public PedidoItem() {
    }

    public PedidoItem(Long produtoId, String nomeProduto, double precoUnitario, int quantidade) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
        this.subtotal = precoUnitario * quantidade;
    }

    public Long getId() { return id; }
    public Long getProdutoId() { return produtoId; }
    public String getNomeProduto() { return nomeProduto; }
    public double getPrecoUnitario() { return precoUnitario; }
    public int getQuantidade() { return quantidade; }
    public double getSubtotal() { return subtotal; }
}
