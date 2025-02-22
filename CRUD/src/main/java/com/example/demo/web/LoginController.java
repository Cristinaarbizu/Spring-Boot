package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/")
    public String redirigirDespuésDeLogin() {
        return "redirect:/proyectos"; // Redirige a la lista de proyectos
    }
}
