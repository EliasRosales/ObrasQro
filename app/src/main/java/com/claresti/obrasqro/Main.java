package com.claresti.obrasqro;

import android.Manifest;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Main extends FragmentActivity implements OnMapReadyCallback {

    //Menu, Declaracion de variables
    private DrawerLayout drawerLayout;
    final List<MenuItem> items = new ArrayList<>();
    private Menu menu;
    private ImageView btnMenu;
    private NavigationView nav;
    //Variable mapa
    private GoogleMap mMap;
    //variable layout
    private RelativeLayout ventana;
    private ProgressBar progreso;
    //permisos
    private final int MY_PERMISSION = 100;
    private static final String TAG = "Json";
    //Declaracion de variables para el control de bottom sheet
    private Button btnConBottomSheet;
    private LinearLayout bottomSheet;
    //BD
    private BD bd;
    //Variable intent
    private Intent i;
    private double latUsu;
    private double lonUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Menu, Inicia las variables del menu y llama la funcion encargada de su manipulacion
        drawerLayout = (DrawerLayout) findViewById(R.id.dLayout);
        nav = (NavigationView) findViewById(R.id.navigation);
        menu = nav.getMenu();
        menuNav();

        //asignacion variables de layout
        ventana = (RelativeLayout) findViewById(R.id.l_ventana);
        progreso = (ProgressBar) findViewById(R.id.progress);
        bd = new BD(getApplicationContext());

        //Llamada de las variables para el control de bottomsheet
        bottomSheet = (LinearLayout)findViewById(R.id.bottomSheet);
        final BottomSheetBehavior bsb = BottomSheetBehavior.from(bottomSheet);

        if(bd.selectUsuario().equals("0")) {
            //funcion para expandir bottomsheet en cuanto inicia la app
            bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
            bd.updateUsuario();
        }else{
            bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        //Control para esconder bottomsheet
        btnConBottomSheet=(Button)findViewById(R.id.btnConBottomSheet);
        btnConBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new BalloonAdapter(getLayoutInflater()));
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
        marcadores("todas");
        cargarFavoritos();
    }

    private void cargarFavoritos() {
        ArrayList<ObjMarcador> marcadores = bd.selectMarcadores();
        Log.i("bd", "tamaño: " + marcadores.size());
        if(marcadores.size() > 0){
            for(ObjMarcador marcador : marcadores){
                Log.i("bd", "for si entra");
                mMap.addMarker(
                        new MarkerOptions().
                                position(new LatLng(marcador.getLat(), marcador.getLon())).
                                title(marcador.getNombre()).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.favorito))
                );
            }
        }
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
            Snackbar.make(ventana, "Los permisos son necesarios para poder usar la aplicación", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View v) {
                            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, MY_PERMISSION);
                        }
                    }).show();
        }else{
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, MY_PERMISSION);
        }
        return false;
    }

    /**
     * Funcion que da funcionalidad al menu
     */
    private void menuNav() {
        for (int i = 0; i < menu.size(); i++) {
            items.add(menu.getItem(i));
        }
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.actuales:
                        marcadores("actuales");
                        break;
                    case R.id.proximos:
                        marcadores("proximas");
                        break;
                    case R.id.obras:
                        marcadores("tipo,obra");
                        break;
                    case R.id.eventos:
                        marcadores("tipo,evento");
                        break;
                    case R.id.todo:
                        marcadores("todas");
                        cargarFavoritos();
                        break;
                    case R.id.acerca_de:
                        i = new Intent(Main.this, acerca.class);
                        startActivity(i);
                        break;
                    case R.id.puntos:
                        i = new Intent(Main.this, ListarPuntos.class);
                        startActivity(i);
                        break;
                    case R.id.agregar_puntos_personales:
                        i = new Intent(Main.this, agregar_punto_personal.class);
                        startActivity(i);
                        break;
                    case R.id.ver_puntos_personales:
                        i = new Intent(Main.this, mostrar_puntos_personales.class);
                        startActivity(i);
                        break;
                }
                drawerLayout.closeDrawer(nav);
                item.setChecked(false);
                return false;
            }
        });
        //Asignacion del header menu en una bariable
        View headerview = nav.getHeaderView(0);
        //Funcionalidad del boton de menu
        btnMenu = (ImageView)findViewById(R.id.Btnmenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(nav);
            }
        });
    }

    private void marcadores(String operacion) {
        mMap.clear();
        progreso.setVisibility(View.VISIBLE);
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(Main.this).
                addToRequestQueue(
                        request = new JsonObjectRequest(
                                Request.Method.GET,
                                "http://hackaton.claresti.com/api/?o=" + operacion,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String res = response.getString("res");
                                            switch(res){
                                                case "1":
                                                    JSONArray jArrayMarcadores = response.getJSONArray("registros");
                                                    objetoSucesos[] arrayMarcadores = gson.fromJson(jArrayMarcadores.toString(), objetoSucesos[].class);
                                                    Log.i("JSON", arrayMarcadores.length + "");
                                                    for(objetoSucesos marcador : arrayMarcadores){
                                                        Log.i("JSON - for", "Si entra");
                                                        agregarMarcador(marcador);
                                                    }
                                                    progreso.setVisibility(View.GONE);
                                                    break;
                                                case "0":
                                                    //Regresar mensaje de que no hay registros
                                                    break;
                                            }
                                        }catch(JSONException json){
                                            Log.e("JSON", json.toString());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        )
                );
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    /**
     * funcion encargada de poner los pines en el mapa
     * @param sucesos objeto que contiene los elementos del Json de la peticion
     */
    private void agregarMarcador(objetoSucesos sucesos) {
        LatLng pos = new LatLng(Double.parseDouble(sucesos.getLatitud()), Double.parseDouble(sucesos.getLongitud()));
        switch (sucesos.getTipo()){
            case "obra":
                Log.i("JSON - obra", "crea marcador obra");
                mMap.addMarker(
                        new MarkerOptions().
                                position(pos).
                                title(sucesos.getTitulo()).
                                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).
                                snippet("Inicio: " + sucesos.getFecha_inicio() + "\nFin: " + sucesos.getFecha_fin())
                );
                break;
            case "evento":
                Log.i("JSON - evento", "crea marcador evento");
                mMap.addMarker(
                        new MarkerOptions().
                                position(pos).
                                title(sucesos.getTitulo()).
                                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).
                                snippet("Inicio: " + sucesos.getFecha_inicio() + "\nFin: " + sucesos.getFecha_fin())
                );
                break;
            case "estacionamiento":
                Log.i("JSON - evento", "crea marcador evento");
                mMap.addMarker(
                        new MarkerOptions().
                                position(pos).
                                title(sucesos.getTitulo()).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.estacionamiento))
                );
                break;
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

    //Autocomplete text

    public String getPlaceAutoCompleteUrl(String input) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/place/autocomplete/json");
        urlString.append("?input=");
        try {
            urlString.append(URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        urlString.append("&amp;location=");
        urlString.append(latUsu + "," + lonUsu); // append lat long of current location to show nearby results.
        urlString.append("&amp;radius=500&amp;language=en");
        urlString.append("&amp;key=" + "AIzaSyAZBliYPwbdtcHSReLIsBK3QwaVe_W5xuM");
        return urlString.toString();
    }


    //Clase para mostrar informacion marcador

    public class BalloonAdapter implements GoogleMap.InfoWindowAdapter {

        LayoutInflater inflater = null;

        public BalloonAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            View v = inflater.inflate(R.layout.punto_marcador, null);
            if(marker != null){
                TextView titulo = (TextView)v.findViewById(R.id.txt_nombre);
                TextView fecha = (TextView)v.findViewById(R.id.txt_fechaInicio);
                titulo.setText(marker.getTitle());
                fecha.setText(marker.getSnippet());
            }
            return v;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.e("Exception url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

}
