package com.cristian.gastos.gasto.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de entrada para crear o editar un Gasto.
 * Validaciones con Bean Validation:
 * - categoriaId: obligatorio y debe existir (validado en el servicio)
 * - monto: obligatorio, mayor a 0
 * - fecha: obligatoria, no puede ser futura
 */
public class GastoRequestDTO {

    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;

    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
    private String descripcion;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @Digits(integer = 13, fraction = 2,
            message = "El monto no puede tener más de 2 decimales")
    private BigDecimal monto;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fecha;

    public GastoRequestDTO() {}

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}
