package com.shoplite.shoplite_api.service;

import com.shoplite.shoplite_api.exception.EstoqueInsuficienteException;
import com.shoplite.shoplite_api.model.ItemPedido;
import com.shoplite.shoplite_api.model.Pedido;
import com.shoplite.shoplite_api.model.Produto;
import com.shoplite.shoplite_api.repository.ProdutoRepository;

public class PedidoService {
	
	private final ProdutoRepository produtoRepository;
	
	public PedidoService(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}
	
	public void adicionarItem(Pedido pedido, Long produtoId, Integer quantidade) {
////
	}

}
