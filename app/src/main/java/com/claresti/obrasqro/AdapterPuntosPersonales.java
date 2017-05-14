package com.claresti.obrasqro;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterPuntosPersonales extends BaseAdapter {

    LayoutInflater inflator;
    ArrayList<ObjMarcador> marcadores;
    Context context;
    private FragmentManager fragmentManager;

    public AdapterPuntosPersonales(Context context, ArrayList<ObjMarcador> marcadores, FragmentManager fragmentManager) {
        inflator = LayoutInflater.from(context);
        this.marcadores = marcadores;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return marcadores.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflator.inflate(R.layout.punto_personal, null);
        final ImageView eliminar;
        final TextView txt_nombre;
        eliminar = (ImageView)convertView.findViewById(R.id.img_borrar);
        txt_nombre = (TextView)convertView.findViewById(R.id.txt_nombre);
        txt_nombre.setText(marcadores.get(position).getNombre());
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Click:", "Se preciono el boton de eliminar de: " + marcadores.get(position).getNombre());
                DialogoAdvertenciaFavoritos advertencia = new DialogoAdvertenciaFavoritos();
                Bundle variable = new Bundle();
                variable.putInt("id", marcadores.get(position).getId());
                advertencia.setArguments(variable);
                advertencia.show(fragmentManager, "Advertencia");
            }
        });
        return convertView;
    }
}
