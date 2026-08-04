package com.cristian.gastos.reporte.dto;

import java.math.BigDecimal;

/**
 * DTO para el balance general mensual (Ingresos vs Gastos).
 * Incluye un estado explícito ("POSITIVO", "NEGATIVO", "NEUTRO") para consumo directo del frontend.
 */
public class BalanceMensualDTO {

    public enum EstadoBalance {
        POSITIVO,
        NEGATIVO,
        NEUTRO
    }

    private String mes;
    private BigDecimal totalIngresos;
    private BigDecimal totalGastos;
    private BigDecimal balance;
    private EstadoBalance estado;

    public BalanceMensualDTO() {}

    public BalanceMensualDTO(String mes, BigDecimal totalIngresos, BigDecimal totalGastos) {
        this.mes = mes;
        this.totalIngresos = totalIngresos != null ? totalIngresos : BigDecimal.ZERO;
        this.totalGastos = totalGastos != null ? totalGastos : BigDecimal.ZERO;
        this.balance = this.totalIngresos.subtract(this.totalGastos);

        int comp = this.balance.compareTo(BigDecimal.ZERO);
        if (comp > 0) {
            this.estado = EstadoBalance.POSITIVO;
        } else if (comp < 0) {
            this.estado = EstadoBalance.NEGATIVO;
        } else {
            this.estado = EstadoBalance.NEUTRO;
        }
    }

    public String getMes() { return mes; }
    public BigDecimal getTotalIngresos() { return totalIngresos; }
    public BigDecimal getTotalGastos() { return totalGastos; }
    public BigDecimal getBalance() { return balance; }
    public EstadoBalance getEstado() { return estado; }
}
