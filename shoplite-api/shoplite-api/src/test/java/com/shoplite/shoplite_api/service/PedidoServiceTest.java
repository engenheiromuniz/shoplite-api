package com.shoplite.shoplite_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.shoplite.shoplite_api.exception.EstoqueInsuficienteException;
import com.shoplite.shoplite_api.model.Cupom;
import com.shoplite.shoplite_api.model.ItemPedido;
import com.shoplite.shoplite_api.model.Pedido;
import com.shoplite.shoplite_api.model.Produto;
import com.shoplite.shoplite_api.model.TipoDesconto;
import com.shoplite.shoplite_api.repository.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {
	
    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void deveLancarExcecaoQuandoQuantidadeMaiorQueEstoque() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setEstoque(2);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Pedido pedido = new Pedido();

        assertThrows(EstoqueInsuficienteException.class, () ->
            pedidoService.adicionarItem(pedido, 1L, 5)
        );
    }
    
    @Test
    void deveCalcularTotalComDescontoPercentual() {
        Cupom cupom = new Cupom();
        cupom.setTipo(TipoDesconto.PERCENTUAL);
        cupom.setPercentualDesconto(new BigDecimal("0.10"));

        ItemPedido item = new ItemPedido();
        item.setPrecoUnitario(new BigDecimal("100"));
        item.setQuantidade(2);

        Pedido pedido = new Pedido();
        pedido.setCupom(cupom);
        pedido.setItens(List.of(item));

        BigDecimal total = pedidoService.calcularTotal(pedido);

        assertEquals(new BigDecimal("180.00"), total.setScale(2));
    }    
}