package com.watata.zpos.dbdmlddl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.watata.zpos.DbNames;
import com.watata.zpos.ddlclass.HelperFPDtls;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DbFpDetails {
    
    DbNames dbNames;

    public DbFpDetails(DbNames dbNames) {
        this.dbNames = dbNames;
    }

    public void createTable(SQLiteDatabase db){
        try {
            String createTable = "CREATE TABLE " + dbNames.getTbl_fp_dtls()
                    + " ( "
                           + dbNames.getCol_fp_id() + " INTEGER "
                    + " ," + dbNames.getCol_fp_date() + " TEXT "
                    + " ," + dbNames.getCol_fp_end_of_day_total() + " TEXT "
                    + " ," + dbNames.getCol_fp_payment_advice() + " TEXT "
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
        db.execSQL("DROP TABLE IF EXISTS " + dbNames.getTbl_fp_dtls());
    }

    public void refreshFPDtls(List<HelperFPDtls> listHelperFPDtls, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();

        String query = "DELETE FROM " + dbNames.getTbl_fp_dtls();
        db.execSQL(query);

        for (int i=0; i < listHelperFPDtls.size(); i++) {
            contentValues.put(dbNames.getCol_fp_id(), listHelperFPDtls.get(i).getFp_id());
            contentValues.put(dbNames.getCol_fp_date(), listHelperFPDtls.get(i).getFp_date());
            contentValues.put(dbNames.getCol_fp_end_of_day_total(), listHelperFPDtls.get(i).getFp_end_of_day_total());
            contentValues.put(dbNames.getCol_fp_payment_advice(), listHelperFPDtls.get(i).getFp_payment_advice());
            long result = db.insert(dbNames.getTbl_fp_dtls(), null, contentValues);
            if (result == -1) {
                Log.d("refreshFPDtls", "refreshFPDtls failed insert");
            } else {
                Log.d("refreshFPDtls", "refreshFPDtls success insert");
            }
        }

        db.close();
    }

    public List<HelperFPDtls> listHelperFPDtls(SQLiteDatabase db){

        List<HelperFPDtls> helperFPDtls = new LinkedList<HelperFPDtls>();
        helperFPDtls.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_fp_dtls();
        Cursor cursor = db.rawQuery(query, null);
        HelperFPDtls helperFPDtl = null;

        if (cursor.moveToFirst()) {
            do {
                helperFPDtl = new HelperFPDtls();
                helperFPDtl.setFp_id(Integer.parseInt(cursor.getString(0)));
                helperFPDtl.setFp_date(cursor.getString(1));
                helperFPDtl.setFp_end_of_day_total(cursor.getString(2));
                helperFPDtl.setFp_payment_advice(cursor.getString(3));
                helperFPDtls.add(helperFPDtl);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperFPDtls;
    }

    public boolean addFPDtls(HelperFPDtls helperFPDtl, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();

        contentValues.put(dbNames.getCol_fp_id(), helperFPDtl.getFp_id());
        contentValues.put(dbNames.getCol_fp_date(), helperFPDtl.getFp_date());
        contentValues.put(dbNames.getCol_fp_end_of_day_total(), helperFPDtl.getFp_end_of_day_total());
        contentValues.put(dbNames.getCol_fp_payment_advice(), helperFPDtl.getFp_payment_advice());
        long result = db.insert(dbNames.getTbl_fp_dtls(), null, contentValues);
        db.close();

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteFPDtls(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_fp_dtls();
        db.execSQL(query);
        db.close();
    }

}
