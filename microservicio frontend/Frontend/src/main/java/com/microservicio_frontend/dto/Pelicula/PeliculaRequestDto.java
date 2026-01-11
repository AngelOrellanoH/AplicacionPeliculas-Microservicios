package com.microservicio_frontend.dto.Pelicula;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PeliculaRequestDto {
    private String titulo;
    private Integer anio;
    private Integer duracion;
    private String pais;
    private String direccion;
    private String genero;
    private String sinopsis;
    private String imagenPortada;
    private Set<Integer> actoresIds;
}
