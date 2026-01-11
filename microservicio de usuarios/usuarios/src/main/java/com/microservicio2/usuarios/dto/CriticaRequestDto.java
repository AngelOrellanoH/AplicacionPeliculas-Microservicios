package com.microservicio2.usuarios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriticaRequestDto {
    private Integer idPelicula;
    private String valoracion;
    private Integer nota;
}
