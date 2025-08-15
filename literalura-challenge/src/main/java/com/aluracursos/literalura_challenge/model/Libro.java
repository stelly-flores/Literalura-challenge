package com.aluracursos.literalura_challenge.model;


import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idioma;
    private Long numeroDescargas;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro() {}

    public Libro(String titulo, String idioma, Long numeroDescargas) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.numeroDescargas = numeroDescargas;
    }

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.idioma = datosLibro.idiomas().get(0);
        this.numeroDescargas = datosLibro.numeroDeDescargas().longValue();
        this.autor = new Autor(datosLibro.autores().get(0));
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public Long getNumeroDescargas() { return numeroDescargas; }
    public void setNumeroDescargas(Long numeroDescargas) { this.numeroDescargas = numeroDescargas; }
    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }

    @Override
    public String toString() {
        return "---------- Libro ----------" +
                "\nTitulo: " + titulo +
                "\nAutor: " + (autor != null ? autor.getNombre() : "N/A") +
                "\nIdioma: " + idioma +
                "\nNumero de descargas: " + numeroDescargas +
                "\n------------------------";
    }
}