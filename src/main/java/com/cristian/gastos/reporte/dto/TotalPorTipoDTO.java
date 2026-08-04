package com.cristian.gastos.reporte.dto;

import java.math.BigDecimal;

/**
 * DTO que desglosa el total del mes en gastos FIJOS vs VARIABLES.
 */
public class TotalPorTipoDTO {

    private String mes;
    private BigDecimal totalFijo;
    private BigDecimal totalVariable;
    private BigDecimal totalGeneral;

    public TotalPorTipoDTO() {}

    public TotalPorTipoDTO(String mes, BigDecimal totalFijo, BigDecimal totalVariable) {
        this.mes = mes;
        this.totalFijo = totalFijo != null ? totalFijo : BigDecimal.ZERO;
        this.totalVariable = totalVariable != null ? totalVariable : BigDecimal.ZERO;
        this.totalGeneral = this.totalFijo.add(this.totalVariable);
    }

    public String getMes() { return mes; }
    public BigDecimal getTotalFijo() { return totalFijo; }
    public BigDecimal getTotalVariable() { return totalVariable; }
    public BigDecimal getTotalGeneral() { return totalGeneral; }
}
