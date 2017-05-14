package com.claresti.obrasqro;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    public AdapterPuntosPersonales(Context context, ArrayList<ObjMarcador> marcadores) {
        inflator = LayoutInflater.from(context);
        this.marcadores = marcadores;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflator.inflate(R.layout.punto_personal, null);
        final ImageView eliminar;
        final TextView txt_nombre;
        eliminar = (ImageView)convertView.findViewById(R.id.img_borrar);
        txt_nombre = (TextView)convertView.findViewById(R.id.txt_nombre);
        txt_nombre.setText(marcadores.get(position).getNombre());

        return convertView;
    }
}
