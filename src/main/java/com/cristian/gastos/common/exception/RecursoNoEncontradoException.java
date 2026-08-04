package com.cristian.gastos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando no se encuentra una entidad por id.
 * El @ControllerAdvice la convierte automáticamente en un 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + " con id " + id + " no encontrado/a");
    }

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
