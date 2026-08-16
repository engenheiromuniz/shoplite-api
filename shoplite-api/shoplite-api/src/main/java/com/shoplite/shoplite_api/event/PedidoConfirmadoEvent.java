package com.shoplite.shoplite_api.event;

import com.shoplite.shoplite_api.model.Pedido;
import org.springframework.context.ApplicationEvent;

public class PedidoConfirmadoEvent extends ApplicationEvent {

    private final Pedido pedido;

    public PedidoConfirmadoEvent(Object source, Pedido pedido) {
        super(source);
        this.pedido = pedido;
    }

    public Pedido getPedido() {
        return pedido;
    }
}