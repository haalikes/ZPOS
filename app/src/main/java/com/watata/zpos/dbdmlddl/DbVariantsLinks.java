package com.watata.zpos.dbdmlddl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.watata.zpos.DbNames;
import com.watata.zpos.ddlclass.HelperVariantsLinks;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DbVariantsLinks {
    DbNames dbNames;

    public DbVariantsLinks(DbNames dbNames) {
        this.dbNames = dbNames;
    }

    public void createTable(SQLiteDatabase db){
        try {
            String createTable = "CREATE TABLE " + dbNames.getTbl_variants_links()
                    + " ( "
                           + dbNames.getCol_link_id() + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + ", " + dbNames.getCol_item_id() + " INTEGER "
                    + ", " + dbNames.getCol_var_hdr_id() + " INTEGER "
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
        db.execSQL("DROP TABLE IF EXISTS " + dbNames.getTbl_variants_links());
    }

    public void refreshVariantsLinks(List<HelperVariantsLinks> helperVariantsLinks, SQLiteDatabase db){
        deleteAllVariantsLinks(db);
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < helperVariantsLinks.size(); i++) {
            contentValues.put(dbNames.getCol_item_id(), helperVariantsLinks.get(i).getItem_id());
            contentValues.put(dbNames.getCol_var_hdr_id(), helperVariantsLinks.get(i).getVar_hdr_id());
            long result = db.insert(dbNames.getTbl_variants_links(), null, contentValues);
            if (result == -1) {
                Log.d("refreshVariantsLinks", "failed insert");
            } else {
                Log.d("refreshVariantsLinks", "success insert");
            }
        }

        db.close();

    }

    public void deleteAllVariantsLinks(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_variants_links();
        db.execSQL(query);
    }

    public List<HelperVariantsLinks> listVariantsLinks(SQLiteDatabase db){

        List<HelperVariantsLinks> helperVariantsLinks = new LinkedList<HelperVariantsLinks>();
        helperVariantsLinks.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_variants_links();
        Cursor cursor = db.rawQuery(query, null);
        HelperVariantsLinks helperVariantsLink = null;

        if (cursor.moveToFirst()) {
            do {
                helperVariantsLink = new HelperVariantsLinks();
                helperVariantsLink.setLink_id(Integer.parseInt(cursor.getString(0)));
                helperVariantsLink.setItem_id(Integer.parseInt(cursor.getString(1)));
                helperVariantsLink.setVar_hdr_id(Integer.parseInt(cursor.getString(2)));
                helperVariantsLinks.add(helperVariantsLink);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperVariantsLinks;
    }

    public List<HelperVariantsLinks> listVariantsLinks(int item_id, SQLiteDatabase db){

        List<HelperVariantsLinks> helperVariantsLinks = new LinkedList<HelperVariantsLinks>();
        helperVariantsLinks.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_variants_links() + " WHERE " + dbNames.getCol_item_id() + " = " + item_id;
        Cursor cursor = db.rawQuery(query, null);
        HelperVariantsLinks helperVariantsLink = null;

        if (cursor.moveToFirst()) {
            do {
                helperVariantsLink = new HelperVariantsLinks();
                helperVariantsLink.setLink_id(Integer.parseInt(cursor.getString(0)));
                helperVariantsLink.setItem_id(Integer.parseInt(cursor.getString(1)));
                helperVariantsLink.setVar_hdr_id(Integer.parseInt(cursor.getString(2)));
                helperVariantsLinks.add(helperVariantsLink);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperVariantsLinks;
    }

    public boolean variantsExists(int item_id, SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_variants_links() + " WHERE " + dbNames.getCol_item_id() + " = " + item_id;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.close();
            Log.d("Record Already Exists", query);
            return true;
        }
        Log.d("New Record  ", query);
        db.close();

        return false;
    }

}
