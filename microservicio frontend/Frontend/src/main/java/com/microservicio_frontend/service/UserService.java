package com.microservicio_frontend.service;

import com.microservicio_frontend.dto.Actor.ActorRequestDto;
import com.microservicio_frontend.dto.Critica.CriticaDto;
import com.microservicio_frontend.dto.Critica.CriticaVistaDto;
import com.microservicio_frontend.dto.Pelicula.PeliculaDto;
import com.microservicio_frontend.dto.User.UserDto;
import com.microservicio_frontend.dto.User.UserRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<UserDto> obtenerTodosLosUsuarios(HttpServletRequest request) {
        String url = "http://localhost:8090/api/usuario";

        var entity = new HttpEntity<>(null, new HttpHeaders() {{
            String cookieHeader = request.getHeader("Cookie");
            if (cookieHeader != null) {
                add("Cookie", cookieHeader);
            }
        }});

        try {
            ResponseEntity<List<UserDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<UserDto>>() {
                    }
            );

            return response.getBody();

        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Acceso denegado al obtener los usuarios: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            return null;
        }
    }

    public void eliminarUsuario(String username, HttpServletRequest request) {
        String url = "http://localhost:8090/api/usuario/" + username;

        HttpHeaders headers = new HttpHeaders();
        String cookieHeader = request.getHeader("Cookie");

        if (cookieHeader != null) {
            headers.add("Cookie", cookieHeader);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    public List<UserDto> buscarUsuarios(String query, String rol, HttpServletRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8090/api/usuario/buscar");

        if (query != null) {
            builder.queryParam("query", query);
        }
        if (rol != null) {
            builder.queryParam("rol", rol);
        }

        String uri = builder.build().encode().toUriString();

        HttpHeaders headers = new HttpHeaders();
        String cookieHeader = request.getHeader("Cookie");

        if (cookieHeader != null) {
            headers.add("Cookie", cookieHeader);
        }

        System.out.println(">> URL generada: " + uri);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDto[]> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                UserDto[].class
        );

        UserDto[] body = response.getBody();
        return body != null ? Arrays.asList(body) : Collections.emptyList();
    }

    public void actualizarUsuario(String username, UserRequestDto dto, HttpServletRequest request) {
        String url = "http://localhost:8090/api/usuario/" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            headers.add("Cookie", cookieHeader);
        }

        HttpEntity<UserRequestDto> entity = new HttpEntity<>(dto, headers);
        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    }
}
