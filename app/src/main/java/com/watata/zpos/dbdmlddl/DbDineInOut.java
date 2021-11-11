package com.watata.zpos.dbdmlddl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.watata.zpos.DbNames;

import java.io.IOException;

public class DbDineInOut {
    DbNames dbNames;

    public DbDineInOut(DbNames dbNames) {
        this.dbNames = dbNames;
    }

    public void createTable(SQLiteDatabase db){
        try {
            String createTable = "CREATE TABLE " + dbNames.getTbl_dine_in_out() 
                    + " ( " 
                            + dbNames.getCol_dine_in_out() + " TEXT "
                    + ")";
            db.execSQL(createTable);

        } catch (SQLiteException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void onUpgrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + dbNames.getTbl_dine_in_out() );
    }

    public void deleteAllDineInOut(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_dine_in_out();
        db.execSQL(query);
    }

    public void insertDefaultDineInOut(SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();

        deleteAllDineInOut(db);

        contentValues.put(dbNames.getCol_dine_in_out(), "Y");
        long result = db.insert(dbNames.getTbl_dine_in_out(), null, contentValues);
        if (result == -1) {
            Log.d("insertDefaultDineInOut", "failed insert");
        } else {
            Log.d("insertDefaultDineInOut", "success insert");
        }
    }

    public boolean dineExists(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_dine_in_out();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.close();
            return true;
        }

        insertDefaultDineInOut(db);

        db.close();

        return false;
    }

    public boolean dineIn(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_dine_in_out() + " WHERE " + dbNames.getCol_dine_in_out() + " = 'Y'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.close();
            return true;
        }
        db.close();

        return false;
    }

    public void updateDineInOut(String dine_in_out, SQLiteDatabase db){
        String query = "UPDATE " + dbNames.getTbl_dine_in_out() + " SET "
                + dbNames.getCol_dine_in_out() + " = '" + dine_in_out + "' ";
        Log.d("updateDineInOut", query);
        db.execSQL(query);
    }
}
