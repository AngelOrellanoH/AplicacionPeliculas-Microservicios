package com.microservicio2.usuarios.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriticaResumenResponseDto {
    private int idPelicula;
    private int cantidadCriticas;
    private float notaMedia;
}
