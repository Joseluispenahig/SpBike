package com.dam.spbike;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.dam.spbike.Usuarios;
import com.dam.spbike.Estaciones;

//Tendremos la base de datos que contendra la tabla contactos con sus campos y tuplas.

public class MiBaseDatos extends SQLiteOpenHelper {


    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "mibasedatos.db";
    //String con la creacion de la tabla contactos en nuestra BD.
    private static final String TABLA_USUARIOS ="CREATE TABLE IF NOT EXISTS usuarios " +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, apellido TEXT, email TEXT, contrasena TEXT, reservada INTEGER)";

    private static final String TABLA_PARADAS ="CREATE TABLE IF NOT EXISTS estaciones " +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, direccion TEXT,latitude TEXT, longitude TEXT, ciudad TEXT, uid INTEGER, cantidad INTEGER, libres INTEGER, reservadas INTEGER)";

    public MiBaseDatos(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    //Creamos las tablas usuarios y paradas en nuestra base de datos.
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLA_USUARIOS);
        db.execSQL(TABLA_PARADAS);
    }

    @Override
    //Para Eliminar la tabla y volverla a crear si ya existe.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLA_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS" + TABLA_PARADAS);
        onCreate(db);
    }

    //Insertamos un usuario en la BD.
    public boolean insertarUSUARIO(String nom, String ape, String email,String contrasena) {
        long salida=0;
        String[] filtroselect={"nombre","apellido","email","contrasena"};
        String[] filtrocolumna={email};
        int reservada=0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("nombre", nom);
            valores.put("apellido", ape);
            valores.put("email", email);
            valores.put("contrasena", contrasena);
            valores.put("reservada",reservada);

            //Condición para que no tengamos repetición de información.
            Cursor c =db.query("usuarios",filtroselect,"email=?",filtrocolumna,null,null,null);
            int cuenta=c.getCount();
            if(c.getCount()<=0)
            {
                salida = db.insert("usuarios", null, valores);
            }
        }
        db.close();
        return(salida>0);
    }


    //Modificamos un usuario en la tabla contactos de la BD.
    public boolean  modificarUSUARIO(String contrasena, String email){
        long salida=0;
        SQLiteDatabase db = getWritableDatabase();
        String [] condicion={email};
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("contrasena", contrasena);
            salida=db.update("usuarios", valores, "email=?", condicion);
        }
        db.close();
        return(salida>0);
    }
    //Modificamos un usuario en la tabla contactos de la BD.
    public boolean  modificarreservaUSUARIO(int id,int reserva){
        long salida=0;
        SQLiteDatabase db = getWritableDatabase();
        String [] condicion={Integer.toString(id)};
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("reservada", reserva);
            salida=db.update("usuarios", valores, "id=?", condicion);
            System.out.println("Salida");
        }
        db.close();
        return(salida>0);
    }
    //Recuperamos las estaciones correspondientes a una ciudad.
    public int obtenerReserva(int id) {
        SQLiteDatabase db = getReadableDatabase();
        int reservada=0;
        String[] valores_recuperar = {"reservada"};
        String[] filtroColumnas = {Integer.toString(id)};
        Cursor c = db.query("usuarios", valores_recuperar, "id=?", filtroColumnas, null, null, "id", null);
        //Si el numero de elementos tupla del cursor es 0,no tendremos usuarios y devolvemos Null,
        //c.getCount devuelve los elementos de tipo tupla(filas).
        if (c.getCount() == 0) {
            System.out.println("No existe ese valor");
        } else {
            c.moveToFirst();
            do {
               reservada=c.getInt(0);
            } while (c.moveToNext());
        }
        db.close();
        c.close();

        return reservada;
    }


    public boolean compruebaemailBD(String email) {
        boolean correcto=false;
        String[] filtroselect={"nombre","apellido","email","contrasena"};
        String[] filtrocolumna={email};
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("email", email);
            //Condición para que no tengamos repetición de información.
            Cursor c =db.query("usuarios",filtroselect,"email=?",filtrocolumna,null,null,null);
            int cuenta=c.getCount();
            if(c.getCount()>0)
            {
                correcto=true;
            }
        }
        db.close();
        return correcto;
    }

    //Borramos un usuario
    public boolean  borrarUSUARIO(int id) {
        SQLiteDatabase db = getWritableDatabase();
        long salida=0;
        if (db != null) {
            salida=db.delete("usuarios", "id=" + id, null);
        }
        db.close();
        return(salida>0);
    }
    //Recuperamos un usuario,para ello usamos una sentencia select.

    public Usuarios recuperarUSUARIO(String email,String contrasena) {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"id", "nombre", "apellido", "email","contrasena","reservada"};
        String[] filtrocolumna={email,contrasena};
        Usuarios usuarios;
        Cursor c =db.query("usuarios",valores_recuperar,"email=? and contrasena=?",filtrocolumna,null,null,null);
        if(c.getCount()>0) {
            c.moveToFirst();
            usuarios = new Usuarios(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),c.getInt(5));
        }
        else{
            usuarios=null;
        }
        db.close();
        c.close();
        return usuarios;
    }

    //Recuperamos la lista de todos los usuarios.
    public ArrayList<Usuarios> recuperarUSUARIOS() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Usuarios> lista_usuarios = new ArrayList<Usuarios>();
        String[] valores_recuperar = {"_id", "nombre", "telefono", "email"};
        Cursor c = db.query("usuarios", valores_recuperar, null, null, null, null, null, null);
        //Si el numero de elementos tupla del cursor es 0,no tendremos usuarios y devolvemos Null,
        //c.getCount devuelve los elementos de tipo tupla(filas).
        if(c.getCount()==0){
            return null;
        }
        else {
            c.moveToFirst();
            do {
                Usuarios usuarios = new Usuarios(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),c.getString(4),c.getInt(5));
                lista_usuarios.add(usuarios);
            } while (c.moveToNext());
        }
        db.close();
        c.close();

        return lista_usuarios;
    }

    //Ponemos un nuevo método que borre todos los usuarios(Limpiar)
    public boolean  limpiarUSUARIOS() {
        SQLiteDatabase db = getWritableDatabase();
        long salida=0;
        if (db != null) {
            salida=db.delete("usuarios",null, null);
        }
        db.close();
        return(salida>0);
    }

    /*Metodos usados para la Tabla ESTACIONES*/
    //Insertamos un usuario en la BD.
    public boolean insertarESTACION(String nombre,String direccion, String latitude,String longitude, String ciudad,int uid,int cantidad,int libres,int reservadas) {
        long salida=0;
        String[] filtroselect={"nombre","direccion","ciudad"};
        String[] filtrocolumna={nombre,direccion,ciudad};
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("nombre", nombre);
            valores.put("direccion", direccion);
            valores.put("latitude", latitude);
            valores.put("longitude", longitude);
            valores.put("ciudad", ciudad);
            valores.put("uid", uid);
            valores.put("cantidad", cantidad);
            valores.put("libres", libres);
            valores.put("reservadas", reservadas);

            //Condición para que no tengamos repetición de información.
            Cursor c =db.query("estaciones",filtroselect,"nombre=? and direccion=? and ciudad=?",filtrocolumna,null,null,"id");
            int cuenta=c.getCount();
            if(c.getCount()<=0)
            {
                salida = db.insert("estaciones", null, valores);
            }
            else{
                ContentValues valoresact = new ContentValues();
                //Solo podriamos acualizar libres,el resto no haria falta.
                valores.put("nombre", nombre);
                valores.put("direccion", direccion);
                valores.put("latitude", latitude);
                valores.put("longitude", longitude);
                valores.put("ciudad", ciudad);
                valores.put("uid", uid);
                valores.put("cantidad", cantidad);
                valores.put("libres", libres);
                salida = db.update("estaciones",valores,"nombre=? and direccion=? and ciudad=?",filtrocolumna);
            }
        }
        db.close();
        return(salida>0);
    }
    //Ponemos un nuevo método que borre todas las usuarios(Limpiar)
    public boolean  limpiarESTACIONES() {
        SQLiteDatabase db = getWritableDatabase();
        long salida=0;
        if (db != null) {
            salida=db.delete("estaciones",null, null);
        }
        db.close();
        return(salida>0);
    }

    //Recuperamos las estaciones correspondientes a una ciudad.
    public ArrayList<String> obtenerESTACIONES(String ciudad) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> lista_usuarios = new ArrayList<String>();
        String[] valores_recuperar = {"uid", "nombre"};
        String[] filtroColumnas = {ciudad};
        Cursor c = db.query("estaciones", valores_recuperar, "ciudad=?", filtroColumnas, null, null, "uid", null);
        //Si el numero de elementos tupla del cursor es 0,no tendremos usuarios y devolvemos Null,
        //c.getCount devuelve los elementos de tipo tupla(filas).
        if (c.getCount() == 0) {
            return null;
        } else {
            c.moveToFirst();
            do {
                String concatenado=Integer.toString(c.getInt(0)) + ". " + c.getString(1);
                lista_usuarios.add(concatenado);
            } while (c.moveToNext());
        }
        db.close();
        c.close();

        return lista_usuarios;
    }
    //Recuperamos las ciudades
    public ArrayList<String> obtenerCIUDADES() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> lista_ciudades = new ArrayList<String>();
        String[] valores_recuperar = {"ciudad"};
        String[] filtroColumnas = {};
        Cursor c = db.query(true,"estaciones", valores_recuperar, null, null, null, null, "id", null);
        //Si el numero de elementos tupla del cursor es 0,no tendremos usuarios y devolvemos Null,
        //c.getCount devuelve los elementos de tipo tupla(filas).
        if (c.getCount() == 0) {
            return null;
        } else {
            c.moveToFirst();
            do {
                String cadena=c.getString(0);
                lista_ciudades.add(cadena);
            } while (c.moveToNext());
        }
        db.close();
        c.close();

        return lista_ciudades;
    }
    //Recuperamos las estaciones correspondientes a una ciudad.
    public Estaciones obtenerESTACION(String ciudad,String nombre) {
        SQLiteDatabase db = getReadableDatabase();
        //ArrayList<String> lista_usuarios = new ArrayList<String>();
        Estaciones estacion;
        String[] valores_recuperar = {"id","nombre","direccion","latitude","longitude","ciudad","uid","cantidad","libres","reservadas"};
        String[] filtroColumnas = {ciudad,nombre};
        Cursor c = db.query("estaciones", valores_recuperar, " ciudad=? AND nombre=?", filtroColumnas, null, null, "uid", null);
        //Si el numero de elementos tupla del cursor es 0,no tendremos usuarios y devolvemos Null,
        //c.getCount devuelve los elementos de tipo tupla(filas).
        if (c.getCount() == 0) {
            return null;
        }
        else {
            c.moveToFirst();
            System.out.println(Integer.toString(c.getInt(0)) + " " + c.getString(1) + " " + c.getString(2) + " " +
                    c.getString(3) + " " + c.getString(4) + " " + c.getString(5) + " " + Integer.toString(c.getInt(6)) + " " + Integer.toString(c.getInt(7)) + " " + Integer.toString(c.getInt(8)) + " " + Integer.toString(c.getInt(9)));
            String concatenado = c.getString(0) + " " + c.getString(1) + " " +
                    c.getString(2) + " " + c.getString(3) + " " + c.getString(4) + " " +
                    Integer.toString(c.getInt(5)) + " " + Integer.toString(c.getInt(6)) + " " + Integer.toString(c.getInt(7));
            estacion = new Estaciones(c.getInt(0), c.getString(1),
                    c.getString(2), c.getString(3), c.getString(4),
                    c.getString(5), c.getInt(6), c.getInt(7), c.getInt(8), c.getInt(9));
            //lista_usuarios.add(concatenado);
        }
        db.close();
        c.close();

        return estacion;
    }
    //Recuperamos las estaciones correspondientes a una ciudad.
    public Estaciones obtenerESTACIONporUID(String ciudad,String uid) {
        SQLiteDatabase db = getReadableDatabase();
        //ArrayList<String> lista_usuarios = new ArrayList<String>();
        Estaciones estacion;
        String[] valores_recuperar = {"id","nombre","direccion","latitude","longitude","ciudad","uid","cantidad","libres","reservadas"};
        String[] filtroColumnas = {ciudad,uid};
        Cursor c = db.query("estaciones", valores_recuperar, " ciudad=? AND uid=?", filtroColumnas, null, null, "uid", null);
        //Si el numero de elementos tupla del cursor es 0,no tendremos usuarios y devolvemos Null,
        //c.getCount devuelve los elementos de tipo tupla(filas).
        if (c.getCount() == 0) {
            return null;
        }
        else {
            c.moveToFirst();
            System.out.println(Integer.toString(c.getInt(0)) + " " + c.getString(1) + " " + c.getString(2) + " " +
                    c.getString(3) + " " + c.getString(4) + " " + c.getString(5) + " " + Integer.toString(c.getInt(6)) + " " + Integer.toString(c.getInt(7)) + " " + Integer.toString(c.getInt(8)) + " " + Integer.toString(c.getInt(9)));
            String concatenado = c.getString(0) + " " + c.getString(1) + " " +
                    c.getString(2) + " " + c.getString(3) + " " + c.getString(4) + " " +
                    Integer.toString(c.getInt(5)) + " " + Integer.toString(c.getInt(6)) + " " + Integer.toString(c.getInt(7));
            estacion = new Estaciones(c.getInt(0), c.getString(1),
                    c.getString(2), c.getString(3), c.getString(4),
                    c.getString(5), c.getInt(6), c.getInt(7), c.getInt(8), c.getInt(9));
            //lista_usuarios.add(concatenado);
        }
        db.close();
        c.close();

        return estacion;
    }
    //Recuperamos las estaciones correspondientes a una ciudad.
    public int obtenerLibres(int id) {
        SQLiteDatabase db = getReadableDatabase();
        int libres=0;
        String[] valores_recuperar = {"libres"};
        String[] filtroColumnas = {Integer.toString(id)};
        Cursor c = db.query("estaciones", valores_recuperar, "id=?", filtroColumnas, null, null, "id", null);
        //Si el numero de elementos tupla del cursor es 0,no tendremos usuarios y devolvemos Null,
        //c.getCount devuelve los elementos de tipo tupla(filas).
        if (c.getCount() == 0) {
            System.out.println("No existe ese valor");
        } else {
            c.moveToFirst();
            do {
                libres=c.getInt(0);
            } while (c.moveToNext());
        }
        db.close();
        c.close();

        return libres;
    }

    //Recuperamos las estaciones correspondientes a una ciudad.
    public int obtenerReservadas(int id) {
        SQLiteDatabase db = getReadableDatabase();
        int reservadas=0;
        String[] valores_recuperar = {"reservadas"};
        String[] filtroColumnas = {Integer.toString(id)};
        Cursor c = db.query("estaciones", valores_recuperar, "id=?", filtroColumnas, null, null, "id", null);
        //Si el numero de elementos tupla del cursor es 0,no tendremos usuarios y devolvemos Null,
        //c.getCount devuelve los elementos de tipo tupla(filas).
        if (c.getCount() == 0) {
            System.out.println("No existe ese valor");
        } else {
            c.moveToFirst();
            do {
                reservadas=c.getInt(0);
            } while (c.moveToNext());
        }
        db.close();
        c.close();

        return reservadas;
    }

    //Modificamos un usuario en la tabla contactos de la BD.
    public boolean  modificarreservadaESTACION(int id,int reserva){
        long salida=0;
        SQLiteDatabase db = getWritableDatabase();
        String [] condicion={Integer.toString(id)};
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("reservadas", reserva);
            salida=db.update("estaciones", valores, "id=?", condicion);
            System.out.println("Salida");
        }
        db.close();
        return(salida>0);
    }
}
