package com.microservicio1.peliculas.repository;

import com.microservicio1.peliculas.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor,Integer> {

    List<Actor> findByNombreContainingIgnoreCase(String name);

    @Query("SELECT a FROM Actor a JOIN a.peliculas p WHERE p.id = :idPelicula")
    List<Actor> findByPeliculaId(@Param("idPelicula") Integer idPelicula);

    @Query("SELECT a FROM Actor a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(a.paisNacimiento) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Actor> buscarPorNombreOPais(@Param("texto") String texto);
}
