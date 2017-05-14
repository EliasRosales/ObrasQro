package com.claresti.obrasqro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListarPuntos extends AppCompatActivity {

    //elementos Listview
    private ListView list_categoria;
    private ProgressBar progreso;
    private ImageView btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_listar_puntos);

        //asignacion de variables
        list_categoria = (ListView)findViewById(R.id.lis_categoria);
        progreso = (ProgressBar)findViewById(R.id.progress);
        btnRegresar = (ImageView)findViewById(R.id.Btnregresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cargarLista();
    }

    private void cargarLista() {
        marcadores("todas");
    }

    private void marcadores(String operacion) {
        progreso.setVisibility(View.VISIBLE);
        final Gson gson = new Gson();
        JsonObjectRequest request;
        VolleySingleton.getInstance(ListarPuntos.this).
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
                                                    list_categoria.setAdapter(new AdapterObrasEventos(getApplicationContext(), arrayMarcadores));
                                                    progreso.setVisibility(View.GONE);
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
}
