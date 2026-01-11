package com.microservicio2.usuarios.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CriticaResponseDto {
    private Integer id;
    private Integer idPelicula;
    private String valoracion;
    private Integer nota;
    private LocalDate fecha;
    private String username;
}
