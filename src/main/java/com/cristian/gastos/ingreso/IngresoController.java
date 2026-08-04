package com.cristian.gastos.ingreso;

import com.cristian.gastos.ingreso.dto.IngresoRequestDTO;
import com.cristian.gastos.ingreso.dto.IngresoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ingresos")
public class IngresoController {

    private final IngresoService ingresoService;

    public IngresoController(IngresoService ingresoService) {
        this.ingresoService = ingresoService;
    }

    /** POST /api/v1/ingresos → crear ingreso. Devuelve 201 Created. */
    @PostMapping
    public ResponseEntity<IngresoResponseDTO> crear(
            @Valid @RequestBody IngresoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingresoService.crear(dto));
    }

    /** GET /api/v1/ingresos?mes=2026-08 → listar, filtro opcional por mes. */
    @GetMapping
    public ResponseEntity<List<IngresoResponseDTO>> listar(
            @RequestParam(required = false) String mes) {
        return ResponseEntity.ok(ingresoService.listar(mes));
    }

    /** GET /api/v1/ingresos/{id} → detalle */
    @GetMapping("/{id}")
    public ResponseEntity<IngresoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ingresoService.obtener(id));
    }

    /** PUT /api/v1/ingresos/{id} → editar */
    @PutMapping("/{id}")
    public ResponseEntity<IngresoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody IngresoRequestDTO dto) {
        return ResponseEntity.ok(ingresoService.actualizar(id, dto));
    }

    /** DELETE /api/v1/ingresos/{id} → eliminar. Devuelve 204 No Content. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ingresoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
