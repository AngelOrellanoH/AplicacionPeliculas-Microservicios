package com.microservicio_frontend.dto.Pelicula;


import com.microservicio_frontend.dto.Critica.ResumenCriticaDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeliculaVistaDto {

    private PeliculaDto pelicula;
    private ResumenCriticaDto resumen;

    public PeliculaVistaDto(PeliculaDto pelicula, ResumenCriticaDto resumen) {
        this.pelicula = pelicula;
        this.resumen = resumen;
    }
}
