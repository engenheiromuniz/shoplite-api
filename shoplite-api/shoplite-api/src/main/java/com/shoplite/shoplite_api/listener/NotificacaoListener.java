package com.shoplite.shoplite_api.listener;

import com.shoplite.shoplite_api.event.PedidoConfirmadoEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoListener {

    @EventListener
    public void aoConfirmarPedido(PedidoConfirmadoEvent evento) {
        System.out.println("Notificação: pedido #" + evento.getPedido().getId() + " confirmado com sucesso!");
    }
}