package com.shoplite.shoplite_api.controller;

import com.shoplite.shoplite_api.dto.ItemPedidoRequest;
import com.shoplite.shoplite_api.dto.PedidoResponse;
import com.shoplite.shoplite_api.model.Cliente;
import com.shoplite.shoplite_api.model.Pedido;
import com.shoplite.shoplite_api.model.StatusPedido;
import com.shoplite.shoplite_api.model.Usuario;
import com.shoplite.shoplite_api.repository.ClienteRepository;
import com.shoplite.shoplite_api.repository.PedidoRepository;
import com.shoplite.shoplite_api.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    public PedidoController(PedidoService pedidoService, PedidoRepository pedidoRepository,
                             ClienteRepository clienteRepository) {
        this.pedidoService = pedidoService;
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@AuthenticationPrincipal Usuario usuario) {
        Cliente cliente = clienteRepository.findByEmail(usuario.getEmail())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado para este usuário"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setData(LocalDate.now());
        pedido.setStatus(StatusPedido.ABERTO);
        pedido.setItens(new ArrayList<>());
        pedidoRepository.save(pedido);

        return ResponseEntity.ok(PedidoResponse.builder()
            .id(pedido.getId())
            .status(pedido.getStatus().name())
            .total(null)
            .build());
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<Void> adicionarItem(@PathVariable Long id, @RequestBody ItemPedidoRequest request) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedidoService.adicionarItem(pedido, request.produtoId(), request.quantidade());
        pedidoRepository.save(pedido);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<PedidoResponse> confirmar(@PathVariable Long id) {
        Pedido pedido = pedidoService.confirmar(id);
        var total = pedidoService.calcularTotal(pedido);

        return ResponseEntity.ok(PedidoResponse.builder()
            .id(pedido.getId())
            .status(pedido.getStatus().name())
            .total(total)
            .build());
    }
}