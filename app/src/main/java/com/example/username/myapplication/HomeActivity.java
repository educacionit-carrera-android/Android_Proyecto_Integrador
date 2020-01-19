package com.example.username.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ListView lvLibros;
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

        initializeSyncService();
        logFCMToken();
        subscribeToTopic("Terror");
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mis libros");
    }

    private void setupAdapter() {
        lvLibros = findViewById(R.id.lvLibros);
        adapter = new LibrosAdapter(getLibros());
        lvLibros.setAdapter(adapter);
        lvLibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Libro libro = adapter.getItem(position);
                Toast.makeText(HomeActivity.this, libro.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeSyncService() {
        Intent intent = new Intent(this, SyncService.class);
        startService(intent);
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

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, SyncService.class));
        super.onDestroy();
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
