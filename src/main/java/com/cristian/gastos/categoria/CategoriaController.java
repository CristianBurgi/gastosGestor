package com.cristian.gastos.categoria;

import com.cristian.gastos.categoria.dto.CategoriaRequestDTO;
import com.cristian.gastos.categoria.dto.CategoriaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /** POST /api/v1/categorias → crear nueva categoría */
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crear(@Valid @RequestBody CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria(dto.getNombre().trim(), dto.getTipo());
        Categoria guardada = categoriaRepository.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaResponseDTO.from(guardada));
    }
}
