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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

private const val LOCATION_PERMISSION_CODE = 1
private const val TAG = "MapsActivity"

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val capitalFederal = LatLng(-34.6118529, -58.4560401)
        val ateneoMarker = LatLng(-34.5959839, -58.3942276)
        val bibliotecaNacionalMarker = LatLng(-34.5844482, -58.3980291)

        mMap.addMarker(crearMarcador(ateneoMarker, "El Ateneo"))
        mMap.addMarker(crearMarcador(bibliotecaNacionalMarker, "Biblioteca Nacional Mariano Moreno"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(capitalFederal, 12F))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(capitalFederal))

        actualizarUbicacionActual()
    }

    @SuppressLint("NewApi")
    private fun actualizarUbicacionActual() {
        if (checkSelfPermission(locationPermission) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
        } else {
            mMap.isMyLocationEnabled = false
            mMap.uiSettings.isMyLocationButtonEnabled = false
            callLocationPermission()
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        //TODO realizar accion
        return false
    }

    private fun crearMarcador(latLng: LatLng, title: String): MarkerOptions {
        return MarkerOptions()
                .position(latLng)
                .title(title)
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
