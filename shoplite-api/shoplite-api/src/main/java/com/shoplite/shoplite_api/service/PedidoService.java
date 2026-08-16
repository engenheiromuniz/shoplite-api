package com.shoplite.shoplite_api.service;

import com.shoplite.shoplite_api.event.PedidoConfirmadoEvent;
import com.shoplite.shoplite_api.exception.EstoqueInsuficienteException;
import com.shoplite.shoplite_api.model.*;
import com.shoplite.shoplite_api.repository.PedidoRepository;
import com.shoplite.shoplite_api.repository.ProdutoRepository;
import com.shoplite.shoplite_api.service.desconto.DescontoStrategyFactory;
import com.shoplite.shoplite_api.service.desconto.EstrategiaDesconto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class PedidoService {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final DescontoStrategyFactory descontoStrategyFactory;
    private final ApplicationEventPublisher eventPublisher;

    public PedidoService(ProdutoRepository produtoRepository, PedidoRepository pedidoRepository,
                          DescontoStrategyFactory descontoStrategyFactory, ApplicationEventPublisher eventPublisher) {
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
        this.descontoStrategyFactory = descontoStrategyFactory;
        this.eventPublisher = eventPublisher;
    }

    public void adicionarItem(Pedido pedido, Long produtoId, Integer quantidade) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        validarEstoque(produto, quantidade);

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        pedido.getItens().add(item);
    }

    private void validarEstoque(Produto produto, Integer quantidade) {
        if (quantidade > produto.getEstoque()) {
            throw new EstoqueInsuficienteException(
                "Estoque insuficiente para o produto: " + produto.getNome());
        }
    }

    public BigDecimal calcularTotal(Pedido pedido) {
        BigDecimal subtotal = pedido.getItens().stream()
            .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        EstrategiaDesconto estrategia = descontoStrategyFactory.criar(pedido.getCupom());
        return estrategia.calcular(subtotal);
    }

    public Pedido confirmar(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getItens().isEmpty()) {
            throw new IllegalStateException("Pedido não pode ser confirmado sem itens");
        }

        pedido.setStatus(StatusPedido.CONFIRMADO);
        pedidoRepository.save(pedido);

        eventPublisher.publishEvent(new PedidoConfirmadoEvent(this, pedido));

        return pedido;
    }
}