package com.cristian.gastos.reporte.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Reporte general comparativo entre dos meses.
 */
public class ComparacionMesesDTO {

    private String mesActual;
    private String mesAnterior;
    private BigDecimal totalMesAnterior;
    private BigDecimal totalMesActual;
    private BigDecimal diferenciaAbsoluta;
    private BigDecimal diferenciaPorcentual;
    private List<ComparacionCategoriaDTO> categorias;

    public ComparacionMesesDTO() {}

    public ComparacionMesesDTO(String mesActual, String mesAnterior, BigDecimal totalMesActual, BigDecimal totalMesAnterior, List<ComparacionCategoriaDTO> categorias) {
        this.mesActual = mesActual;
        this.mesAnterior = mesAnterior;
        this.totalMesActual = totalMesActual != null ? totalMesActual : BigDecimal.ZERO;
        this.totalMesAnterior = totalMesAnterior != null ? totalMesAnterior : BigDecimal.ZERO;
        this.diferenciaAbsoluta = this.totalMesActual.subtract(this.totalMesAnterior);

        if (this.totalMesAnterior.compareTo(BigDecimal.ZERO) > 0) {
            this.diferenciaPorcentual = this.diferenciaAbsoluta
                    .multiply(BigDecimal.valueOf(100))
                    .divide(this.totalMesAnterior, 2, RoundingMode.HALF_UP);
        } else if (this.totalMesActual.compareTo(BigDecimal.ZERO) > 0) {
            this.diferenciaPorcentual = BigDecimal.valueOf(100.00);
        } else {
            this.diferenciaPorcentual = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        this.categorias = categorias;
    }

    public String getMesActual() { return mesActual; }
    public String getMesAnterior() { return mesAnterior; }
    public BigDecimal getTotalMesAnterior() { return totalMesAnterior; }
    public BigDecimal getTotalMesActual() { return totalMesActual; }
    public BigDecimal getDiferenciaAbsoluta() { return diferenciaAbsoluta; }
    public BigDecimal getDiferenciaPorcentual() { return diferenciaPorcentual; }
    public List<ComparacionCategoriaDTO> getCategorias() { return categorias; }
}
