package com.shoplite.shoplite_api.service.desconto;

import java.math.BigDecimal;

public interface EstrategiaDesconto {
	BigDecimal calcular (BigDecimal valorTotal);
}
