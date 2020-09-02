package com.example.username.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.PendingIntent.FLAG_NO_CREATE;
import static android.content.Intent.ACTION_AIRPLANE_MODE_CHANGED;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvLibros;
    private AirplaneStateReceiver airplaneStateReceiver = new AirplaneStateReceiver();
    private LibrosAdapter adapter;
    private Toolbar toolbar;
    final static String LIBRO = "LIBRO";

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
        createSyncAlarm();
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
                goToDetalleLibro(libro);
            }
        });
        rvLibros.setAdapter(adapter);
    }

    private void goToDetalleLibro(Libro libro) {
        Intent intent = new Intent(this, DetalleLibroActivity.class);
        intent.putExtra(LIBRO, libro);
        startActivity(intent);
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

        registerReceiver(airplaneStateReceiver, new IntentFilter(ACTION_AIRPLANE_MODE_CHANGED));
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
        } else if (item.getItemId() == R.id.item_mapa) {
            launchMaps();
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchMaps() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, SyncService.class));
        unregisterReceiver(airplaneStateReceiver);
        super.onDestroy();
    }

    private void createSyncAlarm() {
        Intent intent = new Intent(this, SyncDataReceiver.class);
        boolean alarmExists = PendingIntent.getBroadcast(this, 0, intent, FLAG_NO_CREATE) != null;

        if (!alarmExists) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);

            alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 86400000, pendingIntent);
        }
    }
}
