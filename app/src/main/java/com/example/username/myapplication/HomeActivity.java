package com.example.username.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvLibros;
    private LibrosAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        saludarUsuario();
        setupToolbar();
        setupAdapter();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        prefs.getString("Nombre", "");
        prefs.getInt("Edad", 0);

        logFCMToken();
        subscribeToTopic("Terror");
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mis libros");
    }

    private void setupAdapter() {
        rvLibros = findViewById(R.id.rvLibros);
        adapter = new LibrosAdapter(getLibros(), new LibrosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Libro libro) {
                Toast.makeText(HomeActivity.this, libro.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });
        rvLibros.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setLibros(getLibros());
        adapter.notifyDataSetChanged();
    }

    private List<Libro> getLibros() {
        try {
            return LibroManager.getInstance(this).getLibros();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saludarUsuario() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String usuario = bundle.getString("USUARIO");
            Toast.makeText(HomeActivity.this, "Bienvenido " + usuario, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_agregar) {
            Intent intent = new Intent(HomeActivity.this, AgregarLibroActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void subscribeToTopic(final String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseMessaging",
                                    "Se suscribió al tema " + topic);
                        } else {
                            Log.d("FirebaseMessaging",
                                    "No se pudo suscribir al tema " + topic);
                        }
                    }
                });
    }

    private void logFCMToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Firebase", "Fallo al obtener el token", task.getException());
                            return;
                        }
                        Log.d("Firebase", "Token: " + task.getResult().getToken());
                    }
                });
    }

    private void unSubcribeFromTopicTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }
}
