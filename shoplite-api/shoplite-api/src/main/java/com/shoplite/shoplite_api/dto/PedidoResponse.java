package com.shoplite.shoplite_api.dto;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record PedidoResponse(Long id, String status, BigDecimal total) {
}