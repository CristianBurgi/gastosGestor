package com.cristian.gastos.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejo centralizado de excepciones para toda la API REST.
 * Convierte excepciones de negocio y de validación en respuestas JSON consistentes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404 — entidad no encontrada por id.
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            RecursoNoEncontradoException ex, HttpServletRequest req) {

        ErrorResponse body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "No encontrado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * 400 — error de validación de Bean Validation (@Valid en el controller).
     * Construye un mapa campo → primer mensaje de error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe ->
                errores.put(fe.getField(), fe.getDefaultMessage())
        );

        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validación fallida",
                "Hay errores en los campos enviados",
                errores
        );
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * 400 — parámetro de tipo incorrecto (ej: id no numérico en la URL).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String msg = "El parámetro '" + ex.getName() + "' debe ser de tipo "
                + (ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "válido");

        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Parámetro inválido",
                msg
        );
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * 400 — JSON malformado o no legible.
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex) {

        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "JSON malformado",
                "El cuerpo de la solicitud no es un JSON válido o contiene tipos incompatibles"
        );
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * 400 — Error en importación masiva de gastos. Ningún registro es guardado (rollback atómico).
     */
    @ExceptionHandler(ImportacionFallidaException.class)
    public ResponseEntity<ErrorResponse> handleImportacionFallida(
            ImportacionFallidaException ex) {

        Map<String, String> detalleErrores = new LinkedHashMap<>();
        for (var err : ex.getResumen().getErrores()) {
            detalleErrores.put("Línea " + err.getLinea(),
                    "[" + err.getContenido() + "] Error: " + err.getError());
        }

        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Importación cancelada (Rollback completo)",
                ex.getMessage(),
                detalleErrores
        );
        return ResponseEntity.badRequest().body(body);
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 500 — cualquier error no anticipado.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Error no manejado: ", ex);
        ErrorResponse body = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno",
                "Ocurrió un error inesperado. Revisá los logs del servidor."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
