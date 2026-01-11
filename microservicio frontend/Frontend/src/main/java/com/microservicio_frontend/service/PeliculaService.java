package com.microservicio_frontend.service;

import com.microservicio_frontend.dto.Actor.ActorRequestDto;
import com.microservicio_frontend.dto.Pelicula.PeliculaDto;
import com.microservicio_frontend.dto.Pelicula.PeliculaRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class PeliculaService {

    private final RestTemplate restTemplate;

    public PeliculaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<PeliculaDto> obtenerTodas() {
        PeliculaDto[] peliculas = restTemplate.getForObject("http://localhost:8090/api/peliculas", PeliculaDto[].class);
        return Arrays.asList(peliculas);
    }

    public PeliculaDto obtenerPeliculaPorId(Integer id) {
        return restTemplate.getForObject("http://localhost:8090/api/peliculas/" + id, PeliculaDto.class);
    }

    public List<PeliculaDto> buscarPeliculas(String query, String genero) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8090/api/peliculas/filtro");

        if (query != null) {
            builder.queryParam("query", query);
        }
        if (genero != null) {
            builder.queryParam("genero", genero);
        }

        String uri = builder.build().encode().toUriString();

        System.out.println(">> URL generada: " + uri);

        ResponseEntity<PeliculaDto[]> response = restTemplate.getForEntity(
                uri, PeliculaDto[].class);

        PeliculaDto[] body = response.getBody();
        return body != null ? Arrays.asList(body) : Collections.emptyList();
    }

    public void crearPelicula(PeliculaRequestDto dto, HttpServletRequest request) {
        String url = "http://localhost:8090/api/peliculas";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            headers.add("Cookie", cookieHeader);
        }

        HttpEntity<PeliculaRequestDto> entity = new HttpEntity<>(dto, headers);
        restTemplate.postForEntity(url, entity, Void.class);
    }

    public void actualizarPelicula(Integer id, PeliculaRequestDto dto, HttpServletRequest request) {
        String url = "http://localhost:8090/api/peliculas/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            headers.add("Cookie", cookieHeader);
        }

        HttpEntity<PeliculaRequestDto> entity = new HttpEntity<>(dto, headers);
        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    }

    public void eliminarPeliculaPorId(Integer id, HttpServletRequest request) {
        String url = "http://localhost:8090/api/peliculas/" + id;

        HttpHeaders headers = new HttpHeaders();
        String cookieHeader = request.getHeader("Cookie");

        if (cookieHeader != null) {
            headers.add("Cookie", cookieHeader);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }
}
