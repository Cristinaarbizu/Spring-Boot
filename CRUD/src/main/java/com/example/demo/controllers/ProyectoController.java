package com.example.demo.controllers;

import com.example.demo.entities.Proyecto;
import com.example.demo.services.ProyectoService;
import com.example.demo.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarProyectos(Model model) {
        List<Proyecto> proyectos = proyectoService.listarTodos();
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("isAdmin", isAdmin());
        return "proyectos";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        return "crearProyecto";
    }

    @GetMapping("/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioEditar(@RequestParam("id") Long id, Model model) {
        Proyecto proyecto = proyectoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        model.addAttribute("proyecto", proyecto);
        return "crearProyecto";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardarProyecto(@ModelAttribute Proyecto proyecto) {
        proyectoService.guardar(proyecto);
        return "redirect:/proyectos";
    }

    @GetMapping("/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminarProyecto(@RequestParam("id") Long id) {
        proyectoService.eliminar(id);
        return "redirect:/proyectos";
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return usuarioService.isAdmin(auth.getName());
    }
    
    @GetMapping("/reporte")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarReporte(Model model) {
        Map<String, Long> resumen = proyectoService.obtenerResumenProyectos();
        model.addAttribute("resumen", resumen);
        return "reporteProyectos";
    }
}
