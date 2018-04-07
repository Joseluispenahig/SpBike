package com.dam.spbike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    EditText editUsuario, editContraseña;
    private MiBaseDatos MDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MDB = new MiBaseDatos(getApplicationContext());
    }
    public void IrActividadRegistro(View view){
        Intent intent= new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
    public void IrActividadRecuperar(View view){
        Intent intent= new Intent(this,RecoveryActivity.class);
        startActivity(intent);
    }
    public void IrActividadPrincipal(View view) {
        editUsuario = (EditText) findViewById(R.id.input_usuario);
        editContraseña = (EditText) findViewById(R.id.input_contrasena);
        String usuario = editUsuario.getText().toString();
        String contrasena = editContraseña.getText().toString();
        System.out.println("Usuario: " + usuario);
        System.out.println("Contraseña: " + contrasena);
        int tamano1=usuario.length();
        int tamano2=contrasena.length();
        if (usuario.length()>0 && contrasena.length()>0) {

            Usuarios usuarioPrincipal = MDB.recuperarUSUARIO(usuario, contrasena);
            if (usuarioPrincipal == null) {
                Toast.makeText(getApplicationContext(),
                        "Datos Incorrectos", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Se ha iniciado sesion correctamente", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MenuLateralActivity.class);
                startActivity(intent);
            }
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Inserte datos", Toast.LENGTH_LONG).show();
        }
    }

    public void IrActividadPrueba(View view) {
        editUsuario = (EditText) findViewById(R.id.input_usuario);
        editContraseña = (EditText) findViewById(R.id.input_contrasena);
        String usuario = editUsuario.getText().toString();
        String contrasena = editContraseña.getText().toString();
        System.out.println("Usuario: " + usuario);
        System.out.println("Contraseña: " + contrasena);
        int tamano1=usuario.length();
        int tamano2=contrasena.length();
        if (usuario.length()>0 && contrasena.length()>0) {

            Usuarios usuarioPrincipal = MDB.recuperarUSUARIO(usuario, contrasena);
            if (usuarioPrincipal == null) {
                Toast.makeText(getApplicationContext(),
                        "Datos Incorrectos", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Se ha iniciado sesion correctamente", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, PruebaActivity.class);
                startActivity(intent);
            }
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Inserte datos", Toast.LENGTH_LONG).show();
        }
    }
}
