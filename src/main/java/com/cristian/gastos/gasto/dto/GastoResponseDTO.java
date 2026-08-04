package com.cristian.gastos.gasto.dto;

import com.cristian.gastos.gasto.Gasto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de salida para Gasto. Incluye nombre de categoría para que el cliente
 * no tenga que hacer un segundo request a /categorias.
 */
public class GastoResponseDTO {

    private Long id;
    private Long categoriaId;
    private String categoriaNombre;
    private String descripcion;
    private BigDecimal monto;
    private LocalDate fecha;
    private String mes;
    private LocalDateTime createdAt;

    public GastoResponseDTO() {}

    /** Mapeo desde entidad. */
    public static GastoResponseDTO from(Gasto g) {
        GastoResponseDTO dto = new GastoResponseDTO();
        dto.id = g.getId();
        dto.categoriaId = g.getCategoria().getId();
        dto.categoriaNombre = g.getCategoria().getNombre();
        dto.descripcion = g.getDescripcion();
        dto.monto = g.getMonto();
        dto.fecha = g.getFecha();
        dto.mes = g.getMes();
        dto.createdAt = g.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public Long getCategoriaId() { return categoriaId; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getMonto() { return monto; }
    public LocalDate getFecha() { return fecha; }
    public String getMes() { return mes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
