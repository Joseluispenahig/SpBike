package com.dam.spbike;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;


public class PrincipalFragment extends Fragment {

    final private static int LISTA=0;
    private Spinner spinner1;//= (Spinner) findViewById(R.id.spinner1);
    private Spinner spinner2;//= (Spinner) findViewById(R.id.spinner2);
    private Spinner spinner3;//= (Spinner) findViewById(R.id.spinner3);
    private Button enviar;
    private String[] letra = {"Seleccionar","A","B","C","D","E"};
    private String[] opciona={"Repsol","Cepsa"};
    private String[] opcionb={"Renault","Clio"};
    ArrayList<String> ciudadesBD,estacionesBD;
    String [] ciudades,estaciones;
    private MiBaseDatos MDB;
    private View vista;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        super.onCreate(savedInstanceState);
        //super.onCreate(savedInstanceState);
        MDB = new MiBaseDatos(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_principal,container,false);

        ciudadesBD=MDB.obtenerCIUDADES();
        ciudadesBD.add(0,"Seleccionar");
        ciudades= new String[ciudadesBD.size()];
        ciudades = ciudadesBD.toArray(ciudades);

        Spinner spinner1=(Spinner) view.findViewById(R.id.spinner1);
        final Spinner spinner2= (Spinner) view.findViewById(R.id.spinner2);
        Spinner spinner3= (Spinner) view.findViewById(R.id.spinner3);
        System.out.println(getActivity().getApplicationContext());

        spinner1.setAdapter(new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, ciudades));
        //spinner2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));
        spinner3.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, letra));

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String escogido = ciudades[position];
                if (escogido != "Seleccionar") {
                    estacionesBD=MDB.obtenerESTACIONES(escogido);
                    estacionesBD.add(0,"Seleccionar");
                    estaciones= new String[estacionesBD.size()];
                    estaciones = estacionesBD.toArray(estaciones);
                    spinner2.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, estaciones));
                }
                else{
                    spinner2.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item));
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        return view;
       /* public void onItemSelected(AdapterView<?> parent, View view,
        int pos, long id) {
            if(pos==0){
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.arrayone, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
            else{
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.arraytwo, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

            }
            */

    }


       /* enviar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                boolean res=MDB.insertarCONTACTO(0,editNombre.getText().toString(),
                        editTelefono.getText().toString(),editEmail.getText().toString());
                if(res)
                    Toast.makeText(getApplicationContext(),
                            "Contacto añadido correctamente", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(),
                            "No se ha podido guardar el contacto" ,   Toast.LENGTH_LONG).show();
            }

        });*/

    public void clicEnBoton_IrMapa (View view) {
        //   Intent intent = new Intent(this, MapsActivity.class);
        //  startActivityForResult(intent, LISTA);
    }

}
