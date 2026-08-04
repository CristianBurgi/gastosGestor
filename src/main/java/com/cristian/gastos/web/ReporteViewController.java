package com.cristian.gastos.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/reportes")
public class ReporteViewController {

    private static final DateTimeFormatter MES_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @GetMapping
    public String index(@RequestParam(required = false) String mes, Model model) {
        String mesSeleccionado = (mes != null && !mes.isBlank())
                ? mes
                : LocalDate.now().format(MES_FORMATTER);

        model.addAttribute("mesSeleccionado", mesSeleccionado);
        return "reportes/index";
    }
}
