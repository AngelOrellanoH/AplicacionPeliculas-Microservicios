package com.microservicio1.peliculas.controller;


import com.microservicio1.peliculas.dto.ActorRequestDto;
import com.microservicio1.peliculas.dto.ActorResponseDto;
import com.microservicio1.peliculas.model.Actor;
import com.microservicio1.peliculas.service.ActorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actores")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public List<ActorResponseDto> listarActores() {
        return actorService.listarActores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorResponseDto> obtenerPorId(@PathVariable Integer id) {
        return actorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<ActorResponseDto> buscarPorTexto(@RequestParam String texto) {
        return actorService.buscarPorTexto(texto);
    }

    @GetMapping("/pelicula/{idPelicula}")
    public List<ActorResponseDto> buscarPorPelicula(@PathVariable Integer idPelicula) {
        return actorService.buscarPorPelicula(idPelicula);
    }

    @PostMapping
    public ResponseEntity<ActorResponseDto> crear(@RequestBody ActorRequestDto dto) {
        return ResponseEntity.ok(actorService.guardarActor(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActorResponseDto> actualizar(
            @PathVariable Integer id,
            @RequestBody ActorRequestDto dto) {
        return actorService.actualizarActor(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        actorService.eliminarActor(id);
        return ResponseEntity.noContent().build();
    }
}
