package com.dam.spbike;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import com.google.android.gms.maps.MapFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    private Spinner spinner;
    private MiBaseDatos MDB;
    ArrayList<String> ciudadesBD, estacionesBD;
    String[] ciudades, estaciones;
    String escogidaciudad;
    ArrayList<Estaciones> estacionseleccionada;
    LatLng Sevilla;
    LatLng Madrid;
    LatLng Santander;
    LatLng Valencia;

    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps2);
        final View view = inflater.inflate(R.layout.fragment_maps2, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        Sevilla=new LatLng(37.3890431,-5.9844864);
        Madrid=new LatLng(40.4168702,-3.7037499);
        Santander= new LatLng(43.4623433,-3.8103373);
        Valencia=new LatLng(39.4721485,-0.3795534);

        MDB = new MiBaseDatos(getContext().getApplicationContext());
        ciudadesBD = MDB.obtenerCIUDADES();
        ciudadesBD.add(0, "Seleccionar");
        ciudades = new String[ciudadesBD.size()];
        ciudades = ciudadesBD.toArray(ciudades);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ciudades));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                escogidaciudad = ciudades[position];
                onMapReady(mMap);
               /* if (escogidaciudad != "Seleccionar") {
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
                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        return view;
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
        if(escogidaciudad !=null){
            if (escogidaciudad != "Seleccionar") {
                estacionesBD = MDB.obtenerESTACIONES(escogidaciudad.toString());
                estacionseleccionada=MDB.obtenerESTACIONESPORCIUDAD(escogidaciudad.toString());
                mMap = googleMap;
                for(int i=0;i<estacionesBD.size();i++) {
                    // Add a marker in Sydney and move the camera
                    //estacionseleccionada=MDB.obtenerESTACIONESPORCIUDAD(escogidaciudad.toString());
                    LatLng posicion = new LatLng(Double.parseDouble(estacionseleccionada.get(i).getLatitude().toString()), Double.parseDouble(estacionseleccionada.get(i).getLongitude().toString()));
                    mMap.addMarker(new MarkerOptions().position(posicion).title(estacionseleccionada.get(i).getNombre()).snippet("Disponibles " +estacionseleccionada.get(i).getLibres()));
                }
                switch (escogidaciudad.toLowerCase()){
                    case "madrid": mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Madrid,13));break;
                    case "sevilla": mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Sevilla,13));break;
                    case "santander": mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Santander,13));break;
                    case "valencia": mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Valencia,13));break;
                }
                /*
                if(escogidaciudad.equalsIgnoreCase("Madrid")){
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Madrid,13));
                }
                else{
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Sevilla,13));
                }
                */
            }
        }
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                Intent intent = new Intent(getContext(), StreetActivity.class);
                Estaciones estacionseleccionada2 = MDB.obtenerESTACION(escogidaciudad.toString(),marker.getTitle().toString());
                intent.putExtra("Lat",Double.parseDouble(estacionseleccionada2.getLatitude().toString()));
                intent.putExtra("Long", Double.parseDouble(estacionseleccionada2.getLongitude().toString()));
                startActivity(intent);
            }
        });

        mMap.setOnInfoWindowClickListener( new GoogleMap.OnInfoWindowClickListener(){
            @Override
            public void onInfoWindowClick(Marker marker){
                Intent intent = new Intent(getContext(), MapsActivity.class);
                Estaciones estacionseleccionada2 = MDB.obtenerESTACION(escogidaciudad.toString(),marker.getTitle().toString());
                intent.putExtra("ciudad", estacionseleccionada2.getCiudad());
                intent.putExtra("estacion", estacionseleccionada2.getNombre());
                intent.putExtra("parametro", PrincipalFragment.usuarioinicio);
                intent.putExtra("estaciones", estacionseleccionada2);
                startActivity(intent);
            }
        });
        //else if(escogidaciudad==null);
    }
}
