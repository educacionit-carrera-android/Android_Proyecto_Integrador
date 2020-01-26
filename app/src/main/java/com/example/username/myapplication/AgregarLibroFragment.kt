package com.example.username.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.sql.SQLException

class AgregarLibroFragment : Fragment() {

    private lateinit var etNombreLibro: EditText
    private lateinit var etAutor: EditText
    private lateinit var btnGuardar: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_agregar_libro, container, false)
        setupUI(view)
        initializeBtnGuardar()

        return view
    }


    private fun setupUI(view: View) {
        etNombreLibro = view.findViewById(R.id.etNombreLibro)
        etAutor = view.findViewById(R.id.etAutor)
        btnGuardar = view.findViewById(R.id.btnGuardar)
    }

    private fun initializeBtnGuardar() {
        btnGuardar.setOnClickListener {
            if (datosValidos()) {
                val libro = Libro()
                libro.nombre = etNombreLibro.text.toString()
                libro.autor = etAutor.text.toString()
                try {
                    LibroManager.getInstance(context).agregarLibro(libro)
                    activity?.finish()
                } catch (e: SQLException) {
                    e.printStackTrace()
                    Toast.makeText(context, "No se pudo agregar el libro", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(context, "Completar todos los campos", Toast.LENGTH_SHORT).show()
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

}