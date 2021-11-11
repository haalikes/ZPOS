package com.watata.zpos.dbdmlddl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.watata.zpos.DbNames;
import com.watata.zpos.HelperChanges;

import java.io.IOException;
import java.util.List;

public class DbChanges {
    DbNames dbNames;

    public DbChanges(DbNames dbNames) {
        this.dbNames = dbNames;
    }

    public void createTable(SQLiteDatabase db){
        try {
            String createTable = "CREATE TABLE " + dbNames.getTbl_changes()
                    + " ( "
                           + dbNames.getCol_change_all() + " TEXT "
                    + " ," + dbNames.getCol_stock_names() + " TEXT "
                    + " ," + dbNames.getCol_category() + " TEXT "
                    + " ," + dbNames.getCol_items() + " TEXT "
                    + " ," + dbNames.getCol_variants_links() + " TEXT "
                    + " ," + dbNames.getCol_variants_hdr() + " TEXT "
                    + " ," + dbNames.getCol_variants_dtls() + " TEXT "
                    + " ," + dbNames.getCol_composite_links() + " TEXT "
                    + " ," + dbNames.getCol_stock_histories() + " TEXT "
                    + " ," + dbNames.getCol_sales() +  " TEXT "
                    + " ," + dbNames.getCol_fp_dtls() + " TEXT "
                    + " ," + dbNames.getCol_csv_links() + " TEXT "
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
        db.execSQL("DROP TABLE IF EXISTS " + dbNames.getTbl_changes());
    }

    public void refreshChanges(List<HelperChanges> helperChanges, SQLiteDatabase db){
        deleteAllChanges(db);
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < helperChanges.size(); i++) {
            contentValues.put(dbNames.getCol_change_all(), helperChanges.get(i).getChange_all());
            contentValues.put(dbNames.getCol_stock_names(), helperChanges.get(i).getStock_names());
            contentValues.put(dbNames.getCol_category(), helperChanges.get(i).getCategory());
            contentValues.put(dbNames.getCol_items(), helperChanges.get(i).getItems());
            contentValues.put(dbNames.getCol_variants_links(), helperChanges.get(i).getVariants_links());
            contentValues.put(dbNames.getCol_variants_hdr(), helperChanges.get(i).getVariants_hdr());
            contentValues.put(dbNames.getCol_variants_dtls(), helperChanges.get(i).getVariants_dtls());
            contentValues.put(dbNames.getCol_composite_links(), helperChanges.get(i).getComposite_links());
            contentValues.put(dbNames.getCol_stock_histories(), helperChanges.get(i).getStock_histories());
            contentValues.put(dbNames.getCol_sales(), helperChanges.get(i).getSales());
            contentValues.put(dbNames.getCol_fp_dtls(), helperChanges.get(i).getFp_dtls());
            contentValues.put(dbNames.getCol_csv_links(), helperChanges.get(i).getCsv_links());

            long result = db.insert(dbNames.getTbl_changes(), null, contentValues);
            if (result == -1) {
                Log.d("refreshChanges", "failed insert");
            } else {
                Log.d("refreshChanges", "success insert");
            }
        }

        db.close();

    }

    public void deleteAllChanges(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_changes();
        db.execSQL(query);
    }

    public boolean dbChanged(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_changes()
                + " WHERE " + dbNames.getCol_change_all() + " = 'Y'";
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

    public boolean dbChangedStockNames(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_changes()
                + " WHERE " + dbNames.getCol_stock_names() + " = 'Y'";
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

    public boolean dbChangedCategory(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_changes()
                + " WHERE " + dbNames.getCol_category() + " = 'Y'";
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

    public boolean dbChangedItems(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_changes()
                + " WHERE " + dbNames.getCol_items() + " = 'Y'";
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

    public boolean dbChangedVariantsLinks(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_changes()
                + " WHERE " + dbNames.getCol_variants_links() + " = 'Y'";
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

    public boolean dbChangedVariantsHdr(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_changes()
                + " WHERE " + dbNames.getCol_variants_hdr() + " = 'Y'";
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

    public boolean dbChangedVariantsDtls(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_changes()
                + " WHERE " + dbNames.getCol_variants_dtls() + " = 'Y'";
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

    public boolean dbChangedCompositeLinks(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_changes()
                + " WHERE " + dbNames.getCol_composite_links() + " = 'Y'";
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

    public boolean dbChangedStockHistories(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_changes()
                + " WHERE " + dbNames.getCol_stock_histories() + " = 'Y'";
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

    public boolean dbChangedSales(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_changes()
                + " WHERE " + dbNames.getCol_sales() + " = 'Y'";
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

    public boolean dbChangedFPDtls(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_changes()
                + " WHERE " + dbNames.getCol_fp_dtls() + " = 'Y'";
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

    public boolean dbChangedCsvLinks(SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_csv_links()
                    + " WHERE " + dbNames.getCol_csv_links() + " = 'Y'";
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
