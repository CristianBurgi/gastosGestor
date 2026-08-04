package com.cristian.gastos.reporte;

import com.cristian.gastos.reporte.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    /**
     * GET /api/v1/reportes/totales-por-categoria?mes=2026-08
     * Devuelve la suma de gastos agrupados por categoría.
     */
    @GetMapping("/totales-por-categoria")
    public ResponseEntity<List<TotalPorCategoriaDTO>> obtenerTotalesPorCategoria(
            @RequestParam String mes) {
        return ResponseEntity.ok(reporteService.obtenerTotalesPorCategoria(mes));
    }

    /**
     * GET /api/v1/reportes/total-mes?mes=2026-08
     * Devuelve el total general del mes desglosado por tipo (FIJO vs VARIABLE).
     */
    @GetMapping("/total-mes")
    public ResponseEntity<TotalPorTipoDTO> obtenerTotalMes(
            @RequestParam String mes) {
        return ResponseEntity.ok(reporteService.obtenerTotalMes(mes));
    }

    /**
     * GET /api/v1/reportes/comparacion?mesActual=2026-08&mesAnterior=2026-07
     * Compara los gastos entre dos meses indicando diferencias absolutas y porcentuales.
     */
    @GetMapping("/comparacion")
    public ResponseEntity<ComparacionMesesDTO> comparar(
            @RequestParam String mesActual,
            @RequestParam String mesAnterior) {
        return ResponseEntity.ok(reporteService.compararMeses(mesActual, mesAnterior));
    }

    /**
     * GET /api/v1/reportes/balance?mes=2026-08
     * Devuelve el balance del mes: ingresos vs gastos y signo ("POSITIVO"/"NEGATIVO"/"NEUTRO").
     */
    @GetMapping("/balance")
    public ResponseEntity<BalanceMensualDTO> obtenerBalance(
            @RequestParam String mes) {
        return ResponseEntity.ok(reporteService.obtenerBalance(mes));
    }
}
