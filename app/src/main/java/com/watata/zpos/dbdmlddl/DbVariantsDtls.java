package com.watata.zpos.dbdmlddl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.watata.zpos.DbNames;
import com.watata.zpos.ddlclass.HelperVariantsDtls;
import com.watata.zpos.ddlclass.HelperVariantsLinks;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DbVariantsDtls {
    DbNames dbNames;

    public DbVariantsDtls(DbNames dbNames) {
        this.dbNames = dbNames;
    }

    public void createTable(SQLiteDatabase db){
        try {
            String createTable = "CREATE TABLE " + dbNames.getTbl_variants_dtls() 
                    + " ( " 
                           + dbNames.getCol_var_hdr_id() + " INTEGER "
                    + " ," + dbNames.getCol_var_dtls_id() + " INTEGER "
                    + " ," + dbNames.getCol_var_dtls_image() + " TEXT "
                    + " ," + dbNames.getCol_var_dtls_name() + " TEXT "
                    + " ," + dbNames.getCol_var_selling_price() + " TEXT "
                    + " ," + dbNames.getCol_var_dtls_default() + " TEXT "
                    + " ," + dbNames.getCol_var_dtls_add_on() + " TEXT "
                    + " ," + dbNames.getCol_composite_required() + " TEXT "
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
        db.execSQL("DROP TABLE IF EXISTS " + dbNames.getTbl_variants_dtls());
    }

    public void refreshVariantsDtls(List<HelperVariantsDtls> helperVariantsDtls, SQLiteDatabase db){

        deleteAllVariantsDtls(db);

        ContentValues contentValues = new ContentValues();

        for (int i=0; i < helperVariantsDtls.size(); i++) {
            contentValues.put(dbNames.getCol_var_hdr_id(), helperVariantsDtls.get(i).getVar_hdr_id());
            contentValues.put(dbNames.getCol_var_dtls_id(), helperVariantsDtls.get(i).getVar_dtls_id());
            contentValues.put(dbNames.getCol_var_dtls_image(), helperVariantsDtls.get(i).getVar_dtls_image());
            contentValues.put(dbNames.getCol_var_dtls_name(), helperVariantsDtls.get(i).getVar_dtls_name());
            contentValues.put(dbNames.getCol_var_selling_price(), helperVariantsDtls.get(i).getVar_selling_price());
            contentValues.put(dbNames.getCol_var_dtls_default(), helperVariantsDtls.get(i).getVar_dtls_default());
            contentValues.put(dbNames.getCol_var_dtls_add_on(), helperVariantsDtls.get(i).getVar_dtls_add_on());
            contentValues.put(dbNames.getCol_composite_required(), helperVariantsDtls.get(i).getComposite_required());
            long result = db.insert(dbNames.getTbl_variants_dtls(), null, contentValues);
            if (result == -1) {
                Log.d("refreshVariantsDtls", "failed insert");
            } else {
                Log.d("refreshVariantsDtls", "success insert");
            }
        }

        db.close();

    }

    public void deleteAllVariantsDtls(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_variants_dtls();
        db.execSQL(query);
    }

    public List<HelperVariantsDtls> listVariantsDtls(SQLiteDatabase db){
        List<HelperVariantsDtls> helperVariantsDtls = new LinkedList<HelperVariantsDtls>();
        helperVariantsDtls.clear();
        String query = "SELECT * FROM " + dbNames.getTbl_variants_dtls() ;
        Cursor cursor = db.rawQuery(query, null);
        HelperVariantsDtls helperVariantsDtl = null;
        if (cursor.moveToFirst()) {
            do {
                helperVariantsDtl = new HelperVariantsDtls();
                helperVariantsDtl.setVar_hdr_id(Integer.parseInt(cursor.getString(0)));
                helperVariantsDtl.setVar_dtls_id(Integer.parseInt(cursor.getString(1)));
                helperVariantsDtl.setVar_dtls_image(cursor.getString(2));
                helperVariantsDtl.setVar_dtls_name(cursor.getString(3));
                helperVariantsDtl.setVar_selling_price(cursor.getString(4));
                helperVariantsDtl.setVar_dtls_default(cursor.getString(5));
                helperVariantsDtl.setVar_dtls_add_on(cursor.getString(6));
                helperVariantsDtl.setComposite_required(cursor.getString(7));
                helperVariantsDtls.add(helperVariantsDtl);
            } while (cursor.moveToNext());
        }
        db.close();
        return helperVariantsDtls;
    }

    public List<HelperVariantsDtls> listVariantsDtls(int var_hdr_id, SQLiteDatabase db){
        List<HelperVariantsDtls> helperVariantsDtls = new LinkedList<HelperVariantsDtls>();
        helperVariantsDtls.clear();
        String query = "SELECT * FROM " + dbNames.getTbl_variants_dtls() + " WHERE " + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id;
        Cursor cursor = db.rawQuery(query, null);
        HelperVariantsDtls helperVariantsDtl = null;
        if (cursor.moveToFirst()) {
            do {
                helperVariantsDtl = new HelperVariantsDtls();
                helperVariantsDtl.setVar_hdr_id(Integer.parseInt(cursor.getString(0)));
                helperVariantsDtl.setVar_dtls_id(Integer.parseInt(cursor.getString(1)));
                helperVariantsDtl.setVar_dtls_image(cursor.getString(2));
                helperVariantsDtl.setVar_dtls_name(cursor.getString(3));
                helperVariantsDtl.setVar_selling_price(cursor.getString(4));
                helperVariantsDtl.setVar_dtls_default(cursor.getString(5));
                helperVariantsDtl.setVar_dtls_add_on(cursor.getString(6));
                helperVariantsDtl.setComposite_required(cursor.getString(7));
                helperVariantsDtls.add(helperVariantsDtl);
            } while (cursor.moveToNext());
        }
        db.close();
        return helperVariantsDtls;
    }

    public List<HelperVariantsDtls> listVariantsDtls(List<HelperVariantsLinks> helperVariantsLinks, SQLiteDatabase db){
        String in_var_hdr_id = "";
        for (int i = 0; i < helperVariantsLinks.size(); i++){
            if (i == helperVariantsLinks.size() - 1){
                in_var_hdr_id += helperVariantsLinks.get(i).getVar_hdr_id();
            } else {
                in_var_hdr_id += helperVariantsLinks.get(i).getVar_hdr_id() + ",";
            }
        }
        List<HelperVariantsDtls> helperVariantsDtls = new LinkedList<HelperVariantsDtls>();
        helperVariantsDtls.clear();
        String query = "SELECT * FROM " + dbNames.getTbl_variants_dtls() + " WHERE " + dbNames.getCol_var_hdr_id() + " IN (" + in_var_hdr_id + ")";
        Cursor cursor = db.rawQuery(query, null);
        HelperVariantsDtls helperVariantsDtl = null;
        if (cursor.moveToFirst()) {
            do {
                helperVariantsDtl = new HelperVariantsDtls();
                helperVariantsDtl.setVar_hdr_id(Integer.parseInt(cursor.getString(0)));
                helperVariantsDtl.setVar_dtls_id(Integer.parseInt(cursor.getString(1)));
                helperVariantsDtl.setVar_dtls_image(cursor.getString(2));
                helperVariantsDtl.setVar_dtls_name(cursor.getString(3));
                helperVariantsDtl.setVar_selling_price(cursor.getString(4));
                helperVariantsDtl.setVar_dtls_default(cursor.getString(5));
                helperVariantsDtl.setVar_dtls_add_on(cursor.getString(6));
                helperVariantsDtl.setComposite_required(cursor.getString(7));
                helperVariantsDtls.add(helperVariantsDtl);
            } while (cursor.moveToNext());
        }
        db.close();
        Log.d("listVariantsDtls", query);
        return helperVariantsDtls;
    }

    public boolean compositeRequired(int var_hdr_id, int var_dtls_id, SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_variants_dtls()
                + " WHERE " + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id + " "
                + " AND " + dbNames.getCol_var_dtls_id() + " = " + var_dtls_id + " "
                + " AND " + dbNames.getCol_composite_required() + " = 'N'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }
    public String getVarHdrId(String var_dtls_id, SQLiteDatabase db){
        String var_hdr_id = "";
        String query = "SELECT " + dbNames.getCol_var_hdr_id() + " FROM " + dbNames.getTbl_variants_dtls() + " WHERE " + dbNames.getCol_var_dtls_id() + " = " + var_dtls_id;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            var_hdr_id = cursor.getString(0);
        }
        db.close();
        return var_hdr_id;
    }
    public String getVarDtlsName(String var_dtls_id, SQLiteDatabase db){
        String s = "";
        String query = "SELECT " + dbNames.getCol_var_dtls_name() + " FROM " + dbNames.getTbl_variants_dtls()
                + " WHERE " + dbNames.getCol_var_dtls_id() + " = " + var_dtls_id
                ;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            s = cursor.getString(0);
        }
        db.close();
        return s;
    }
    public String getVarDtlsSellingPric(String var_dtls_id, SQLiteDatabase db){
        String s = "";
        String query = "SELECT " + dbNames.getCol_var_selling_price() + " FROM " + dbNames.getTbl_variants_dtls()
                + " WHERE " + dbNames.getCol_var_dtls_id() + " = " + var_dtls_id
                ;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            s = cursor.getString(0);
        }
        db.close();
        return s;
    }
}
