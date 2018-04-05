package com.dam.spbike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class PrincipalActivity extends AppCompatActivity {

    final private static int LISTA=0;
    private Spinner spinner1;//= (Spinner) findViewById(R.id.spinner1);
    private Spinner spinner2;//= (Spinner) findViewById(R.id.spinner2);
    private Spinner spinner3;//= (Spinner) findViewById(R.id.spinner3);
    private Button enviar;
    private String[] letra = {"Seleccionar","A","B","C","D","E"};
    private String[] opciona={"Repsol","Cepsa"};
    private String[] opcionb={"Renault","Clio"};
    private View vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Spinner spinner1=(Spinner) findViewById(R.id.spinner1);
        final Spinner spinner2= (Spinner) findViewById(R.id.spinner2);
        Spinner spinner3= (Spinner) findViewById(R.id.spinner3);

        spinner1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));
        //spinner2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));
        spinner3.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));

        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String escogido = letra[position];
                if (escogido != "Seleccionar") {
                    if (escogido == "A") {
                        spinner2.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, opciona));
                    } else if (escogido == "B") {
                        spinner2.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, opcionb));
                    } else {
                        System.out.println("hola3");
                    }
                    System.out.println("hola");
                    System.out.println("letra");
                }
                else{
                    spinner2.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item));
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

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
