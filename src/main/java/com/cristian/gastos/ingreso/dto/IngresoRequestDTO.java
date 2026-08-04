package com.cristian.gastos.ingreso.dto;

import jakarta.validation.constraints.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de entrada para crear o editar un Ingreso.
 */
public class IngresoRequestDTO {

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
    private String descripcion;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @Digits(integer = 13, fraction = 2,
            message = "El monto no puede tener más de 2 decimales")
    private BigDecimal monto;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha = LocalDate.now();

    public IngresoRequestDTO() {}

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}
