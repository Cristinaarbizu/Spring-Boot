package com.example.demo.services;

import com.example.demo.entities.Tarea;
import com.example.demo.entities.Proyecto;
import com.example.demo.repositories.TareaRepository;
import com.example.demo.repositories.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TareaService {
    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    public List<Tarea> listarTodas() {
        return tareaRepository.findAll();
    }

    public Optional<Tarea> buscarPorId(Long id) {
        return tareaRepository.findById(id);
    }

    @Transactional
    public void guardar(Tarea tarea) {
        if (tarea.getId() != null) {
            Tarea tareaExistente = tareaRepository.findById(tarea.getId())
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
            
            tareaExistente.setTitulo(tarea.getTitulo());
            tareaExistente.setDescripcion(tarea.getDescripcion());
            tareaExistente.setEstado(tarea.getEstado());
            
            if (tarea.getProyecto() != null && !tarea.getProyecto().equals(tareaExistente.getProyecto())) {
                Proyecto nuevoProyecto = proyectoRepository.findById(tarea.getProyecto().getId())
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
                tareaExistente.setProyecto(nuevoProyecto);
            }
            
            tareaRepository.save(tareaExistente);
        } else {
            if (tarea.getProyecto() != null) {
                Proyecto proyecto = proyectoRepository.findById(tarea.getProyecto().getId())
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
                tarea.setProyecto(proyecto);
            }
            tareaRepository.save(tarea);
        }
    }

    @Transactional
    public void eliminar(Long id) {
        Tarea tarea = tareaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        if (tarea.getProyecto() != null) {
            tarea.getProyecto().getTareas().remove(tarea);
        }
        tareaRepository.delete(tarea);
    }
}
