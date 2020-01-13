package com.example.username.myapplication;

import java.util.ArrayList;
import java.util.List;

public class LibroManager {

    private static LibroManager instance;
    private List<Libro> libros;

    public static LibroManager getInstance() {
        if (instance == null) {
            instance = new LibroManager();
        }
        return instance;
    }

    private LibroManager() {
        libros = new ArrayList<>();
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }

}
