package com.microservicio_frontend.dto.Pelicula;

import com.microservicio_frontend.dto.Actor.ActorResumidoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PeliculaDto {

    private Integer id;
    private String titulo;
    private Integer anio;
    private Integer duracion;
    private String pais;
    private String direccion;
    private String genero;
    private String sinopsis;
    private String imagenPortada;
    private List<ActorResumidoDto> actores;
}
