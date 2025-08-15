package com.aluracursos.literalura_challenge.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
