package com.aluracursos.literalura_challenge.principal;

import com.aluracursos.literalura_challenge.model.Autor;
import com.aluracursos.literalura_challenge.model.DatosAutor;
import com.aluracursos.literalura_challenge.model.DatosLibro;
import com.aluracursos.literalura_challenge.model.Libro;
import com.aluracursos.literalura_challenge.repository.AutorRepository;
import com.aluracursos.literalura_challenge.repository.LibroRepository;
import com.aluracursos.literalura_challenge.service.ConsumoAPI;
import com.aluracursos.literalura_challenge.service.ConvierteDatos;


import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repositorioLibro;
    private AutorRepository repositorioAutor;
    private List<Libro> libros;
    private List<Autor> autores;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.repositorioLibro = libroRepository;
        this.repositorioAutor = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    Elija la opción a través de su número:
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Por favor, ingrese un número válido.");
                teclado.nextLine(); // Limpiar el buffer del scanner
                continue;
            }

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var titulo = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + titulo.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, com.aluracursos.literalura_challenge.model.Datos.class);
        Optional<com.aluracursos.literalura_challenge.model.DatosLibro> libroBuscado = datos.libros().stream()
                .findFirst();
        if (libroBuscado.isPresent()) {
            System.out.println("Libro encontrado");
            System.out.println("Titulo: " + libroBuscado.get().titulo());
            System.out.println("Autor: " + libroBuscado.get().autores().get(0).nombre());
            System.out.println("Idioma: " + libroBuscado.get().idiomas().get(0));
            System.out.println("Numero de descargas: " + libroBuscado.get().numeroDeDescargas());

            DatosLibro datosLibro = libroBuscado.get();
            Optional<Libro> libroExistente = repositorioLibro.findByTituloContainsIgnoreCase(datosLibro.titulo());

            if (libroExistente.isPresent()) {
                System.out.println("El libro ya está guardado en la base de datos.");
            } else {
                DatosAutor datosAutor = datosLibro.autores().get(0);
                Autor autor = repositorioAutor.findByNombre(datosAutor.nombre());
                if (autor == null) {
                    autor = new Autor(datosAutor);
                    repositorioAutor.save(autor);
                }

                Libro libro = new Libro(datosLibro);
                libro.setAutor(autor);
                repositorioLibro.save(libro);

                System.out.println("Libro guardado en la base de datos");
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void listarLibrosRegistrados() {
        libros = repositorioLibro.findAll();
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        autores = repositorioAutor.findAll();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año para buscar autores vivos");
        try {
            var anio = teclado.nextInt();
            teclado.nextLine();
            autores = repositorioAutor.findByAnioNacimientoLessThanEqualAndAnioFallecimientoGreaterThanEqual(anio, anio);
            autores.forEach(System.out::println);
        } catch (java.util.InputMismatchException e) {
            System.out.println("Por favor, ingrese un año válido.");
            teclado.nextLine();
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma para buscar los libros");
        var idioma = teclado.nextLine();
        libros = repositorioLibro.findByIdioma(idioma);
        libros.forEach(System.out::println);
    }
}
