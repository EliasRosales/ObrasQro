package com.claresti.obrasqro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class Main extends FragmentActivity implements OnMapReadyCallback {

    //Menu, Declaracion de variables
    private DrawerLayout drawerLayout;
    final List<MenuItem> items = new ArrayList<>();
    private Menu menu;
    private ImageView btnMenu;
    private NavigationView nav;
    //Variable mapa
    private GoogleMap mMap;

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
        nav = (NavigationView)findViewById(R.id.navigation);
        menu = nav.getMenu();
        menuNav();

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

        // Add a marker in Sydney and move the camera
        LatLng qro = new LatLng(20.5897233, -100.3915028);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(qro, 15));
    }

    /**
     * Funcion que da funcionalidad al menu
     */
    private void menuNav() {
        for (int i = 0; i < menu.size(); i++) {
            items.add(menu.getItem(i));
        }
        items.get(0).setChecked(true);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.actuales:
                        msg("actuales");
                        break;
                    case R.id.proximos:
                        msg("proximos");
                        break;
                    case R.id.obras:
                        msg("obras");
                        break;
                    case R.id.eventos:
                        msg("eventos");
                        break;
                    case R.id.todo:
                        msg("todo");
                        break;
                    case R.id.acerca_de:
                        Intent i = new Intent(Main.this, acerca.class);
                        startActivity(i);
                        break;
                }
                /*
                int pos = items.indexOf(item);
                if (pos == 0) {

                } else if (pos == 1) {
                    //Intent i = new Intent(agregarGasto.this, agregar_categoria.class);
                    //startActivity(i);
                } else if (pos == 2) {
                    //Intent i = new Intent(agregarGasto.this, presupuestos.class);
                    //startActivity(i);
                } else if (pos == 3) {
                    //Intent i = new Intent(agregarGasto.this, registros.class);
                    //startActivity(i);
                } else if (pos == 4) {
                    //Intent i = new Intent(agregarGasto.this, acerca.class);
                    //startActivity(i);
                } else if (pos == 5) {
                    Intent i = new Intent(Main.this, acerca.class);
                    startActivity(i);
                }
                */
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

    private void msg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


}
