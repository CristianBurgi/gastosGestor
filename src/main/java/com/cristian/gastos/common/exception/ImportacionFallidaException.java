package com.cristian.gastos.common.exception;

import com.cristian.gastos.gasto.dto.ImportacionResumenDTO;

/**
 * Excepción lanzada cuando uno o más registros fallan durante la importación masiva.
 * Garantiza el rollback atómico en la transacción y transporta el informe detallado de errores.
 */
public class ImportacionFallidaException extends RuntimeException {

    private final ImportacionResumenDTO resumen;

    public ImportacionFallidaException(ImportacionResumenDTO resumen) {
        super("La importación fue cancelada. Ningún registro fue guardado debido a errores en "
                + resumen.getFallidos() + " de " + resumen.getProcesados() + " líneas.");
        this.resumen = resumen;
    }

    public ImportacionResumenDTO getResumen() {
        return resumen;
    }
}
