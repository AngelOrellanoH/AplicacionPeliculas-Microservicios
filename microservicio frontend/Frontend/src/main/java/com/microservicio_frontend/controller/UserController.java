package com.microservicio_frontend.controller;


import com.microservicio_frontend.dto.Actor.ActorDto;
import com.microservicio_frontend.dto.Actor.ActorRequestDto;
import com.microservicio_frontend.dto.Critica.CriticaVistaDto;
import com.microservicio_frontend.dto.Pelicula.PeliculaDto;
import com.microservicio_frontend.dto.User.UserDto;
import com.microservicio_frontend.dto.User.UserRequestDto;
import com.microservicio_frontend.service.ActorService;
import com.microservicio_frontend.service.CriticaService;
import com.microservicio_frontend.service.PeliculaService;
import com.microservicio_frontend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    private final CriticaService criticaService;
    private final PeliculaService peliculaService;
    private final ActorService actorService;
    private final UserService userService;

    public UserController(CriticaService criticaService, PeliculaService peliculaService, ActorService actorService, UserService userService) {
        this.criticaService = criticaService;
        this.peliculaService = peliculaService;
        this.actorService = actorService;
        this.userService = userService;
    }

    @GetMapping("/perfil")
    public String verPerfil(HttpServletRequest request, Model model) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_USER".equals(role)) {
            return "redirect:/";
        }
        List<PeliculaDto> peliculas = peliculaService.obtenerTodas();
        List<CriticaVistaDto> criticas = criticaService.obtenerCriticasPorUsuario(username, request, peliculas);

        if (criticas == null) {
            return "redirect:/";
        }

        model.addAttribute("criticas", criticas);
        return "usuario/perfil";
    }

    @PostMapping("/usuario/editar/{id}")
    public String guardarEdicionUser(@PathVariable String id, @ModelAttribute UserRequestDto user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_ADMIN".equals(role)) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes permisos para editar este Usuario.");
            return "redirect:/";
        }

        try {
            userService.actualizarUsuario(id,user,request);
            redirectAttributes.addFlashAttribute("mensajeExito", "User modificado correctamente.");
        } catch (HttpClientErrorException.Forbidden e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes autorización para modificar este User.");
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("mensajeError", "El User ya no existe.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al editar el User.");
        }

        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuario/eliminar/{id}")
    public String elminarUser(@PathVariable String id , HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_ADMIN".equals(role)) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes permisos para eliminar este Usuario.");
            return "redirect:/";
        }

        try {
            userService.eliminarUsuario(id,request);
            redirectAttributes.addFlashAttribute("mensajeExito", "User eliminado correctamente.");
        } catch (HttpClientErrorException.Forbidden e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes autorización para eliminar este User.");
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("mensajeError", "El User ya no existe.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al eliminar el User.");
        }

        return "redirect:/admin/usuarios";
    }

}
