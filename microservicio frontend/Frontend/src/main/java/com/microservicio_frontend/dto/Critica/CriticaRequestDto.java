package com.microservicio_frontend.dto.Critica;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriticaRequestDto {
    private Integer idPelicula;
    private Integer nota;
    private String valoracion;
}
