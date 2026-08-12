package com.shoplite.shoplite_api.service;

import com.shoplite.shoplite_api.exception.EstoqueInsuficienteException;
import com.shoplite.shoplite_api.model.ItemPedido;
import com.shoplite.shoplite_api.model.Pedido;
import com.shoplite.shoplite_api.model.Produto;
import com.shoplite.shoplite_api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    private final ProdutoRepository produtoRepository;

    public PedidoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void adicionarItem(Pedido pedido, Long produtoId, Integer quantidade) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        //Chamar o MÉTODO REFATORADO:
        validarEstoque(produto, quantidade);
        
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        
        pedido.getItens().add(item);
    }

    //MÉTODO REFATORADO:
    private void validarEstoque(Produto produto, Integer quantidade) {
        if (quantidade > produto.getEstoque()) {
            throw new EstoqueInsuficienteException(
                "Estoque insuficiente para o produto: " + produto.getNome());
        }
    }
}
