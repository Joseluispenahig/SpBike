package com.dam.spbike;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class MenuLateralActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private MiBaseDatos MDB;
    TextView usuario,correo;
    Usuarios usuarioinicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MDB = new MiBaseDatos(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lateral);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Send email", "");

                String[] TO = {"esspbike@gmail.com"};
                //String[] CC = {"xyz@gmail.com"};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");


                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
               // emailIntent.putExtra(Intent.EXTRA_CC, CC);
               // emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
              //  emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    Log.i("Finished sending email", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MenuLateralActivity.this,
                            "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //con esto generamos el usuario en el header del menu-------------------------------
        View hView = navigationView.getHeaderView(0);
        TextView correo = (TextView) hView.findViewById(R.id.correo);
        TextView usuario = (TextView) hView.findViewById(R.id.usuario);
        //Obtenemos el objeto que se ha pasado de la actividad Main.
        Usuarios usuarioinicio=  (Usuarios) getIntent().getExtras().getSerializable("parametro");

        usuario.setText(usuarioinicio.getNombre());
        correo.setText(usuarioinicio.getEmail());

        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor,new PrincipalFragment()).commit();




    }

    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //if (drawer.isDrawerOpen(GravityCompat.START)) {
        //    drawer.closeDrawer(GravityCompat.START);
        //} else {
        //    super.onBackPressed();
        //}
        //Creamos una ventana de dialogo para el cierre de sesion
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cierre de sesión");
        builder.setMessage("¿Desea cerrar sesión?");
        builder.setPositiveButton("Si",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                irInicio();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //No hace nada cuando se pulsa
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            fragmentManager.beginTransaction().replace(R.id.contenedor,new PrincipalFragment()).commit();
        } else if (id == R.id.nav_gallery) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new MapsFragment()).commit();
        }/* else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } */else if (id == R.id.nav_share) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new EnlacesIntFragment()).commit();
        } else if (id == R.id.nav_send) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new ContactoFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void irInicio(){
        //Utilizando finish se mantienen los datos escritos
        //finish();
        //Si lo ponemos con Intend se borran los campos delos editText
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
