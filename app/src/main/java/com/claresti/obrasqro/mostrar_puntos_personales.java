package com.claresti.obrasqro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class mostrar_puntos_personales extends AppCompatActivity {

    //elementos Listview
    private ListView list_marcadores;
    private ImageView btnRegresar;
    //variables
    private BD bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mostrar_puntos_personales);

        //Asignacion variables
        list_marcadores = (ListView)findViewById(R.id.lis_categoria);
        btnRegresar = (ImageView)findViewById(R.id.Btnregresar);
        bd = new BD(getApplicationContext());

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cargarLista();
    }

    private void cargarLista() {
        ArrayList<ObjMarcador> marcadores = bd.selectMarcadores();
        list_marcadores.setAdapter(new AdapterPuntosPersonales(getApplicationContext(), marcadores));
    }
}
