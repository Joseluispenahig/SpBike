package com.dam.spbike;

public class Usuarios{

    private int id;
    private String nombre;
    private String apellido;
    private String contrasena;
    private String email;
    private boolean reservada;

    public Usuarios(int id,String nombre,String apellido,String contrasena,String email){
        this.id=id;
        this.nombre=nombre;
        this.apellido=apellido;
        this.contrasena=contrasena;
        this.email=email;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id= id;
    }

    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido(){
        return apellido;
    }

    public void setApellido(String apellido){
        this.apellido=apellido;
    }

    public String getContrasena(){
        return contrasena;
    }

    public void setContrasena(String contrasena){
        this.contrasena=contrasena;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public boolean getReservada(){
        return reservada;
    }

    public void setReservada(boolean reservada){
        this.reservada=reservada;
    }
}