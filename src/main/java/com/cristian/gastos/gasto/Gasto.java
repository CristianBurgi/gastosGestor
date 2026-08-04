package com.cristian.gastos.gasto;

import com.cristian.gastos.categoria.Categoria;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa un gasto registrado en un mes determinado.
 *
 * El campo {@code mes} (YYYY-MM) se deriva automáticamente de {@code fecha}
 * en {@code @PrePersist} / {@code @PreUpdate} para evitar inconsistencias.
 *
 * Se usa BigDecimal para el monto porque los tipos de punto flotante (double/float)
 * no pueden representar exactamente todas las fracciones decimales, lo que genera
 * errores de redondeo inaceptables en importes monetarios.
 */
@Entity
@Table(name = "gasto")
public class Gasto {

    private static final DateTimeFormatter MES_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Size(max = 255)
    @Column(length = 255)
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

    protected Gasto() {
        // requerido por JPA
    }

    public Gasto(Categoria categoria, String descripcion, BigDecimal monto, LocalDate fecha) {
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
    }

    // ── Getters y Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

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
        return "Gasto{id=" + id + ", categoria=" + (categoria != null ? categoria.getNombre() : "null")
                + ", monto=" + monto + ", fecha=" + fecha + "}";
    }
}
