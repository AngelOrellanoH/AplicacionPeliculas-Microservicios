package com.microservicio_frontend.dto.Critica;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriticaVistaDto {
    private int id;
    private int idPelicula;
    private String pelicula;
    private String valoracion;
    private int nota;
    private String fecha;
    private String username;
}
