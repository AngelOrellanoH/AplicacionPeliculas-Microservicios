package com.microservicio1.peliculas.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PeliculaResponseDto {
    private Integer id;
    private String titulo;
    private Integer anio;
    private Integer duracion;
    private String pais;
    private String direccion;
    private String genero;
    private String sinopsis;
    private String imagenPortada;
    private List<ActorDto> actores;
}
