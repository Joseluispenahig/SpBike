package com.dam.spbike;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Spinner spinner;
    private MiBaseDatos MDB;
    ArrayList<String> ciudadesBD, estacionesBD;
    String[] ciudades, estaciones;
    String escogidaciudad;
    Estaciones estacionseleccionada;
    LatLng Sevilla;
    LatLng Madrid;

    //@Override
    protected void onCreate(Bundle savedInstanceState, final GoogleMap googleMap) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Sevilla=new LatLng(37.3890431,-5.9844864);
        Madrid=new LatLng(40.4168702,-3.7037499);

        MDB = new MiBaseDatos(getApplicationContext());
        ciudadesBD = MDB.obtenerCIUDADES();
        ciudadesBD.add(0, "Seleccionar");
        ciudades = new String[ciudadesBD.size()];
        ciudades = ciudadesBD.toArray(ciudades);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ciudades));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                escogidaciudad = ciudades[position];
                if (escogidaciudad != "Seleccionar") {
                    estacionesBD = MDB.obtenerESTACIONES(escogidaciudad);

                    mMap = googleMap;
                    for(int i=0;i<estacionesBD.size();i++) {
                        // Add a marker in Sydney and move the camera
                        estacionseleccionada=MDB.obtenerESTACIONporUID(escogidaciudad.toString(),String.valueOf(i));
                        LatLng posicion = new LatLng(Double.parseDouble(estacionseleccionada.getLatitude().toString()), Double.parseDouble(estacionseleccionada.getLongitude().toString()));
                        mMap.addMarker(new MarkerOptions().position(posicion).title(estacionseleccionada.getNombre()));
                    }
                    if(escogidaciudad.equalsIgnoreCase("Madrid")){
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Madrid));
                    }
                    else{
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Sevilla));
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
