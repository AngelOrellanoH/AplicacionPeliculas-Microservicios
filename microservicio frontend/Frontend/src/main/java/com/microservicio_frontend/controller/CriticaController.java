package com.microservicio_frontend.controller;


import com.microservicio_frontend.dto.Critica.CriticaRequestDto;
import com.microservicio_frontend.service.CriticaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CriticaController {

    private final CriticaService criticaService;

    public CriticaController(CriticaService criticaService) {
        this.criticaService = criticaService;
    }

    @PostMapping("/critica")
    public String publicarCritica(@ModelAttribute CriticaRequestDto critica,
                                  @CookieValue(value = "token", required = false) String token,
                                  RedirectAttributes redirectAttributes) {
        try {
            criticaService.publicarCritica(critica, token);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Crítica publicada exitosamente!");
        } catch (HttpClientErrorException.Unauthorized e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Debes iniciar sesión para publicar una crítica.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al publicar la crítica.");
        }

        return "redirect:/pelicula/" + critica.getIdPelicula();
    }

    @PostMapping("/critica/eliminar/{id}")
    public String eliminarCritica(@PathVariable Integer id,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes permisos para eliminar esta crítica.");
            return "redirect:/";
        }

        try {
            criticaService.eliminarCriticaPorId(id, request);
            redirectAttributes.addFlashAttribute("mensajeExito", "Crítica eliminada correctamente.");
        } catch (HttpClientErrorException.Forbidden e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes autorización para eliminar esta crítica.");
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("mensajeError", "La crítica ya no existe.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al eliminar la crítica.");
        }

        if("ROLE_USER".equals(role)){
            return "redirect:/perfil";
        }else{
            return "redirect:/admin/criticas";
        }

    }

    @PostMapping("/critica/editar/{id}")
    public String guardarEdicionCritica(@PathVariable Integer id, @ModelAttribute CriticaRequestDto critica, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            criticaService.actualizarCritica(id, critica, request);
            redirectAttributes.addFlashAttribute("mensajeExito", "Crítica modificada correctamente.");
        } catch (HttpClientErrorException.Forbidden e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes autorización para modificar esta crítica.");
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("mensajeError", "La crítica ya no existe.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al editar la crítica.");
        }

        return "redirect:/perfil";
    }

}
