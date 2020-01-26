package com.example.username.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public class AgregarLibroActivity extends AppCompatActivity {

    private EditText etNombreLibro;
    private EditText etAutor;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_libro);

        setupUI();
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datosValidos()) {
                    Libro libro = new Libro();
                    libro.setNombre(etNombreLibro.getText().toString());
                    libro.setAutor(etAutor.getText().toString());
                    try {
                        LibroManager.getInstance(AgregarLibroActivity.this).agregarLibro(libro);
                        finish();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Toast.makeText(AgregarLibroActivity.this, "No se pudo agregar el libro", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AgregarLibroActivity.this, "Completar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean datosValidos() {
        boolean datosValidos = true;
        if (etNombreLibro.getText().toString().isEmpty() || etAutor.getText().toString().isEmpty()) {
            datosValidos = false;
        }
        return datosValidos;
    }

    private void setupUI() {
        etNombreLibro = findViewById(R.id.etNombreLibro);
        etAutor = findViewById(R.id.etAutor);
        btnGuardar = findViewById(R.id.btnGuardar);
    }
}
