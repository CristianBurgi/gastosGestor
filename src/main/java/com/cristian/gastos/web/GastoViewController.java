package com.cristian.gastos.web;

import com.cristian.gastos.categoria.CategoriaRepository;
import com.cristian.gastos.gasto.GastoService;
import com.cristian.gastos.gasto.dto.GastoRequestDTO;
import com.cristian.gastos.gasto.dto.GastoResponseDTO;
import com.cristian.gastos.reporte.ReporteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/gastos")
public class GastoViewController {

    private static final DateTimeFormatter MES_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final GastoService gastoService;
    private final ReporteService reporteService;
    private final CategoriaRepository categoriaRepository;

    public GastoViewController(GastoService gastoService,
                               ReporteService reporteService,
                               CategoriaRepository categoriaRepository) {
        this.gastoService = gastoService;
        this.reporteService = reporteService;
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * GET /gastos?mes=2026-08
     * Página principal de gestión de gastos con tabla, resumen y formulario.
     */
    @GetMapping
    public String index(@RequestParam(required = false) String mes,
                        @RequestParam(required = false) Long editId,
                        Model model) {

        String mesSeleccionado = (mes != null && !mes.isBlank())
                ? mes
                : LocalDate.now().format(MES_FORMATTER);

        cargarAtributosComunes(model, mesSeleccionado);

        // Preparar formulario (Crear o Editar)
        if (!model.containsAttribute("gastoForm")) {
            if (editId != null) {
                GastoResponseDTO dto = gastoService.obtener(editId);
                GastoRequestDTO form = new GastoRequestDTO();
                form.setCategoriaId(dto.getCategoriaId());
                form.setDescripcion(dto.getDescripcion());
                form.setMonto(dto.getMonto());
                form.setFecha(dto.getFecha());
                model.addAttribute("gastoForm", form);
                model.addAttribute("editId", editId);
            } else {
                GastoRequestDTO form = new GastoRequestDTO();
                form.setFecha(LocalDate.now());
                model.addAttribute("gastoForm", form);
            }
        } else if (editId != null) {
            model.addAttribute("editId", editId);
        }

        return "gastos/index";
    }

    /**
     * POST /gastos/guardar → Crear nuevo gasto
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("gastoForm") GastoRequestDTO form,
                          BindingResult bindingResult,
                          @RequestParam(required = false) String mesActual,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String mesTarget = (mesActual != null && !mesActual.isBlank()) ? mesActual : LocalDate.now().format(MES_FORMATTER);
            cargarAtributosComunes(model, mesTarget);
            return "gastos/index";
        }

        gastoService.crear(form);
        String mesGuardado = form.getFecha().format(MES_FORMATTER);
        redirectAttributes.addFlashAttribute("mensajeExito", "Gasto registrado correctamente.");
        return "redirect:/gastos?mes=" + mesGuardado;
    }

    /**
     * POST /gastos/actualizar/{id} → Editar gasto existente
     */
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("gastoForm") GastoRequestDTO form,
                             BindingResult bindingResult,
                             @RequestParam(required = false) String mesActual,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String mesTarget = (mesActual != null && !mesActual.isBlank()) ? mesActual : LocalDate.now().format(MES_FORMATTER);
            cargarAtributosComunes(model, mesTarget);
            model.addAttribute("editId", id);
            return "gastos/index";
        }

        gastoService.actualizar(id, form);
        String mesGuardado = form.getFecha().format(MES_FORMATTER);
        redirectAttributes.addFlashAttribute("mensajeExito", "Gasto actualizado correctamente.");
        return "redirect:/gastos?mes=" + mesGuardado;
    }

    /**
     * POST /gastos/eliminar/{id} → Borrar gasto
     */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           @RequestParam(required = false) String mes,
                           RedirectAttributes redirectAttributes) {
        gastoService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Gasto eliminado correctamente.");
        String mesRedirect = (mes != null && !mes.isBlank()) ? mes : LocalDate.now().format(MES_FORMATTER);
        return "redirect:/gastos?mes=" + mesRedirect;
    }

    private void cargarAtributosComunes(Model model, String mes) {
        model.addAttribute("mesSeleccionado", mes);
        model.addAttribute("gastos", gastoService.listar(mes));
        model.addAttribute("totalMes", reporteService.obtenerTotalMes(mes));
        model.addAttribute("balance", reporteService.obtenerBalance(mes));
        model.addAttribute("categorias", categoriaRepository.findAll());
        if (!model.containsAttribute("nuevaCategoriaForm")) {
            model.addAttribute("nuevaCategoriaForm", new com.cristian.gastos.categoria.dto.CategoriaRequestDTO());
        }
    }
}
