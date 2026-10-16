package com.cardapio.backend.pedido;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "http://localhost:4200")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody PedidoRequest dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criar(dados));
    }

    @GetMapping
    public List<Pedido> listar() {
        return pedidoService.listar();
    }

    @GetMapping("/sessao/{sessaoCliente}")
    public List<Pedido> listarPorSessao(@PathVariable String sessaoCliente) {
        return pedidoService.listarPorSessao(sessaoCliente);
    }

    @GetMapping("/{id}")
    public Pedido buscar(@PathVariable Long id) {
        return pedidoService.buscar(id);
    }

    @PutMapping("/{id}/status")
    public Pedido atualizarStatus(@PathVariable Long id, @RequestBody StatusPedidoRequest dados) {
        return pedidoService.atualizarStatus(id, dados.status());
    }
}
