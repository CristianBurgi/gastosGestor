package com.cristian.gastos.reporte.dto;

import com.cristian.gastos.categoria.TipoCategoria;

import java.math.BigDecimal;

/**
 * DTO para la suma de gastos agregada por categoría.
 */
public class TotalPorCategoriaDTO {

    private Long categoriaId;
    private String categoriaNombre;
    private TipoCategoria tipo;
    private BigDecimal total;

    public TotalPorCategoriaDTO() {}

    public TotalPorCategoriaDTO(Long categoriaId, String categoriaNombre, TipoCategoria tipo, BigDecimal total) {
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
        this.tipo = tipo;
        this.total = total != null ? total : BigDecimal.ZERO;
    }

    public Long getCategoriaId() { return categoriaId; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public TipoCategoria getTipo() { return tipo; }
    public BigDecimal getTotal() { return total; }
}
