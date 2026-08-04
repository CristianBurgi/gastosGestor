package com.cristian.gastos.web;

import com.cristian.gastos.ingreso.IngresoService;
import com.cristian.gastos.ingreso.dto.IngresoRequestDTO;
import com.cristian.gastos.ingreso.dto.IngresoResponseDTO;
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
@RequestMapping("/ingresos")
public class IngresoViewController {

    private static final DateTimeFormatter MES_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final IngresoService ingresoService;
    private final ReporteService reporteService;

    public IngresoViewController(IngresoService ingresoService, ReporteService reporteService) {
        this.ingresoService = ingresoService;
        this.reporteService = reporteService;
    }

    /**
     * GET /ingresos?mes=2026-08
     * Página principal de ingresos con tabla, balance y formulario.
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
        if (!model.containsAttribute("ingresoForm")) {
            if (editId != null) {
                IngresoResponseDTO dto = ingresoService.obtener(editId);
                IngresoRequestDTO form = new IngresoRequestDTO();
                form.setDescripcion(dto.getDescripcion());
                form.setMonto(dto.getMonto());
                form.setFecha(dto.getFecha());
                model.addAttribute("ingresoForm", form);
                model.addAttribute("editId", editId);
            } else {
                IngresoRequestDTO form = new IngresoRequestDTO();
                form.setFecha(LocalDate.now());
                model.addAttribute("ingresoForm", form);
            }
        } else if (editId != null) {
            model.addAttribute("editId", editId);
        }

        return "ingresos/index";
    }

    /**
     * POST /ingresos/guardar → Crear nuevo ingreso
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("ingresoForm") IngresoRequestDTO form,
                          BindingResult bindingResult,
                          @RequestParam(required = false) String mesActual,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String mesTarget = (mesActual != null && !mesActual.isBlank()) ? mesActual : LocalDate.now().format(MES_FORMATTER);
            cargarAtributosComunes(model, mesTarget);
            return "ingresos/index";
        }

        ingresoService.crear(form);
        String mesGuardado = form.getFecha().format(MES_FORMATTER);
        redirectAttributes.addFlashAttribute("mensajeExito", "Ingreso registrado correctamente.");
        return "redirect:/ingresos?mes=" + mesGuardado;
    }

    /**
     * POST /ingresos/actualizar/{id} → Editar ingreso existente
     */
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("ingresoForm") IngresoRequestDTO form,
                             BindingResult bindingResult,
                             @RequestParam(required = false) String mesActual,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String mesTarget = (mesActual != null && !mesActual.isBlank()) ? mesActual : LocalDate.now().format(MES_FORMATTER);
            cargarAtributosComunes(model, mesTarget);
            model.addAttribute("editId", id);
            return "ingresos/index";
        }

        ingresoService.actualizar(id, form);
        String mesGuardado = form.getFecha().format(MES_FORMATTER);
        redirectAttributes.addFlashAttribute("mensajeExito", "Ingreso actualizado correctamente.");
        return "redirect:/ingresos?mes=" + mesGuardado;
    }

    /**
     * POST /ingresos/eliminar/{id} → Borrar ingreso
     */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           @RequestParam(required = false) String mes,
                           RedirectAttributes redirectAttributes) {
        ingresoService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Ingreso eliminado correctamente.");
        String mesRedirect = (mes != null && !mes.isBlank()) ? mes : LocalDate.now().format(MES_FORMATTER);
        return "redirect:/ingresos?mes=" + mesRedirect;
    }

    private void cargarAtributosComunes(Model model, String mes) {
        model.addAttribute("mesSeleccionado", mes);
        model.addAttribute("fechaHoy", LocalDate.now());
        model.addAttribute("ingresos", ingresoService.listar(mes));
        model.addAttribute("balance", reporteService.obtenerBalance(mes));
    }
}
