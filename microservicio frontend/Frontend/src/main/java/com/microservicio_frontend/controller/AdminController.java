package com.microservicio_frontend.controller;


import com.microservicio_frontend.dto.Actor.ActorDto;
import com.microservicio_frontend.dto.Critica.CriticaVistaDto;
import com.microservicio_frontend.dto.Pelicula.PeliculaDto;
import com.microservicio_frontend.dto.User.UserDto;
import com.microservicio_frontend.service.ActorService;
import com.microservicio_frontend.service.CriticaService;
import com.microservicio_frontend.service.PeliculaService;
import com.microservicio_frontend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {
    private final CriticaService criticaService;
    private final PeliculaService peliculaService;
    private final ActorService actorService;
    private final UserService userService;

    public AdminController(CriticaService criticaService, PeliculaService peliculaService, ActorService actorService, UserService userService) {
        this.criticaService = criticaService;
        this.peliculaService = peliculaService;
        this.actorService = actorService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String verAdmin(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_ADMIN".equals(role)) {
            return "redirect:/";
        }

        return "admin/dashboard";
    }

    @GetMapping("/admin/peliculas")
    public String gestionarPeliculas(HttpServletRequest request , Model model, @RequestParam(required = false) String q, @RequestParam(required = false) String genero) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_ADMIN".equals(role)) {
            return "redirect:/";
        }

        List<PeliculaDto> peliculas;
        if(q == null || genero == null || (q.isEmpty() && genero.isEmpty()) ) {
            peliculas = peliculaService.obtenerTodas();
        }else{
            peliculas = peliculaService.buscarPeliculas(q, genero);
        }
        List<ActorDto> actores = actorService.obtenerTodosLosActores();


        if (peliculas == null) {
            return "redirect:/";
        }

        model.addAttribute("query", q);
        model.addAttribute("genero", genero);
        model.addAttribute("peliculas", peliculas);
        model.addAttribute("actores", actores);

        return "admin/peliculas";
    }

    @GetMapping("/admin/actores")
    public String gestionarActores(HttpServletRequest request, Model model, @RequestParam(required = false) String q) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_ADMIN".equals(role)) {
            return "redirect:/";
        }

        List<ActorDto> actores;
        if(q == null || q.isEmpty()){
            actores = actorService.obtenerTodosLosActores();
        }else{
            actores = actorService.buscarPorTexto(q, request);
        }

        if (actores == null) {
            return "redirect:/";
        }
        model.addAttribute("query", q);
        model.addAttribute("actores", actores);

        return "admin/actores";
    }

    @GetMapping("/admin/usuarios")
    public String gestionarUsuarios(HttpServletRequest request, Model model, @RequestParam(required = false) String q,  @RequestParam(required = false) String rol) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_ADMIN".equals(role)) {
            return "redirect:/";
        }
        List<UserDto> usuarios;

        if(q == null || rol == null || (q.isEmpty() && rol.isEmpty()) ) {
            usuarios = userService.obtenerTodosLosUsuarios(request);
        }else{
            usuarios = userService.buscarUsuarios(q, rol, request);
        }
        if (usuarios == null) {
            return "redirect:/";
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("query", q);
        model.addAttribute("rol", rol);

        return "admin/usuarios";
    }

    @GetMapping("/admin/criticas")
    public String gestionarCriticas(HttpServletRequest request, Model model, @RequestParam(required = false) String q) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_ADMIN".equals(role)) {
            return "redirect:/";
        }

        List<PeliculaDto> peliculas = peliculaService.obtenerTodas();
        List<CriticaVistaDto> criticas;
        if(q == null || q.isEmpty()){
            criticas = criticaService.obtenerTodasLasCriticas(request, peliculas);
        }else{
            criticas = criticaService.buscarPorTexto(q, request, peliculas);
        }


        if (criticas == null) {
            return "redirect:/admin";
        }
        model.addAttribute("query", q);
        model.addAttribute("criticas", criticas);

        return "admin/criticas";
    }
}
