package com.microservicio_frontend.controller;


import com.microservicio_frontend.dto.Actor.ActorRequestDto;
import com.microservicio_frontend.dto.Critica.CriticaDto;
import com.microservicio_frontend.dto.Critica.CriticaRequestDto;
import com.microservicio_frontend.dto.Pelicula.PeliculaDto;
import com.microservicio_frontend.dto.Critica.ResumenCriticaDto;
import com.microservicio_frontend.dto.Pelicula.PeliculaRequestDto;
import com.microservicio_frontend.service.CriticaService;
import com.microservicio_frontend.service.PeliculaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class PeliculaController {
    private final PeliculaService peliculaService;
    private final CriticaService criticaService;

    public PeliculaController(PeliculaService peliculaService, CriticaService criticaService) {
        this.peliculaService = peliculaService;
        this.criticaService = criticaService;
    }

    @GetMapping("/pelicula/{id}")
    public String verDetallePelicula(@PathVariable("id") Integer id, Model model) {

        PeliculaDto pelicula = peliculaService.obtenerPeliculaPorId(id);
        List<CriticaDto> criticas = criticaService.obtenerCriticasPorPelicula(id);
        Map<Integer, ResumenCriticaDto> resumenMap = criticaService.obtenerResumenGeneral();
        ResumenCriticaDto resumen = resumenMap.get(id);

        if (pelicula == null) {
            return "redirect:/";
        }

        model.addAttribute("pelicula", pelicula);
        model.addAttribute("criticas", criticas);
        model.addAttribute("resumenCritica", resumen);

        CriticaRequestDto nuevaCritica = new CriticaRequestDto();
        nuevaCritica.setIdPelicula(pelicula.getId());
        model.addAttribute("critica", nuevaCritica);

        return "pelicula/detalle";
    }

    @PostMapping("/pelicula")
    public String crearPelicula(@ModelAttribute PeliculaRequestDto pelicula,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {

        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_ADMIN".equals(role)) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes permisos para crear esta Pelicula.");
            return "redirect:/";
        }

        try {
            peliculaService.crearPelicula(pelicula, request);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Pelicula creada exitosamente!");
        } catch (HttpClientErrorException.Unauthorized e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Debes iniciar sesión para crear una pelicula.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al crear la pelicula.");
        }

        return "redirect:/admin/peliculas";
    }

    @PostMapping("/pelicula/editar/{id}")
    public String guardarEdicionPelicula(@PathVariable Integer id, @ModelAttribute PeliculaRequestDto pelicula,
                                      HttpServletRequest request, RedirectAttributes redirectAttributes) {

        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_ADMIN".equals(role)) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes permisos para Actualizar esta Pelicula.");
            return "redirect:/";
        }

        try {
            peliculaService.actualizarPelicula(id, pelicula, request);
            redirectAttributes.addFlashAttribute("mensajeExito", "Pelicula modificada correctamente.");
        } catch (HttpClientErrorException.Forbidden e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes autorización para modificar esta Pelicula.");
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("mensajeError", "La pelicula ya no existe.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al editar la pelicula.");
        }

        return "redirect:/admin/peliculas";
    }

    @PostMapping("/pelicula/eliminar/{id}")
    public String eliminarPelicula(@PathVariable Integer id,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        if (username == null || !"ROLE_ADMIN".equals(role)) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes permisos para eliminar esta Pelicula.");
            return "redirect:/";
        }

        try {
            peliculaService.eliminarPeliculaPorId(id, request);
            redirectAttributes.addFlashAttribute("mensajeExito", "Pelicula eliminada correctamente.");
        } catch (HttpClientErrorException.Forbidden e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes autorización para eliminar esta Pelicula.");
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("mensajeError", "La Pelicula ya no existe.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al eliminar la pelicula.");
        }


        return "redirect:/admin/peliculas";
    }
}
