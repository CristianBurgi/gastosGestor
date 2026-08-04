package com.cristian.gastos.gasto.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Resumen del resultado de una operación de importación masiva de gastos.
 */
public class ImportacionResumenDTO {

    private int procesados;
    private int creados;
    private int fallidos;
    private List<ImportacionLineaErrorDTO> errores = new ArrayList<>();

    public ImportacionResumenDTO() {}

    public ImportacionResumenDTO(int procesados, int creados, int fallidos, List<ImportacionLineaErrorDTO> errores) {
        this.procesados = procesados;
        this.creados = creados;
        this.fallidos = fallidos;
        this.errores = errores != null ? errores : new ArrayList<>();
    }

    public int getProcesados() { return procesados; }
    public int getCreados() { return creados; }
    public int getFallidos() { return fallidos; }
    public List<ImportacionLineaErrorDTO> getErrores() { return errores; }
}
