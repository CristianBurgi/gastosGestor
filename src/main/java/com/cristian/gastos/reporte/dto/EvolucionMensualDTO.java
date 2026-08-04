package com.cristian.gastos.reporte.dto;

import java.math.BigDecimal;
import java.util.List;

public class EvolucionMensualDTO {

    private List<String> meses;
    private List<BigDecimal> ingresos;
    private List<BigDecimal> gastos;

    public EvolucionMensualDTO() {
    }

    public EvolucionMensualDTO(List<String> meses, List<BigDecimal> ingresos, List<BigDecimal> gastos) {
        this.meses = meses;
        this.ingresos = ingresos;
        this.gastos = gastos;
    }

    public List<String> getMeses() {
        return meses;
    }

    public void setMeses(List<String> meses) {
        this.meses = meses;
    }

    public List<BigDecimal> getIngresos() {
        return ingresos;
    }

    public void setIngresos(List<BigDecimal> ingresos) {
        this.ingresos = ingresos;
    }

    public List<BigDecimal> getGastos() {
        return gastos;
    }

    public void setGastos(List<BigDecimal> gastos) {
        this.gastos = gastos;
    }
}
