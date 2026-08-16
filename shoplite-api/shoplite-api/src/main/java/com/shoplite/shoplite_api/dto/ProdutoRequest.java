package com.shoplite.shoplite_api.dto;

public record ProdutoRequest(String nome, java.math.BigDecimal preco, Integer estoque, Long categoriaId) {
}