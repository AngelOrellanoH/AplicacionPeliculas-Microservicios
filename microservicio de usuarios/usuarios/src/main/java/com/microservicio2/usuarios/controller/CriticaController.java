package com.microservicio2.usuarios.controller;


import com.microservicio2.usuarios.dto.CriticaRequestDto;
import com.microservicio2.usuarios.dto.CriticaResponseDto;
import com.microservicio2.usuarios.dto.CriticaResumenResponseDto;
import com.microservicio2.usuarios.model.Critica;
import com.microservicio2.usuarios.service.CriticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/critica")
public class CriticaController {

    private final CriticaService criticaService;

    public CriticaController(CriticaService criticaService) {
        this.criticaService = criticaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<CriticaResponseDto> crearCritica(@RequestBody CriticaRequestDto request,
                                                           Authentication authentication) {
        String username = authentication.getName();
        Critica critica = criticaService.guardarCritica(username, request.getIdPelicula(),
                request.getValoracion(), request.getNota());
        return ResponseEntity.ok(toResponseDto(critica));
    }

    @GetMapping("/usuario/{username}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<CriticaResponseDto>> obtenerPorUsuario(@PathVariable String username,
                                                                      Authentication auth) {

        System.out.println("auth.getName() = " + auth.getName());
        System.out.println("username param = " + username);

        if (!auth.getName().equals(username)) {
            return ResponseEntity.status(403).build(); // No autorizado
        }
        List<Critica> criticas = criticaService.obtenerCriticasPorUsuario(username);
        return ResponseEntity.ok(criticas.stream().map(this::toResponseDto).toList());
    }

    @GetMapping("/pelicula/{idPelicula}")
    public ResponseEntity<List<CriticaResponseDto>> obtenerPorPelicula(@PathVariable Integer idPelicula) {
        List<Critica> criticas = criticaService.obtenerCriticasPorPelicula(idPelicula);
        return ResponseEntity.ok(criticas.stream().map(this::toResponseDto).toList());
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CriticaResponseDto>> listarTodas() {
        return ResponseEntity.ok(criticaService
                .buscarPorTexto("").stream().map(this::toResponseDto).toList());
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CriticaResponseDto>> buscar(@RequestParam String texto) {
        return ResponseEntity.ok(criticaService
                .buscarPorTexto(texto).stream().map(this::toResponseDto).toList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<CriticaResponseDto> editarCritica(@PathVariable Integer id,
                                                            @RequestBody CriticaRequestDto dto,
                                                            Authentication auth) {
        try {
            Critica actualizada = criticaService.actualizarCritica(id, auth.getName(), dto);
            return ResponseEntity.ok(toResponseDto(actualizada));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id, Authentication auth) {
        try {
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                criticaService.eliminarCritica(id);
            } else {
                criticaService.eliminarCriticaPorUsuario(auth.getName(), id);
            }
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/resumen")
    public List<CriticaResumenResponseDto> obtenerResumen() {
        return criticaService.obtenerResumenPorPeliculas();
    }

    private CriticaResponseDto toResponseDto(Critica critica) {
        CriticaResponseDto dto = new CriticaResponseDto();
        dto.setId(critica.getId());
        dto.setIdPelicula(critica.getIdPelicula());
        dto.setValoracion(critica.getValoracion());
        dto.setNota(critica.getNota());
        dto.setFecha(critica.getFecha());
        dto.setUsername(critica.getUser().getUsername());
        return dto;
    }


}
