package com.microservicio1.peliculas.controller;


import com.microservicio1.peliculas.dto.PeliculaRequestDto;
import com.microservicio1.peliculas.dto.PeliculaResponseDto;
import com.microservicio1.peliculas.service.PeliculaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {

    private final PeliculaService peliculaService;

    public PeliculaController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    @GetMapping
    public List<PeliculaResponseDto> listarPeliculas() {
        return peliculaService.listarPeliculas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeliculaResponseDto> obtenerPorId(@PathVariable Integer id) {
        return peliculaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/filtro")
    public List<PeliculaResponseDto> filtrar(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String genero ){

        if (genero != null) {
            genero = URLDecoder.decode(genero, StandardCharsets.UTF_8);
        }
        if (query != null) {
            query = URLDecoder.decode(query, StandardCharsets.UTF_8);
        }
        System.out.println(">> Genero recibido: " + genero);
        return peliculaService.buscarConFiltros(query, genero);
    }

    @PostMapping
    public ResponseEntity<PeliculaResponseDto> crear(@RequestBody PeliculaRequestDto dto) {
        return ResponseEntity.ok(peliculaService.guardarPelicula(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeliculaResponseDto> actualizar(
            @PathVariable Integer id,
            @RequestBody PeliculaRequestDto dto) {
        return peliculaService.actualizarPelicula(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        peliculaService.eliminarPelicula(id);
        return ResponseEntity.noContent().build();
    }
}
