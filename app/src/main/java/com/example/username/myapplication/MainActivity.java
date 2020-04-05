package com.example.username.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final String USUARIO = "USUARIO";
    private LinearLayout container;
    private Button btnIniciarSesion;
    private Button btnCrearUsuario;
    private TextView etUsuario;
    private TextView etPassword;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getPreferences(MODE_PRIVATE);
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etContraseña);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnCrearUsuario = findViewById(R.id.btnCrearUsuario);
        container = findViewById(R.id.container);

        btnCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarInProgress();
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUsuario.getText().toString();
                String password = etPassword.getText().toString();
                if (usuario.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Completar datos", Toast.LENGTH_SHORT).show();
                } else {
                    guardarSharedPref(usuario);
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("USUARIO", usuario);
                    intent.putExtra("PASSWORD", password);
                    startActivity(intent);
                    finish();
                }
            }
        });

        cargarSharedPref();
    }

    private void mostrarInProgress() {
        Snackbar.make(container, "En progreso", Snackbar.LENGTH_LONG).show();
    }

    private void guardarSharedPref(String usuario) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USUARIO, usuario);
        editor.apply();
    }

    private void cargarSharedPref() {
        String usuario = pref.getString(USUARIO, "");
        etUsuario.setText(usuario);
    }
}
