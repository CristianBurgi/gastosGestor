package com.cristian.gastos.ingreso;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa un ingreso registrado en un mes (sueldo, freelance, etc.).
 * No tiene categoría por ahora — es más simple que Gasto.
 */
@Entity
@Table(name = "ingreso")
public class Ingreso {

    private static final DateTimeFormatter MES_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @NotNull
    @Column(nullable = false)
    private LocalDate fecha;

    /** Derivado automáticamente de fecha. Formato YYYY-MM. */
    @Column(nullable = false, length = 7)
    private String mes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ── Lifecycle hooks ──────────────────────────────────────────────────────

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        sincronizarMes();
    }

    @PreUpdate
    protected void onUpdate() {
        sincronizarMes();
    }

    private void sincronizarMes() {
        if (fecha != null) {
            mes = fecha.format(MES_FORMATTER);
        }
    }

    // ── Constructores ────────────────────────────────────────────────────────

    protected Ingreso() {
        // requerido por JPA
    }

    public Ingreso(String descripcion, BigDecimal monto, LocalDate fecha) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
    }

    // ── Getters y Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getMes() { return mes; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "Ingreso{id=" + id + ", descripcion='" + descripcion + "', monto=" + monto + ", mes='" + mes + "'}";
    }
}
