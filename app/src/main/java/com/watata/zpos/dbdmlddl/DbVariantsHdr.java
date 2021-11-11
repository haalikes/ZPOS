package com.watata.zpos.dbdmlddl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.watata.zpos.DbNames;
import com.watata.zpos.ddlclass.HelperVariantsHdr;
import com.watata.zpos.ddlclass.HelperVariantsLinks;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DbVariantsHdr {

    DbNames dbNames;

    public DbVariantsHdr(DbNames dbNames) {
        this.dbNames = dbNames;
    }

    public void createTable(SQLiteDatabase db){
        try {
            String createTable = "CREATE TABLE " + dbNames.getTbl_variants_hdr()
                    + " ( "
                           + dbNames.getCol_var_hdr_id() + " INTEGER "
                    + " ," + dbNames.getCol_var_hdr_image() + " TEXT "
                    + " ," + dbNames.getCol_var_hdr_name() + " TEXT "
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
        db.execSQL("DROP TABLE IF EXISTS " + dbNames.getTbl_variants_hdr());
    }

    public void refreshVariantsHdr(List<HelperVariantsHdr> helperVariantsHdr, SQLiteDatabase db){
        deleteAllVariantsHdr(db);

        ContentValues contentValues = new ContentValues();

        for (int i=0; i < helperVariantsHdr.size(); i++) {
            contentValues.put(dbNames.getCol_var_hdr_id(), helperVariantsHdr.get(i).getVar_hdr_id());
            contentValues.put(dbNames.getCol_var_hdr_image(), helperVariantsHdr.get(i).getVar_hdr_image());
            contentValues.put(dbNames.getCol_var_hdr_name(), helperVariantsHdr.get(i).getVar_hdr_name());
            long result = db.insert(dbNames.getTbl_variants_hdr(), null, contentValues);
            if (result == -1) {
                Log.d("refreshVariantsHdr", "failed insert");
            } else {
                Log.d("refreshVariantsHdr", "success insert");
            }
        }

        db.close();

    }

    public void deleteAllVariantsHdr(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_variants_hdr();
        db.execSQL(query);
    }

    public List<HelperVariantsHdr> listVariantsHdr(SQLiteDatabase db){

        List<HelperVariantsHdr> helperVariantsHdrs = new LinkedList<HelperVariantsHdr>();
        helperVariantsHdrs.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_variants_hdr();
        Cursor cursor = db.rawQuery(query, null);
        HelperVariantsHdr helperVariantsHdr = null;

        if (cursor.moveToFirst()) {
            do {
                helperVariantsHdr = new HelperVariantsHdr();
                helperVariantsHdr.setVar_hdr_id(Integer.parseInt(cursor.getString(0)));
                helperVariantsHdr.setVar_hdr_image(cursor.getString(1));
                helperVariantsHdr.setVar_hdr_name(cursor.getString(2));
                helperVariantsHdrs.add(helperVariantsHdr);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperVariantsHdrs;
    }

    public List<HelperVariantsHdr> listVariantsHdr(List<HelperVariantsLinks> helperVariantsLinks, SQLiteDatabase db){

        String in_var_hdr_id = "";

        for (int i = 0; i < helperVariantsLinks.size(); i++){
            if (i == helperVariantsLinks.size() - 1){
                in_var_hdr_id += helperVariantsLinks.get(i).getVar_hdr_id();
            } else {
                in_var_hdr_id += helperVariantsLinks.get(i).getVar_hdr_id() + ",";
            }
        }

        List<HelperVariantsHdr> helperVariantsHdrs = new LinkedList<HelperVariantsHdr>();
        helperVariantsHdrs.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_variants_hdr()
                + " WHERE " + dbNames.getCol_var_hdr_id() + " IN (" + in_var_hdr_id + ")";
        Cursor cursor = db.rawQuery(query, null);
        HelperVariantsHdr helperVariantsHdr = null;

        if (cursor.moveToFirst()) {
            do {
                helperVariantsHdr = new HelperVariantsHdr();
                helperVariantsHdr.setVar_hdr_id(Integer.parseInt(cursor.getString(0)));
                helperVariantsHdr.setVar_hdr_image(cursor.getString(1));
                helperVariantsHdr.setVar_hdr_name(cursor.getString(2));
                helperVariantsHdrs.add(helperVariantsHdr);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperVariantsHdrs;
    }

}
