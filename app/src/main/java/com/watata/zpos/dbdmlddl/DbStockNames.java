package com.watata.zpos.dbdmlddl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.watata.zpos.DbNames;
import com.watata.zpos.ddlclass.HelperStockNames;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DbStockNames {
    DbNames dbNames;

    public DbStockNames(DbNames dbNames) {
        this.dbNames = dbNames;
    }

    public void createTable(SQLiteDatabase db){
        try {
            String createTable = "CREATE TABLE " + dbNames.getTblStockNames() 
                    + " ( " 
                           + dbNames.getCol_stock_id() + " INTEGER "
                    + " ," + dbNames.getColStockName() + " TEXT "
                    + " ," + dbNames.getColMeasureUsed() + " TEXT "
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
        db.execSQL("DROP TABLE IF EXISTS " + dbNames.getTblStockNames());
    }

    public List<HelperStockNames> listStockNames(SQLiteDatabase db){

        List<HelperStockNames> stockNames = new LinkedList<HelperStockNames>();
        stockNames.clear();

        String query = "SELECT * FROM " + dbNames.getTblStockNames();
        Cursor cursor = db.rawQuery(query, null);
        HelperStockNames stockName = null;

        if (cursor.moveToFirst()) {
            do {
                stockName = new HelperStockNames();
                stockName.setStock_id(Integer.parseInt(cursor.getString(0)));
                stockName.setStock_name(cursor.getString(1));
                stockName.setMeasure_used(cursor.getString(2));
                stockNames.add(stockName);
            } while (cursor.moveToNext());
        }

        db.close();

        return stockNames;
    }

    public void refreshStockNames(List<HelperStockNames> stockNames, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();

        String query = "DELETE FROM " + dbNames.getTblStockNames();
        db.execSQL(query);

        for (int i=0; i < stockNames.size(); i++) {
            contentValues.put(dbNames.getCol_stock_id(), stockNames.get(i).getStock_id());
            contentValues.put(dbNames.getColStockName(), stockNames.get(i).getStock_name());
            contentValues.put(dbNames.getColMeasureUsed(), stockNames.get(i).getMeasure_used());
            long result = db.insert(dbNames.getTblStockNames(), null, contentValues);
            if (result == -1) {
                Log.d("refreshStockNames", "failed insert");
            } else {
                Log.d("refreshStockNames", "success insert");
            }
        }

        db.close();
    }

    public boolean addStock(HelperStockNames stockNames, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();

        contentValues.put(dbNames.getCol_stock_id(), stockNames.getStock_id());
        contentValues.put(dbNames.getColStockName(), stockNames.getStock_name());
        contentValues.put(dbNames.getColMeasureUsed(), stockNames.getMeasure_used());
        long result = db.insert(dbNames.getTblStockNames(), null, contentValues);
        db.close();

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String getStockNameMeasuredUsed(String stock_name, SQLiteDatabase db){
        String measureUsed = "";
        String query = "SELECT " + dbNames.getColMeasureUsed() + " FROM " + dbNames.getTblStockNames() + " WHERE " + dbNames.getColStockName() + " = '" + stock_name + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            measureUsed = cursor.getString(0);
        }

        db.close();
        return measureUsed;
    }

    public String getStockName(int stock_id, SQLiteDatabase db){
        String measureUsed = "";
        String query = "SELECT " + dbNames.getColStockName() + " FROM " + dbNames.getTblStockNames() + " WHERE " + dbNames.getCol_stock_id() + " = '" + stock_id + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            measureUsed = cursor.getString(0);
        }

        db.close();
        return measureUsed;
    }

    public String getItemName(String item_id, SQLiteDatabase db){
        String itemName = "";
        String query = "SELECT " + dbNames.getCol_item_name() + " FROM " + dbNames.getTbl_items() + " WHERE " + dbNames.getCol_item_id() + " = " + item_id;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            itemName = cursor.getString(0);
        }

        db.close();
        return itemName;
    }

    public String getItemSellingPrice(String item_id, SQLiteDatabase db){
        String selling_price = "";
        String query = "SELECT " + dbNames.getCol_item_selling_price() + " FROM " + dbNames.getTbl_items() + " WHERE " + dbNames.getCol_item_id() + " = " + item_id;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            selling_price = cursor.getString(0);
        }

        db.close();
        return selling_price;
    }

    public void deleteStockNames(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTblStockNames();
        db.execSQL(query);
        db.close();
    }

    public boolean maintainStockNames(SQLiteDatabase db){
        boolean ok = true;

        String query = "SELECT " + " a.ROWID"
                + ", a." + dbNames.getCol_stock_id()
                + ", a." + dbNames.getColStockName()
                + ", b." + dbNames.getCol_stock_id()
                + ", b." + dbNames.getColStockName()
                + " FROM " + dbNames.getTbl_stocks_history() + " a " + ", " + dbNames.getTblStockNames() + " b"
                + " WHERE a." + dbNames.getCol_stock_id() + " = b." + dbNames.getCol_stock_id()
                + " AND a." + dbNames.getColStockName() + " <> b." + dbNames.getColStockName()
                ;

        Log.d("maintainStockNames", query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Log.d("maintainStockNames", "history_rowid=" + cursor.getInt(0) + "history_stock_id=" + cursor.getInt(1) + " history_stock_name=" + cursor.getString(2) + " name_stock_id=" + cursor.getInt(3) + " name_stock_name=" + cursor.getString(4));
                ok = false;
            } while (cursor.moveToNext());
        }
        db.close();
        return ok;
    }
    
}
