package com.gimnasio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gimnasio.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    // Historial de pagos de un cliente, del más reciente al más viejo
    //SELECT * FROM pagos WHERE cliente_id = ? ORDER BY fecha_pago DESC
    List<Pago> findByClienteIdOrderByFechaPagoDesc(Long clienteId);

    // Último pago del cliente (el primero de la lista anterior)
    //SELECT * FROM pagos WHERE cliente_id = ? ORDER BY fecha_pago DESC LIMIT 1
    Optional<Pago> findFirstByClienteIdOrderByFechaPagoDesc(Long clienteId);
}