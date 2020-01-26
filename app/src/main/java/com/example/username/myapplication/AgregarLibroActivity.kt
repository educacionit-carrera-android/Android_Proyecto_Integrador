package com.example.username.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.sql.SQLException

class AgregarLibroActivity : AppCompatActivity() {
    private lateinit var etNombreLibro: EditText
    private lateinit var etAutor: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_libro)
        setupUI()
        btnGuardar.setOnClickListener {
            if (datosValidos()) {
                val libro = Libro()
                libro.nombre = etNombreLibro.text.toString()
                libro.autor = etAutor.text.toString()
                try {
                    LibroManager.getInstance(this@AgregarLibroActivity).agregarLibro(libro)
                    finish()
                } catch (e: SQLException) {
                    e.printStackTrace()
                    Toast.makeText(this@AgregarLibroActivity, "No se pudo agregar el libro", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@AgregarLibroActivity, "Completar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun datosValidos(): Boolean {
        var datosValidos = true
        if (etNombreLibro.text.toString().isEmpty() || etAutor.text.toString().isEmpty()) {
            datosValidos = false
        }
        return datosValidos
    }

    private fun setupUI() {
        etNombreLibro = findViewById(R.id.etNombreLibro)
        etAutor = findViewById(R.id.etAutor)
        btnGuardar = findViewById(R.id.btnGuardar)
    }
}