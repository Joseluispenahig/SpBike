package com.dam.spbike;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    Usuarios usuarioinicio;
    Estaciones estacion;

    //Declaramos nuestra BD
    private MiBaseDatos MDB;

    //Variable que gestiona la disponibilidad de bicicletas en una parada
    int disponibles;
    int libres;
    int reservadas;
    TextView bicic_disp;
    int id;
    LatLng PosEstacion;
    private static final long START_TIME_IN_MILLIS = 30000;
    private TextView mTextViewCountDown;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        final ImageButton refrescar = (ImageButton) findViewById(R.id.imageButton);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mTextViewCountDown.setVisibility(View.INVISIBLE);
        refrescar.bringToFront();
        refrescar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //refrescar.animate().rotation(refrescar.getRotation()-360).start();

                refrescar.animate().rotationBy(360).setInterpolator(new LinearInterpolator()).start();
                actualizardatosAPI(view);
                //refrescar.animate().cancel();
            }
        });
        MDB = new MiBaseDatos(getApplicationContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        usuarioinicio = (Usuarios) getIntent().getExtras().getSerializable("parametro");
        System.out.println("Usuario que ha iniciado sesion: " + usuarioinicio.getNombre() + " " + usuarioinicio.getApellido() + " " + usuarioinicio.getEmail());
        //Obtenemos el objeto que se ha pasado de la actividad Main.
        estacion = (Estaciones) getIntent().getExtras().getSerializable("estaciones");
        System.out.println("Estacion seleccionada: " + estacion.getCiudad() + " " + estacion.getNombre());

        libres = MDB.obtenerLibres(estacion.getId());
        reservadas = MDB.obtenerReservadas(estacion.getId());
        disponibles = libres - reservadas;
        bicic_disp = (TextView) findViewById(R.id.disponibles);
        bicic_disp.setText("Bicicletas disponibles: " + disponibles);
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
        PosEstacion = new LatLng(Double.parseDouble(estacion.getLatitude()), Double.parseDouble(estacion.getLongitude()));
        mMap.addMarker(new MarkerOptions().position(PosEstacion).title("Estacion " + estacion.getNombre()).snippet("Disponibles: " + disponibles));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PosEstacion, 18));
        mMap.setOnInfoWindowLongClickListener(new OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                Intent intent = new Intent(getApplicationContext(), StreetActivity.class);
                intent.putExtra("Lat", PosEstacion.latitude);
                intent.putExtra("Long", PosEstacion.longitude);
                startActivity(intent);
            }
        });
    }

    /*@Override
    public abstract void onInfoWindowLongClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
    }*/

    public void Reservar(View view) {

        actualizalibresreservadas();
        //Condicion de que se realize la reserva si hay bicicletas disponibles o el usuario no haya reservado
        if (MDB.obtenerReserva(usuarioinicio.getId()) == 0 && disponibles > 0) {
            mMap.clear();
            System.out.println("Realiza la reserva");
            //Metodo en base de datos que actualizara los la variable reservada en BD
            MDB.modificarreservaUSUARIO(usuarioinicio.getId(), 1);
            reservadas++;
            MDB.modificarreservadaESTACION(estacion.getId(), reservadas);
            usuarioinicio.setReservada(1);
            estacion.setReservadas(reservadas);
            disponibles = libres - reservadas;
            mMap.addMarker(new MarkerOptions().position(PosEstacion).title("Estacion " + estacion.getNombre()).snippet("Disponibles: " + disponibles));
            Toast.makeText(getApplicationContext(),
                    "Se ha realizado la reserva correctamente", Toast.LENGTH_SHORT).show();
            startTimer();
        } else {
            System.out.println("No se puede realizar la reserva");
            Toast.makeText(getApplicationContext(),
                    "No se puede realizar la reserva", Toast.LENGTH_SHORT).show();
        }
        bicic_disp.setText("Bicicletas disponibles: " + disponibles);
    }

    public void CancelarReserva(View view) {
        libres = MDB.obtenerLibres(estacion.getId());
        reservadas = MDB.obtenerReservadas(estacion.getId());
        //Condicion de que se realize cancele la reserva si el usuario no haya reservado
        if (MDB.obtenerReserva(usuarioinicio.getId()) == 1) {
            mMap.clear();
            System.out.println("Cancela la reserva");
            //Metodo en base de datos que actualizara los la variable reservada en BD
            MDB.modificarreservaUSUARIO(usuarioinicio.getId(), 0);
            usuarioinicio.setReservada(0);
            reservadas--;
            MDB.modificarreservadaESTACION(estacion.getId(), reservadas);
            estacion.setReservadas(reservadas);
            disponibles = libres - reservadas;
            mMap.addMarker(new MarkerOptions().position(PosEstacion).title("Estacion " + estacion.getNombre()).snippet("Disponibles: " + disponibles));
            Toast.makeText(getApplicationContext(),
                    "Se ha cancelado la reserva", Toast.LENGTH_SHORT).show();
            pauseTimer();
            resetTimer();
        } else {
            System.out.println("No tiene realizada ninguna reserva");
            Toast.makeText(getApplicationContext(),
                    "No tiene realizada ninguna reserva", Toast.LENGTH_SHORT).show();
        }
        bicic_disp.setText("Bicicletas disponibles: " + disponibles);
    }

    public void actualizardatosAPI(View view) {
        mMap.clear();
        List<String> supplierNames = new ArrayList<String>();
        supplierNames.add("sevici");
        supplierNames.add("bicimad");
        supplierNames.add("valenbisi");
        supplierNames.add("tusbic");

        System.out.println(supplierNames.get(0));
        System.out.println(supplierNames.get(1));
        System.out.println(supplierNames.get(2));
        //Ponemos el contador de identificadores a 0
        id = 0;

        for (int i = 0; i < supplierNames.size(); i++) {
            System.out.println(supplierNames.get(i));
            final String URL = "http://api.citybik.es/v2/networks/" + supplierNames.get(i);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, (String) null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // dlg.dismiss();
                            //Miramos si el email es correcto:
                            try {

                                //Usando opción 1:
                                //String hostExists = response.getString("hostExists");

                                String ciudad = response.getJSONObject("network").getJSONObject("location").getString("city");

                                JSONArray estaciones = response.getJSONObject("network").getJSONArray("stations");

                                //poner donde j<2 --> estaciones.length()
                                for (int j = 0; j < estaciones.length(); j++) {
                                    //Para insertar en la BD cogeremos una variable que sera t que sera un contador de estaciones.
                                    //La id la usaremos para saber por donde vamos
                                    id++;
                                    String ident = Integer.toString(id);
                                    JSONObject estacions = estaciones.getJSONObject(j);
                                    String nombre = estacions.getString("name");
                                    String direccion = estacions.getJSONObject("extra").getString("address");
                                    String latitude = estacions.getString("latitude");
                                    String longitude = estacions.getString("longitude");
                                    String uids = estacions.getJSONObject("extra").getString("uid");
                                    String cantidads = estacions.getJSONObject("extra").getString("slots");
                                    String libresAPI = estacions.getString("free_bikes");

                                    int uid = Integer.parseInt(uids);
                                    int cantidad = Integer.parseInt(cantidads);

                                    int libres = Integer.parseInt(libresAPI);
                                    int reservadas = 0;

                                    // System.out.println(ident + " " + nombre + " " + direccion + " " + latitude + " " + longitude + " " + cantidads + " " + uids + " " + Integer.toString(libres) + " " + Integer.toString(reservadas));
                                    if (estacion.getNombre().equals(nombre) && estacion.getCiudad().equals(ciudad)) {
                                        //Insertamos en BD
                                        System.out.println(ident + " " + nombre + " " + direccion + " " + latitude + " " + longitude + " " + cantidads + " " + uids + " " + Integer.toString(libres) + " " + Integer.toString(reservadas));
                                        MDB.insertarESTACION(nombre, direccion, latitude, longitude, ciudad, uid, cantidad, libres, reservadas);
                                        actualizalibresreservadas();
                                        bicic_disp.setText("Bicicletas disponibles: " + disponibles);
                                        mMap.addMarker(new MarkerOptions().position(PosEstacion).title("Estacion " + estacion.getNombre()).snippet("Disponibles: " + disponibles));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            VolleyLog.v("Response:%n %s", response);
                        }
                        //mTextView.setTexzt(response.toString());
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            // add the request object to the queue to be executed
            PracticaServiciosWebApplication.getInstance().getRequestQueue().add(request);
        }
    }

    public void actualizalibresreservadas() {
        libres = MDB.obtenerLibres(estacion.getId());
        reservadas = MDB.obtenerReservadas(estacion.getId());
        disponibles = libres - reservadas;
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void startTimer() {
        mTextViewCountDown.setVisibility(View.VISIBLE);
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                //Metodo en base de datos que actualizara los la variable reservada en BD
                MDB.modificarreservaUSUARIO(usuarioinicio.getId(), 0);
                usuarioinicio.setReservada(0);
                reservadas--;
                MDB.modificarreservadaESTACION(estacion.getId(), reservadas);
                disponibles = libres - reservadas;
                Toast.makeText(getApplicationContext(),
                        "Ha sobrepasado el tiempo de reserva y se ha cancelado", Toast.LENGTH_SHORT).show();
                bicic_disp.setText("Bicicletas disponibles: " + disponibles);
                resetTimer();
            }
        }.start();
    }
    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mTextViewCountDown.setVisibility(View.INVISIBLE);
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }
}
