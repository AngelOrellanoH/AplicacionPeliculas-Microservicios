package com.microservicio2.usuarios.service;

import com.microservicio2.usuarios.dto.CriticaRequestDto;
import com.microservicio2.usuarios.dto.CriticaResumenResponseDto;
import com.microservicio2.usuarios.model.Critica;
import com.microservicio2.usuarios.model.User;
import com.microservicio2.usuarios.repository.CriticaRepository;
import com.microservicio2.usuarios.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CriticaService {

    private final CriticaRepository criticaRepository;
    private final UserRepository userRepository;

    public CriticaService(CriticaRepository criticaRepository, UserRepository userRepository) {
        this.criticaRepository = criticaRepository;
        this.userRepository = userRepository;
    }

    public Critica guardarCritica(String username, Integer idPelicula, String valoracion, int nota) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Critica critica = new Critica();
        critica.setUser(user);
        critica.setIdPelicula(idPelicula);
        critica.setValoracion(valoracion);
        critica.setNota(nota);
        critica.setFecha(LocalDate.now());

        return criticaRepository.save(critica);
    }

    public List<Critica> obtenerCriticasPorUsuario(String username) {
        return criticaRepository.findByUserUsername(username);
    }

    public List<Critica> obtenerCriticasPorPelicula(Integer idPelicula) {
        return criticaRepository.findByIdPelicula(idPelicula);
    }

    public void eliminarCritica(Integer id) {
        criticaRepository.deleteById(id);
    }

    public Optional<Critica> buscarPorId(Integer id) {
        return criticaRepository.findById(id);
    }

    public List<Critica> buscarPorTexto(String texto) {
        return criticaRepository.buscarPorTexto(texto);
    }

    public Critica actualizarCritica(Integer id, String username, CriticaRequestDto dto) {
        Critica critica = criticaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crítica no encontrada"));

        if (!critica.getUser().getUsername().equals(username)) {
            throw new SecurityException("No autorizado para editar esta crítica");
        }

        critica.setValoracion(dto.getValoracion());
        critica.setNota(dto.getNota());
        critica.setFecha(LocalDate.now());
        return criticaRepository.save(critica);
    }

    public void eliminarCriticaPorUsuario(String username, Integer id) {
        Critica critica = criticaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crítica no encontrada"));

        if (!critica.getUser().getUsername().equals(username)) {
            throw new SecurityException("No autorizado para eliminar esta crítica");
        }

        criticaRepository.deleteById(id);
    }

    public List<CriticaResumenResponseDto> obtenerResumenPorPeliculas() {
        List<Object[]> resultados = criticaRepository.obtenerResumenPorPelicula();

        return resultados.stream().map(obj -> {
            CriticaResumenResponseDto dto = new CriticaResumenResponseDto();
            dto.setIdPelicula((Integer) obj[0]);
            dto.setCantidadCriticas(((Long) obj[1]).intValue());

            BigDecimal nota = BigDecimal.valueOf((Double) obj[2]).setScale(1, RoundingMode.HALF_UP);
            dto.setNotaMedia(nota.floatValue());

            return dto;
        }).toList();
    }
}
