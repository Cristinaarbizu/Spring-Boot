package com.example.demo.services;

import com.example.demo.entities.Proyecto;
import com.example.demo.repositories.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProyectoService {
    @Autowired
    private ProyectoRepository proyectoRepository;

    public List<Proyecto> listarTodos() {
        return proyectoRepository.findAll();
    }

    public Optional<Proyecto> buscarPorId(Long id) {
        return proyectoRepository.findById(id);
    }

    @Transactional
    public void guardar(Proyecto proyecto) {
        if (proyecto.getId() != null) {
            Proyecto proyectoExistente = proyectoRepository.findById(proyecto.getId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
            
            proyectoExistente.setNombre(proyecto.getNombre());
            proyectoExistente.setDescripcion(proyecto.getDescripcion());
            proyectoExistente.setEstado(proyecto.getEstado());
            proyectoExistente.setFechaInicio(proyecto.getFechaInicio());
            
            // No actualizamos las tareas aquí
            
            proyectoRepository.save(proyectoExistente);
        } else {
            proyectoRepository.save(proyecto);
        }
    }

    public void eliminar(Long id) {
        proyectoRepository.deleteById(id);
    }
    
    public Map<String, Long> obtenerResumenProyectos() {
        List<Proyecto> proyectos = proyectoRepository.findAll();
        Map<String, Long> resumen = new HashMap<>();
        resumen.put("Total", (long) proyectos.size());
        resumen.put("Pendientes", proyectos.stream().filter(p -> "Pendiente".equals(p.getEstado())).count());
        resumen.put("EnProgreso", proyectos.stream().filter(p -> "En Progreso".equals(p.getEstado())).count());
        resumen.put("Completados", proyectos.stream().filter(p -> "Completado".equals(p.getEstado())).count());
        return resumen;
    }
}
