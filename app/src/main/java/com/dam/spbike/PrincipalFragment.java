package com.dam.spbike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    final private static int LISTA = 0;
    private Spinner spinner1;//= (Spinner) findViewById(R.id.spinner1);
    private Spinner spinner2;//= (Spinner) findViewById(R.id.spinner2);
    private Spinner spinner3;//= (Spinner) findViewById(R.id.spinner3);
    private Button enviar;
    private String[] letra = {"Seleccionar","A","B","C","D","E"};
    ArrayList<String> ciudadesBD, estacionesBD;
    String[] ciudades, estaciones;
    private MiBaseDatos MDB;
    private View vista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        super.onCreate(savedInstanceState);
        //super.onCreate(savedInstanceState);
        MDB = new MiBaseDatos(getActivity().getApplicationContext());
        final View view = inflater.inflate(R.layout.fragment_principal, container, false);

        ciudadesBD = MDB.obtenerCIUDADES();
        ciudadesBD.add(0, "Seleccionar");
        ciudades = new String[ciudadesBD.size()];
        ciudades = ciudadesBD.toArray(ciudades);


        //Spinner spinner1=(Spinner) view.findViewById(R.id.spinner1);
        Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner2);
        Button enviar= (Button) view.findViewById(R.id.button);
        final Spinner spinner3 = (Spinner) view.findViewById(R.id.spinner3);
        System.out.println(getActivity().getApplicationContext());

        spinner2.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, ciudades));
        //spinner2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));
        //spinner3.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, letra));
        spinner3.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item));

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String escogido = ciudades[position];
                if (escogido != "Seleccionar") {
                    estacionesBD = MDB.obtenerESTACIONES(escogido);
                    estacionesBD.add(0, "Seleccionar");
                    estaciones = new String[estacionesBD.size()];
                    estaciones = estacionesBD.toArray(estaciones);
                    spinner3.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, estaciones));
                    MainActivity.opciona = parentView.getItemAtPosition(position).toString();
                } else {
                    spinner3.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                MainActivity.opcionb = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext() ,MapsActivity.class);
                intent.putExtra("ciudad", MainActivity.opciona);
                intent.putExtra("estacion", MainActivity.opcionb);
                startActivity(intent);
            }
        });
        return view;
        }
    }