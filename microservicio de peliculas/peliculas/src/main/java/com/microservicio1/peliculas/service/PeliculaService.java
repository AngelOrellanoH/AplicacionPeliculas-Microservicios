package com.microservicio1.peliculas.service;


import com.microservicio1.peliculas.dto.ActorDto;
import com.microservicio1.peliculas.dto.PeliculaRequestDto;
import com.microservicio1.peliculas.dto.PeliculaResponseDto;
import com.microservicio1.peliculas.model.Actor;
import com.microservicio1.peliculas.model.Pelicula;
import com.microservicio1.peliculas.repository.ActorRepository;
import com.microservicio1.peliculas.repository.PeliculaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PeliculaService {
    private final PeliculaRepository peliculaRepository;
    private final ActorRepository actorRepository;

    public PeliculaService(PeliculaRepository peliculaRepository, ActorRepository actorRepository) {
        this.peliculaRepository = peliculaRepository;
        this.actorRepository = actorRepository;
    }

    public List<PeliculaResponseDto> listarPeliculas() {
        return peliculaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<PeliculaResponseDto> buscarPorId(Integer id) {
        return peliculaRepository.findById(id)
                .map(this::convertirADTO);
    }

    public List<PeliculaResponseDto> buscarConFiltros(String query, String genero) {
        return peliculaRepository.buscarConFiltros(query, genero).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public PeliculaResponseDto guardarPelicula(PeliculaRequestDto dto) {
        Pelicula pelicula = new Pelicula();
        actualizarEntidadDesdeDTO(dto, pelicula);
        return convertirADTO(peliculaRepository.save(pelicula));
    }

    public Optional<PeliculaResponseDto> actualizarPelicula(Integer id, PeliculaRequestDto dto) {
        return peliculaRepository.findById(id).map(p -> {
            actualizarEntidadDesdeDTO(dto, p);
            return convertirADTO(peliculaRepository.save(p));
        });
    }

    public void eliminarPelicula(Integer id) {
        peliculaRepository.deleteById(id);
    }

    private void actualizarEntidadDesdeDTO(PeliculaRequestDto dto, Pelicula pelicula) {
        pelicula.setTitulo(dto.getTitulo());
        pelicula.setAnio(dto.getAnio());
        pelicula.setDuracion(dto.getDuracion());
        pelicula.setPais(dto.getPais());
        pelicula.setDireccion(dto.getDireccion());
        pelicula.setGenero(dto.getGenero());
        pelicula.setSinopsis(dto.getSinopsis());
        pelicula.setImagenPortada(dto.getImagenPortada());

        if (dto.getActoresIds() != null) {
            Set<Actor> actores = dto.getActoresIds().stream()
                    .map(id -> actorRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Actor con ID " + id + " no encontrado")))
                    .collect(Collectors.toSet());
            pelicula.setActores(actores);
        }
    }

    private PeliculaResponseDto convertirADTO(Pelicula pelicula) {
        PeliculaResponseDto dto = new PeliculaResponseDto();
        dto.setId(pelicula.getId());
        dto.setTitulo(pelicula.getTitulo());
        dto.setAnio(pelicula.getAnio());
        dto.setDuracion(pelicula.getDuracion());
        dto.setPais(pelicula.getPais());
        dto.setDireccion(pelicula.getDireccion());
        dto.setGenero(pelicula.getGenero());
        dto.setSinopsis(pelicula.getSinopsis());
        dto.setImagenPortada(pelicula.getImagenPortada());

        List<ActorDto> actores = pelicula.getActores().stream()
                .map(actor -> new ActorDto(actor.getId(), actor.getNombre()))
                .collect(Collectors.toList());

        dto.setActores(actores);
        return dto;
    }
}
