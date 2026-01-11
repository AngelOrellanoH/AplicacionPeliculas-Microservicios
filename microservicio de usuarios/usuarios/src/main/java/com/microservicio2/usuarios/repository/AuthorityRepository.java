package com.microservicio2.usuarios.repository;

import com.microservicio2.usuarios.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority,Integer> {

    List<Authority> findByUserUsername(String username);
}
