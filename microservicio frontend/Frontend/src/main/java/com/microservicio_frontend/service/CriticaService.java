package com.microservicio_frontend.service;


import com.microservicio_frontend.dto.Critica.CriticaDto;
import com.microservicio_frontend.dto.Critica.CriticaRequestDto;
import com.microservicio_frontend.dto.Critica.CriticaVistaDto;
import com.microservicio_frontend.dto.Pelicula.PeliculaDto;
import com.microservicio_frontend.dto.Critica.ResumenCriticaDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CriticaService {

    private final RestTemplate restTemplate;

    public CriticaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<Integer, ResumenCriticaDto> obtenerResumenGeneral() {
        String url = "http://localhost:8090/api/critica/resumen";
        ResumenCriticaDto[] resumenArray = restTemplate.getForObject(url, ResumenCriticaDto[].class);

        Map<Integer, ResumenCriticaDto> resumenMap = new HashMap<>();
        if (resumenArray != null) {
            for (ResumenCriticaDto resumen : resumenArray) {
                resumenMap.put(resumen.getIdPelicula(), resumen);
            }
        }
        return resumenMap;
    }

    public List<CriticaDto> obtenerCriticasPorPelicula(Integer id) {
        ResponseEntity<List<CriticaDto>> response =
                restTemplate.exchange(
                        "http://localhost:8090/api/critica/pelicula/" + id,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<CriticaDto>>() {});
        return response.getBody();
    }

    public List<CriticaVistaDto> obtenerTodasLasCriticas(HttpServletRequest request, List<PeliculaDto> peliculas) {
        String url = "http://localhost:8090/api/critica";

        List<CriticaDto> criticas;

        var entity = new HttpEntity<>(null, new HttpHeaders() {{
            String cookieHeader = request.getHeader("Cookie");
            if (cookieHeader != null) {
                add("Cookie", cookieHeader);
            }
        }});

        try {
            ResponseEntity<List<CriticaDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<CriticaDto>>() {
                    }
            );
            criticas = response.getBody();

            Map<Integer, String> idToTitulo = peliculas.stream()
                    .collect(Collectors.toMap(PeliculaDto::getId, PeliculaDto::getTitulo));

            return criticas.stream()
                    .filter(c -> idToTitulo.containsKey(c.getIdPelicula()))
                    .map(c -> {
                        CriticaVistaDto vista = new CriticaVistaDto();
                        vista.setId(c.getId());
                        vista.setIdPelicula(c.getIdPelicula());
                        vista.setPelicula(idToTitulo.get(c.getIdPelicula()));
                        vista.setValoracion(c.getValoracion());
                        vista.setNota(c.getNota());
                        vista.setFecha(c.getFecha());
                        vista.setUsername(c.getUsername());
                        return vista;
                    })
                    .collect(Collectors.toList());

        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Acceso denegado al obtener críticas: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            return null;
        }
    }

    public List<CriticaVistaDto> obtenerCriticasPorUsuario(String username, HttpServletRequest request , List<PeliculaDto> peliculas) {
        String url = "http://localhost:8090/api/critica/usuario/" + username;
        List<CriticaDto> criticas;

        var entity = new HttpEntity<>(null, new HttpHeaders() {{
            String cookieHeader = request.getHeader("Cookie");
            if (cookieHeader != null) {
                add("Cookie", cookieHeader);
            }
        }});

        try {
            ResponseEntity<List<CriticaDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<CriticaDto>>() {
                    }
            );
            criticas = response.getBody();

            Map<Integer, String> idToTitulo = peliculas.stream()
                    .collect(Collectors.toMap(PeliculaDto::getId, PeliculaDto::getTitulo));

            return criticas.stream()
                    .filter(c -> idToTitulo.containsKey(c.getIdPelicula()))
                    .map(c -> {
                        CriticaVistaDto vista = new CriticaVistaDto();
                        vista.setId(c.getId());
                        vista.setIdPelicula(c.getIdPelicula());
                        vista.setPelicula(idToTitulo.get(c.getIdPelicula()));
                        vista.setValoracion(c.getValoracion());
                        vista.setNota(c.getNota());
                        vista.setFecha(c.getFecha());
                        vista.setUsername(c.getUsername());
                        return vista;
                    })
                    .collect(Collectors.toList());

        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Acceso denegado al obtener críticas: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            return null;
        }
    }

    public void publicarCritica(CriticaRequestDto dto, String token) {
        String url = "http://localhost:8090/api/critica";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (token != null && !token.isEmpty()) {
            headers.add(HttpHeaders.COOKIE, "token=" + token);
        }

        HttpEntity<CriticaRequestDto> request = new HttpEntity<>(dto, headers);
        restTemplate.postForEntity(url, request, Void.class);
    }

    public void eliminarCriticaPorId(Integer id, HttpServletRequest request) {
        String url = "http://localhost:8090/api/critica/" + id;
        HttpHeaders headers = new HttpHeaders();
        String cookieHeader = request.getHeader("Cookie");

        if (cookieHeader != null) {
            headers.add("Cookie", cookieHeader);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    public void actualizarCritica(Integer id,CriticaRequestDto dto, HttpServletRequest request) {
        String url = "http://localhost:8090/api/critica/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            headers.add("Cookie", cookieHeader);
        }

        HttpEntity<CriticaRequestDto> entity = new HttpEntity<>(dto, headers);
        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    }

    public List<CriticaVistaDto> buscarPorTexto(String texto, HttpServletRequest request, List<PeliculaDto> peliculas) {
        String url = "http://localhost:8090/api/critica/buscar?texto=" + texto;

        var entity = new HttpEntity<>(null, new HttpHeaders() {{
            String cookieHeader = request.getHeader("Cookie");
            if (cookieHeader != null) {
                add("Cookie", cookieHeader);
            }
        }});
        try {
            ResponseEntity<List<CriticaDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<CriticaDto>>() {}
            );
            List<CriticaDto> criticas = response.getBody();
            Map<Integer, String> idToTitulo = peliculas.stream()
                    .collect(Collectors.toMap(PeliculaDto::getId, PeliculaDto::getTitulo));

            return criticas.stream()
                    .filter(c -> idToTitulo.containsKey(c.getIdPelicula()))
                    .map(c -> {
                        CriticaVistaDto vista = new CriticaVistaDto();
                        vista.setId(c.getId());
                        vista.setIdPelicula(c.getIdPelicula());
                        vista.setPelicula(idToTitulo.get(c.getIdPelicula()));
                        vista.setValoracion(c.getValoracion());
                        vista.setNota(c.getNota());
                        vista.setFecha(c.getFecha());
                        vista.setUsername(c.getUsername());
                        return vista;
                    })
                    .collect(Collectors.toList());

        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Acceso denegado al buscar críticas: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Error inesperado al buscar críticas: " + e.getMessage());
            return null;
        }
    }

}
