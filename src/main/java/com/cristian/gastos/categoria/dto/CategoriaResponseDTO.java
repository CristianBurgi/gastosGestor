package com.cristian.gastos.categoria.dto;

import com.cristian.gastos.categoria.Categoria;
import com.cristian.gastos.categoria.TipoCategoria;

/**
 * DTO de salida para Categoria. No expone la entidad JPA directamente.
 */
public class CategoriaResponseDTO {

    private Long id;
    private String nombre;
    private TipoCategoria tipo;

    public CategoriaResponseDTO() {}

    /** Mapeo desde entidad. */
    public static CategoriaResponseDTO from(Categoria c) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.id = c.getId();
        dto.nombre = c.getNombre();
        dto.tipo = c.getTipo();
        return dto;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public TipoCategoria getTipo() { return tipo; }
}
