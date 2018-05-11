package com.dam.spbike;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


//Añadimos los elementos necesarios de la libreria ksoap
import org.json.JSONArray;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

//Añadimos las librerias relacionadas con JSON y xmlPullParser
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

//Para volley
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.dam.spbike.Estaciones;

public class PruebaActivity extends Activity {
    //En nuestro caso,añadimos nuestro nuevo servicio web
    //Método que queremos ejecutar en el servicio web
    //private static final String METHOD_NAME = "GetMortgagePayment";
    //Namespace definido en el servicio web
    //private static final String NAMESPACE = "http://www.webserviceX.NET/";
    //namespace + método
    //private static final String SOAP_ACTION = "http://www.webserviceX.NET/GetMortgagePayment";
    //Fichero de definición del servicio web
   // private static final String URL = "http://www.webservicex.net/mortgage.asmx?WSDL";
    private TextView int_men;
    private TextView tas_men;
    private TextView seg_men;
    private TextView pago_total;
    private EditText anios;
    private EditText interes;
    private EditText cant_emp;
    private EditText tas_anu;
    private EditText seg_anu;
    private EditText em;

    //Declaramos nuestra BD
    private MiBaseDatos MDB;

    //Para que coja la id 1 para insertar en la BD.
    private int id=0;



    //private Prevision response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
        MDB = new MiBaseDatos(getApplicationContext());
    }
    public void Hacer_peticion_REST (View view) {
            List<String> supplierNames = new ArrayList<String>();
            supplierNames.add("sevici");
            supplierNames.add("bicimad");
            System.out.println(supplierNames.get(0));
            System.out.println(supplierNames.get(1));
            //Ponemos el contador de identificadores a 0
            id=0;

        for(int i=0;i<supplierNames.size();i++){
                System.out.println(supplierNames.get(i));
            final String URL = "http://api.citybik.es/v2/networks/" + supplierNames.get(i) ;

            final ProgressDialog dlg = ProgressDialog.show(this,
                    "Obteniendo los datos REST", "Por favor, espere...", true);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, (String) null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dlg.dismiss();
                            //Miramos si el email es correcto:
                            try {

                                //Usando opción 1:
                                //String hostExists = response.getString("hostExists");

                                String ciudad = response.getJSONObject("network").getJSONObject("location").getString("city");

                                JSONArray estaciones=response.getJSONObject("network").getJSONArray("stations");

                                //poner donde j<2 --> estaciones.length()
                                for(int j=0;j<estaciones.length();j++) {
                                    //Para insertar en la BD cogeremos una variable que sera t que sera un contador de estaciones.
                                    id++;
                                    String ident=Integer.toString(id);
                                    JSONObject estacion=estaciones.getJSONObject(j);
                                    String nombre=estacion.getString("name");
                                    String direccion=estacion.getJSONObject("extra").getString("address");
                                    String latitude=estacion.getString("latitude");
                                    String longitude=estacion.getString("longitude");
                                    String cantidads=estacion.getJSONObject("extra").getString("slots");
                                    String uids=estacion.getJSONObject("extra").getString("uid");

                                    int uid=Integer.parseInt(uids);
                                    int cantidad=Integer.parseInt(cantidads);

                                    int libres=cantidad;
                                    int ocupadas=0;

                                    System.out.println(ident+" "+ nombre + " " + direccion + " " + latitude+ " "+ longitude + " "+  cantidads + " " + uids + " " + Integer.toString(libres) + " " + Integer.toString(ocupadas) );

                                    //Insertamos en BD
                                    MDB.insertarESTACION(nombre,direccion,latitude,longitude,ciudad,uid,cantidad,libres,ocupadas);
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
                    dlg.dismiss();
                    // mTextView.setText(error.toString());
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            // add the request object to the queue to be executed
            PracticaServiciosWebApplication.getInstance().getRequestQueue().add(request);
        }
    }

    void Hacer_request_estaciones (View view){
        ArrayList<Estaciones> estaciones;
        estaciones=MDB.obtenerESTACIONESPORCIUDAD("Madrid");
        for(int i=0;i<estaciones.size();i++){
            System.out.println("Parada: " + estaciones.get(i).getId()+ " " + estaciones.get(i).getNombre()+ " " + estaciones.get(i).getCiudad()+ " " + estaciones.get(i).getUid());
        }
    }

    void Hacer_request_ciudades (View view){
        ArrayList<String> ciudades;
        ciudades=MDB.obtenerCIUDADES();
        for(int i=0;i<ciudades.size();i++){
            System.out.println(ciudades.get(i));
        }
    }

    void Hacer_request_estacion (View view){
        Estaciones estacion=MDB.obtenerESTACION("Sevilla","013_CALLE FERIA");
            System.out.println(estacion.getNombre() +" " +  estacion.getDireccion() +" " + estacion.getCiudad() + " " + estacion.getLatitude() + " " + estacion.getLongitude() );

    }

}
