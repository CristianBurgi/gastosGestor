package com.cristian.gastos.ingreso.dto;

import com.cristian.gastos.ingreso.Ingreso;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de salida para Ingreso.
 */
public class IngresoResponseDTO {

    private Long id;
    private String descripcion;
    private BigDecimal monto;
    private LocalDate fecha;
    private String mes;
    private LocalDateTime createdAt;

    public IngresoResponseDTO() {}

    /** Mapeo desde entidad. */
    public static IngresoResponseDTO from(Ingreso i) {
        IngresoResponseDTO dto = new IngresoResponseDTO();
        dto.id = i.getId();
        dto.descripcion = i.getDescripcion();
        dto.monto = i.getMonto();
        dto.fecha = i.getFecha();
        dto.mes = i.getMes();
        dto.createdAt = i.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getMonto() { return monto; }
    public LocalDate getFecha() { return fecha; }
    public String getMes() { return mes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
