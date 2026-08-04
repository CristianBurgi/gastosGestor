package com.cristian.gastos.web;

import com.cristian.gastos.categoria.Categoria;
import com.cristian.gastos.categoria.CategoriaRepository;
import com.cristian.gastos.categoria.dto.CategoriaRequestDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaViewController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaViewController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("nuevaCategoriaForm") CategoriaRequestDTO form,
                           BindingResult bindingResult,
                           @RequestParam(required = false) String mesActual,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorCategoria", "Formulario de categoría inválido.");
            String mesTarget = (mesActual != null && !mesActual.isBlank()) ? mesActual : "";
            return "redirect:/gastos?mes=" + mesTarget;
        }

        Categoria nueva = new Categoria(form.getNombre().trim(), form.getTipo());
        categoriaRepository.save(nueva);

        redirectAttributes.addFlashAttribute("mensajeExito", "Categoría '" + nueva.getNombre() + "' creada correctamente.");
        String mesTarget = (mesActual != null && !mesActual.isBlank()) ? mesActual : "";
        return "redirect:/gastos?mes=" + mesTarget;
    }
}
