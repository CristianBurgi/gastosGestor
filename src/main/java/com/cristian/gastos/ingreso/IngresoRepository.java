package com.cristian.gastos.ingreso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio JPA para Ingreso.
 */
public interface IngresoRepository extends JpaRepository<Ingreso, Long> {

    /** Todos los ingresos de un mes dado (ej: "2025-08"), ordenados por fecha. */
    List<Ingreso> findByMesOrderByFechaDesc(String mes);

    /** Suma total de ingresos en un mes (útil para el balance mensual). */
    @Query("SELECT COALESCE(SUM(i.monto), 0) FROM Ingreso i WHERE i.mes = :mes")
    BigDecimal sumMontoByMes(@Param("mes") String mes);
}
