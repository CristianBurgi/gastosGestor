package com.cristian.gastos.categoria;

import com.cristian.gastos.categoria.dto.CategoriaResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoint de solo lectura para listar categorías.
 * No tiene CRUD completo: las categorías son datos de referencia (seed).
 */
@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /** GET /api/v1/categorias → lista completa de categorías */
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar() {
        List<CategoriaResponseDTO> resultado = categoriaRepository
                .findAll()
                .stream()
                .map(CategoriaResponseDTO::from)
                .toList();
        return ResponseEntity.ok(resultado);
    }
}
