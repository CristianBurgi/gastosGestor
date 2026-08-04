package com.cristian.gastos.gasto;

import com.cristian.gastos.gasto.dto.GastoRequestDTO;
import com.cristian.gastos.gasto.dto.GastoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gastos")
public class GastoController {

    private final GastoService gastoService;

    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    /** POST /api/v1/gastos → crear gasto. Devuelve 201 Created. */
    @PostMapping
    public ResponseEntity<GastoResponseDTO> crear(
            @Valid @RequestBody GastoRequestDTO dto) {
        GastoResponseDTO creado = gastoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * GET /api/v1/gastos?mes=2026-08 → listar todos o filtrar por mes.
     * El parámetro mes es opcional.
     */
    @GetMapping
    public ResponseEntity<List<GastoResponseDTO>> listar(
            @RequestParam(required = false) String mes) {
        return ResponseEntity.ok(gastoService.listar(mes));
    }

    /** GET /api/v1/gastos/{id} → detalle de un gasto */
    @GetMapping("/{id}")
    public ResponseEntity<GastoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(gastoService.obtener(id));
    }

    /** PUT /api/v1/gastos/{id} → reemplazar un gasto completo */
    @PutMapping("/{id}")
    public ResponseEntity<GastoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody GastoRequestDTO dto) {
        return ResponseEntity.ok(gastoService.actualizar(id, dto));
    }

    /** DELETE /api/v1/gastos/{id} → eliminar un gasto. Devuelve 204 No Content. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        gastoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/v1/gastos/importar → Importación masiva de gastos desde CSV/texto plano.
     * Operación 100% transaccional: guarda todos o cancela todo si hay algún error.
     */
    @PostMapping(value = "/importar", consumes = {"text/plain", "text/csv", "application/json"})
    public ResponseEntity<com.cristian.gastos.gasto.dto.ImportacionResumenDTO> importar(
            @RequestBody String contenidoCsv) {
        com.cristian.gastos.gasto.dto.ImportacionResumenDTO resumen = gastoService.importarCsv(contenidoCsv);
        return ResponseEntity.ok(resumen);
    }
}
