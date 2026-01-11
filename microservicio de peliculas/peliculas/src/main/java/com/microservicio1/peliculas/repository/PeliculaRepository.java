package com.microservicio1.peliculas.repository;

import com.microservicio1.peliculas.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PeliculaRepository extends JpaRepository<Pelicula,Integer> {

    @Query("""
        SELECT DISTINCT p FROM Pelicula p
        LEFT JOIN p.actores a
        WHERE (
            :query = '' OR 
            LOWER(p.titulo) LIKE LOWER(CONCAT('%', :query, '%')) OR 
            LOWER(a.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR 
            LOWER(p.pais) LIKE LOWER(CONCAT('%', :query, '%'))
        )
        AND (
            :genero = '' OR LOWER(p.genero) = LOWER(:genero)
        )
    """)
    List<Pelicula> buscarConFiltros(@Param("query") String query,
                                    @Param("genero") String genero);
}
