package com.watata.zpos.dbdmlddl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.watata.zpos.DbNames;
import com.watata.zpos.HelperCategory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DbCategory {
    DbNames dbNames;

    public DbCategory(DbNames dbNames) {
        this.dbNames = dbNames;
    }

    public void createTable(SQLiteDatabase db){
        try {
            String createTable = "CREATE TABLE " + dbNames.getTblCategory() 
                    + " ( " 
                           + dbNames.getCol_cat_id() + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + " ," + dbNames.getCol_cat_image() + " TEXT "
                    + " ," + dbNames.getCol_cat_name() + " TEXT "
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
        db.execSQL("DROP TABLE IF EXISTS " + dbNames.getTblCategory());
    }



    public List<HelperCategory> listCategory(SQLiteDatabase db){

        List<HelperCategory> categories = new LinkedList<HelperCategory>();
        categories.clear();

        String query = "SELECT * FROM " + dbNames.getTblCategory() + " a "
                + " WHERE EXISTS ("
                + " SELECT 1 FROM " + dbNames.getTbl_items() + " b "
                + " WHERE a." + dbNames.getCol_cat_id() + " = b." + dbNames.getCol_cat_id()
                + ") "
                ;
        Cursor cursor = db.rawQuery(query, null);
        HelperCategory category = null;

        if (cursor.moveToFirst()) {
            do {
                category = new HelperCategory();
                category.setCat_id(Integer.parseInt(cursor.getString(0)));
                category.setCat_image(cursor.getString(1));
                category.setCat_name(cursor.getString(2));
                categories.add(category);
            } while (cursor.moveToNext());
        }

        db.close();

        return categories;
    }

    public int categoryMaxId(SQLiteDatabase db){

        String query = "SELECT MAX(CAT_ID) FROM " + dbNames.getTblCategory();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int categoryCount(SQLiteDatabase db){

        String query = "SELECT COUNT(*) FROM " + dbNames.getTblCategory();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public boolean addCategory(HelperCategory helperCategory, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbNames.getCol_cat_id(), helperCategory.getCat_id());
        contentValues.put(dbNames.getCol_cat_image(), helperCategory.getCat_image());
        contentValues.put(dbNames.getCol_cat_name(), helperCategory.getCat_name());
        long result = db.insert(dbNames.getTblCategory(), null, contentValues);
        db.close();

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean existsCategory(int cat_id, SQLiteDatabase db) {

        String query = "SELECT * FROM " + dbNames.getTblCategory() + " WHERE " + dbNames.getCol_cat_id() + " = " + cat_id;
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

    public void updateCategory(int cat_id, String cat_image, String cat_name, SQLiteDatabase db){
        
        String query = "UPDATE " + dbNames.getTblCategory() + " SET "
                + dbNames.getCol_cat_image() + " = '" + cat_image + "' "
                + ", " + dbNames.getCol_cat_name() + " = '" + cat_name + "' "
                + " WHERE " + dbNames.getCol_cat_id() + " = " + cat_id;
        Log.d("updateCategory", query);
        db.execSQL(query);
    }

    public void deleteCategory(int cat_id, SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTblCategory()
                +" WHERE " + dbNames.getCol_cat_id() + " = " + cat_id;
        Log.d("deleteCategory", query);
        db.execSQL(query);
        db.close();
    }

    public void deleteAllCategory(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTblCategory();
        db.execSQL(query);
    }

    public void refreshCategory(List<HelperCategory> categories, SQLiteDatabase db){
        deleteAllCategory(db);
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < categories.size(); i++) {
            contentValues.put(dbNames.getCol_cat_id(), categories.get(i).getCat_id());
            contentValues.put(dbNames.getCol_cat_image(), categories.get(i).getCat_image());
            contentValues.put(dbNames.getCol_cat_name(), categories.get(i).getCat_name());
            long result = db.insert(dbNames.getTblCategory(), null, contentValues);
            if (result == -1) {
                Log.d("refreshCategory", "failed insert");
            } else {
                Log.d("refreshCategory", "success insert");
            }
        }

        db.close();

    }
    
}
