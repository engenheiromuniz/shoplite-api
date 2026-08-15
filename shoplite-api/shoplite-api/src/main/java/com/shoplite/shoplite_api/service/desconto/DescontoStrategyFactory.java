package com.shoplite.shoplite_api.service.desconto;

import com.shoplite.shoplite_api.model.Cupom;
import org.springframework.stereotype.Component;

@Component
public class DescontoStrategyFactory {

    public EstrategiaDesconto criar(Cupom cupom) {
        if (cupom == null) {
            return new SemDesconto();
        }
        
        return switch (cupom.getTipo()) {
            case PERCENTUAL -> new DescontoPercentual(cupom.getPercentualDesconto());
            case VALOR_FIXO -> new DescontoValorFixo(cupom.getPercentualDesconto());
        };
    }
}
