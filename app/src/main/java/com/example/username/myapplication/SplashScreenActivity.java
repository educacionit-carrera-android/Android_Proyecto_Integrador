package com.example.username.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.username.myapplication.firebase.RemoteConfigManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SplashScreenActivity extends AppCompatActivity {

    private static final long DELAYED_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initFirebaseRemoteConfigAndLaunchMainActivity();

            }
        }, DELAYED_TIME);   //Como segundo parámetro le pasamos el tiempo de espera que tendrá antes
        // de que se ejecuté el bloque de código dentro del método run()
    }

    private void initFirebaseRemoteConfigAndLaunchMainActivity() {
        RemoteConfigManager.init();
        RemoteConfigManager.getInstance().fetch(0).addOnCompleteListener(   // Se fuerza la actualización
                this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Configuraciones obtenidas correctamente
                            activateConfigs();
                        } else {
                            // No se obtuvieron las configuraciones
                            launchMainActivity();
                        }
                    }
                }
        );
    }

    private void activateConfigs() {
        RemoteConfigManager.getInstance().activate().addOnCompleteListener(
                this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            // Configuraciones activadas correctamente
                        }
                        launchMainActivity();
                    }
                });
    }

    private void launchMainActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);  //Iniciamos la nueva Activity
        finish();   //Finalizamos la Activity actual
    }
}
