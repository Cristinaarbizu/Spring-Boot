package com.example.demo.controllers;

import com.example.demo.entities.Tarea;
import com.example.demo.services.TareaService;
import com.example.demo.services.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @Autowired
    private ProyectoService proyectoService;

    @GetMapping
    public String listarTareas(Model model) {
        List<Tarea> tareas = tareaService.listarTodas();
        model.addAttribute("tareas", tareas);
        return "tareas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("tarea", new Tarea());
        model.addAttribute("proyectos", proyectoService.listarTodos());
        return "crearTarea";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam("id") Long id, Model model) {
        Tarea tarea = tareaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        model.addAttribute("tarea", tarea);
        model.addAttribute("proyectos", proyectoService.listarTodos());
        return "crearTarea";
    }

    @PostMapping("/guardar")
    public String guardarTarea(@ModelAttribute Tarea tarea) {
        tareaService.guardar(tarea);
        return "redirect:/tareas";
    }

    @GetMapping("/eliminar")
    public String eliminarTarea(@RequestParam("id") Long id) {
        tareaService.eliminar(id);
        return "redirect:/tareas";
    }
}
