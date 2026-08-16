package com.shoplite.shoplite_api.dto;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record ProdutoResponse(Long id, String nome, BigDecimal preco, Integer estoque) {
}