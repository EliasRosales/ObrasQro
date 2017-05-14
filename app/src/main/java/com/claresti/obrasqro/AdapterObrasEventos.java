package com.claresti.obrasqro;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterObrasEventos extends BaseAdapter {

    LayoutInflater inflator;
    objetoSucesos[] sucesos;
    Context context;

    public AdapterObrasEventos(Context context, objetoSucesos[] sucesos) {
        inflator = LayoutInflater.from(context);
        this.sucesos = sucesos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sucesos.length;
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
        convertView = inflator.inflate(R.layout.punto, null);
        final ImageView icono;
        final TextView txt_nombre;
        final TextView txt_descirpcion;
        final TextView txt_fechaInicio;
        final TextView txt_fechaFin;
        final RelativeLayout ventana;
        icono = (ImageView)convertView.findViewById(R.id.img_Tipo);
        txt_nombre = (TextView)convertView.findViewById(R.id.txt_nombre);
        txt_descirpcion = (TextView)convertView.findViewById(R.id.txt_descripcion);
        txt_fechaInicio = (TextView)convertView.findViewById(R.id.txt_fechaInicio);
        txt_fechaFin = (TextView)convertView.findViewById(R.id.txt_fechaFin);
        ventana = (RelativeLayout)convertView.findViewById(R.id.layout);
        if(sucesos[position].getTipo().equals("obra")){
            icono.setImageResource(R.drawable.obras_osc);
        }else if(sucesos[position].getTipo().equals("evento")){
            icono.setImageResource(R.drawable.eventos_osc);
        }else if(sucesos[position].getTipo().equals("estacionamiento")){
            icono.setImageResource(R.drawable.estacionamiento_osc);
        }
        txt_nombre.setText(sucesos[position].getTitulo());
        txt_descirpcion.setText(sucesos[position].getDescripcion());
        txt_fechaInicio.setText(sucesos[position].getFecha_inicio());
        txt_fechaFin.setText(sucesos[position].getFecha_fin());
        ventana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(sucesos[position].getLink());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(i);
            }
        });
        return convertView;
    }
}
