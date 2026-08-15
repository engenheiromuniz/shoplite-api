package com.shoplite.shoplite_api.service.desconto;

import java.math.BigDecimal;

public class DescontoValorFixo implements EstrategiaDesconto {

    private final BigDecimal valorFixo;

    public DescontoValorFixo(BigDecimal valorFixo) {
        this.valorFixo = valorFixo;
    }

    @Override
    public BigDecimal calcular(BigDecimal valorTotal) {
        BigDecimal resultado = valorTotal.subtract(valorFixo);
        return resultado.max(BigDecimal.ZERO); //pelo que foi pesquisado e testado esse retorno impede que o valor fique negativo. Em outras palavras, seria um absurdo a loja dar o desconto e ainda ficar devendo ao cliente.
    }
}