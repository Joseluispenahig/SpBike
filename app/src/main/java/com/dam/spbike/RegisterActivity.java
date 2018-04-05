package com.dam.spbike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.Principal;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private MiBaseDatos MDB;

    EditText editNombre, editApellido,editEmail,editContrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        MDB = new MiBaseDatos(getApplicationContext());
       // editUsuario = (EditText) findViewById(R.id.input_usuario);
        //editEmail = (EditText) findViewById(R.id.input_contrasena);
    }

    public void IrComprobar(View view){
        editNombre = (EditText) findViewById(R.id.input_nombre);
        editApellido=(EditText) findViewById(R.id.input_ape);
        editEmail=(EditText) findViewById(R.id.input_email);
        editContrasena = (EditText) findViewById(R.id.input_contrasena);

        String nombre=editNombre.getText().toString();
        String apellido=editApellido.getText().toString();
        String email=editEmail.getText().toString();
        String contrasena=editContrasena.getText().toString();

        System.out.println("Nombre: " + nombre);
        System.out.println("Apellido: " + apellido);
        System.out.println("email: " + email);
        System.out.println("contrasena: " + contrasena);
        boolean salida;

        if(nombre.length()>0 || apellido.length()>0 || email.length()>0 || contrasena.length()>0) {
            if(!validarEmail(email)){
                editEmail.setError("Email no valido");
            }
            if (contrasena.length() < 8) {
                editContrasena.setError("La contraseña al menos debe tener 8 caracteres");
            } else {
                //Corregir que vaya metiendo datos con id autoincremento
                salida = MDB.insertarUSUARIO(nombre, apellido, email, contrasena);
                if (salida == false) {
                    Toast.makeText(getApplicationContext(),
                            "Ya esta registrado en el sistema. Inicie sesion", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Se ha registrado correctamente,volviendo a la pagina principal", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Inserte datos", Toast.LENGTH_LONG).show();
        }
    }
    public boolean validarEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}