package com.microservicio2.usuarios.repository;

import com.microservicio2.usuarios.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u JOIN u.authorities a " +
            "WHERE (:query IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "   OR LOWER(u.correo) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:rol IS NULL OR a.authority = :rol)")
    List<User> buscarPorQueryYRol(@Param("query") String query, @Param("rol") String rol);
}
