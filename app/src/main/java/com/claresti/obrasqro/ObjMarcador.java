package com.claresti.obrasqro;

import com.google.android.gms.maps.model.LatLng;

public class ObjMarcador {

    private int id;
    private String nombre;
    private double lat;
    private double lon;

    public ObjMarcador(int id, String nombre, double lat, double lon) {
        this.id = id;
        this.nombre = nombre;
        this.lat = lat;
        this.lon = lon;
    }

    public ObjMarcador(){

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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public LatLng getLatLng(){
        return new LatLng(lat, lon);
    }
}
