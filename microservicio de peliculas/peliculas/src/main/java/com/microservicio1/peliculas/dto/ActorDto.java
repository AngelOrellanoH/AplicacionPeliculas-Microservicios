package com.microservicio1.peliculas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActorDto {
    private Integer id;
    private String nombre;

    public ActorDto(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
