package com.claresti.obrasqro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


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

        //Crear tablas
        db.execSQL(tablaUsuario);

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
}
