package com.shoplite.shoplite_api.repository;

import com.shoplite.shoplite_api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}