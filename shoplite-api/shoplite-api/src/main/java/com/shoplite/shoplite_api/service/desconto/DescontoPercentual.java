package com.shoplite.shoplite_api.service.desconto;


import java.math.BigDecimal;

public class DescontoPercentual implements EstrategiaDesconto {

    private final BigDecimal percentual; // Usar 0.10 para 10%, aplicando a ideia para outros casos tb

    public DescontoPercentual(BigDecimal percentual) {
        this.percentual = percentual;
    }

    @Override
    public BigDecimal calcular(BigDecimal valorTotal) {
        return valorTotal.subtract(valorTotal.multiply(percentual));
    }
}