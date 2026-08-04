package com.cristian.gastos.gasto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio JPA para Gasto.
 * Los métodos derivados del nombre los genera Spring Data automáticamente.
 */
public interface GastoRepository extends JpaRepository<Gasto, Long> {

    /** Todos los gastos de un mes dado (ej: "2025-08"). */
    List<Gasto> findByMesOrderByFechaDesc(String mes);

    /** Todos los gastos de una categoría. */
    List<Gasto> findByCategoriaId(Long categoriaId);

    /** Gastos de una categoría en un mes concreto. */
    List<Gasto> findByCategoriaIdAndMes(Long categoriaId, String mes);

    /** Suma total de gastos en un mes (útil para el dashboard). */
    @Query("SELECT COALESCE(SUM(g.monto), 0) FROM Gasto g WHERE g.mes = :mes")
    BigDecimal sumMontoByMes(@Param("mes") String mes);

    /** Suma de gastos agrupada por categoría en un mes concreto. */
    @Query("""
        SELECT new com.cristian.gastos.reporte.dto.TotalPorCategoriaDTO(
            c.id, c.nombre, c.tipo, SUM(g.monto)
        )
        FROM Gasto g
        JOIN g.categoria c
        WHERE g.mes = :mes
        GROUP BY c.id, c.nombre, c.tipo
        ORDER BY SUM(g.monto) DESC
    """)
    List<com.cristian.gastos.reporte.dto.TotalPorCategoriaDTO> findTotalesPorCategoriaByMes(@Param("mes") String mes);

    /** Suma de gastos agrupada por tipo (FIJO / VARIABLE) en un mes concreto. */
    @Query("""
        SELECT c.tipo, SUM(g.monto)
        FROM Gasto g
        JOIN g.categoria c
        WHERE g.mes = :mes
        GROUP BY c.tipo
    """)
    List<Object[]> findTotalesPorTipoByMesRaw(@Param("mes") String mes);
}
