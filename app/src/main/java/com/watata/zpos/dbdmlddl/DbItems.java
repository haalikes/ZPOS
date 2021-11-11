package com.watata.zpos.dbdmlddl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.watata.zpos.DbNames;
import com.watata.zpos.ddlclass.HelperItem;
import com.watata.zpos.ddlclass.HelperTwoString;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DbItems {
    DbNames dbNames;

    public DbItems(DbNames dbNames) {
        this.dbNames = dbNames;
    }

    public void createTable(SQLiteDatabase db){
        try {
            String createTable = "CREATE TABLE " + dbNames.getTbl_items()
                    + " ( "
                           + dbNames.getCol_item_id() + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + " ," + dbNames.getCol_cat_id() + " INTEGER "
                    + " ," + dbNames.getCol_item_name() + " TEXT "
                    + " ," + dbNames.getCol_item_image() + " TEXT "
                    + " ," + dbNames.getCol_item_selling_price() + " TEXT "
                    + " ," + dbNames.getCol_var_hdr_id() + " TEXT "
                    + " ," + dbNames.getCol_stock_link_id() + " TEXT "
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
        db.execSQL("DROP TABLE IF EXISTS " + dbNames.getTbl_items());
    }

    public void refreshItems(List<HelperItem> helperItems, SQLiteDatabase db){
        deleteAllItems(db);

        ContentValues contentValues = new ContentValues();

        for (int i=0; i < helperItems.size(); i++) {
            contentValues.put(dbNames.getCol_item_id(), helperItems.get(i).getItem_id());
            contentValues.put(dbNames.getCol_cat_id(), helperItems.get(i).getCat_id());
            contentValues.put(dbNames.getCol_item_name(), helperItems.get(i).getItem_name());
            contentValues.put(dbNames.getCol_item_image(), helperItems.get(i).getItem_image());
            contentValues.put(dbNames.getCol_item_selling_price(), helperItems.get(i).getItem_selling_price());
            contentValues.put(dbNames.getCol_var_hdr_id(), helperItems.get(i).getVar_hdr_id());
            contentValues.put(dbNames.getCol_stock_link_id(), helperItems.get(i).getStock_hdr_id());
            long result = db.insert(dbNames.getTbl_items(), null, contentValues);
            if (result == -1) {
                Log.d("refreshItems", "failed insert");
            } else {
                Log.d("refreshItems", "success insert");
            }
        }

        db.close();

    }

    public void deleteAllItems(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_items();
        db.execSQL(query);
    }

    public List<HelperItem> listItems(SQLiteDatabase db){

        List<HelperItem> helperItems = new LinkedList<HelperItem>();
        helperItems.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_items();
        Cursor cursor = db.rawQuery(query, null);
        HelperItem helperItem = null;

        if (cursor.moveToFirst()) {
            do {
                helperItem = new HelperItem();
                helperItem.setItem_id(Integer.parseInt(cursor.getString(0)));
                helperItem.setCat_id(Integer.parseInt(cursor.getString(1)));
                helperItem.setItem_name(cursor.getString(2));
                helperItem.setItem_image(cursor.getString(3));
                helperItem.setItem_selling_price(cursor.getString(4));
                helperItem.setVar_hdr_id(cursor.getString(5));
                helperItem.setStock_hdr_id(cursor.getString(6));
                helperItems.add(helperItem);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperItems;
    }

    public List<HelperItem> listItems(int cat_id, SQLiteDatabase db){

        List<HelperItem> helperItems = new LinkedList<HelperItem>();
        helperItems.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_items() + " WHERE " + dbNames.getCol_cat_id() + " = " + cat_id;
        Cursor cursor = db.rawQuery(query, null);
        HelperItem helperItem = null;

        if (cursor.moveToFirst()) {
            do {
                helperItem = new HelperItem();
                helperItem.setItem_id(Integer.parseInt(cursor.getString(0)));
                helperItem.setCat_id(Integer.parseInt(cursor.getString(1)));
                helperItem.setItem_name(cursor.getString(2));
                helperItem.setItem_image(cursor.getString(3));
                helperItem.setItem_selling_price(cursor.getString(4));
                helperItem.setVar_hdr_id(cursor.getString(5));
                helperItem.setStock_hdr_id(cursor.getString(6));
                helperItems.add(helperItem);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperItems;
    }

    public List<String> listItemNameVariants(SQLiteDatabase db){
        List<String> listItemVariants = new LinkedList<>();
        listItemVariants.clear();

        String query = " SELECT " + dbNames.getCol_item_name()
                    + " FROM " + dbNames.getTbl_items()
                    + " UNION ALL "
                    + " SELECT " + dbNames.getCol_item_name() + "|| ',' ||" + dbNames.getCol_var_dtls_name()
                    + " FROM " + dbNames.getTbl_items() + " a," + dbNames.getTbl_variants_links() + " b," + dbNames.getTbl_variants_dtls() + " c"
                    + " WHERE a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                    + " AND b." + dbNames.getCol_var_hdr_id() + " = c." + dbNames.getCol_var_hdr_id()
                    + " ORDER BY 1 "
                ;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                listItemVariants.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        db.close();
        return listItemVariants;
    }

    public List<HelperTwoString> listHelperTwoStrings(SQLiteDatabase db){
        List<HelperTwoString> lHelperTwoStrings = new LinkedList<>();
        lHelperTwoStrings.clear();

        String query = " SELECT " + dbNames.getCol_item_name() + ", " + dbNames.getCol_item_id()
                + " FROM " + dbNames.getTbl_items()
                + " UNION ALL "
                + " SELECT " + dbNames.getCol_item_name() + "|| ',' ||" + dbNames.getCol_var_dtls_name()
                    + ", a." + dbNames.getCol_item_id() + "|| ','|| c." + dbNames.getCol_var_hdr_id() +"|| ',' ||" + "c." + dbNames.getCol_var_dtls_id()
                + " FROM " + dbNames.getTbl_items() + " a," + dbNames.getTbl_variants_links() + " b," + dbNames.getTbl_variants_dtls() + " c"
                + " WHERE a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND b." + dbNames.getCol_var_hdr_id() + " = c." + dbNames.getCol_var_hdr_id()
                + " ORDER BY 1 "
                ;
        Log.d("listHelperTwoStrings", "query=" + query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                HelperTwoString helperTwoString = new HelperTwoString();
                helperTwoString.setFirst_string(cursor.getString(0));
                helperTwoString.setSecond_string(cursor.getString(1));
                lHelperTwoStrings.add(helperTwoString);
            } while (cursor.moveToNext());
        }

        db.close();
        return lHelperTwoStrings;
    }



}
