package com.claresti.obrasqro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Bienvenida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_bienvenida);
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.i("hilo", "antes sleep");
                    Thread.sleep(500);
                    Log.i("hilo", "despues sleep");
                    Intent i = new Intent(Bienvenida.this, Main.class);
                    startActivity(i);
                    finish();
                }catch (Exception e){

                }
            }
        });
        hilo.start();
    }
}
