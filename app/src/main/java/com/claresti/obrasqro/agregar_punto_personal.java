package com.claresti.obrasqro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class agregar_punto_personal extends FragmentActivity implements OnMapReadyCallback {

    //Variable mapa
    private GoogleMap mMap;
    //Variables layout
    private ImageView btnRegresar;
    private EditText txt_nombre;
    private RelativeLayout ventana;
    private ProgressBar progeso;
    //Variables
    private BD bd;
    private double latUsu;
    private double lonUsu;
    private double latMar = 0;
    private double lonMar = 0;
    private final int MY_PERMISSION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_punto_personal);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //asignacion de variables
        bd = new BD(getApplicationContext());
        btnRegresar = (ImageView)findViewById(R.id.Btnregresar);
        txt_nombre = (EditText)findViewById(R.id.input_nombre);
        ventana = (RelativeLayout)findViewById(R.id.l_ventana);
        progeso = (ProgressBar)findViewById(R.id.progress);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng qro = new LatLng(20.5897233, -100.3915028);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(qro, 14));
        //Validacion de permisos
        if (permisos()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            findMe();
        }
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                latMar = latLng.latitude;
                lonMar = latLng.longitude;
                mMap.addMarker(
                        new MarkerOptions().
                                position(latLng)
                );
            }
        });
    }

    /**
     * funcion que consigue la ubicacion actual del usuario y lo posiciona en el mapa
     */
    private void findMe() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        latUsu = location.getLatitude();
        lonUsu = location.getLongitude();
        LatLng myLocation = new LatLng(latUsu, lonUsu);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14));
    }

    /**
     * funcion encargada de verificar los permisos de ubicacion y en caso de no tenerlos
     * solicita al usuario activarlos
     * @return true en caso de tener los permisos, false en caso contrario
     */
    private boolean permisos() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        if((checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        if((shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) || (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION))){

        }else{
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, MY_PERMISSION);
        }
        return false;
    }

    public void agregarMarcador(View view){
        if(lonMar == 0 && latMar == 0){
            msg("Debes de selecionar un punto en el mapa");
        }else if(txt_nombre.toString().equals("")){
            msg("Debes de asignarle un nombre al marcado");
        }else{
            progeso.setVisibility(View.VISIBLE);
            ObjMarcador marcador = new ObjMarcador();
            marcador.setLat(latMar);
            marcador.setLon(lonMar);
            marcador.setNombre(txt_nombre.getText().toString());
            String res = bd.insertMarcador(marcador);
            if(res.equals("1")){
                msg("Se agrego correctamente :)");
                try{
                    Thread.sleep(500);
                }catch (Exception e){

                }
                progeso.setVisibility(View.GONE);
                Intent i = new Intent(agregar_punto_personal.this, Main.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }else{
                msg("Ocurrio el error: " + res);
            }
        }
    }

    /**
     * funcion encargada de crear un snackbar con un mensaje y un boton de aceptar para ocultarla
     * @param msg String que contiene el mensaje
     */
    private void msg(String msg){
        Snackbar.make(ventana, msg, Snackbar.LENGTH_SHORT).setAction("Aceptar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

}
