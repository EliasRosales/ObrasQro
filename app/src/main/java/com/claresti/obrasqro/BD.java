package com.claresti.obrasqro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class BD extends SQLiteOpenHelper {

    public static final int DataBaseVersion = 1;
    public static final String DataBaseName = "obrasQro.db";

    public BD(Context context) {
        super(context, DataBaseName, null, DataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tablaUsuario = "CREATE TABLE Usuario(" +
                "usuPrimera INTEGER NOT NULL PRIMARY KEY" +
                ")";

        String tablaMarcadores = "CREATE TABLE marcadores (" +
                "marId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "marNombre TEXT," +
                "marLat FLOAT," +
                "marLon FLOAT" +
                ")";

        //Crear tablas
        db.execSQL(tablaUsuario);
        db.execSQL(tablaMarcadores);

        //ObjUsuario
        ContentValues v = new ContentValues();
        v.put("usuPrimera", "0");
        db.insert("Usuario", null, v);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String updateUsuario(){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues v = new ContentValues();
            v.put("usuPrimera", 1);
            db.update("Usuario", v, "usuPrimera = 0", null);
            return "1";
        }catch(Exception e){
            return e.toString();
        }
    }

    public String selectUsuario(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Usuario", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex("usuPrimera"));
        }
        return null;
    }

    public String insertMarcador(ObjMarcador marcador){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues v = new ContentValues();
            v.put("marNombre", marcador.getNombre());
            v.put("marLat", marcador.getLat());
            v.put("marLon", marcador.getLon());
            db.insert("marcadores", null, v);
            return "1";
        }catch(Exception e){
            return e.toString();
        }
    }

    public String updateMarcador(ObjMarcador marcador){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues v = new ContentValues();
            v.put("marNombre", marcador.getNombre());
            v.put("marLat", marcador.getLat());
            v.put("marLon", marcador.getLon());
            db.update("marcadores", v , "marId = " + marcador.getId(), null);
            return "1";
        }catch(Exception e){
            return e.toString();
        }
    }

    public String deleteMarcador(ObjMarcador marcador){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("marcadores", "marId = " + marcador.getId(), null);
            return "1";
        }catch(Exception e){
            return e.toString();
        }
    }

    public String deleteMarcador(int id){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("marcadores", "marId = " + id, null);
            return "1";
        }catch(Exception e){
            return e.toString();
        }
    }

    public ArrayList<ObjMarcador> selectMarcadores(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("marcadores", null, null, null, null, null, null);
        ArrayList<ObjMarcador> marcadores = new ArrayList<ObjMarcador>();
        if(cursor.moveToFirst()){
            do{
                ObjMarcador marcador = new ObjMarcador(
                        cursor.getInt(cursor.getColumnIndex("marId")),
                        cursor.getString(cursor.getColumnIndex("marNombre")),
                        cursor.getFloat(cursor.getColumnIndex("marLat")),
                        cursor.getFloat(cursor.getColumnIndex("marLon"))
                );
                marcadores.add(marcador);
            }while(cursor.moveToNext());
        }
        return marcadores;
    }
}
