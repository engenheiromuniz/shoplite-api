package com.shoplite.shoplite_api.service.desconto;

import java.math.BigDecimal;

public class SemDesconto implements EstrategiaDesconto {
    @Override
    public BigDecimal calcular(BigDecimal valorTotal) {
        return valorTotal; // Como o próprio nome da classe explica, aqui o cliente não vai receber descontos
    }
}