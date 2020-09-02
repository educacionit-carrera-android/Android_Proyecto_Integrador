package com.example.username.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

private const val LOCATION_PERMISSION_CODE = 1
private const val TAG = "MapsActivity"

class MapsActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        callLocationPermission()
    }

    private fun callLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                checkSelfPermission(locationPermission) == PackageManager.PERMISSION_GRANTED -> {
                    // Permiso concedido previamente, avanzar con el flujo
                    Log.i(TAG, "Permiso concedido previamente")
                    getLastKnowLocation()
                }
                shouldShowRequestPermissionRationale(locationPermission) -> {
                    Log.i(TAG, "Debe pedir permiso racional")
                    // Mostrar una pantalla/vista/diálogo indicando por qué es importante que acepte el permiso
                    requestPermissions(
                            arrayOf(locationPermission),
                            LOCATION_PERMISSION_CODE
                    )
                }
                else -> {
                    // Solicitar permiso
                    Log.i(TAG, "Solicitar permiso")
                    requestPermissions(
                            arrayOf(locationPermission),
                            LOCATION_PERMISSION_CODE
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnowLocation() {
        fusedLocationProviderClient
                .lastLocation
                .addOnSuccessListener { location ->
                    // Ubicación obtenida
                    Log.i(TAG, "Ubicación: ${location.latitude}, ${location.longitude} ")
                }
                .addOnFailureListener {
                    Log.e(TAG, "No se pudo obtener la ubicación", it)
                }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() &&
                                grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.i(TAG, "Permiso aceptado")
                    getLastKnowLocation()
                } else {
                    Log.i(TAG, "Permiso denegado")
                    // Explicar al usuario que la funcionalidad a la que quiere acceder no esta
                    // disponible ya que requiere la aprobación del permiso en cuestión
                    if (!shouldShowRequestPermissionRationale(locationPermission)) {
                        Log.i(TAG, "Permiso denegado de manera permanente")
                    }
                }
            }
        }
    }
}