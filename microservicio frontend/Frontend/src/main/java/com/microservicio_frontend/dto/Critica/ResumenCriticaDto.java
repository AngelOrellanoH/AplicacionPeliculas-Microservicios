package com.microservicio_frontend.dto.Critica;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumenCriticaDto {

    private Integer idPelicula;
    private Integer cantidadCriticas;
    private Double notaMedia;
}
