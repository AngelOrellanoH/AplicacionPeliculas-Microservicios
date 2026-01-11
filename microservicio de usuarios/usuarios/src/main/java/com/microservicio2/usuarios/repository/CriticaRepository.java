package com.microservicio2.usuarios.repository;

import com.microservicio2.usuarios.model.Critica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CriticaRepository extends JpaRepository<Critica, Integer> {

    List<Critica> findByUserUsername(String username);

    List<Critica> findByIdPelicula(Integer idPelicula);

    @Query("SELECT c FROM Critica c WHERE " +
            "LOWER(c.valoracion) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(c.user.username) LIKE LOWER(CONCAT('%', :texto, '%')) " )
    List<Critica> buscarPorTexto(@Param("texto") String texto);

    @Query("SELECT c.idPelicula, COUNT(c), AVG(c.nota) FROM Critica c GROUP BY c.idPelicula")
    List<Object[]> obtenerResumenPorPelicula();
}
