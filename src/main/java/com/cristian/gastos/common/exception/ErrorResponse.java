package com.cristian.gastos.common.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Cuerpo estándar de respuesta de error para toda la API.
 *
 * Ejemplo 404:
 * {
 *   "timestamp": "2026-08-03T12:00:00",
 *   "status": 404,
 *   "error": "No encontrado",
 *   "mensaje": "Gasto con id 99 no encontrado/a",
 *   "erroresCampos": null
 * }
 *
 * Ejemplo 400 por validación:
 * {
 *   "timestamp": "...",
 *   "status": 400,
 *   "error": "Validación fallida",
 *   "mensaje": "Hay errores en los campos enviados",
 *   "erroresCampos": { "monto": "El monto debe ser mayor a 0" }
 * }
 */
public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String mensaje;
    private final Map<String, String> erroresCampos;

    public ErrorResponse(int status, String error, String mensaje) {
        this(status, error, mensaje, null);
    }

    public ErrorResponse(int status, String error, String mensaje,
                         Map<String, String> erroresCampos) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
        this.erroresCampos = erroresCampos;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMensaje() { return mensaje; }
    public Map<String, String> getErroresCampos() { return erroresCampos; }
}
