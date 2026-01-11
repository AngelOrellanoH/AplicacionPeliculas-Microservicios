package com.microservicio1.peliculas.service;


import com.microservicio1.peliculas.dto.ActorRequestDto;
import com.microservicio1.peliculas.dto.ActorResponseDto;
import com.microservicio1.peliculas.model.Actor;
import com.microservicio1.peliculas.repository.ActorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActorService {
    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<ActorResponseDto> listarActores() {
        return actorRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<ActorResponseDto> buscarPorId(Integer id) {
        return actorRepository.findById(id)
                .map(this::convertirADTO);
    }

    public List<ActorResponseDto> buscarPorNombre(String nombre) {
        return actorRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ActorResponseDto> buscarPorTexto(String texto) {
        return actorRepository.buscarPorNombreOPais(texto).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ActorResponseDto> buscarPorPelicula(Integer idPelicula) {
        return actorRepository.findByPeliculaId(idPelicula).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ActorResponseDto guardarActor(ActorRequestDto dto) {
        Actor actor = new Actor();
        actualizarDesdeDTO(actor, dto);
        return convertirADTO(actorRepository.save(actor));
    }

    public Optional<ActorResponseDto> actualizarActor(Integer id, ActorRequestDto dto) {
        return actorRepository.findById(id).map(a -> {
            actualizarDesdeDTO(a, dto);
            return convertirADTO(actorRepository.save(a));
        });
    }

    public void eliminarActor(Integer id) {
        actorRepository.deleteById(id);
    }

    private void actualizarDesdeDTO(Actor actor, ActorRequestDto dto) {
        actor.setNombre(dto.getNombre());
        actor.setFechaNacimiento(dto.getFechaNacimiento());
        actor.setPaisNacimiento(dto.getPaisNacimiento());
    }

    private ActorResponseDto convertirADTO(Actor actor) {
        ActorResponseDto dto = new ActorResponseDto();
        dto.setId(actor.getId());
        dto.setNombre(actor.getNombre());
        dto.setFechaNacimiento(actor.getFechaNacimiento());
        dto.setPaisNacimiento(actor.getPaisNacimiento());
        return dto;
    }


}
