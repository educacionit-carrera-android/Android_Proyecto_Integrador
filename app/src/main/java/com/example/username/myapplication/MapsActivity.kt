package com.example.username.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

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

}
