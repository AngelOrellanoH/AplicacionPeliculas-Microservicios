package com.microservicio_frontend.service;

import com.microservicio_frontend.dto.Actor.ActorDto;
import com.microservicio_frontend.dto.Actor.ActorRequestDto;
import com.microservicio_frontend.dto.Critica.CriticaDto;
import com.microservicio_frontend.dto.Critica.CriticaRequestDto;
import com.microservicio_frontend.dto.Critica.CriticaVistaDto;
import com.microservicio_frontend.dto.Pelicula.PeliculaDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ActorService {

    private final RestTemplate restTemplate;

    public ActorService(RestTemplate  restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ActorDto> obtenerTodosLosActores() {
        String url = "http://localhost:8090/api/actores";

        try {
            ResponseEntity<List<ActorDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ActorDto>>() {}
            );

            return response.getBody();

        } catch (Exception e) {
            System.out.println("Error al obtener actores: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void eliminarActorPorId(Integer id, HttpServletRequest request) {
        String url = "http://localhost:8090/api/actores/" + id;

        HttpHeaders headers = new HttpHeaders();
        String cookieHeader = request.getHeader("Cookie");

        if (cookieHeader != null) {
            headers.add("Cookie", cookieHeader);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    public void actualizarActor(Integer id, ActorRequestDto dto, HttpServletRequest request) {
        String url = "http://localhost:8090/api/actores/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            headers.add("Cookie", cookieHeader);
        }

        HttpEntity<ActorRequestDto> entity = new HttpEntity<>(dto, headers);
        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    }

    public void crearActor(ActorRequestDto dto, HttpServletRequest request) {
        String url = "http://localhost:8090/api/actores";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            headers.add("Cookie", cookieHeader);
        }

        HttpEntity<ActorRequestDto> entity = new HttpEntity<>(dto, headers);
        restTemplate.postForEntity(url, entity, Void.class);
    }

    public List<ActorDto> buscarPorTexto(String texto, HttpServletRequest request) {
        String url = "http://localhost:8090/api/actores/buscar?texto=" + texto;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Reinyectar cookies del request original
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            headers.set("Cookie", cookieHeader);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<ActorDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<ActorDto>>() {}
            );

            return response.getBody() != null ? response.getBody() : Collections.emptyList();

        } catch (Exception e) {
            System.out.println("Error al obtener actores: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
