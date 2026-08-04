package com.cristian.gastos.reporte;

import com.cristian.gastos.categoria.Categoria;
import com.cristian.gastos.categoria.CategoriaRepository;
import com.cristian.gastos.categoria.TipoCategoria;
import com.cristian.gastos.gasto.GastoRepository;
import com.cristian.gastos.ingreso.IngresoRepository;
import com.cristian.gastos.reporte.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ReporteService {

    private final GastoRepository gastoRepository;
    private final IngresoRepository ingresoRepository;
    private final CategoriaRepository categoriaRepository;

    public ReporteService(GastoRepository gastoRepository,
                          IngresoRepository ingresoRepository,
                          CategoriaRepository categoriaRepository) {
        this.gastoRepository = gastoRepository;
        this.ingresoRepository = ingresoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Suma de gastos agrupados por categoría en un mes.
     * SQL directo: GROUP BY c.id, c.nombre, c.tipo
     */
    public List<TotalPorCategoriaDTO> obtenerTotalesPorCategoria(String mes) {
        return gastoRepository.findTotalesPorCategoriaByMes(mes);
    }

    /**
     * Total general del mes desglosado en FIJO y VARIABLE.
     * SQL directo: GROUP BY c.tipo
     */
    public TotalPorTipoDTO obtenerTotalMes(String mes) {
        List<Object[]> resultadosRaw = gastoRepository.findTotalesPorTipoByMesRaw(mes);

        BigDecimal totalFijo = BigDecimal.ZERO;
        BigDecimal totalVariable = BigDecimal.ZERO;

        for (Object[] fila : resultadosRaw) {
            TipoCategoria tipo = (TipoCategoria) fila[0];
            BigDecimal monto = (BigDecimal) fila[1];
            if (monto == null) continue;

            if (tipo == TipoCategoria.FIJO) {
                totalFijo = totalFijo.add(monto);
            } else if (tipo == TipoCategoria.VARIABLE) {
                totalVariable = totalVariable.add(monto);
            }
        }

        return new TotalPorTipoDTO(mes, totalFijo, totalVariable);
    }

    /**
     * Compara los gastos entre dos meses (totales y por categoría).
     * Si no hay datos en alguno de los meses, no rompe: devuelve 0.
     */
    public ComparacionMesesDTO compararMeses(String mesActual, String mesAnterior) {
        BigDecimal totalActual = gastoRepository.sumMontoByMes(mesActual);
        BigDecimal totalAnterior = gastoRepository.sumMontoByMes(mesAnterior);

        List<TotalPorCategoriaDTO> actualesPorCat = gastoRepository.findTotalesPorCategoriaByMes(mesActual);
        List<TotalPorCategoriaDTO> anterioresPorCat = gastoRepository.findTotalesPorCategoriaByMes(mesAnterior);

        Map<Long, BigDecimal> mapActuales = new HashMap<>();
        actualesPorCat.forEach(c -> mapActuales.put(c.getCategoriaId(), c.getTotal()));

        Map<Long, BigDecimal> mapAnteriores = new HashMap<>();
        anterioresPorCat.forEach(c -> mapAnteriores.put(c.getCategoriaId(), c.getTotal()));

        // Obtener la unión de todas las categorías involucradas (o todas las del sistema)
        List<Categoria> todasCategorias = categoriaRepository.findAll();
        List<ComparacionCategoriaDTO> comparacionCategorias = new ArrayList<>();

        for (Categoria cat : todasCategorias) {
            BigDecimal mAnterior = mapAnteriores.getOrDefault(cat.getId(), BigDecimal.ZERO);
            BigDecimal mActual = mapActuales.getOrDefault(cat.getId(), BigDecimal.ZERO);

            // Incluir si la categoría tuvo movimientos en alguno de los dos meses
            if (mAnterior.compareTo(BigDecimal.ZERO) > 0 || mActual.compareTo(BigDecimal.ZERO) > 0) {
                comparacionCategorias.add(new ComparacionCategoriaDTO(
                        cat.getId(),
                        cat.getNombre(),
                        mAnterior,
                        mActual
                ));
            }
        }

        return new ComparacionMesesDTO(
                mesActual,
                mesAnterior,
                totalActual,
                totalAnterior,
                comparacionCategorias
        );
    }

    /**
     * Balance mensual: total de ingresos vs total de gastos.
     * Devuelve el monto diferencial y el estado (POSITIVO, NEGATIVO, NEUTRO).
     */
    public BalanceMensualDTO obtenerBalance(String mes) {
        BigDecimal totalIngresos = ingresoRepository.sumMontoByMes(mes);
        BigDecimal totalGastos = gastoRepository.sumMontoByMes(mes);

        return new BalanceMensualDTO(mes, totalIngresos, totalGastos);
    }

    /**
     * Devuelve la evolución de ingresos y gastos en los últimos N meses
     * finalizando en mesHasta (o el mes actual por defecto).
     */
    public EvolucionMensualDTO obtenerEvolucionUltimosMeses(String mesHasta, int cantidadMeses) {
        YearMonth fin = (mesHasta != null && !mesHasta.isBlank())
                ? YearMonth.parse(mesHasta)
                : YearMonth.now();

        List<String> meses = new ArrayList<>();
        List<BigDecimal> ingresos = new ArrayList<>();
        List<BigDecimal> gastos = new ArrayList<>();

        for (int i = cantidadMeses - 1; i >= 0; i--) {
            YearMonth ym = fin.minusMonths(i);
            String mesStr = ym.toString();
            meses.add(mesStr);

            BigDecimal totalIngreso = ingresoRepository.sumMontoByMes(mesStr);
            ingresos.add(totalIngreso != null ? totalIngreso : BigDecimal.ZERO);

            BigDecimal totalGasto = gastoRepository.sumMontoByMes(mesStr);
            gastos.add(totalGasto != null ? totalGasto : BigDecimal.ZERO);
        }

        return new EvolucionMensualDTO(meses, ingresos, gastos);
    }
}
