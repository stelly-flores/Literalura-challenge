package com.aluracursos.literalura_challenge.repository;

import com.aluracursos.literalura_challenge.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombre(String nombre);

    List<Autor> findByAnioNacimientoLessThanEqualAndAnioFallecimientoGreaterThanEqual(Integer anio, Integer anio2);
}
