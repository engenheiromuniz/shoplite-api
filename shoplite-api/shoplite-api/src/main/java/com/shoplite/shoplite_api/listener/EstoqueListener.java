package com.shoplite.shoplite_api.listener;

import com.shoplite.shoplite_api.event.PedidoConfirmadoEvent;
import com.shoplite.shoplite_api.model.ItemPedido;
import com.shoplite.shoplite_api.model.Produto;
import com.shoplite.shoplite_api.repository.ProdutoRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EstoqueListener {

    private final ProdutoRepository produtoRepository;

    public EstoqueListener(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @EventListener
    public void aoConfirmarPedido(PedidoConfirmadoEvent evento) {
        for (ItemPedido item : evento.getPedido().getItens()) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() - item.getQuantidade());
            produtoRepository.save(produto);
        }
    }
}