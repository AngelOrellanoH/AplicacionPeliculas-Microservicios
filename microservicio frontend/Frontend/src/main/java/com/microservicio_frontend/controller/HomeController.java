package com.microservicio_frontend.controller;


import com.microservicio_frontend.dto.Pelicula.PeliculaDto;
import com.microservicio_frontend.dto.Pelicula.PeliculaVistaDto;
import com.microservicio_frontend.dto.Critica.ResumenCriticaDto;
import com.microservicio_frontend.service.CriticaService;
import com.microservicio_frontend.service.PeliculaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final PeliculaService peliculaService;
    private final CriticaService criticaService;

    public HomeController(PeliculaService peliculaService, CriticaService criticaService) {
        this.peliculaService = peliculaService;
        this.criticaService = criticaService;
    }

    @GetMapping("/")
    public String verInicio(@RequestParam(name = "query", required = false) String query,
                            @RequestParam(name = "genero", required = false) String genero,
                            Model model) {

        List<PeliculaDto> peliculas;

        if ((query != null && !query.isEmpty()) || (genero != null && !genero.isEmpty())) {
            peliculas = peliculaService.buscarPeliculas(query, genero);
        } else {
            peliculas = peliculaService.obtenerTodas();
        }


        Map<Integer, ResumenCriticaDto> resumenMap = criticaService.obtenerResumenGeneral();

        List<PeliculaVistaDto> peliculasVista = new ArrayList<>();
        for (PeliculaDto p : peliculas) {
            ResumenCriticaDto resumen = resumenMap.getOrDefault(p.getId(),
                    new ResumenCriticaDto());
            if (resumen.getNotaMedia() == null) resumen.setNotaMedia(0.0);
            if (resumen.getCantidadCriticas() == null) resumen.setCantidadCriticas(0);
            resumen.setIdPelicula(p.getId());

            peliculasVista.add(new PeliculaVistaDto(p, resumen));
        }

        model.addAttribute("query", query);
        model.addAttribute("genero", genero);
        model.addAttribute("peliculas", peliculasVista);
        return "home/index";
    }
}
