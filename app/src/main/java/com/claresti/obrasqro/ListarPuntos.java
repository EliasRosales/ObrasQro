package com.claresti.obrasqro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListarPuntos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_listar_puntos);
    }
}
