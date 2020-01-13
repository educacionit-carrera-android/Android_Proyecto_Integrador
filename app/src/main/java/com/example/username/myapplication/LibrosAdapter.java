package com.example.username.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class LibrosAdapter extends BaseAdapter {

    private List<Libro> libros;

    public LibrosAdapter(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public int getCount() {
        return libros.size();
    }

    @Override
    public Libro getItem(int position) {
        return libros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return libros.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Asignamos al convertView a un objeto del tipo vista para ver si es nulo. Si no lo es,
        //quiere decir que ya existía una vista, por lo que en vez de inflarla, la reutilizaremos.
        View view = convertView;
        if (view == null){
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_libro, parent, false);
        }
        //Obtenemos el libro a partir de la posición que nos viene
        Libro libro = libros.get(position);

        //Obtenemos los TextViews de nuestra vista (el layout item_libro)
        TextView txtNombre = view.findViewById(R.id.txtNombre);
        TextView txtAutor = view.findViewById(R.id.txtAutor);

        txtNombre.setText(libro.getNombre());
        txtAutor.setText(libro.getAutor());

        return view;
    }
}
