package com.aluracursos.literalura_challenge.repository;



import com.aluracursos.literalura_challenge.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);

    List<Libro> findByIdioma(String idioma);
}
