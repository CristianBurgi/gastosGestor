package com.cristian.gastos.gasto.dto;

/**
 * Detalle de error para una línea específica durante la importación CSV.
 */
public class ImportacionLineaErrorDTO {

    private int linea;
    private String contenido;
    private String error;

    public ImportacionLineaErrorDTO() {}

    public ImportacionLineaErrorDTO(int linea, String contenido, String error) {
        this.linea = linea;
        this.contenido = contenido;
        this.error = error;
    }

    public int getLinea() { return linea; }
    public String getContenido() { return contenido; }
    public String getError() { return error; }
}
