package com.cardapio.backend.pedido;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer mesa;
    private String cliente;
    private String sessaoCliente;
    private double total;
    private LocalDateTime criadoEm;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> itens = new ArrayList<>();

    public Pedido() {
        this.criadoEm = LocalDateTime.now();
        this.status = StatusPedido.RECEBIDO;
    }

    public Long getId() { return id; }
    public Integer getMesa() { return mesa; }
    public void setMesa(Integer mesa) { this.mesa = mesa; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getSessaoCliente() { return sessaoCliente; }
    public void setSessaoCliente(String sessaoCliente) { this.sessaoCliente = sessaoCliente; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public List<PedidoItem> getItens() { return itens; }
    public void adicionarItem(PedidoItem item) { this.itens.add(item); }
}
