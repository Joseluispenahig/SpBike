package com.dam.spbike;

public class Estaciones{
    private int id;
    private String nombre;
    private String direccion;
    private String latitude;
    private String longitude;
    private String ciudad;
    private int uid;
    private int cantidad;
    private int libres;
    private int ocupadas;

    public Estaciones(int id,String nombre,String direccion,String latitude,String longitude,
                      String ciudad,int uid,int cantidad,int libres,int ocupadas){
        this.id=id;
        this.nombre=nombre;
        this.direccion=direccion;
        this.latitude=latitude;
        this.longitude=longitude;
        this.ciudad=ciudad;
        this.uid=uid;
        this.cantidad=cantidad;
        this.libres=libres;
        this.ocupadas=ocupadas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getLibres() {
        return libres;
    }

    public void setLibres(int libres) {
        this.libres = libres;
    }

    public int getOcupadas() {
        return ocupadas;
    }

    public void setOcupadas(int ocupadas) {
        this.ocupadas = ocupadas;
    }
}