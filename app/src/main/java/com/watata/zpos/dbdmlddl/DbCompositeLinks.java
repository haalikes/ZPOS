package com.watata.zpos.dbdmlddl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.watata.zpos.DbNames;
import com.watata.zpos.HelperCompositeLinks;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DbCompositeLinks {

    DbNames dbNames;

    public DbCompositeLinks(DbNames dbNames) {
        this.dbNames = dbNames;
    }

    public void createTable(SQLiteDatabase db){
        try {
            String createTable = "CREATE TABLE " + dbNames.getTbl_composite_links()
                    + " ( "
                           + dbNames.getCol_composite_link_id() + " INTEGER "
                    + " ," + dbNames.getCol_item_id() + " INTEGER "
                    + " ," + dbNames.getCol_stock_id() + " INTEGER "
                    + " ," + dbNames.getCol_var_hdr_id() + " TEXT "
                    + " ," + dbNames.getCol_var_dtls_id() + " TEXT "
                    + " ," + dbNames.getCol_qty() + " TEXT "
                    + " ," + dbNames.getCol_unit() + " TEXT "
                    + " ," + dbNames.getCol_req() + " TEXT "
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
        db.execSQL("DROP TABLE IF EXISTS " + dbNames.getTbl_composite_links());
    }


    public void refreshCompositeLinks(List<HelperCompositeLinks> helperCompositeLinks, SQLiteDatabase db){

        deleteAllCompositeLinks(db);
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < helperCompositeLinks.size(); i++) {
            contentValues.put(dbNames.getCol_composite_link_id(), helperCompositeLinks.get(i).getComposite_link_id());
            contentValues.put(dbNames.getCol_item_id(), helperCompositeLinks.get(i).getItem_id());
            contentValues.put(dbNames.getCol_stock_id(), helperCompositeLinks.get(i).getStock_id());
            contentValues.put(dbNames.getCol_var_hdr_id(), helperCompositeLinks.get(i).getVar_hdr_id());
            contentValues.put(dbNames.getCol_var_dtls_id(), helperCompositeLinks.get(i).getVar_dtls_id());
            contentValues.put(dbNames.getCol_qty(), helperCompositeLinks.get(i).getQty());
            contentValues.put(dbNames.getCol_unit(), helperCompositeLinks.get(i).getUnit());
            contentValues.put(dbNames.getCol_req(), helperCompositeLinks.get(i).getReq());

            Log.d("refreshCompositeLinks", helperCompositeLinks.get(i).getItem_id() + " --- " + helperCompositeLinks.get(i).getStock_id() +  " >>>> " + helperCompositeLinks.get(i).getVar_hdr_id() );

            long result = db.insert(dbNames.getTbl_composite_links(), null, contentValues);
            if (result == -1) {
                Log.d("refreshCompositeLinks", "failed insert");
            } else {
                Log.d("refreshCompositeLinks", "success insert");
            }
        }

        db.close();

    }

    public void deleteAllCompositeLinks(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_composite_links();
        db.execSQL(query);
    }

    public List<HelperCompositeLinks> listCompositeLinks(SQLiteDatabase db){

        List<HelperCompositeLinks> helperCompositeLinks = new LinkedList<HelperCompositeLinks>();
        helperCompositeLinks.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_composite_links() ;
        Cursor cursor = db.rawQuery(query, null);
        HelperCompositeLinks helperCompositeLink = null;

        if (cursor.moveToFirst()) {
            do {
                helperCompositeLink = new HelperCompositeLinks();
                helperCompositeLink.setComposite_link_id(Integer.parseInt(cursor.getString(0)));
                helperCompositeLink.setItem_id(Integer.parseInt(cursor.getString(1)));
                helperCompositeLink.setStock_id(Integer.parseInt(cursor.getString(2)));
                helperCompositeLink.setVar_hdr_id(cursor.getString(3));
                helperCompositeLink.setVar_dtls_id(cursor.getString(4));
                helperCompositeLink.setQty(cursor.getString(5));
                helperCompositeLink.setUnit(cursor.getString(6));
                helperCompositeLink.setReq(cursor.getString(7));
                helperCompositeLinks.add(helperCompositeLink);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperCompositeLinks;
    }

    public List<HelperCompositeLinks> listCompositeLinks(int var_hdr_id, int var_dtls_id, SQLiteDatabase db){

        List<HelperCompositeLinks> helperCompositeLinks = new LinkedList<HelperCompositeLinks>();
        helperCompositeLinks.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id
                + " AND " + dbNames.getCol_var_dtls_id() + " = " + var_dtls_id;
        Cursor cursor = db.rawQuery(query, null);
        HelperCompositeLinks helperCompositeLink = null;

        if (cursor.moveToFirst()) {
            do {
                helperCompositeLink = new HelperCompositeLinks();
                helperCompositeLink.setComposite_link_id(Integer.parseInt(cursor.getString(0)));
                helperCompositeLink.setItem_id(Integer.parseInt(cursor.getString(1)));
                helperCompositeLink.setStock_id(Integer.parseInt(cursor.getString(2)));
                helperCompositeLink.setVar_hdr_id(cursor.getString(3));
                helperCompositeLink.setVar_dtls_id(cursor.getString(4));
                helperCompositeLink.setQty(cursor.getString(5));
                helperCompositeLink.setUnit(cursor.getString(6));
                helperCompositeLink.setReq(cursor.getString(7));
                helperCompositeLinks.add(helperCompositeLink);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperCompositeLinks;
    }

    public List<HelperCompositeLinks> listCompositeLinks(int item_id, SQLiteDatabase db){

        List<HelperCompositeLinks> helperCompositeLinks = new LinkedList<HelperCompositeLinks>();
        helperCompositeLinks.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id() + " = " + item_id;
        Cursor cursor = db.rawQuery(query, null);
        HelperCompositeLinks helperCompositeLink = null;

        if (cursor.moveToFirst()) {
            do {
                helperCompositeLink = new HelperCompositeLinks();
                helperCompositeLink.setComposite_link_id(Integer.parseInt(cursor.getString(0)));
                helperCompositeLink.setItem_id(Integer.parseInt(cursor.getString(1)));
                helperCompositeLink.setStock_id(Integer.parseInt(cursor.getString(2)));
                helperCompositeLink.setVar_hdr_id(cursor.getString(3));
                helperCompositeLink.setVar_dtls_id(cursor.getString(4));
                helperCompositeLink.setQty(cursor.getString(5));
                helperCompositeLink.setUnit(cursor.getString(6));
                helperCompositeLink.setReq(cursor.getString(7));
                helperCompositeLinks.add(helperCompositeLink);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperCompositeLinks;
    }

}
