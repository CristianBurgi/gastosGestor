package com.cristian.gastos.reporte.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Detalle comparativo por categoría entre dos meses.
 */
public class ComparacionCategoriaDTO {

    private Long categoriaId;
    private String categoriaNombre;
    private BigDecimal montoMesAnterior;
    private BigDecimal montoMesActual;
    private BigDecimal diferenciaAbsoluta;
    private BigDecimal diferenciaPorcentual;

    public ComparacionCategoriaDTO() {}

    public ComparacionCategoriaDTO(Long categoriaId, String categoriaNombre, BigDecimal montoMesAnterior, BigDecimal montoMesActual) {
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
        this.montoMesAnterior = montoMesAnterior != null ? montoMesAnterior : BigDecimal.ZERO;
        this.montoMesActual = montoMesActual != null ? montoMesActual : BigDecimal.ZERO;
        this.diferenciaAbsoluta = this.montoMesActual.subtract(this.montoMesAnterior);

        if (this.montoMesAnterior.compareTo(BigDecimal.ZERO) > 0) {
            this.diferenciaPorcentual = this.diferenciaAbsoluta
                    .multiply(BigDecimal.valueOf(100))
                    .divide(this.montoMesAnterior, 2, RoundingMode.HALF_UP);
        } else if (this.montoMesActual.compareTo(BigDecimal.ZERO) > 0) {
            this.diferenciaPorcentual = BigDecimal.valueOf(100.00);
        } else {
            this.diferenciaPorcentual = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public Long getCategoriaId() { return categoriaId; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public BigDecimal getMontoMesAnterior() { return montoMesAnterior; }
    public BigDecimal getMontoMesActual() { return montoMesActual; }
    public BigDecimal getDiferenciaAbsoluta() { return diferenciaAbsoluta; }
    public BigDecimal getDiferenciaPorcentual() { return diferenciaPorcentual; }
}
