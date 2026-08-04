package com.cristian.gastos.categoria;

/**
 * Clasifica si el gasto asociado a una categoría es recurrente y fijo
 * (mismo importe todos los meses, ej: alquiler) o variable (puede variar
 * en monto o frecuencia, ej: comida, farmacia).
 */
public enum TipoCategoria {
    FIJO,
    VARIABLE
}
