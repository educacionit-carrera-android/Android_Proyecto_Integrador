package com.example.username.myapplication;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private ListView lvLibros;
    private LibrosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        saludarUsuario();
        setupAdapter();
    }

    private void setupAdapter() {
        lvLibros = findViewById(R.id.lvLibros);
        adapter = new LibrosAdapter(getLibros());
        lvLibros.setAdapter(adapter);
    }

    private List<Libro> getLibros() {
        List<Libro> libros = new ArrayList<>();
        libros.add(new Libro(1, "Canción de hielo y fuego", "George R. R. Martin"));
        libros.add(new Libro(2, "Harry Potter y el cáliz de fuego", "J. K. Rowling"));
        libros.add(new Libro(3, "Los juegos del hambre en llamas", "Suzanne Collins"));
        libros.add(new Libro(4, "Maze runner", "James Dashner"));
        return libros;
    }

    private void saludarUsuario() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String usuario = bundle.getString("USUARIO");
            Toast.makeText(HomeActivity.this, "Bienvenido " + usuario, Toast.LENGTH_SHORT).show();
        }
    }
}
