package com.dam.spbike;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class InicioActivity extends Activity {
    //Declaramos nuestra BD
    private MiBaseDatos MDB;

    //Para que coja la id 1 para insertar en la BD.
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        MDB = new MiBaseDatos(getApplicationContext());
        List<String> supplierNames = new ArrayList<String>();
        supplierNames.add("sevici");
        supplierNames.add("bicimad");
        System.out.println(supplierNames.get(0));
        System.out.println(supplierNames.get(1));
        //Ponemos el contador de identificadores a 0
        id = 0;

        for (int i = 0; i < supplierNames.size(); i++) {
            System.out.println(supplierNames.get(i));
            final String URL = "http://api.citybik.es/v2/networks/" + supplierNames.get(i);

            //final ProgressDialog dlg = ProgressDialog.show(this,
            //        "Obteniendo los datos REST", "Por favor, espere...", true);
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
                                    JSONObject estacion = estaciones.getJSONObject(j);
                                    String nombre = estacion.getString("name");
                                    String direccion = estacion.getJSONObject("extra").getString("address");
                                    String latitude = estacion.getString("latitude");
                                    String longitude = estacion.getString("longitude");
                                    String uids = estacion.getJSONObject("extra").getString("uid");
                                    String cantidads = estacion.getJSONObject("extra").getString("slots");
                                    String libresAPI=estacion.getString("free_bikes");

                                    int uid = Integer.parseInt(uids);
                                    int cantidad = Integer.parseInt(cantidads);

                                    int libres = Integer.parseInt(libresAPI);
                                    int reservadas = 0;

                                    System.out.println(ident + " " + nombre + " " + direccion + " " + latitude + " " + longitude + " " + cantidads + " " + uids + " " + Integer.toString(libres) + " " + Integer.toString(reservadas));

                                    //Insertamos en BD
                                    MDB.insertarESTACION(nombre, direccion, latitude, longitude, ciudad, uid, cantidad, libres, reservadas);
                                }
                                //Usando opción 2:
                                //String hostExists = response.getString("valid");

                                //if (ciudad == "true") {
                                //   //El email es correcto Toast toast1 =
                                //    Toast.makeText(getApplicationContext(), "Email correcto", Toast.LENGTH_SHORT).show();
                                //} else {
                                //    //El email es incorrecto
                                //    Toast.makeText(getApplicationContext(), "Email erróneo", Toast.LENGTH_SHORT).show();
                                //}
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            VolleyLog.v("Response:%n %s", response);
                        }
                        //mTextView.setTexzt(response.toString());
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //dlg.dismiss();
                    // mTextView.setText(error.toString());
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            // add the request object to the queue to be executed
            PracticaServiciosWebApplication.getInstance().getRequestQueue().add(request);
        }
        //Timer para que cambie a otra actividad cada cierto 5000 ms (5s)
        new Timer().schedule(new TimerTask(){
            public void run() {
                InicioActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        startActivity(new Intent(InicioActivity.this, MainActivity.class));
                    }
                });
            }
        }, 5000);
       /* new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                System.out.println("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                System.out.println("done!");
                //Puede sernos util para la gestion de la reserva
                Intent intent= new Intent(this.getClass(),MainActivity.class);
                startActivity(intent);
            }
        }.start();
        //Intent intent= new Intent(this,MainActivity.class);
        //startActivity(intent);
    }
    */
    }
}
