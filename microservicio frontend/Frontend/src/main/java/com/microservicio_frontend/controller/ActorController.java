package com.microservicio_frontend.controller;


import com.microservicio_frontend.dto.Actor.ActorRequestDto;
import com.microservicio_frontend.dto.Critica.CriticaRequestDto;
import com.microservicio_frontend.service.ActorService;
import com.microservicio_frontend.service.CriticaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @PostMapping("/actor/eliminar/{id}")
    public String eliminarActor(@PathVariable Integer id,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_ADMIN".equals(role)) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes permisos para eliminar este Actor.");
            return "redirect:/";
        }

        try {
            actorService.eliminarActorPorId(id, request);
            redirectAttributes.addFlashAttribute("mensajeExito", "Actor eliminado correctamente.");
        } catch (HttpClientErrorException.Forbidden e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes autorización para eliminar este Actor.");
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("mensajeError", "El Actor ya no existe.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al eliminar el Actor.");
        }


        return "redirect:/admin/actores";
    }

    @PostMapping("/actor/editar/{id}")
    public String guardarEdicionActor(@PathVariable Integer id, @ModelAttribute ActorRequestDto actor, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_ADMIN".equals(role)) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes permisos para editar este Actor.");
            return "redirect:/";
        }

        try {
            actorService.actualizarActor(id, actor, request);
            redirectAttributes.addFlashAttribute("mensajeExito", "Actor modificado correctamente.");
        } catch (HttpClientErrorException.Forbidden e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes autorización para modificar este Actor.");
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("mensajeError", "El Actor ya no existe.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al editar el Actor.");
        }

        return "redirect:/admin/actores";
    }

    @PostMapping("/actor")
    public String crearActor(@ModelAttribute ActorRequestDto actor,
                                    HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_ADMIN".equals(role)) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes permisos para crear este Actor.");
            return "redirect:/";
        }

        try {
            actorService.crearActor(actor, request);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Actor creado exitosamente!");
        } catch (HttpClientErrorException.Unauthorized e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Debes iniciar sesión para crear una actor.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al crear el actor.");
        }

        return "redirect:/admin/actores";
    }
}
