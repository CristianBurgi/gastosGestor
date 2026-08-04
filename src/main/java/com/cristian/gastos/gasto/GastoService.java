package com.cristian.gastos.gasto;

import com.cristian.gastos.categoria.Categoria;
import com.cristian.gastos.categoria.CategoriaRepository;
import com.cristian.gastos.common.exception.RecursoNoEncontradoException;
import com.cristian.gastos.gasto.dto.GastoRequestDTO;
import com.cristian.gastos.gasto.dto.GastoResponseDTO;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GastoService {

    private final GastoRepository gastoRepository;
    private final CategoriaRepository categoriaRepository;

    public GastoService(GastoRepository gastoRepository,
                        CategoriaRepository categoriaRepository) {
        this.gastoRepository = gastoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // ── Crear ────────────────────────────────────────────────────────────────

    public GastoResponseDTO crear(GastoRequestDTO dto) {
        Categoria categoria = resolverCategoria(dto.getCategoriaId());
        Gasto gasto = new Gasto(categoria, dto.getDescripcion(),
                dto.getMonto(), dto.getFecha());
        return GastoResponseDTO.from(gastoRepository.save(gasto));
    }

    // ── Listar ───────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<GastoResponseDTO> listar(String mes) {
        List<Gasto> gastos;
        if (mes != null && !mes.isBlank()) {
            gastos = gastoRepository.findByMesOrderByFechaDesc(mes);
        } else {
            gastos = gastoRepository.findAll(
                    Sort.by(Sort.Direction.DESC, "fecha"));
        }
        return gastos.stream().map(GastoResponseDTO::from).toList();
    }

    // ── Obtener por id ───────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public GastoResponseDTO obtener(Long id) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Gasto", id));
        return GastoResponseDTO.from(gasto);
    }

    // ── Actualizar ───────────────────────────────────────────────────────────

    public GastoResponseDTO actualizar(Long id, GastoRequestDTO dto) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Gasto", id));

        Categoria categoria = resolverCategoria(dto.getCategoriaId());
        gasto.setCategoria(categoria);
        gasto.setDescripcion(dto.getDescripcion());
        gasto.setMonto(dto.getMonto());
        gasto.setFecha(dto.getFecha());  // @PreUpdate recalcula mes automáticamente

        return GastoResponseDTO.from(gastoRepository.save(gasto));
    }

    // ── Eliminar ─────────────────────────────────────────────────────────────

    public void eliminar(Long id) {
        if (!gastoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Gasto", id);
        }
        gastoRepository.deleteById(id);
    }

    // ── Importar CSV ─────────────────────────────────────────────────────────

    public com.cristian.gastos.gasto.dto.ImportacionResumenDTO importarCsv(String contenidoCsv) {
        if (contenidoCsv == null || contenidoCsv.isBlank()) {
            throw new IllegalArgumentException("El contenido CSV no puede estar vacío");
        }

        String[] lineas = contenidoCsv.split("\\r?\\n");
        List<com.cristian.gastos.gasto.dto.ImportacionLineaErrorDTO> errores = new java.util.ArrayList<>();
        List<Gasto> gastosParaGuardar = new java.util.ArrayList<>();

        // Mapa de categorías por nombre (case-insensitive) para acelerar la búsqueda
        java.util.Map<String, Categoria> mapaCategorias = new java.util.HashMap<>();
        for (Categoria cat : categoriaRepository.findAll()) {
            mapaCategorias.put(cat.getNombre().trim().toLowerCase(java.util.Locale.ROOT), cat);
        }

        int numeroLinea = 0;
        int lineasProcesadas = 0;

        for (String lineaRaw : lineas) {
            numeroLinea++;
            String linea = lineaRaw.trim();
            if (linea.isEmpty()) continue;

            // Ignorar cabecera si existe
            if (numeroLinea == 1 && linea.toLowerCase(java.util.Locale.ROOT).startsWith("categoria")) {
                continue;
            }

            lineasProcesadas++;
            String[] tokens = linea.split(",");
            if (tokens.length < 4) {
                errores.add(new com.cristian.gastos.gasto.dto.ImportacionLineaErrorDTO(
                        numeroLinea, linea, "Formato incorrecto. Se esperaban 4 columnas: categoria,descripcion,monto,fecha"));
                continue;
            }

            String catNombre = tokens[0].trim();
            String descripcion = tokens[1].trim();
            String montoStr = tokens[2].trim();
            String fechaStr = tokens[3].trim();

            // 1. Validar categoría
            Categoria categoria = mapaCategorias.get(catNombre.toLowerCase(java.util.Locale.ROOT));
            if (categoria == null) {
                errores.add(new com.cristian.gastos.gasto.dto.ImportacionLineaErrorDTO(
                        numeroLinea, linea, "Categoría '" + catNombre + "' no existe"));
                continue;
            }

            // 2. Validar monto
            java.math.BigDecimal monto;
            try {
                monto = new java.math.BigDecimal(montoStr);
                if (monto.compareTo(java.math.BigDecimal.ZERO) <= 0) {
                    errores.add(new com.cristian.gastos.gasto.dto.ImportacionLineaErrorDTO(
                            numeroLinea, linea, "El monto debe ser mayor a 0 (recibido: " + montoStr + ")"));
                    continue;
                }
            } catch (NumberFormatException e) {
                errores.add(new com.cristian.gastos.gasto.dto.ImportacionLineaErrorDTO(
                        numeroLinea, linea, "Monto inválido: '" + montoStr + "'"));
                continue;
            }

            // 3. Validar fecha / mes
            java.time.LocalDate fecha;
            try {
                if (fechaStr.length() == 7) { // YYYY-MM
                    fecha = java.time.LocalDate.parse(fechaStr + "-01");
                } else {
                    fecha = java.time.LocalDate.parse(fechaStr);
                }

                if (fecha.isAfter(java.time.LocalDate.now())) {
                    errores.add(new com.cristian.gastos.gasto.dto.ImportacionLineaErrorDTO(
                            numeroLinea, linea, "La fecha no puede ser futura (" + fechaStr + ")"));
                    continue;
                }
            } catch (java.time.format.DateTimeParseException e) {
                errores.add(new com.cristian.gastos.gasto.dto.ImportacionLineaErrorDTO(
                        numeroLinea, linea, "Formato de fecha inválido: '" + fechaStr + "'. Usar YYYY-MM-DD o YYYY-MM"));
                continue;
            }

            gastosParaGuardar.add(new Gasto(categoria, descripcion, monto, fecha));
        }

        // Transaccionalidad estricta: si hay 1 o más errores, NINGÚN registro se guarda y se lanza excepción con el informe
        if (!errores.isEmpty()) {
            var resumen = new com.cristian.gastos.gasto.dto.ImportacionResumenDTO(
                    lineasProcesadas, 0, errores.size(), errores);
            throw new com.cristian.gastos.common.exception.ImportacionFallidaException(resumen);
        }

        // Si todo es válido, guardamos todo atómicamente
        gastoRepository.saveAll(gastosParaGuardar);

        return new com.cristian.gastos.gasto.dto.ImportacionResumenDTO(
                lineasProcesadas, gastosParaGuardar.size(), 0, java.util.Collections.emptyList());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Categoria resolverCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Categoría con id " + categoriaId + " no encontrada. " +
                        "Consultá GET /api/v1/categorias para ver las disponibles."));
    }
}
