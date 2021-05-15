package com.watata.zpos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import androidx.constraintlayout.solver.widgets.Helper;

public class HelperDatabase extends SQLiteOpenHelper {
    private static final int sale_sorter = 100;

    private static final String DB_NAME = "ZPOS_TBLS";

    //stock_names
    private static final String TBL_STOCK_NAMES = "TBL_STOCK_NAMES";
    private static final String col_stock_id = "stock_id";
    private static final String COL_STOCK_NAME = "STOCK_NAME";
    private static final String COL_MEASURE_USED = "MEASURE_USED";

    //stocks_history
    private static final String tbl_stocks_history = "stocks_history";
    private static final String col_in_out = "in_out";
    //private static final String col_stock_id = "stock_id";
    //private static final String COL_STOCK_NAME = "STOCK_NAME";
    //private static final String col_qty = "qty";
    //private static final String COL_MEASURE_USED = "MEASURE_USED";
    private static final String col_time = "time";
    private static final String col_username = "username";
    private static final String col_cost = "cost";

    //category_tbl
    private static final String TBL_CATEGORY = "TBL_CATEGORY";
    //private static final String COL_CAT_ID = "CAT_ID";
    //private static final String COL_CAT_IMAGE = "CAT_IMAGE";
    //private static final String COL_CAT_NAME = "CAT_NAME";
    private static final String col_cat_id = "cat_id";
    private static final String col_cat_image = "cat_image";
    private static final String col_cat_name = "cat_name";

    //items
    private static final String tbl_items = "tbl_items";
    private static final String col_item_id = "item_id";
    //private static final String col_cat_id = "cat_id";
    private static final String col_item_name = "item_name";
    private static final String col_item_image = "item_image";
    private static final String col_item_selling_price = "item_selling_price";
    private static final String col_var_hdr_id = "var_hdr_id";
    private static final String col_stock_link_id = "stock_link_id";

    //variants_links
    private static final String tbl_variants_links = "variants_links";
    private static final String col_link_id = "link_id";
    //private static final String col_item_id = "item_id";
    //private static final String col_var_hdr_id = "var_hdr_id";

    //variants_hdr
    private static final String tbl_variants_hdr = "variants_hdr";
    //private static final String col_var_hdr_id = "var_hdr_id";
    private static final String col_var_hdr_image = "var_hdr_image";
    private static final String col_var_hdr_name = "var_hdr_name";


    //variants_details
    private static final String tbl_variants_dtls = "variants_dtls";
    //private static final String col_var_hdr_id = "var_hdr_id";
    private static final String col_var_dtls_id = "var_dtls_id";
    private static final String col_var_dtls_image = "var_dtls_image";
    private static final String col_var_dtls_name = "var_dtls_name";
    private static final String col_var_selling_price = "var_selling_price";
    private static final String col_var_dtls_default = "var_dtls_default";
    private static final String col_var_dtls_add_on = "var_dtls_add_on";

    //sales
    private static final String tbl_sales = "sales";
    private static final String col_transaction_id = "transaction_id";
    private static final String col_transaction_counter = "transaction_counter";
    private static final String col_transaction_per_entry = "transaction_per_entry";
    //private static final String col_item_id = "item_id";
    private static final String col_sort_order_id = "sort_order_id";
    private static final String col_machine_name = "machine_name";
    //private static final String col_var_hdr_id = "var_hdr_id"; // the only string id, to null the FB
    //private static final String col_var_dtls_id = "var_dtls_id"; // the only string id, to null the FB
    //private static final String col_item_name = "item_name";
    private static final String col_qty = "qty";
    private static final String col_selling_price = "selling_price";
    private static final String col_date = "date";
    private static final String col_created_by = "created_by";
    private static final String col_completed = "completed";
    private static final String col_dine_in_out = "dine_in_out";

    //changes
    private static final String tbl_changes = "changes";
    private static final String col_change_all = "change_all";
    private static final String col_stock_names = "stock_names";
    private static final String col_category = "category";
    private static final String col_items = "items";
    private static final String col_variants_links = "variants_links";
    private static final String col_variants_hdr = "variants_hdr";
    private static final String col_variants_dtls = "variants_dtls";
    private static final String col_composite_links = "composite_links";
    private static final String col_stock_histories = "stock_histories";



    //composite_links
    private static final String tbl_composite_links = "composite_links";
    private static final String col_composite_link_id = "composite_link_id";
    //private static final String col_item_id = "item_id";
    //private static final String col_stock_id = "stock_id";
    //private static final String col_var_hdr_id = "var_hdr_id";
    //private static final String col_var_dtls_id = "var_dtls_id";
    //private static final String col_qty = "qty";
    private static final String col_unit = "unit";

    //dine in out
    private static final String tbl_dine_in_out = "dine_in_out";
    //private static final String col_dine_in_out = "dine_in_out";


    public HelperDatabase(Context context) {
        super(context, DB_NAME, null, 65);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            //item_name
            String createTable = "CREATE TABLE " + TBL_STOCK_NAMES + " ( " + col_stock_id + " INTEGER "
                    + " ," + COL_STOCK_NAME + " TEXT "
                    + " ," + COL_MEASURE_USED + " TEXT "
                    + ")";
            db.execSQL(createTable);

            //stocks_history in_out, stock_name, qty, measure_used, time, username;
            createTable = "CREATE TABLE " + tbl_stocks_history + " ( " + col_in_out + " TEXT "
                    + " ," + col_stock_id + " INTEGER "
                    + " ," + COL_STOCK_NAME + " TEXT "
                    + " ," + col_qty + " NUMBER "
                    + " ," + COL_MEASURE_USED + " TEXT "
                    + " ," + col_time + " TEXT "
                    + " ," + col_username + " TEXT "
                    + " ," + col_cost + " TEXT "
                    + ")";
            db.execSQL(createTable);

            //category table
            createTable = "CREATE TABLE " + TBL_CATEGORY + " ( " + col_cat_id + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + " ," + col_cat_image + " TEXT "
                    + " ," + col_cat_name + " TEXT "
                    + ")";
            db.execSQL(createTable);

            //items table
            createTable = "CREATE TABLE " + tbl_items + " ( " + col_item_id + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + " ," + col_cat_id + " INTEGER "
                    + " ," + col_item_name + " TEXT "
                    + " ," + col_item_image + " TEXT "
                    + " ," + col_item_selling_price + " TEXT "
                    + " ," + col_var_hdr_id + " TEXT "
                    + " ," + col_stock_link_id + " TEXT "
                    + ")";
            db.execSQL(createTable);

            //variants_links
            createTable = "CREATE TABLE " + tbl_variants_links + " ( " + col_link_id + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + ", " + col_item_id + " INTEGER "
                    + ", " + col_var_hdr_id + " INTEGER "
                    + ")";
            db.execSQL(createTable);

            //variants_hdr
            createTable = "CREATE TABLE " + tbl_variants_hdr + " ( " + col_var_hdr_id + " INTEGER "
                    + " ," + col_var_hdr_image + " TEXT "
                    + " ," + col_var_hdr_name + " TEXT "
                    + ")";
            db.execSQL(createTable);

            //variants_dtls
            createTable = "CREATE TABLE " + tbl_variants_dtls + " ( " + col_var_hdr_id + " INTEGER "
                    + " ," + col_var_dtls_id + " INTEGER "
                    + " ," + col_var_dtls_image + " TEXT "
                    + " ," + col_var_dtls_name + " TEXT "
                    + " ," + col_var_selling_price + " TEXT "
                    + " ," + col_var_dtls_default + " TEXT "
                    + " ," + col_var_dtls_add_on + " TEXT "
                    + ")";
            db.execSQL(createTable);

            //sales
            createTable = "CREATE TABLE " + tbl_sales + " ( " + col_transaction_id + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + " ," + col_transaction_counter + " INTEGER "
                    + " ," + col_transaction_per_entry + " INTEGER "
                    + " ," + col_item_id + " INTEGER "
                    + " ," + col_sort_order_id + " INTEGER "
                    + " ," + col_machine_name + " TEXT "
                    + " ," + col_var_hdr_id + " TEXT "
                    + " ," + col_var_dtls_id + " TEXT "
                    + " ," + col_item_name + " TEXT "
                    + " ," + col_qty + " TEXT "
                    + " ," + col_selling_price + " TEXT "
                    + " ," + col_date + " TEXT "
                    + " ," + col_created_by + " TEXT "
                    + " ," + col_completed + " TEXT "
                    + " ," + col_dine_in_out + " TEXT "
                    + ")";
            db.execSQL(createTable);

            //changes
            createTable = "CREATE TABLE " + tbl_changes + " ( " + col_change_all + " TEXT "
                    + " ," + col_stock_names + " TEXT "
                    + " ," + col_category + " TEXT "
                    + " ," + col_items + " TEXT "
                    + " ," + col_variants_links + " TEXT "
                    + " ," + col_variants_hdr + " TEXT "
                    + " ," + col_variants_dtls + " TEXT "
                    + " ," + col_composite_links + " TEXT "
                    + " ," + col_stock_histories + " TEXT "
            + ")";
            db.execSQL(createTable);

            //composite_links
            createTable = "CREATE TABLE " + tbl_composite_links + " ( " + col_composite_link_id + " INTEGER "
                    + " ," + col_item_id + " INTEGER "
                    + " ," + col_stock_id + " INTEGER "
                    + " ," + col_var_hdr_id + " TEXT "
                    + " ," + col_var_dtls_id + " TEXT "
                    + " ," + col_qty + " TEXT "
                    + " ," + col_unit + " TEXT "
                    + ")";
            db.execSQL(createTable);

            //dine_in_out
            createTable = "CREATE TABLE " + tbl_dine_in_out + " ( " + col_dine_in_out + " TEXT "
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_STOCK_NAMES);
        db.execSQL("DROP TABLE IF EXISTS " + tbl_stocks_history);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + tbl_items);
        db.execSQL("DROP TABLE IF EXISTS " + tbl_variants_links);
        db.execSQL("DROP TABLE IF EXISTS " + tbl_variants_hdr);
        db.execSQL("DROP TABLE IF EXISTS " + tbl_variants_dtls);
        db.execSQL("DROP TABLE IF EXISTS " + tbl_sales);
        db.execSQL("DROP TABLE IF EXISTS " + tbl_changes);
        db.execSQL("DROP TABLE IF EXISTS " + tbl_composite_links);
        onCreate(db);
    }

    //STOCK_NAMES start
    public List<HelperStockNames> listStockNames(){

        List<HelperStockNames> stockNames = new LinkedList<HelperStockNames>();
        stockNames.clear();

        String query = "SELECT * FROM " + TBL_STOCK_NAMES;
        SQLiteDatabase db = this.getWritableDatabase();
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

    public void refreshStockNames(List<HelperStockNames> stockNames){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String query = "DELETE FROM " + TBL_STOCK_NAMES;
        db.execSQL(query);

        for (int i=0; i < stockNames.size(); i++) {
            contentValues.put(col_stock_id, stockNames.get(i).getStock_id());
            contentValues.put(COL_STOCK_NAME, stockNames.get(i).getStock_name());
            contentValues.put(COL_MEASURE_USED, stockNames.get(i).getMeasure_used());
            long result = db.insert(TBL_STOCK_NAMES, null, contentValues);
            if (result == -1) {
                Log.d("refreshStockNames", "failed insert");
            } else {
                Log.d("refreshStockNames", "success insert");
            }
        }

        db.close();
    }

    public boolean addStock(HelperStockNames stockNames){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(col_stock_id, stockNames.getStock_id());
        contentValues.put(COL_STOCK_NAME, stockNames.getStock_name());
        contentValues.put(COL_MEASURE_USED, stockNames.getMeasure_used());
        long result = db.insert(TBL_STOCK_NAMES, null, contentValues);
        db.close();

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String getStockNameMeasuredUsed(String stock_name){
        String measureUsed = "";
        String query = "SELECT " + COL_MEASURE_USED + " FROM " + TBL_STOCK_NAMES + " WHERE " + COL_STOCK_NAME + " = '" + stock_name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            measureUsed = cursor.getString(0);
        }

        db.close();
        return measureUsed;
    }

    public String getStockName(int stock_id){
        String measureUsed = "";
        String query = "SELECT " + COL_STOCK_NAME + " FROM " + TBL_STOCK_NAMES + " WHERE " + col_stock_id + " = '" + stock_id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            measureUsed = cursor.getString(0);
        }

        db.close();
        return measureUsed;
    }

    public void deleteStockNames(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TBL_STOCK_NAMES;
        db.execSQL(query);
        db.close();
    }
    //STOCK_NAMES end

    //STOCKS_HISTORY start
    public List<HelperStockHistory> listStocksHistory(){

        List<HelperStockHistory> helperStockHistories = new LinkedList<HelperStockHistory>();
        helperStockHistories.clear();

        String query = "SELECT * FROM " + tbl_stocks_history;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        HelperStockHistory helperStockHistory = null;

        //in_out, stock_name, qty, measure_used, time, username;

        if (cursor.moveToFirst()) {
            do {
                helperStockHistory = new HelperStockHistory();
                helperStockHistory.setIn_out(cursor.getString(0));
                helperStockHistory.setStock_id(Integer.parseInt(cursor.getString(1)));
                helperStockHistory.setStock_name(cursor.getString(2));
                helperStockHistory.setQty(cursor.getString(3));
                helperStockHistory.setMeasure_used(cursor.getString(4));
                helperStockHistory.setTime(cursor.getString(5));
                helperStockHistory.setUsername(cursor.getString(6));
                helperStockHistory.setCost(cursor.getString(7));
                helperStockHistories.add(helperStockHistory);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperStockHistories;
    }

    public void refreshStocksHistory(List<HelperStockHistory> listHelperStockHistories){
        deleteStocksHistory();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < listHelperStockHistories.size(); i++) {

            Log.d("refreshStockNames", listHelperStockHistories.get(i).getStock_id() + ">>" + listHelperStockHistories.get(i).getStock_name() + "--" + listHelperStockHistories.get(i).getMeasure_used());

            contentValues.put(col_in_out, listHelperStockHistories.get(i).getIn_out());
            contentValues.put(col_stock_id, listHelperStockHistories.get(i).getStock_id());
            contentValues.put(COL_STOCK_NAME, listHelperStockHistories.get(i).getStock_name());
            contentValues.put(col_qty, listHelperStockHistories.get(i).getQty());
            contentValues.put(COL_MEASURE_USED, listHelperStockHistories.get(i).getMeasure_used());
            contentValues.put(col_time, listHelperStockHistories.get(i).getTime());
            contentValues.put(col_username, listHelperStockHistories.get(i).getUsername());
            contentValues.put(col_cost, listHelperStockHistories.get(i).getCost());
            long result = db.insert(tbl_stocks_history, null, contentValues);
            if (result == -1) {
                Log.d("refreshStockNames", "failed insert");
            } else {
                Log.d("refreshStockNames", "success insert");
            }
        }

        db.close();
    }

    public boolean addStocksHistory(HelperStockHistory helperStockHistory){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(col_in_out, helperStockHistory.getIn_out());
        contentValues.put(COL_STOCK_NAME, helperStockHistory.getStock_name());
        contentValues.put(col_qty, helperStockHistory.getQty());
        contentValues.put(COL_MEASURE_USED, helperStockHistory.getMeasure_used());
        contentValues.put(col_time, helperStockHistory.getTime());
        contentValues.put(col_username, helperStockHistory.getUsername());
        long result = db.insert(TBL_STOCK_NAMES, null, contentValues);
        db.close();

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteStocksHistory(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tbl_stocks_history;
        db.execSQL(query);
        db.close();
    }

    public List<HelperStockHistory> listHelperStockHistRem(){

        List<HelperStockHistory> listHelperStockHistories = new LinkedList<HelperStockHistory>();
        listHelperStockHistories.clear();

        String query = "SELECT " + COL_STOCK_NAME
                + ", " + " SUM( CASE " + col_in_out + " WHEN 'Out' THEN -" + col_qty + " ELSE " + col_qty + " END) AS remaining "
                + ", " + COL_MEASURE_USED
                + " FROM " + tbl_stocks_history
                + " GROUP BY " + COL_STOCK_NAME
                + ", " + COL_MEASURE_USED;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        HelperStockHistory helperStockHistory = null;

        if (cursor.moveToFirst()) {
            do {
                helperStockHistory = new HelperStockHistory();
                helperStockHistory.setStock_name(cursor.getString(0));
                helperStockHistory.setQty(cursor.getString(1));
                helperStockHistory.setMeasure_used(cursor.getString(2));
                listHelperStockHistories.add(helperStockHistory);
            } while (cursor.moveToNext());
        }

        db.close();

        return listHelperStockHistories;
    }

    public boolean optionalComposite(){
        //can bypass outof stock with alert.
        //ex. no tissue, no tomatoe or catsup in burger.
        //add tag in tbl_stock_names, optional composite

        return false;
    }

    public boolean decQtyOnSale(){
        //add tag in tbl_stocks_names, decrease_qty_on_sale
        //product names like Food Panda, None FP, Grab Food will not insert "out" in tbl_stocks_history

        return false;
    }

    public boolean stocksOk(int stock_id, SQLiteDatabase db, int needed_qty, String needed_unit){
        String base_unit = "";

        //int check_unit = 0;
        /*

        String query_check_unit = "SELECT COUNT(DISTINCT " + COL_MEASURE_USED + ") cnt"
                + " FROM " + tbl_stocks_history
                + " WHERE " + col_stock_id + " = " + stock_id
                ;
        Cursor cursor_check_unit = db.rawQuery(query_check_unit, null);

        if (cursor_check_unit.moveToFirst()){
            check_unit = cursor_check_unit.getInt(0);
        }

        if(check_unit==0){
            return false;
        }
        */

        String query_base_unit = "SELECT " + COL_MEASURE_USED
                + " FROM " + TBL_STOCK_NAMES
                + " WHERE " + col_stock_id + " = " + stock_id;
        Cursor cursor_base_unit = db.rawQuery(query_base_unit, null);
        if (cursor_base_unit.moveToFirst()){
            base_unit = cursor_base_unit.getString(0);
        }

        if(!base_unit.equals("")){

            String query;

            if (base_unit.equals(needed_unit)){
                query = "SELECT SUM(CASE WHEN "+ col_in_out + " = 'Out' THEN -" + col_qty + " ELSE " + col_qty + " END) stocks"
                        + ", " + COL_MEASURE_USED
                        + " FROM " + tbl_stocks_history
                        + " WHERE " + col_stock_id + " = " + stock_id
                        + " GROUP BY " +  COL_MEASURE_USED
                ;
            } else {
                query = "SELECT SUM(CASE WHEN "+ col_in_out + " = 'Out' THEN -" + col_qty + " ELSE " + col_qty + " END) stocks"
                        + ", " + COL_MEASURE_USED
                        + " FROM " + tbl_stocks_history
                        + " WHERE " + col_stock_id + " = " + stock_id
                        + " AND " + COL_MEASURE_USED + " = '" + base_unit + "'"
                        + " GROUP BY " + COL_MEASURE_USED
                ;
            }


            Log.d("stocksOkActual", query);

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                //if (cursor.getString(1).equals(needed_unit)){
                if (base_unit.equals(needed_unit)){
                    //EXISTS start
                    int ordered_qty = 0;
                    String query_item = "SELECT SUM(a." + col_qty + ") " + col_qty
                            + " FROM " + tbl_composite_links + " a, " + tbl_sales + " b "
                            + " WHERE a." + col_stock_id + " = " + stock_id
                            + " AND a." + col_unit + " = '" + needed_unit + "' "
                            + " AND a." + col_item_id + " = b." + col_item_id
                            + " AND a." + col_var_hdr_id +  " is null "
                            + " AND a." + col_var_dtls_id + " is null "
                            + " AND b." + col_var_hdr_id +  " is null "
                            + " AND b." + col_var_dtls_id + " is null "
                            + " AND b." + col_created_by + " = 'admin'"
                            + " AND b." + col_machine_name + " = 'pos1'"
                            + " AND b." + col_completed + " IN ( 'W', 'N' )"
                            ;

                    String query_variants = "SELECT SUM(a." + col_qty + ") " + col_qty
                            + " FROM " + tbl_composite_links + " a, " + tbl_sales + " b "
                            + " WHERE a." + col_stock_id + " = " + stock_id
                            + " AND a." + col_unit + " = '" + needed_unit + "' "
                            //+ " AND a." + col_item_id + " = b." + col_item_id
                            + " AND a." + col_var_hdr_id +  " = b." + col_var_hdr_id
                            + " AND a." + col_var_dtls_id + " = b." + col_var_dtls_id
                            + " AND b." + col_created_by + " = 'admin'"
                            + " AND b." + col_machine_name + " = 'pos1'"
                            + " AND b." + col_completed + " IN ( 'W', 'N' )"
                            ;

                    String query_exists = "SELECT SUM(" + col_qty + ") AS " + col_qty
                            + " FROM "
                            + " ( "
                            + query_item + " union all " + query_variants
                            + " ) ";

                    Cursor cursor_exists = db.rawQuery(query_exists, null);
                    if (cursor_exists.moveToFirst()) {
                        ordered_qty = cursor_exists.getInt(0);
                    }

                    //EXISTS end

                    if (( cursor.getInt(0) - ordered_qty )>=needed_qty){
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (cursor.getInt(0)>0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean stocksOk(HelperItem helperItem){
        //check for item only
        String query = "SELECT " + col_stock_id
                + ", " + "SUM(" + col_qty + ") " + col_qty
                + ", " + col_unit
                + " FROM " + tbl_composite_links
                + " WHERE " + col_item_id + " = " + helperItem.getItem_id()
                + " AND " + col_var_hdr_id + " IS NULL "
                + " GROUP BY " + col_stock_id
                + ", " + col_unit
                ;

        Log.d("stocksOkchekcItems", query);

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Log.d("stocksOkchekcItems", "cursor.getInt(0)" + cursor.getInt(0));
                if (!stocksOk(cursor.getInt(0), db, cursor.getInt(1), cursor.getString(2))){
                    return false;
                }
                if (cursor.isLast()){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return false;
    }

    public boolean stocksOk(HelperItem helperItem, int item_id, int var_hdr_id){
        //check for items variants

        String query_item = "SELECT " + col_stock_id
                + ", " + "SUM(" + col_qty + ") " + col_qty
                + ", " + col_unit
                + " FROM " + tbl_composite_links
                + " WHERE " + col_item_id + " = " + item_id
                + " AND " + col_var_hdr_id +  " is null "
                + " AND " + col_var_dtls_id + " is null "
                + " GROUP BY " + col_stock_id
                + ", " + col_unit
                ;

        String query_variants = "SELECT " + col_stock_id
                + ", " + "SUM(" + col_qty + ") " + col_qty
                + ", " + col_unit
                + " FROM " + tbl_composite_links
                + " WHERE " + col_item_id + " = " + item_id
                + " AND " + col_var_hdr_id +  " = " + var_hdr_id
                + " AND " + col_var_dtls_id + " = " + helperItem.getItem_id()
                + " GROUP BY " + col_stock_id
                + ", " + col_unit
                ;

        String query = "SELECT " + col_stock_id
                + ", " + "SUM(" + col_qty + ") " + col_qty
                + ", " + col_unit
                + " FROM "
                + " ( "
                + query_item + " union all " + query_variants
                + " ) "
                + " GROUP BY " + col_stock_id
                + ", " + col_unit
                ;

        Log.d("stocksOk", "item with variants " + query);

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                if (!stocksOk(cursor.getInt(0), db, cursor.getInt(1), cursor.getString(2))){
                    return false;
                }
                if(cursor.isLast()){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return false;
    }

    public boolean stocksOk(HelperSales helperSale){
        String query;

        if (helperSale.getVar_hdr_id()!=null){
            String query_item = "SELECT " + col_stock_id
                    + ", " + "SUM(" + col_qty + ") " + col_qty
                    + ", " + col_unit
                    + " FROM " + tbl_composite_links
                    + " WHERE " + col_item_id + " = " + helperSale.getItem_id()
                    + " AND " + col_var_hdr_id + " IS NULL "
                    + " GROUP BY " + col_stock_id
                    + ", " + col_unit
                    ;

            String query_variants = "SELECT " + col_stock_id
                    + ", " + "SUM(" + col_qty + ") " + col_qty
                    + ", " + col_unit
                    + " FROM " + tbl_composite_links
                    + " WHERE " + col_item_id + " = " + helperSale.getItem_id()
                    + " AND " + col_var_hdr_id + " = " + helperSale.getVar_hdr_id()
                    + " AND " + col_var_dtls_id + " = " + helperSale.getVar_dtls_id()
                    + " GROUP BY " + col_stock_id
                    + ", " + col_unit
                    ;

            query = "SELECT " + col_stock_id
                    + ", " + "SUM(" + col_qty + ") " + col_qty
                    + ", " + col_unit
                    + " FROM "
                    + " ( "
                    + query_item + " union all " + query_variants
                    + " ) "
                    + " GROUP BY " + col_stock_id
                    + ", " + col_unit
            ;



        } else {
            query = "SELECT " + col_stock_id
                    + ", " + "SUM(" + col_qty + ") " + col_qty
                    + ", " + col_unit
                    + " FROM " + tbl_composite_links
                    + " WHERE " + col_item_id + " = " + helperSale.getItem_id()
                    + " AND " + col_var_hdr_id + " IS NULL "
                    + " GROUP BY " + col_stock_id
                    + ", " + col_unit
            ;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                if (!stocksOk(cursor.getInt(0), db, cursor.getInt(1), cursor.getString(2))){
                    return false;
                }
                if (cursor.isLast()){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return false;
    }

    public int stocksAvailable(HelperItem helperItem){
        //display only the minimum possible order


        //check for item only
        String query = "SELECT " + col_stock_id
                        + ", " + "SUM(" + col_qty + ") " + col_qty
                        + ", " + col_unit
                        + " FROM " + tbl_composite_links
                        + " WHERE " + col_item_id + " = " + helperItem.getItem_id()
                        + " AND " + col_var_hdr_id + " IS NULL "
                        + " GROUP BY " + col_stock_id
                        + ", " + col_unit
                        ;

        //REMAINING ORDERS query
        String query_sub0 = "SELECT " + col_stock_id
                            + ", SUM(  " + col_qty + " ) " + col_qty
                            + ", " + col_unit
                            + " FROM " + tbl_composite_links
                            + " WHERE " + col_item_id  + " =  " + helperItem.getItem_id()
                            + " AND " + col_var_hdr_id + " IS NULL "
                            + " GROUP BY " + col_stock_id
                            + ", " + col_unit
                            ;

        String query_sub1 = "SELECT (CASE WHEN a." + col_in_out + "= 'Out' THEN -a." + col_qty
                            + " ELSE a." + col_qty
                            + " END) " + col_qty
                            + ", a." + COL_MEASURE_USED + " " + col_unit
                            + ", a." + col_stock_id
                            + " FROM " + tbl_stocks_history + " a "
                            + ", ( " + query_sub0 + " ) b "
                            + " WHERE a." + col_stock_id + "= b." + col_stock_id
                            + " union all "
                            + " SELECT -b." + col_qty
                            + ", b." + col_unit
                            + ", b." + col_stock_id
                            + " FROM " + tbl_sales + " a "
                            + ", " + tbl_composite_links + " b "
                            + " WHERE 1=1 "
                            ///+ " AND b." + col_item_id + " = " + helperItem.getItem_id()
                            + " AND a." + col_completed + " IN ( 'W', 'N' )"
                            + " AND a." + col_item_id + " = b." + col_item_id
                            + " AND a." + col_var_hdr_id + " is NULL "
                            + " AND b." + col_var_hdr_id + " is NULL "
                            + " AND EXISTS ( SELECT 1 "
                            + " FROM " + tbl_composite_links + " aa "
                            + " WHERE b." + col_stock_id + " = aa." + col_stock_id
                            + " AND aa." + col_item_id + " = " + helperItem.getItem_id()
                            + " AND aa." + col_var_hdr_id + " is NULL ) "
                            ;

        String query_sub2 = "SELECT SUM( aa." + col_qty + " ) " + col_qty
                            + ", aa." + col_unit
                            + ", aa." + col_stock_id
                            + " FROM (" + query_sub1 + " ) aa "
                            + " GROUP BY aa." + col_stock_id
                            + ", aa." + col_unit
                            ;

        String query_sub31 = "SELECT "
                            + "(CASE WHEN x." + col_qty + "=0 THEN 1 ELSE x." + col_qty + " END) AS " + col_qty
                            + " FROM " + tbl_composite_links + " x "
                            + " WHERE x." + col_item_id + " = " + helperItem.getItem_id()
                            + " AND x." + col_var_hdr_id + " is null "
                            + " AND x." + col_stock_id + " = ww." + col_stock_id
                            ;

        String query_sub3 = "SELECT ww." + col_qty
                            + " / "
                            + " ( " + query_sub31 + " ) " + col_qty
                            + " FROM ( " + query_sub2 + " ) ww "
                            ;

        String query_sub4 = "SELECT MIN(uu." + col_qty + ")" + col_qty
                            + " FROM (" + query_sub3 + " ) uu"
                            ;

        Log.d("stocksOkRemainingOrders", query_sub4);

        SQLiteDatabase db = this.getWritableDatabase();

        //Cursor cursor = db.rawQuery(query, null);
        Cursor cursor = db.rawQuery(query_sub4, null);

        if (cursor.moveToFirst()) {
            //do {
            if (cursor.getString(0)==null){
                return 0;
            } else {
                return cursor.getInt(0);
            }

            //} while (cursor.moveToNext());
        }
        db.close();
        return 0;
    }

    public int stocksAvailable(HelperSales helperSales){
        //display only the minimum possible order

        HelperItem helperItem = new HelperItem();
        helperItem.setItem_id(Integer.parseInt(helperSales.getVar_dtls_id()));
        return ( stocksAvailable(helperItem, "" + helperSales.getItem_id(), helperSales.getVar_hdr_id()));

    }

    public int stocksAvailable(HelperItem helperItem, String item_id, String var_hdr_id){
        //display only the minimum possible order

        //REMAINING ORDERS query

        String query_sub0 = "SELECT " + col_stock_id
                                + ", SUM(  " + col_qty + " ) " + col_qty
                                + ", " + col_unit
                                + " FROM " + tbl_composite_links
                                + " WHERE " + col_item_id  + " =  " + item_id
                                + " AND " + col_var_hdr_id + " IS NULL "
                                + " GROUP BY " + col_stock_id
                                + ", " + col_unit
                                ;

        String query_sub100 = "SELECT " + col_stock_id
                                + ", SUM(  " + col_qty + " ) " + col_qty
                                + ", " + col_unit
                                + " FROM " + tbl_composite_links
                                + " WHERE " + col_item_id  + " =  " + item_id
                                + " AND " + col_var_hdr_id + " = " + var_hdr_id
                                + " AND " + col_var_dtls_id + " = " + helperItem.getItem_id()
                                + " GROUP BY " + col_stock_id
                                + ", " + col_unit
                                ;

        String query_sub1000 = "SELECT " + col_stock_id
                                + ", SUM( " + col_qty + " ) " + col_qty
                                + ", "  + col_unit
                                + " FROM ( " + query_sub0 + " UNION ALL " + query_sub100 + " ) "
                                + " GROUP BY " + col_stock_id
                                + ", " + col_unit
                                ;

        String query_sub101 = "SELECT (CASE WHEN a." + col_in_out + "= 'Out' THEN -a." + col_qty
                                + " ELSE a." + col_qty
                                + " END) " + col_qty
                                + ", a." + COL_MEASURE_USED + " " + col_unit
                                + ", a." + col_stock_id
                                + " FROM " + tbl_stocks_history + " a "
                                + ", ( " + query_sub1000 + " ) b "
                                + " WHERE a." + col_stock_id + "= b." + col_stock_id
                                + " union all "
                                + " SELECT -b." + col_qty
                                + ", b." + col_unit
                                + ", b." + col_stock_id
                                + " FROM " + tbl_sales + " a "
                                + ", " + tbl_composite_links + " b "
                                + " WHERE a." + col_item_id + " = b." + col_item_id
                                + " AND a." + col_completed + " IN ( 'W', 'N' )"
                                + " AND a." + col_var_hdr_id + " = b." + col_var_hdr_id
                                + " AND a." + col_var_dtls_id + " = b." + col_var_dtls_id
                                + " AND EXISTS ( SELECT 1 "
                                + " FROM " + tbl_composite_links + " aa "
                                + " WHERE b." + col_stock_id + " = aa." + col_stock_id
                                + " AND aa." + col_item_id + " = " + item_id
                                + " AND aa." + col_var_hdr_id + " = " + var_hdr_id
                                + " AND aa." + col_var_dtls_id + " = " + helperItem.getItem_id()
                                + " ) "
                                + " union all "
                                + " SELECT -b." + col_qty
                                + ", b." + col_unit
                                + ", b." + col_stock_id
                                + " FROM " + tbl_sales + " a "
                                + ", " + tbl_composite_links + " b "
                                + " WHERE 1=1 "
                                + " AND a." + col_completed + " IN ( 'W', 'N' )"
                                + " AND a." + col_item_id + " = b." + col_item_id
                                + " AND a." + col_var_hdr_id + " is NULL "
                                + " AND b." + col_var_hdr_id + " is NULL "
                                + " AND EXISTS ( SELECT 1 "
                                + " FROM " + tbl_composite_links + " aa "
                                + " WHERE b." + col_stock_id + " = aa." + col_stock_id
                                + " AND aa." + col_item_id + " = " + item_id
                                + " AND aa." + col_var_hdr_id + " is NULL ) "
                                ;

        String query_sub102 = "SELECT SUM( aa." + col_qty + " ) " + col_qty
                                + ", aa." + col_unit
                                + ", aa." + col_stock_id
                                + " FROM (" + query_sub101 + " ) aa "
                                + " GROUP BY aa." + col_stock_id
                                + ", aa." + col_unit
                                ;

        String query_sub31 = "SELECT "
                                + "(CASE WHEN x." + col_qty + "=0 THEN 1 ELSE x." + col_qty + " END) AS " + col_qty
                                //+ col_qty
                                + " FROM " + tbl_composite_links + " x "
                                + " WHERE x." + col_item_id + " = " + item_id
                                + " AND x." + col_var_hdr_id + " is null "
                                + " AND x." + col_stock_id + " = ww." + col_stock_id
                                ;

        String query_sub131 = "SELECT "
                                ///+ "(CASE WHEN x." + col_qty + "=0 THEN 1 ELSE x." + col_qty + " END) AS " + col_qty
                                + col_qty
                                + " FROM " + tbl_composite_links + " x "
                                + " WHERE x." + col_item_id + " = " + item_id
                                + " AND x." + col_var_hdr_id + " = " + var_hdr_id
                                + " AND x." + col_var_dtls_id + " = " + helperItem.getItem_id()
                                + " AND x." + col_stock_id + " = ww." + col_stock_id
                                ;

        String query_1031 = "SELECT SUM(" + col_qty + " ) " + col_qty
                                + " FROM (" + query_sub31 + " UNION ALL " + query_sub131 + " ) ";

        String query_sub103 = "SELECT ww." + col_qty
                                + " / "
                                + " ( " + query_1031 + " ) " + col_qty
                                + " FROM ( " + query_sub102 + " ) ww "
                                ;

        String query_sub104 = "SELECT MIN(uu." + col_qty + ")" + col_qty
                                + " FROM (" + query_sub103 + " ) uu"
                                ;

        Log.d("stocksOkRemainingOrders", query_sub104);

        SQLiteDatabase db = this.getWritableDatabase();

        //Cursor cursor = db.rawQuery(query, null);
        Cursor cursor = db.rawQuery(query_sub104, null);

        if (cursor.moveToFirst()) {
            //do {
                if (cursor.getString(0)==null){
                    return 0;
                } else {
                    return cursor.getInt(0);
                }

            //} while (cursor.moveToNext());
        }
        db.close();
        return 0;
    }


    public List<String> listOfStocks(HelperItem helperItem){
        List<String> listStockNames = new LinkedList<>();
        listStockNames.clear();
        String stockName;

        //REMAINING ORDERS query
        String query_sub0 = "SELECT " + col_stock_id
                + ", SUM(  " + col_qty + " ) " + col_qty
                + ", " + col_unit
                + " FROM " + tbl_composite_links
                + " WHERE " + col_item_id  + " =  " + helperItem.getItem_id()
                + " AND " + col_var_hdr_id + " IS NULL "
                + " GROUP BY " + col_stock_id
                + ", " + col_unit
                ;

        String query_sub1 = "SELECT (CASE WHEN a." + col_in_out + "= 'Out' THEN -a." + col_qty
                + " ELSE a." + col_qty
                + " END) " + col_qty
                + ", a." + COL_MEASURE_USED + " " + col_unit
                + ", a." + col_stock_id
                + " FROM " + tbl_stocks_history + " a "
                + ", ( " + query_sub0 + " ) b "
                + " WHERE a." + col_stock_id + "= b." + col_stock_id
                + " union all "
                + " SELECT -b." + col_qty
                + ", b." + col_unit
                + ", b." + col_stock_id
                + " FROM " + tbl_sales + " a "
                + ", " + tbl_composite_links + " b "
                + " WHERE 1=1 "
                + " AND a." + col_completed + " IN ( 'W', 'N' )"
                + " AND a." + col_item_id + " = b." + col_item_id
                + " AND a." + col_var_hdr_id + " is NULL "
                + " AND b." + col_var_hdr_id + " is NULL "
                + " AND EXISTS ( SELECT 1 "
                + " FROM " + tbl_composite_links + " aa "
                + " WHERE b." + col_stock_id + " = aa." + col_stock_id
                + " AND aa." + col_item_id + " = " + helperItem.getItem_id()
                + " AND aa." + col_var_hdr_id + " is NULL ) "
                ;

        String query_sub2 = "SELECT SUM( aa." + col_qty + " ) " + col_qty
                + ", aa." + col_unit
                + ", aa." + col_stock_id
                + " FROM (" + query_sub1 + " ) aa "
                + " GROUP BY aa." + col_stock_id
                + ", aa." + col_unit
                ;

        String query_sub31 = "SELECT "
                + "(CASE WHEN x." + col_qty + "=0 THEN 1 ELSE x." + col_qty + " END) AS " + col_qty
                + " FROM " + tbl_composite_links + " x "
                + " WHERE x." + col_item_id + " = " + helperItem.getItem_id()
                + " AND x." + col_stock_id + " = ww." + col_stock_id
                ;

        String query_sub32 = "SELECT " + COL_STOCK_NAME
                + " FROM " + TBL_STOCK_NAMES
                + " WHERE " + col_stock_id + " = ww." + col_stock_id
                ;

        String query_sub3 = "SELECT ww." + col_qty
                + " / "
                + " ( " + query_sub31 + " ) orders "
                + ", " + " ( " + query_sub32 + " ) stock_name "
                + ", ww." + col_qty
                + ", ww." + col_unit
                + ", ww." + col_stock_id
                + " FROM ( " + query_sub2 + " ) ww "
                ;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query_sub3, null);
        if (cursor.moveToFirst()) {
            do {
                stockName = cursor.getString(0) + " order(s) rem. - " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3) + " current stocks";
                if(stockName!=null){
                    listStockNames.add(stockName);
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return listStockNames;

    }

    public List<String> listOfStocks(HelperItem helperItem, String item_id, String var_hdr_id){
        List<String> listStockNames = new LinkedList<>();
        listStockNames.clear();
        String stockName;

        //REMAINING ORDERS query
        String query_sub0 = "SELECT " + col_stock_id
                + ", SUM(  " + col_qty + " ) " + col_qty
                + ", " + col_unit
                + " FROM " + tbl_composite_links
                + " WHERE " + col_item_id  + " =  " + item_id
                + " AND " + col_var_hdr_id + " = " + var_hdr_id
                + " AND " + col_var_dtls_id + " = " + helperItem.getItem_id()
                + " GROUP BY " + col_stock_id
                + ", " + col_unit
                ;

        String query_sub1 = "SELECT (CASE WHEN a." + col_in_out + "= 'Out' THEN -a." + col_qty
                + " ELSE a." + col_qty
                + " END) " + col_qty
                + ", a." + COL_MEASURE_USED + " " + col_unit
                + ", a." + col_stock_id
                + " FROM " + tbl_stocks_history + " a "
                + ", ( " + query_sub0 + " ) b "
                + " WHERE a." + col_stock_id + "= b." + col_stock_id
                + " union all "
                + " SELECT -b." + col_qty
                + ", b." + col_unit
                + ", b." + col_stock_id
                + " FROM " + tbl_sales + " a "
                + ", " + tbl_composite_links + " b "
                + " WHERE 1=1 "
                + " AND a." + col_completed + " IN ( 'W', 'N' )"
                + " AND a." + col_item_id + " = b." + col_item_id
                + " AND a." + col_var_hdr_id + " = b." + col_var_hdr_id
                + " AND a." + col_var_dtls_id + " = b." + col_var_dtls_id
                + " AND EXISTS ( SELECT 1 "
                + " FROM " + tbl_composite_links + " aa "
                + " WHERE b." + col_stock_id + " = aa." + col_stock_id
                + " AND aa." + col_item_id + " = " + item_id
                + " AND aa." + col_var_hdr_id + " = " +  var_hdr_id
                + " AND aa." + col_var_dtls_id + " = " + helperItem.getItem_id()
                + " ) "
                ;

        String query_sub2 = "SELECT SUM( aa." + col_qty + " ) " + col_qty
                + ", aa." + col_unit
                + ", aa." + col_stock_id
                + " FROM (" + query_sub1 + " ) aa "
                + " GROUP BY aa." + col_stock_id
                + ", aa." + col_unit
                ;

        String query_sub31 = "SELECT "
                + "(CASE WHEN x." + col_qty + "=0 THEN 1 ELSE x." + col_qty + " END) AS " + col_qty
                + " FROM " + tbl_composite_links + " x "
                + " WHERE x." + col_item_id + " = " + item_id
                + " AND x." + col_var_hdr_id + " = " + var_hdr_id
                + " AND x." + col_var_dtls_id + " = " + helperItem.getItem_id()
                + " AND x." + col_stock_id + " = ww." + col_stock_id
                ;

        String query_sub32 = "SELECT " + COL_STOCK_NAME
                + " FROM " + TBL_STOCK_NAMES
                + " WHERE " + col_stock_id + " = ww." + col_stock_id
                ;

        String query_sub3 = "SELECT ww." + col_qty
                + " / "
                + " ( " + query_sub31 + " ) orders "
                + ", " + " ( " + query_sub32 + " ) stock_name "
                + ", ww." + col_qty
                + ", ww." + col_unit
                + ", ww." + col_stock_id
                + " FROM ( " + query_sub2 + " ) ww "
                ;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query_sub3, null);
        if (cursor.moveToFirst()) {
            do {
                stockName = cursor.getString(0) + " order(s) rem. - " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3) + " current stocks";
                if(stockName!=null){
                    listStockNames.add(stockName);
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return listStockNames;

    }


    public String outOfStockName(int stock_id, SQLiteDatabase db, int needed_qty, String needed_unit) {

        int check_unit = 0;
        String base_unit = "";
        String query_check_unit = "SELECT COUNT(DISTINCT " + COL_MEASURE_USED + ") cnt"
                + " FROM " + tbl_stocks_history
                + " WHERE " + col_stock_id + " = " + stock_id;
        Cursor cursor_check_unit = db.rawQuery(query_check_unit, null);

        if (cursor_check_unit.moveToFirst()) {
            check_unit = cursor_check_unit.getInt(0);
            Log.d("outOfStockName", "" + check_unit);
            if (check_unit == 0) {
                String query_name = "SELECT " + COL_STOCK_NAME + " FROM " + TBL_STOCK_NAMES + " WHERE " + col_stock_id + " = '" + stock_id + "'";
                Cursor cursor_name = db.rawQuery(query_name, null);
                if (cursor_name.moveToFirst()) {
                    return cursor_name.getString(0) + " (0)stocks";
                }

            }
        }

        String query_base_unit = "SELECT " + COL_MEASURE_USED
                + " FROM " + TBL_STOCK_NAMES
                + " WHERE " + col_stock_id + " = " + stock_id;
        Cursor cursor_base_unit = db.rawQuery(query_base_unit, null);
        if (cursor_base_unit.moveToFirst()) {
            base_unit = cursor_base_unit.getString(0);
        }

        if (!base_unit.equals("")){

            String query;

            if (base_unit.equals(needed_unit)) {
                query = "SELECT SUM(CASE WHEN " + col_in_out + " = 'Out' THEN -" + col_qty + " ELSE " + col_qty + " END) stocks"
                        + ", " + COL_STOCK_NAME
                        + ", " + COL_MEASURE_USED
                        + " FROM " + tbl_stocks_history
                        + " WHERE " + col_stock_id + " = " + stock_id
                        + " GROUP BY " + COL_STOCK_NAME
                        + ", " + COL_MEASURE_USED
                ;
            } else {
                query = "SELECT SUM(CASE WHEN " + col_in_out + " = 'Out' THEN -" + col_qty + " ELSE " + col_qty + " END) stocks"
                        + ", " + COL_STOCK_NAME
                        + ", " + COL_MEASURE_USED
                        + " FROM " + tbl_stocks_history
                        + " WHERE " + col_stock_id + " = " + stock_id
                        + " AND " + COL_MEASURE_USED + " = '" + base_unit + "'"
                        + " GROUP BY " + COL_STOCK_NAME
                        + ", " + COL_MEASURE_USED
                ;
            }

            Log.d("outOfStockName", "" + query);

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                //if (cursor.getString(2).equals(needed_unit)){
                if (base_unit.equals(needed_unit)) {
                    //EXISTS start
                    int ordered_qty = 0;
                    String query_item = "SELECT SUM(a." + col_qty + ") " + col_qty
                            + " FROM " + tbl_composite_links + " a, " + tbl_sales + " b "
                            + " WHERE a." + col_stock_id + " = " + stock_id
                            + " AND a." + col_unit + " = '" + needed_unit + "' "
                            + " AND a." + col_item_id + " = b." + col_item_id
                            + " AND a." + col_var_hdr_id + " is null "
                            + " AND a." + col_var_dtls_id + " is null "
                            + " AND b." + col_var_hdr_id + " is null "
                            + " AND b." + col_var_dtls_id + " is null "
                            + " AND b." + col_created_by + " = 'admin'"
                            + " AND b." + col_machine_name + " = 'pos1'"
                            + " AND b." + col_completed + " IN ( 'W', 'N' )";

                    String query_variants = "SELECT SUM(a." + col_qty + ") " + col_qty
                            + " FROM " + tbl_composite_links + " a, " + tbl_sales + " b "
                            + " WHERE " + col_stock_id + " = " + stock_id
                            //+ " AND a." + col_unit + " = '" + needed_unit + "' "
                            + " AND a." + col_item_id + " = b." + col_item_id
                            + " AND a." + col_var_hdr_id + " = b." + col_var_hdr_id
                            + " AND a." + col_var_dtls_id + " = b." + col_var_dtls_id
                            + " AND b." + col_created_by + " = 'admin'"
                            + " AND b." + col_machine_name + " = 'pos1'"
                            + " AND b." + col_completed + " IN ( 'W', 'N' )";

                    String query_exists = "SELECT SUM(" + col_qty + ") AS " + col_qty
                            + " FROM "
                            + " ( "
                            + query_item + " union all " + query_variants
                            + " ) ";

                    Cursor cursor_exists = db.rawQuery(query_exists, null);
                    if (cursor_exists.moveToFirst()) {
                        ordered_qty = cursor_exists.getInt(0);
                    }

                    //EXISTS end

                    if ((cursor.getInt(0) - ordered_qty) >= needed_qty) {
                        return null;
                    } else {
                        return cursor.getString(1) + " need(" + needed_qty + ") cart(" + ordered_qty + ") stocks(" + cursor.getInt(0) + ")";
                    }
                } else {
                    if (cursor.getInt(0) > 0) {
                        return null;
                    } else {
                        return cursor.getString(1) + " need(" + needed_qty + ") stocks(" + cursor.getInt(0) + ")";
                    }
                }
            }
        }

        return null;
    }


    public List<String> listOutOfStocks(HelperItem helperItem){
        List<String> listOutStockNames = new LinkedList<>();
        listOutStockNames.clear();
        String stockName;

        //check for item only
        String query = "SELECT " + col_stock_id
                + ", " + "SUM(" + col_qty + ") " + col_qty
                + ", " + col_unit
                + " FROM " + tbl_composite_links
                + " WHERE " + col_item_id + " = " + helperItem.getItem_id()
                + " AND " + col_var_hdr_id + " IS NULL "
                + " GROUP BY " + col_stock_id
                + ", " + col_unit
                ;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                stockName = outOfStockName(cursor.getInt(0), db, cursor.getInt(1), cursor.getString(2));
                if(stockName!=null){
                    listOutStockNames.add(stockName);
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return listOutStockNames;
    }

    public List<String> listOutOfStocks(HelperItem helperItem, int item_id, int var_hdr_id){
        List<String> listOutStockNames = new LinkedList<>();
        listOutStockNames.clear();
        String stockName;

        //check for items variants
        String query_item = "SELECT " + col_stock_id
                + ", " + "SUM(" + col_qty + ") " + col_qty
                + ", " + col_unit
                + " FROM " + tbl_composite_links
                + " WHERE " + col_item_id + " = " + item_id
                + " AND " + col_var_hdr_id +  " is null "
                + " AND " + col_var_dtls_id + " is null "
                + " GROUP BY " + col_stock_id
                + ", " + col_unit
                ;

        String query_variants = "SELECT " + col_stock_id
                + ", " + "SUM(" + col_qty + ") " + col_qty
                + ", " + col_unit
                + " FROM " + tbl_composite_links
                + " WHERE " + col_item_id + " = " + item_id
                + " AND " + col_var_hdr_id +  " = " + var_hdr_id
                + " AND " + col_var_dtls_id + " = " + helperItem.getItem_id()
                + " GROUP BY " + col_stock_id
                + ", " + col_unit
                ;

        String query = "SELECT " + col_stock_id
                + ", " + "SUM(" + col_qty + ") " + col_qty
                + ", " + col_unit
                + " FROM "
                + " ( "
                + query_item + " union all " + query_variants
                + " ) "
                + " GROUP BY " + col_stock_id
                + ", " + col_unit
                ;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Log.d("listOutOfStocks", "cursor1=" + cursor.getInt(0) + " cursor2=" + cursor.getInt(1) + " curosr3=" + cursor.getString(2));
                stockName = outOfStockName(cursor.getInt(0), db, cursor.getInt(1), cursor.getString(2));
                if(stockName!=null){
                    listOutStockNames.add(stockName);
                }
            } while (cursor.moveToNext());

        }
        db.close();
        return listOutStockNames;
    }

    public List<String> listOutOfStocks(HelperSales helperSale){
        List<String> listOutStockNames = new LinkedList<>();
        listOutStockNames.clear();
        String stockName;

        String query;
        if (helperSale.getVar_hdr_id()!=null){
            String query_item = "SELECT " + col_stock_id
                    + ", " + "SUM(" + col_qty + ") " + col_qty
                    + ", " + col_unit
                    + " FROM " + tbl_composite_links
                    + " WHERE " + col_item_id + " = " + helperSale.getItem_id()
                    + " AND " + col_var_hdr_id + " is null "
                    + " AND " + col_var_dtls_id + " is null "
                    + " GROUP BY " + col_stock_id
                    + ", " + col_unit
                    ;

            String query_variants = "SELECT " + col_stock_id
                    + ", " + "SUM(" + col_qty + ") " + col_qty
                    + ", " + col_unit
                    + " FROM " + tbl_composite_links
                    + " WHERE " + col_item_id + " = " + helperSale.getItem_id()
                    + " AND " + col_var_hdr_id + " = " + helperSale.getVar_hdr_id()
                    + " AND " + col_var_dtls_id + " = " + helperSale.getVar_dtls_id()
                    + " GROUP BY " + col_stock_id
                    + ", " + col_unit
                    ;

            query = "SELECT " + col_stock_id
                    + ", " + "SUM(" + col_qty + ") " + col_qty
                    + ", " + col_unit
                    + " FROM "
                    + " ( "
                    + query_item + " union all " + query_variants
                    + " ) "
                    + " GROUP BY " + col_stock_id
                    + ", " + col_unit
            ;
        } else {
            query = "SELECT " + col_stock_id
                    + ", " + "SUM(" + col_qty + ") " + col_qty
                    + ", " + col_unit
                    + " FROM " + tbl_composite_links
                    + " WHERE " + col_item_id + " = " + helperSale.getItem_id()
                    + " AND " + col_var_hdr_id + " IS NULL "
                    + " GROUP BY " + col_stock_id
                    + ", " + col_unit
            ;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                stockName = outOfStockName(cursor.getInt(0), db, cursor.getInt(1), cursor.getString(2));
                if(stockName!=null){
                    listOutStockNames.add(stockName);
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return listOutStockNames;
    }

    public int priceInItemMenu(HelperItem helperItem){
        String query = "SELECT MAX(a." + col_var_selling_price
                + ") "
                + " FROM " + tbl_variants_dtls + " a, " + tbl_variants_links + " b "
                + " WHERE a." + col_var_hdr_id + " = b." + col_var_hdr_id
                + " AND b." + col_item_id + " = " + helperItem.getItem_id();
                ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int getStockId(String stock_name){

        String query = "SELECT " + col_stock_id
                + " FROM " + TBL_STOCK_NAMES
                + " WHERE " + COL_STOCK_NAME + " = '" + stock_name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }



    public List<HelperStockHistory> listStockUsedInPreFinishSale(List<HelperSales> helperSales){
        List<HelperStockHistory> helperStockHistories = new LinkedList<>();
        helperStockHistories.clear();
        String query;
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i = 0; i < helperSales.size(); i++){
            if(helperSales.get(i).getVar_hdr_id()==null){
                query = "SELECT " + col_stock_id
                        + ", " + "SUM(" + col_qty + ") " + col_qty
                        + ", " + col_unit
                        + " FROM " + tbl_composite_links
                        + " WHERE " + col_item_id + " = " + helperSales.get(i).getItem_id()
                        + " AND " + col_var_hdr_id + " is null "
                        + " AND " + col_var_dtls_id + " is null "
                        + " GROUP BY " + col_stock_id
                        + ", " + col_unit
                        ;
            } else {
                query = "SELECT " + col_stock_id
                        + ", " + "SUM(" + col_qty + ") " + col_qty
                        + ", " + col_unit
                        + " FROM " + tbl_composite_links
                        + " WHERE " + col_item_id + " = " + helperSales.get(i).getItem_id()
                        + " AND " + col_var_hdr_id + " = " + helperSales.get(i).getVar_hdr_id()
                        + " AND " + col_var_dtls_id + " = " + helperSales.get(i).getVar_dtls_id()
                        + " GROUP BY " + col_stock_id
                        + ", " + col_unit
                        ;
            }
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToFirst()){
                do{
                    HelperStockHistory helperStockHistory = new HelperStockHistory();
                    helperStockHistory.setStock_id(cursor.getInt(0));
                    helperStockHistory.setIn_out("Out");

                    String query_stock_name = "SELECT " + COL_STOCK_NAME + " FROM " + TBL_STOCK_NAMES + " WHERE " + col_stock_id + " = '" + cursor.getInt(0) + "'";
                    Cursor cursorstock_name = db.rawQuery(query_stock_name, null);
                    if(cursorstock_name.moveToFirst()){
                        helperStockHistory.setStock_name(cursorstock_name.getString(0));
                    }

                    helperStockHistory.setQty(cursor.getString(1));
                    helperStockHistory.setMeasure_used(cursor.getString(2));
                    helperStockHistory.setTime(helperSales.get(i).getDate());
                    helperStockHistory.setUsername(helperSales.get(i).getCreated_by());
                    helperStockHistories.add(helperStockHistory);

                } while (cursor.moveToNext());
            }
        }

        db.close();

        return helperStockHistories;

    }

    //STOCKS_HISTORY end

    //CATEGORY start
    public List<HelperCategory> listCategory(){

        List<HelperCategory> categories = new LinkedList<HelperCategory>();
        categories.clear();

        String query = "SELECT * FROM " + TBL_CATEGORY + " a "
                + " WHERE EXISTS ("
                + " SELECT 1 FROM " + tbl_items + " b "
                + " WHERE a." + col_cat_id + " = b." + col_cat_id
                + ") "
                ;
        SQLiteDatabase db = this.getWritableDatabase();
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

    public int categoryMaxId(){

        String query = "SELECT MAX(CAT_ID) FROM " + TBL_CATEGORY;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int categoryCount(){

        String query = "SELECT COUNT(*) FROM " + TBL_CATEGORY;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public boolean addCategory(HelperCategory helperCategory){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_cat_id, helperCategory.getCat_id());
        contentValues.put(col_cat_image, helperCategory.getCat_image());
        contentValues.put(col_cat_name, helperCategory.getCat_name());
        long result = db.insert(TBL_CATEGORY, null, contentValues);
        db.close();

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean existsCategory(int cat_id) {

        String query = "SELECT * FROM " + TBL_CATEGORY + " WHERE " + col_cat_id + " = " + cat_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.close();
            Log.d("Record Already Exists", query);
            return true;
        }
        Log.d("New Record  ", query);
        db.close();

        return false;

        //SQLiteDatabase db = this.getWritableDatabase();
        //return DatabaseUtils.longForQuery(db, "select count(*) from " + TBL_CATEGORY + " where ID=? limit 1", new String[]{"0"}) > 0;

        /*
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TBL_CATEGORY + " WHERE " + COL_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
        */

        /*
        try
        {
            SQLiteDatabase db=this.getReadableDatabase();
            Cursor cursor=db.rawQuery("SELECT "+COL_ID+" FROM "+TBL_CATEGORY+" WHERE "+COL_ID+"=?", new String[]{id});
            if (cursor.moveToFirst())
            {
                db.close();
                Log.d("Record  Already Exists", "Table is:"+TBL_CATEGORY+" ColumnName:"+COL_ID);
                return true;//record Exists

            }
            Log.d("New Record  ", "Table is:"+TBL_CATEGORY+" ColumnName:"+COL_ID+" Column Value:"+id);
            db.close();
        }
        catch(Exception errorException)
        {
            Log.d("Exception occured", "Exception occured "+errorException);
            // db.close();
        }

        return false;

         */


    }

    public void updateCategory(int cat_id, String cat_image, String cat_name){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE " + TBL_CATEGORY + " SET "
                + col_cat_image + " = '" + cat_image + "' "
                + ", " + col_cat_name + " = '" + cat_name + "' "
                + " WHERE " + col_cat_id + " = " + cat_id;
        Log.d("updateCategory", query);
        db.execSQL(query);
    }

    public void deleteCategory(int cat_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TBL_CATEGORY
                +" WHERE " + col_cat_id + " = " + cat_id;
        Log.d("deleteCategory", query);
        db.execSQL(query);
        db.close();
    }

    public void deleteAllCategory(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TBL_CATEGORY;
        db.execSQL(query);
    }

    public void refreshCategory(List<HelperCategory> categories){

        deleteAllCategory();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < categories.size(); i++) {
            contentValues.put(col_cat_id, categories.get(i).getCat_id());
            contentValues.put(col_cat_image, categories.get(i).getCat_image());
            contentValues.put(col_cat_name, categories.get(i).getCat_name());
            long result = db.insert(TBL_CATEGORY, null, contentValues);
            if (result == -1) {
                Log.d("refreshCategory", "failed insert");
            } else {
                Log.d("refreshCategory", "success insert");
            }
        }

        db.close();

    }

    //CATEGORY end

    //ITEMS start
    public void refreshItems(List<HelperItem> helperItems){

        deleteAllItems();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < helperItems.size(); i++) {
            contentValues.put(col_item_id, helperItems.get(i).getItem_id());
            contentValues.put(col_cat_id, helperItems.get(i).getCat_id());
            contentValues.put(col_item_name, helperItems.get(i).getItem_name());
            contentValues.put(col_item_image, helperItems.get(i).getItem_image());
            contentValues.put(col_item_selling_price, helperItems.get(i).getItem_selling_price());
            contentValues.put(col_var_hdr_id, helperItems.get(i).getVar_hdr_id());
            contentValues.put(col_stock_link_id, helperItems.get(i).getStock_hdr_id());
            long result = db.insert(tbl_items, null, contentValues);
            if (result == -1) {
                Log.d("refreshItems", "failed insert");
            } else {
                Log.d("refreshItems", "success insert");
            }
        }

        db.close();

    }

    public void deleteAllItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tbl_items;
        db.execSQL(query);
    }

    public List<HelperItem> listItems(){

        List<HelperItem> helperItems = new LinkedList<HelperItem>();
        helperItems.clear();

        String query = "SELECT * FROM " + tbl_items;
        SQLiteDatabase db = this.getWritableDatabase();
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

    public List<HelperItem> listItems(int cat_id){

        List<HelperItem> helperItems = new LinkedList<HelperItem>();
        helperItems.clear();

        String query = "SELECT * FROM " + tbl_items + " WHERE " + col_cat_id + " = " + cat_id;
        SQLiteDatabase db = this.getWritableDatabase();
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
    //ITEMS end

    //VARIANTS_LINKS start
    public void refreshVariantsLinks(List<HelperVariantsLinks> helperVariantsLinks){

        deleteAllVariantsLinks();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < helperVariantsLinks.size(); i++) {
            contentValues.put(col_item_id, helperVariantsLinks.get(i).getItem_id());
            contentValues.put(col_var_hdr_id, helperVariantsLinks.get(i).getVar_hdr_id());
            long result = db.insert(tbl_variants_links, null, contentValues);
            if (result == -1) {
                Log.d("refreshVariantsLinks", "failed insert");
            } else {
                Log.d("refreshVariantsLinks", "success insert");
            }
        }

        db.close();

    }

    public void deleteAllVariantsLinks(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tbl_variants_links;
        db.execSQL(query);
    }

    public List<HelperVariantsLinks> listVariantsLinks(){

        List<HelperVariantsLinks> helperVariantsLinks = new LinkedList<HelperVariantsLinks>();
        helperVariantsLinks.clear();

        String query = "SELECT * FROM " + tbl_variants_links;
        SQLiteDatabase db = this.getWritableDatabase();
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

    public List<HelperVariantsLinks> listVariantsLinks(int item_id){

        List<HelperVariantsLinks> helperVariantsLinks = new LinkedList<HelperVariantsLinks>();
        helperVariantsLinks.clear();

        String query = "SELECT * FROM " + tbl_variants_links + " WHERE " + col_item_id + " = " + item_id;
        SQLiteDatabase db = this.getWritableDatabase();
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

    public boolean variantsExists(int item_id){
        String query = "SELECT * FROM " + tbl_variants_links + " WHERE " + col_item_id + " = " + item_id;
        SQLiteDatabase db = this.getWritableDatabase();
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
    //VARIANTS_LINKS end

    //VARIANTS_HDR start
    public void refreshVariantsHdr(List<HelperVariantsHdr> helperVariantsHdr){

        deleteAllVariantsHdr();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < helperVariantsHdr.size(); i++) {
            contentValues.put(col_var_hdr_id, helperVariantsHdr.get(i).getVar_hdr_id());
            contentValues.put(col_var_hdr_image, helperVariantsHdr.get(i).getVar_hdr_image());
            contentValues.put(col_var_hdr_name, helperVariantsHdr.get(i).getVar_hdr_name());
            long result = db.insert(tbl_variants_hdr, null, contentValues);
            if (result == -1) {
                Log.d("refreshVariantsHdr", "failed insert");
            } else {
                Log.d("refreshVariantsHdr", "success insert");
            }
        }

        db.close();

    }

    public void deleteAllVariantsHdr(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tbl_variants_hdr;
        db.execSQL(query);
    }

    public List<HelperVariantsHdr> listVariantsHdr(){

        List<HelperVariantsHdr> helperVariantsHdrs = new LinkedList<HelperVariantsHdr>();
        helperVariantsHdrs.clear();

        String query = "SELECT * FROM " + tbl_variants_hdr;
        SQLiteDatabase db = this.getWritableDatabase();
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

    public List<HelperVariantsHdr> listVariantsHdr(List<HelperVariantsLinks> helperVariantsLinks){

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

        String query = "SELECT * FROM " + tbl_variants_hdr + " WHERE " + col_var_hdr_id + " IN (" + in_var_hdr_id + ")";
        SQLiteDatabase db = this.getWritableDatabase();
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

    //VARIANTS_HDR end

    //VARIANTS_DTLS start
    public void refreshVariantsDtls(List<HelperVariantsDtls> helperVariantsDtls){

        deleteAllVariantsDtls();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < helperVariantsDtls.size(); i++) {
            contentValues.put(col_var_hdr_id, helperVariantsDtls.get(i).getVar_hdr_id());
            contentValues.put(col_var_dtls_id, helperVariantsDtls.get(i).getVar_dtls_id());
            contentValues.put(col_var_dtls_image, helperVariantsDtls.get(i).getVar_dtls_image());
            contentValues.put(col_var_dtls_name, helperVariantsDtls.get(i).getVar_dtls_name());
            contentValues.put(col_var_selling_price, helperVariantsDtls.get(i).getVar_selling_price());
            contentValues.put(col_var_dtls_default, helperVariantsDtls.get(i).getVar_dtls_default());
            contentValues.put(col_var_dtls_add_on, helperVariantsDtls.get(i).getVar_dtls_add_on());
            long result = db.insert(tbl_variants_dtls, null, contentValues);
            if (result == -1) {
                Log.d("refreshVariantsDtls", "failed insert");
            } else {
                Log.d("refreshVariantsDtls", "success insert");
            }
        }

        db.close();

    }

    public void deleteAllVariantsDtls(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tbl_variants_dtls;
        db.execSQL(query);
    }

    public List<HelperVariantsDtls> listVariantsDtls(){

        List<HelperVariantsDtls> helperVariantsDtls = new LinkedList<HelperVariantsDtls>();
        helperVariantsDtls.clear();

        String query = "SELECT * FROM " + tbl_variants_dtls ;
        SQLiteDatabase db = this.getWritableDatabase();
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
                helperVariantsDtls.add(helperVariantsDtl);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperVariantsDtls;
    }

    public List<HelperVariantsDtls> listVariantsDtls(int var_hdr_id){

        List<HelperVariantsDtls> helperVariantsDtls = new LinkedList<HelperVariantsDtls>();
        helperVariantsDtls.clear();

        String query = "SELECT * FROM " + tbl_variants_dtls + " WHERE " + col_var_hdr_id + " = " + var_hdr_id;
        SQLiteDatabase db = this.getWritableDatabase();
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
                helperVariantsDtls.add(helperVariantsDtl);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperVariantsDtls;
    }

    public List<HelperVariantsDtls> listVariantsDtls(List<HelperVariantsLinks> helperVariantsLinks){

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

        String query = "SELECT * FROM " + tbl_variants_dtls + " WHERE " + col_var_hdr_id + " IN (" + in_var_hdr_id + ")";
        SQLiteDatabase db = this.getWritableDatabase();
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
                helperVariantsDtls.add(helperVariantsDtl);
            } while (cursor.moveToNext());
        }

        db.close();
        Log.d("listVariantsDtls", query);
        return helperVariantsDtls;
    }
    //VARIANTS_DTLS end

    //SALES start
    //Cannot delete completed sales
    public void refreshSales(List<HelperSales> helperSales){

        deleteSales();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < helperSales.size(); i++){
            contentValues.put(col_item_id, helperSales.get(i).getItem_id());
            db.insert(tbl_sales, null, contentValues);
        }

        db.close();

    }

    public void deleteSales(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tbl_sales;
        db.execSQL(query);
    }

    public void deleteSalesW(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tbl_sales + " WHERE " + col_completed + " = 'W'";
        db.execSQL(query);
    }

    public void deleteVariant(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tbl_sales
                    + " WHERE " + col_transaction_per_entry + " = '" + helperSale.getTransaction_per_entry() + "'"
                    + " AND " + col_var_hdr_id + " = '" + helperSale.getVar_hdr_id() + "'"
                    + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                    + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                    + " AND " + col_completed + " = '" + helperSale.getCompleted() + "'"
                ;
        Log.d("query", query);
        db.execSQL(query);
    }

    public void deleteSale(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "";
        if (helperSale.getVar_dtls_id() != null){
            query = "DELETE FROM " + tbl_sales
                    + " WHERE ROWID = ("
                    + " SELECT ROWID FROM " + tbl_sales
                    + " WHERE " + col_transaction_per_entry + " = '" + helperSale.getTransaction_per_entry() + "'"
                    + " AND " + col_item_id + " = '" + helperSale.getItem_id() + "'"
                    + " AND " + col_var_dtls_id + " = '" + helperSale.getVar_dtls_id() + "'"
                    + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                    + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                    + " AND " + col_completed + " = '" + helperSale.getCompleted() + "'"
                    + " limit 1"
                    + ")";
        } else {
            query = "DELETE FROM " + tbl_sales
                    + " WHERE ROWID = ("
                    + " SELECT ROWID FROM " + tbl_sales
                    + " WHERE " + col_transaction_per_entry + " = '" + helperSale.getTransaction_per_entry() + "'"
                    + " AND " + col_item_id + " = '" + helperSale.getItem_id() + "'"
                    + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                    + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                    + " AND " + col_completed + " = '" + helperSale.getCompleted() + "'"
                    + " limit 1"
                    + ")";
        }
        Log.d("deleteSale", query);
        db.execSQL(query);
    }

    public void addToCart(HelperSales helperSale){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + tbl_sales + " SET "
                + col_completed + " = 'N'"
                + " WHERE " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + col_completed + " = 'W'";
        Log.d("query", query);
        db.execSQL(query);

        updateDuplicateVariants(helperSale);

    }

    public void updateDuplicateVariants(HelperSales helperSale){

        SQLiteDatabase db = this.getWritableDatabase();

        //MERGE START with variants( ITEM also included )
        String query_concat = "SELECT " + col_transaction_per_entry
                + ", (" + col_item_id
                + " || '-'"
                + " || " + col_var_hdr_id
                + " || " + col_var_dtls_id + " ) AS concat_columns "
                + " FROM " + tbl_sales
                + " WHERE " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + col_completed + " = 'N'"
                + " AND " + col_var_hdr_id + " IS NOT NULL "
                + " ORDER BY 2, 1"
                ;

        String query = "SELECT " + col_transaction_per_entry
                + ", GROUP_CONCAT( DISTINCT concat_columns ) AS gconcat_columns "
                + " FROM " + " ( " + query_concat + " ) "
                + " GROUP BY " + col_transaction_per_entry
                + " ORDER BY 2, 1"
                ;
        Cursor cursor = db.rawQuery(query, null);

        //variables
        String gconcat, gconcat_prev;
        String entry, entry_prev;
        String all_entry;
        boolean dup_found;
        String item_id_dup;
        String base_per_entry;

        //initialize
        gconcat = "9999999";
        gconcat_prev = "8888888";
        entry = "9999999";
        entry_prev = "8888888";
        all_entry = null;
        dup_found = false;
        item_id_dup = null;
        base_per_entry = null;

        if (cursor.moveToFirst()) {
            do {
                ///Log.d("updateDuplicateVariants1", "first loop");
               /// Log.d("updateDuplicateVariantscursor0", cursor.getString(0));
               /// Log.d("updateDuplicateVariantscursor1", cursor.getString(1));

                gconcat_prev = gconcat;
                entry_prev = entry;
                gconcat = cursor.getString(1);
                entry = cursor.getString(0);

                ///Log.d("updateDuplicateVariantsgconcat_prev", "" + gconcat_prev);
                ///Log.d("updateDuplicateVariantsentry_prev", "" + entry_prev);
                ///Log.d("updateDuplicateVariantsgconcat", "" + gconcat);
                ///Log.d("updateDuplicateVariantsentry", "" + entry);
                ///Log.d("updateDuplicateVariantsall_entry", "" + all_entry);

                if(!gconcat.equals(gconcat_prev)){
                    if(dup_found){
                        mergeDupVariants(db, helperSale, gconcat_prev.substring(0,gconcat_prev.indexOf("-")), all_entry, all_entry.substring(0,all_entry.indexOf(",")));
                    }
                    dup_found = false;
                    all_entry = null;
                } else {
                    dup_found = true;
                }

                if(all_entry == null){
                    all_entry = entry;
                } else {
                    all_entry += "," + entry;
                }

                if(cursor.isLast()){
                    ///Log.d("updateDuplicateVariantslast", "last record");
                    if(dup_found){
                        mergeDupVariants(db, helperSale, gconcat_prev.substring(0,gconcat_prev.indexOf("-")), all_entry, all_entry.substring(0,all_entry.indexOf(",")));
                    }
                }

            } while (cursor.moveToNext());
        }

        //MERGE END with variants( ITEM also included )

        //MERGE START with no variant( ITEM only ) kanin, mineral water
        String query_item_only = "SELECT " + col_transaction_per_entry
                            + ", " + col_item_id
                            + " FROM " + tbl_sales
                            + " WHERE " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                            + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                            + " AND " + col_item_id + " = " + helperSale.getItem_id()
                            + " AND " + col_completed + " = 'N'"
                            + " AND " + col_var_hdr_id + " IS NULL "
                            + " EXCEPT "
                            + "SELECT " + col_transaction_per_entry
                            + ", " + col_item_id
                            + " FROM " + tbl_sales
                            + " WHERE " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                            + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                            + " AND " + col_item_id + " = " + helperSale.getItem_id()
                            + " AND " + col_completed + " = 'N'"
                            + " AND " + col_var_hdr_id + " IS NOT NULL "
                            + " ORDER BY 2, 1"
                            ;

        String query_item_only_all = "SELECT " + col_transaction_per_entry
                + ", GROUP_CONCAT( DISTINCT " + col_item_id + " ) AS gconcat_columns "
                + " FROM " + " ( " + query_item_only + " ) "
                + " GROUP BY " + col_transaction_per_entry
                + " ORDER BY 2, 1"
                ;
        Cursor cursor_item_only_all = db.rawQuery(query_item_only_all, null);

        //initialize
        gconcat = "9999999";
        gconcat_prev = "8888888";
        entry = "9999999";
        entry_prev = "8888888";
        all_entry = null;
        dup_found = false;
        item_id_dup = null;
        base_per_entry = null;

        ///Log.d("updateDuplicateVariants1", query_item_only);
        if (cursor_item_only_all.moveToFirst()) {
            do {
                ///Log.d("updateDuplicateVariants1", "first loop except");
                ///Log.d("updateDuplicateVariantsexcept0", cursor_item_only_all.getString(0));
                ///Log.d("updateDuplicateVariantsexcept1", cursor_item_only_all.getString(1));

                gconcat_prev = gconcat;
                entry_prev = entry;
                gconcat = cursor_item_only_all.getString(1);
                entry = cursor_item_only_all.getString(0);

                ///Log.d("updateDuplicateVariantsgconcat_prev", "" + gconcat_prev);
                ///Log.d("updateDuplicateVariantsentry_prev", "" + entry_prev);
                ///Log.d("updateDuplicateVariantsgconcat", "" + gconcat);
                ///Log.d("updateDuplicateVariantsentry", "" + entry);
                ///Log.d("updateDuplicateVariantsall_entry", "" + all_entry);

                if(!gconcat.equals(gconcat_prev)){
                    if(dup_found){
                        mergeDupVariants(db, helperSale, gconcat_prev, all_entry, all_entry.substring(0,all_entry.indexOf(",")));
                    }
                    dup_found = false;
                    all_entry = null;
                } else {
                    dup_found = true;
                }

                if(all_entry == null){
                    all_entry = entry;
                } else {
                    all_entry += "," + entry;
                }

                if(cursor_item_only_all.isLast()){
                    if(dup_found){
                        mergeDupVariants(db, helperSale, gconcat_prev, all_entry, all_entry.substring(0,all_entry.indexOf(",")));
                    }
                }

            } while (cursor_item_only_all.moveToNext());
        }
        //MERGE END with no variant( ITEM only )

        db.close();

    }

    public void mergeDupVariants(SQLiteDatabase db, HelperSales helperSale, String item_id_dup, String all_entry, String base_per_entry){
        String query_update_item = "UPDATE " + tbl_sales
                + " SET " + col_transaction_per_entry + " = " + base_per_entry
                + " WHERE " + col_transaction_per_entry + " in (" + all_entry + ") "
                + " AND " + col_item_id + " = " + item_id_dup
                + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + col_completed + " = 'N'";
        ///Log.d("updateDuplicateVariantsUpdate", query_update_item);
        db.execSQL(query_update_item);
    }

    public boolean haveOtherVariants(String per_entry, String item_id, String var_hdr_id, String var_hdr_dtls_id, SQLiteDatabase db){
        String query = "SELECT * FROM " + tbl_sales
                + " WHERE " + col_completed + " = 'N'"
                + " AND " + col_transaction_per_entry + " = " + per_entry
                + " AND " + col_item_id + " = " + item_id
                + " AND " + col_var_hdr_id + " = " + var_hdr_id
                + " AND " + col_var_dtls_id + " != " + var_hdr_dtls_id
                + " AND " + col_var_hdr_id + " IS NULL ";
                ;
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            //db.close();
            Log.d("Record Already Exists", query);
            return true;
        }
        Log.d("New Record  ", query);
        //db.close();

        return false;
    }

    public List<HelperStockHistory> listStocksAddedToCart(HelperSales helperSaleIn){
        List<HelperStockHistory> helperStockHistories = new LinkedList<>();
        helperStockHistories.clear();

        String query_item = "SELECT 'Out' " + col_in_out
                + ", ( SELECT " + COL_STOCK_NAME + " FROM " + TBL_STOCK_NAMES + " c WHERE c." + col_stock_id + " = b." + col_stock_id + " ) AS " + COL_STOCK_NAME
                + ", b." + col_qty
                + ", b." + col_unit
                + ", a." + col_date
                + ", a." + col_created_by
                + " FROM " + tbl_sales + " AS a, " + tbl_composite_links + " AS b"
                + " WHERE a." + col_created_by + " = '" + helperSaleIn.getCreated_by() + "'"
                + " AND a." + col_machine_name + " = '" + helperSaleIn.getMachine_name() + "'"
                + " AND a." + col_completed + " = 'N'"
                + " AND a." + col_var_hdr_id + " is null "
                + " AND a." + col_item_id + " = b." + col_item_id
                + " AND b." + col_var_hdr_id + " is null "
                ;

        String query_var = "SELECT 'Out' " + col_in_out
                + ", ( SELECT " + COL_STOCK_NAME + " FROM " + TBL_STOCK_NAMES + " c WHERE c." + col_stock_id + " = b." + col_stock_id + " ) AS " + COL_STOCK_NAME
                + ", b." + col_qty
                + ", b." + col_unit
                + ", a." + col_date
                + ", a." + col_created_by
                + " FROM " + tbl_sales + " AS a, " + tbl_composite_links + " AS b"
                + " WHERE a." + col_created_by + " = '" + helperSaleIn.getCreated_by() + "'"
                + " AND a." + col_machine_name + " = '" + helperSaleIn.getMachine_name() + "'"
                + " AND a." + col_completed + " = 'N'"
                + " AND a." + col_var_hdr_id + " = b." + col_var_hdr_id
                + " AND a." + col_var_dtls_id + " = b." + col_var_dtls_id
                + " AND a." + col_item_id + " = b." + col_item_id
                ;

        String query = query_item + " union all " + query_var;

        Log.d("listStocksAddedToCart", query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        HelperStockHistory helperStockHistory = null;

        if (cursor.moveToFirst()) {
            do {
                helperStockHistory = new HelperStockHistory();
                helperStockHistory.setIn_out(cursor.getString(0));
                helperStockHistory.setStock_name(cursor.getString(1));
                helperStockHistory.setQty(cursor.getString(2));
                helperStockHistory.setMeasure_used(cursor.getString(3));
                helperStockHistory.setTime(cursor.getString(4));
                helperStockHistory.setUsername(cursor.getString(5));
                helperStockHistories.add(helperStockHistory);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperStockHistories;

    }

    public List<HelperSales> listAddedToCart(HelperSales helperSaleIn){
        List<HelperSales> helperSales = new LinkedList<>();
        helperSales.clear();

        String query = "SELECT * FROM " + tbl_sales
                + " WHERE " + col_created_by + " = '" + helperSaleIn.getCreated_by() + "'"
                + " AND " + col_machine_name + " = '" + helperSaleIn.getMachine_name() + "'"
                + " AND " + col_completed + " = 'N'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        HelperSales helperSale = null;

        if (cursor.moveToFirst()) {
            do {
                helperSale = new HelperSales();
                helperSale.setTransaction_id(Integer.parseInt(cursor.getString(0)));
                helperSale.setTransaction_counter(Integer.parseInt(cursor.getString(1)));
                helperSale.setTransaction_per_entry(Integer.parseInt(cursor.getString(2)));
                helperSale.setItem_id(Integer.parseInt(cursor.getString(3)));
                helperSale.setSort_order_id(Integer.parseInt(cursor.getString(4)));
                helperSale.setMachine_name(cursor.getString(5));
                helperSale.setVar_hdr_id(cursor.getString(6));
                helperSale.setVar_dtls_id(cursor.getString(7));
                helperSale.setItem_name(cursor.getString(8));
                helperSale.setQty(cursor.getString(9));
                helperSale.setSelling_price(cursor.getString(10));
                helperSale.setDate(cursor.getString(11));
                helperSale.setCreated_by(cursor.getString(12));
                helperSale.setCompleted(cursor.getString(13));
                helperSale.setDine_in_out(cursor.getString(14));
                helperSales.add(helperSale);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperSales;
    }

    public List<HelperSales> listPreFinishSale(HelperSales helperSaleParam){
        List<HelperSales> HelperSales = new LinkedList<HelperSales>();
        HelperSales.clear();

        String query = "SELECT * FROM " + tbl_sales
                + " WHERE " + col_created_by + " = '" + helperSaleParam.getCreated_by() + "'"
                + " AND " + col_machine_name + " = '" + helperSaleParam.getMachine_name() + "'"
                + " AND " + col_completed + " = 'N'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                HelperSales helperSale = new HelperSales();
                helperSale.setTransaction_id(Integer.parseInt(cursor.getString(0)));
                helperSale.setTransaction_counter(Integer.parseInt(cursor.getString(1)));
                helperSale.setTransaction_per_entry(Integer.parseInt(cursor.getString(2)));
                helperSale.setItem_id(Integer.parseInt(cursor.getString(3)));
                helperSale.setSort_order_id(Integer.parseInt(cursor.getString(4)));
                helperSale.setMachine_name(cursor.getString(5));
                if (cursor.getString(6)!=null)
                helperSale.setVar_hdr_id(cursor.getString(6));
                if (cursor.getString(7)!=null)
                helperSale.setVar_dtls_id(cursor.getString(7));
                helperSale.setItem_name(cursor.getString(8));
                helperSale.setQty(cursor.getString(9));
                helperSale.setSelling_price(cursor.getString(10));
                if (cursor.getString(11)!=null)
                helperSale.setDate(cursor.getString(11));
                helperSale.setCreated_by(cursor.getString(12));
                helperSale.setCompleted(cursor.getString(13));
                if (cursor.getString(14)!=null)
                    helperSale.setDine_in_out(cursor.getString(14));
                HelperSales.add(helperSale);
            } while (cursor.moveToNext());
        }

        db.close();

        return HelperSales;
    }


    public void finishSale(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + tbl_sales + " SET "
                + col_completed + " = 'Y'"
                + " WHERE " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + col_completed + " = 'N'";
        Log.d("query", query);
        db.execSQL(query);
    }

    public void insertSaleNew(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //contentValues.put(col_transaction_id, helperSale.getTransaction_id());
        contentValues.put(col_transaction_counter, helperSale.getTransaction_counter());
        contentValues.put(col_transaction_per_entry, helperSale.getTransaction_per_entry());
        contentValues.put(col_item_id, helperSale.getItem_id());
        contentValues.put(col_sort_order_id, helperSale.getSort_order_id());
        contentValues.put(col_machine_name, helperSale.getMachine_name());
        contentValues.put(col_var_hdr_id, helperSale.getVar_hdr_id());
        contentValues.put(col_var_dtls_id, helperSale.getVar_dtls_id());
        contentValues.put(col_item_name, helperSale.getItem_name());
        contentValues.put(col_qty, helperSale.getQty());
        contentValues.put(col_selling_price, helperSale.getSelling_price());
        contentValues.put(col_date, helperSale.getDate());
        contentValues.put(col_created_by, helperSale.getCreated_by());
        contentValues.put(col_completed, helperSale.getCompleted());
        contentValues.put(col_dine_in_out, helperSale.getDine_in_out());
        long result = db.insert(tbl_sales, null, contentValues);
        if (result == -1) {
            Log.d("insertSaleNew", "failed insert sale");
        } else {
            Log.d("insertSaleNew", "success insert sale");
        }


        db.close();
    }

    public HelperSales insertSaleNRet(HelperSales helperSales){
        //only col_completed = 'W'
        if(helperSales.getCompleted().equals("W")){
            helperSales = mergeSameHelperSales(helperSales);
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //contentValues.put(col_transaction_id, helperSales.getTransaction_id());
        contentValues.put(col_transaction_counter, helperSales.getTransaction_counter());
        contentValues.put(col_transaction_per_entry, helperSales.getTransaction_per_entry());
        contentValues.put(col_item_id, helperSales.getItem_id());
        contentValues.put(col_sort_order_id, helperSales.getSort_order_id());
        contentValues.put(col_machine_name, helperSales.getMachine_name());
        contentValues.put(col_var_hdr_id, helperSales.getVar_hdr_id());
        contentValues.put(col_var_dtls_id, helperSales.getVar_dtls_id());
        contentValues.put(col_item_name, helperSales.getItem_name());
        contentValues.put(col_qty, helperSales.getQty());
        contentValues.put(col_selling_price, helperSales.getSelling_price());
        contentValues.put(col_date, helperSales.getDate());
        contentValues.put(col_created_by, helperSales.getCreated_by());
        contentValues.put(col_completed, helperSales.getCompleted());
        contentValues.put(col_dine_in_out, helperSales.getDine_in_out());
        long result = db.insert(tbl_sales, null, contentValues);
        if (result == -1) {
            Log.d("insertSale", "failed insert sale");
        } else {
            Log.d("insertSale", "success insert sale");
        }


        db.close();

        return helperSales;
    }

    public void insertSalexxx(HelperSales helperSales){
        //only col_completed = 'W'
        if(helperSales.getCompleted().equals("W")){
            helperSales = mergeSameHelperSales(helperSales);
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

            //contentValues.put(col_transaction_id, helperSales.getTransaction_id());
            contentValues.put(col_transaction_counter, helperSales.getTransaction_counter());
            contentValues.put(col_transaction_per_entry, helperSales.getTransaction_per_entry());
            contentValues.put(col_item_id, helperSales.getItem_id());
            contentValues.put(col_sort_order_id, helperSales.getSort_order_id());
            contentValues.put(col_machine_name, helperSales.getMachine_name());
            contentValues.put(col_var_hdr_id, helperSales.getVar_hdr_id());
            contentValues.put(col_var_dtls_id, helperSales.getVar_dtls_id());
            contentValues.put(col_item_name, helperSales.getItem_name());
            contentValues.put(col_qty, helperSales.getQty());
            contentValues.put(col_selling_price, helperSales.getSelling_price());
            contentValues.put(col_date, helperSales.getDate());
            contentValues.put(col_created_by, helperSales.getCreated_by());
            contentValues.put(col_completed, helperSales.getCompleted());
            contentValues.put(col_dine_in_out, helperSales.getDine_in_out());
            long result = db.insert(tbl_sales, null, contentValues);
            if (result == -1) {
                Log.d("insertSale", "failed insert sale");
            } else {
                Log.d("insertSale", "success insert sale");
            }


        db.close();
    }

    public HelperSales mergeSameHelperSales(HelperSales helperSale){

        String query;

        if(helperSale.getVar_hdr_id()!= null){
            query = "SELECT DISTINCT " + col_transaction_per_entry
                    + ", " + col_sort_order_id
                    +  " FROM " + tbl_sales
                    + " WHERE " + col_item_id + " = '" + helperSale.getItem_id() + "'"
                    + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                    + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                    + " AND " + col_completed + " = '" + helperSale.getCompleted() + "'"
                    + " AND " + col_var_hdr_id + " = " + helperSale.getVar_hdr_id()
                    + " AND " + col_var_dtls_id + " = " + helperSale.getVar_dtls_id();
        } else {
             query = "SELECT DISTINCT " + col_transaction_per_entry
                    + ", " + col_sort_order_id
                    +  " FROM " + tbl_sales
                    + " WHERE " + col_item_id + " = '" + helperSale.getItem_id() + "'"
                    + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                    + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                    + " AND " + col_completed + " = '" + helperSale.getCompleted() + "'"
                    + " AND " + col_var_hdr_id + " is null";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            helperSale.setTransaction_per_entry(cursor.getInt(0));
            helperSale.setSort_order_id(cursor.getInt(1));
        }

        db.close();

        return helperSale;
    }



    public void deleteNotInCart(String created_by, String machine_name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tbl_sales
                + " WHERE " + col_completed + " = 'W'"
                + " AND " + col_machine_name + " = '" + machine_name + "'"
                + " AND " + col_created_by + " = '" + created_by + "'"
                ;
        db.execSQL(query);
    }

    public boolean notInCartExists(String created_by, String machine_name){
        String query = "SELECT * FROM " + tbl_sales
                + " WHERE " + col_completed + " = 'W'"
                + " AND " + col_machine_name + " = '" + machine_name + "'"
                + " AND " + col_created_by + " = '" + created_by + "'"
                ;
        SQLiteDatabase db = this.getWritableDatabase();
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

    public boolean itemExists(HelperSales helperSale){
        String query = "SELECT * FROM " + tbl_sales
                + " WHERE " + col_item_id + " = '" + helperSale.getItem_id() + "'"
                + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + col_completed + " = '" + helperSale.getCompleted() + "'"
                + " AND " + col_transaction_per_entry + " = '" + helperSale.getTransaction_per_entry() + "'"
                ;
        SQLiteDatabase db = this.getWritableDatabase();
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



    public boolean variantExists(HelperSales helperSale){
        String query;

        if (helperSale.getVar_hdr_id()!=null){
            query = "SELECT * FROM " + tbl_sales
                    + " WHERE " + col_transaction_per_entry + " = '" + helperSale.getTransaction_per_entry() + "'"
                    + " AND " + col_item_id + " = '" + helperSale.getItem_id() + "'"
                    + " AND " + col_var_hdr_id + " = '" + helperSale.getVar_hdr_id() + "'"
                    + " AND " + col_var_dtls_id + " = '" + helperSale.getVar_dtls_id() + "'"
                    + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                    + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                    + " AND " + col_completed + " = '" + helperSale.getCompleted() + "'"
            ;
        } else {
            query = "SELECT * FROM " + tbl_sales
                    + " WHERE " + col_transaction_per_entry + " = '" + helperSale.getTransaction_per_entry() + "'"
                    + " AND " + col_item_id + " = '" + helperSale.getItem_id() + "'"
                    + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                    + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                    + " AND " + col_completed + " = '" + helperSale.getCompleted() + "'"
            ;
        }

        SQLiteDatabase db = this.getWritableDatabase();
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

    public int nextSortOrderIdNoItem(HelperSales helperSale){
        String query = "SELECT MAX(" + col_sort_order_id + ") + " + sale_sorter + " FROM " + tbl_sales
                + " WHERE " + col_sort_order_id + " % " + sale_sorter + " = 0"
                + " AND " + col_transaction_per_entry + " = '" + helperSale.getTransaction_per_entry() + "'"
                + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + col_completed + " = '" + helperSale.getCompleted() + "'"
                ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int getSortOrderIdItem(HelperSales helperSale){
        String query = "SELECT MAX(" + col_sort_order_id + ") FROM " + tbl_sales
                + " WHERE " + col_sort_order_id + " % " + sale_sorter + " = 0"
                + " AND " + col_transaction_per_entry + " = '" + helperSale.getTransaction_per_entry() + "'"
                + " AND " + col_item_id + " = '" + helperSale.getItem_id() + "'"
                + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + col_completed + " = '" + helperSale.getCompleted() + "'"
                ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int getSortOrderIdVariant(HelperSales helperSale){
        String query = "SELECT MAX(" + col_sort_order_id + ") FROM " + tbl_sales
                + " WHERE " + col_transaction_per_entry + " = '" + helperSale.getTransaction_per_entry() + "'"
                + " AND " + col_item_id + " = '" + helperSale.getItem_id() + "'"
                + " AND " + col_var_dtls_id + " = '" + helperSale.getVar_dtls_id() + "'"
                + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + col_completed + " = '" + helperSale.getCompleted() + "'"
                ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int maxTransactionId(){
        String query = "SELECT MAX(" + col_transaction_id + ") FROM " + tbl_sales;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int maxTransactionPerEntry(){
        String query = "SELECT MAX(" + col_transaction_per_entry + ") FROM " + tbl_sales;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }


    public int maxTransactionPerEntryCompN(HelperSales helperSale){

        String query = "SELECT MAX(" + col_transaction_per_entry + ") FROM " + tbl_sales
                + " WHERE " + col_created_by + " = '" + helperSale.getCreated_by() + "' "
                + " AND " + col_item_id + " = '" + helperSale.getItem_id() + "' "
                + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "' "
                + " AND " + col_completed + " = 'N' "
                + " AND " + col_var_hdr_id + " is not null"
                ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int nextTransactionCounter(){
        String query = "SELECT MAX(" + col_transaction_counter + ") + 1 FROM " + tbl_sales
                + " WHERE " + col_completed + " = 'Y'"
                ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int nextTransactionPerEntry(){
        //modify this, to check first if HelperSale does not exists yet, then get new transaction_per_entry no.
        //                              if exists, then reuse that transaction_per_entry no.
        // ex. customer say 1 reg fries cheese
        //                  1 pizza
        //                  1 reg fries cheese
        // display should should be
        //                  2 reg fries cheese
        //                  1 pizza

        String query = "SELECT MAX(" + col_transaction_per_entry + ") + 1 FROM " + tbl_sales
                + " WHERE " + col_completed + " = 'N'"
                ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int addToMaxSortOrderIdVariant(HelperSales helperSale){
        //int to be used is the rownum in table links how it was inserted
        String query = "SELECT  * FROM " + tbl_variants_links
                + " WHERE " + col_item_id + " = '" + helperSale.getItem_id() + "'"
                + " ORDER BY ROWID"
                ;
        Log.d("addToMaxSortOrderIdVar", query );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            do {
                Log.d("addToMaxSortOrderIdVari", "link_id=" + cursor.getInt( 0) + " item_id=" + cursor.getInt( 1) + " var_hdr_id=" + cursor.getInt( 2));
                id += 1;
                if (cursor.getInt( 2) == Integer.parseInt(helperSale.getVar_hdr_id())){
                    return id;
                }
            } while (cursor.moveToNext());
        }

        db.close();

        return id;
    }

    public int nextSortOrderIdVariant(HelperSales helperSale){

        int int_add = addToMaxSortOrderIdVariant(helperSale);

        String query = "SELECT MAX(" + col_sort_order_id + ") + " + int_add + " FROM " + tbl_sales
                + " WHERE " + col_transaction_per_entry + " = '" + helperSale.getTransaction_per_entry() + "'"
                + " AND " + col_item_id + " = '" + helperSale.getItem_id() + "'"
                + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + col_completed + " = '" + helperSale.getCompleted() + "'"
                + " AND " + col_var_hdr_id + " IS NULL "
                ;
        Log.d("nextSortOrderIdVariant", query );

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int sumItemsPerTranPerEntry(HelperSales helperSale){
        //no variant data
        String query = "SELECT sum(" + col_qty +  ") FROM " + tbl_sales
                + " WHERE " + col_transaction_per_entry + " = '" + helperSale.getTransaction_per_entry() + "'"
                + " AND " + col_item_id + " = '" + helperSale.getItem_id() + "'"
                + " AND " + col_machine_name + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + col_created_by + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + col_completed + " = '" + helperSale.getCompleted() + "'"
                + " AND " + col_var_hdr_id + " is null"
                ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        Log.d("sumItemsPerTranPerEntry", query + " id=" + id);

        return id;
    }

    public boolean existingItemVarCompN(HelperSales helperSaleIn){

        String query = "SELECT 1 "
                + " FROM " + tbl_sales
                + " WHERE " + col_created_by + " = '" + helperSaleIn.getCreated_by() + "' "
                + " AND " + col_item_id + " = '" + helperSaleIn.getItem_id() + "' "
                + " AND " + col_machine_name + " = '" + helperSaleIn.getMachine_name() + "' "
                + " AND " + col_transaction_per_entry + " = '" + helperSaleIn.getTransaction_per_entry() + "' "
                + " AND " + col_completed + " = 'N' "
                + " AND " + col_var_hdr_id + " is not null"
                ;

        SQLiteDatabase db = this.getWritableDatabase();
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

    public List<HelperSales> listLastSelectedVar(HelperSales helperSaleIn, int intLastTransactionPerEntry){
        //last selected variants after add to cart
        List<HelperSales> helperSales= new LinkedList<HelperSales>();
        helperSales.clear();

        String query = "SELECT DISTINCT " + col_item_name
                + ", " + col_selling_price
                + ", " + col_var_dtls_id
                + ", " + col_var_hdr_id
                + ", " + col_completed
                + " FROM " + tbl_sales
                + " WHERE " + col_created_by + " = '" + helperSaleIn.getCreated_by() + "' "
                + " AND " + col_item_id + " = '" + helperSaleIn.getItem_id() + "' "
                + " AND " + col_machine_name + " = '" + helperSaleIn.getMachine_name() + "' "
                + " AND " + col_transaction_per_entry + " = '" + intLastTransactionPerEntry + "' "
                + " AND " + col_completed + " = 'N' "
                + " AND " + col_var_hdr_id + " is not null"
                ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);



        if (cursor.moveToFirst()) {
            do {
                HelperSales helperSale = new HelperSales();
                //from input
                helperSale.setCreated_by(helperSaleIn.getCreated_by());
                helperSale.setItem_id(helperSaleIn.getItem_id());
                helperSale.setMachine_name(helperSaleIn.getMachine_name());
                helperSale.setTransaction_per_entry(helperSaleIn.getTransaction_per_entry()); //change only in summary
                helperSale.setCompleted("W"); //Query last selected added to cart, then set it to non-added to cart.

                //from cursor
                helperSale.setItem_name(cursor.getString(0));
                helperSale.setSelling_price(cursor.getString(1));
                helperSale.setVar_dtls_id(cursor.getString(2));
                helperSale.setVar_hdr_id(cursor.getString(3));
                helperSale.setCompleted(cursor.getString(4));
                helperSales.add(helperSale);
            } while (cursor.moveToNext());
        }

        Log.d("listVariantsInItemNew", query);

        db.close();

        return helperSales;
    }

    public List<HelperSales> listVariantsInItemNew(HelperSales helperSaleIn){

        String sCompleted;

        if (helperSaleIn.getCompleted() != null){
            if (helperSaleIn.getCompleted().equals("W")){
                sCompleted = "W";
            } else {
                sCompleted = "N";
            }
        } else {
            sCompleted = "N";
        }

        List<HelperSales> helperSales= new LinkedList<HelperSales>();
        helperSales.clear();

        String query = "SELECT DISTINCT " + col_item_name
                + ", " + col_selling_price
                + ", " + col_var_dtls_id
                + ", " + col_var_hdr_id
                + ", " + col_completed
                + " FROM " + tbl_sales
                + " WHERE " + col_created_by + " = '" + helperSaleIn.getCreated_by() + "' "
                + " AND " + col_item_id + " = '" + helperSaleIn.getItem_id() + "' "
                + " AND " + col_machine_name + " = '" + helperSaleIn.getMachine_name() + "' "
                + " AND " + col_transaction_per_entry + " = '" + helperSaleIn.getTransaction_per_entry() + "' "
                + " AND " + col_completed + " = '" + sCompleted + "' "
                + " AND " + col_var_hdr_id + " is not null"
                ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);



        if (cursor.moveToFirst()) {
            do {
                HelperSales helperSale = new HelperSales();
                //from input
                helperSale.setCreated_by(helperSaleIn.getCreated_by());
                helperSale.setItem_id(helperSaleIn.getItem_id());
                helperSale.setMachine_name(helperSaleIn.getMachine_name());
                helperSale.setTransaction_per_entry(helperSaleIn.getTransaction_per_entry());
                helperSale.setCompleted(sCompleted);

                //from cursor
                helperSale.setItem_name(cursor.getString(0));
                helperSale.setSelling_price(cursor.getString(1));
                helperSale.setVar_dtls_id(cursor.getString(2));
                helperSale.setVar_hdr_id(cursor.getString(3));
                helperSale.setCompleted(cursor.getString(4));
                helperSales.add(helperSale);
            } while (cursor.moveToNext());
        }

        Log.d("listVariantsInItemNew", query);

        db.close();

        return helperSales;
    }

    public List<HelperSales> listVariantsInItem(HelperSales helperSalesParam){

        String sCompleted;

        if (helperSalesParam.getCompleted() != null){
            if (helperSalesParam.getCompleted().equals("W")){
                sCompleted = "W";
            } else {
                sCompleted = "N";
            }
        } else {
            sCompleted = "N";
        }

        List<HelperSales> helperSales= new LinkedList<HelperSales>();
        helperSales.clear();

        String query = "SELECT DISTINCT " + col_item_name
                + ", " + col_selling_price
                + ", " + col_var_dtls_id
                + ", " + col_var_hdr_id
                + ", " + col_transaction_per_entry
                + ", " + col_item_id
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_completed
                + " FROM " + tbl_sales
                + " WHERE " + col_created_by + " = '" + helperSalesParam.getCreated_by() + "' "
                + " AND " + col_item_id + " = '" + helperSalesParam.getItem_id() + "' "
                + " AND " + col_machine_name + " = '" + helperSalesParam.getMachine_name() + "' "
                + " AND " + col_transaction_per_entry + " = '" + helperSalesParam.getTransaction_per_entry() + "' "
                + " AND " + col_completed + " = '" + sCompleted + "' "
                + " AND " + col_var_hdr_id + " is not null"
                ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        HelperSales helperSale = null;

        if (cursor.moveToFirst()) {
            do {
                helperSale = new HelperSales();
                helperSale.setItem_name(cursor.getString(0));
                helperSale.setSelling_price(cursor.getString(1));
                helperSale.setVar_dtls_id(cursor.getString(2));
                helperSale.setVar_hdr_id(cursor.getString(3));
                helperSale.setTransaction_per_entry(Integer.parseInt(cursor.getString(4)));
                helperSale.setItem_id(Integer.parseInt(cursor.getString(5)));
                helperSale.setMachine_name(cursor.getString(6));
                helperSale.setCreated_by(cursor.getString(7));
                helperSale.setCompleted(cursor.getString(8));
                helperSales.add(helperSale);
            } while (cursor.moveToNext());
        }

        Log.d("listVariantsInItem", query);

        db.close();

        return helperSales;
    }

    public List<HelperSales> listPreCompletedSales(HelperSales helperSalesParam){

        String sCompleted;

        if (helperSalesParam.getCompleted() != null){
            if (helperSalesParam.getCompleted().equals("W")){
                sCompleted = "W";
            } else {
                sCompleted = "N";
            }
        } else {
            sCompleted = "N";
        }

        List<HelperSales> helperSales= new LinkedList<HelperSales>();
        helperSales.clear();

        String query = "SELECT sum(" + col_qty + ") || '(' ||" +  col_selling_price + "||'.00)' AS " + col_qty
                + ", sum(" + col_qty + ") * " + col_selling_price + " AS " + col_selling_price
                + ", " + col_item_id
                + ", " + col_item_name
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_sort_order_id
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + ", " + col_var_dtls_id
                + " FROM " + tbl_sales
                + " WHERE " + col_created_by + " = '" + helperSalesParam.getCreated_by() + "' "
                + " AND " + col_machine_name + " = '" + helperSalesParam.getMachine_name() + "' "
                + " AND " + col_completed + " = '" + sCompleted + "' "
                + " GROUP BY " + col_item_id
                + ", " + col_item_name
                + ", " + col_selling_price
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_sort_order_id
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + ", " + col_var_dtls_id
                + " ORDER BY "
                + col_transaction_per_entry  + " DESC " + ", "
                + col_sort_order_id  + " DESC ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        HelperSales helperSale = null;

        if (cursor.moveToFirst()) {
            do {
                helperSale = new HelperSales();
                helperSale.setQty(cursor.getString(0));
                helperSale.setSelling_price(cursor.getString(1));
                helperSale.setItem_id(Integer.parseInt(cursor.getString(2)));
                helperSale.setItem_name(cursor.getString(3));
                helperSale.setMachine_name(cursor.getString(4));
                helperSale.setCreated_by(cursor.getString(5));
                helperSale.setSort_order_id(Integer.parseInt(cursor.getString(6)));
                helperSale.setTransaction_per_entry(Integer.parseInt(cursor.getString(7)));
                helperSale.setCompleted(cursor.getString(8));
                helperSale.setVar_dtls_id(cursor.getString(9));
                helperSales.add(helperSale);
            } while (cursor.moveToNext());
        }

        Log.d("listNotCompletedSales", query);

        db.close();

        return helperSales;
    }

    public List<HelperSales> listSummaryPreCompletedSalesNewLine(HelperSales helperSalesParam){

        String sCompleted;

        if (helperSalesParam.getCompleted() != null){
            if (helperSalesParam.getCompleted().equals("W")){
                sCompleted = "W";
            } else {
                sCompleted = "N";
            }
        } else {
            sCompleted = "N";
        }

        String query_item = "SELECT sum(" + col_qty + ") AS " + col_qty
                + ", sum(" + col_selling_price + ") AS " + col_selling_price
                + ", " + col_item_id
                + ", " + col_item_name
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + ", " + col_sort_order_id
                + " FROM " + tbl_sales
                + " WHERE " + col_created_by + " = '" + helperSalesParam.getCreated_by() + "' "
                + " AND " + col_machine_name + " = '" + helperSalesParam.getMachine_name() + "' "
                + " AND " + col_completed + " = '" + sCompleted + "' "
                + " AND " + col_var_hdr_id + " IS NULL "
                + " GROUP BY " + col_item_id
                + ", " + col_item_name
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + ", " + col_sort_order_id;

        String query_variants = "SELECT sum(" + 0 + ") AS " + col_qty
                + ", sum(" + col_selling_price + ")  AS " + col_selling_price
                + ", " + col_item_id
                + ", ('\n' || '   >' || " + col_item_name + ") AS " + col_item_name
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + ", " + col_sort_order_id
                + " FROM " + tbl_sales
                + " WHERE " + col_created_by + " = '" + helperSalesParam.getCreated_by() + "' "
                + " AND " + col_machine_name + " = '" + helperSalesParam.getMachine_name() + "' "
                + " AND " + col_completed + " = '" + sCompleted + "' "
                + " AND " + col_var_hdr_id + " IS NOT NULL "
                + " GROUP BY " + col_item_id
                + ", ('\n' || '   >' || " + col_item_name + ") "
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + ", " + col_sort_order_id
                + " ORDER BY " + col_sort_order_id;

        String query_all = "SELECT sum(" + col_qty + ") AS " + col_qty
                + ", sum(" + col_selling_price + ")  AS " + col_selling_price
                + ", " + col_item_id
                + ", GROUP_CONCAT( "+ col_item_name + ", ' ') AS " + col_item_name
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + " FROM "
                + " ( "
                + query_item + " union all " + query_variants
                + " ) "
                + " GROUP BY " + col_item_id
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed;

        List<HelperSales> helperSales= new LinkedList<HelperSales>();
        helperSales.clear();

        String query = "SELECT (" + col_qty + ") || '(' || (" + col_selling_price + "/" + col_qty + ") || '.00)'  AS " + col_qty
                + ", " + col_selling_price
                + ", " + col_item_id
                + ", " + col_item_name
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + " FROM "
                + " ( "
                + query_all
                + " ) "
                + " ORDER BY " + col_transaction_per_entry + " DESC";

        Log.d("listSummaryPreCompleted", query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        HelperSales helperSale = null;

        if (cursor.moveToFirst()) {
            do {
                helperSale = new HelperSales();
                helperSale.setQty(cursor.getString(0));
                helperSale.setSelling_price(cursor.getString(1));
                helperSale.setItem_id(Integer.parseInt(cursor.getString(2)));
                helperSale.setItem_name(cursor.getString(3));
                helperSale.setMachine_name(cursor.getString(4));
                helperSale.setCreated_by(cursor.getString(5));
                helperSale.setTransaction_per_entry(Integer.parseInt(cursor.getString(6)));
                helperSale.setCompleted(cursor.getString(7));
                helperSales.add(helperSale);
            } while (cursor.moveToNext());
        }

        Log.d("listSummaryPreCompleted", query);

        db.close();

        return helperSales;
    }

    public List<HelperSales> listSummaryPreCompletedSales(HelperSales helperSalesParam){

        String sCompleted;

        if (helperSalesParam.getCompleted() != null){
            if (helperSalesParam.getCompleted().equals("W")){
                sCompleted = "W";
            } else {
                sCompleted = "N";
            }
        } else {
            sCompleted = "N";
        }

        String query_item = "SELECT sum(" + col_qty + ") AS " + col_qty
                + ", sum(" + col_selling_price + ") AS " + col_selling_price
                + ", " + col_item_id
                + ", " + col_item_name
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + ", " + col_sort_order_id
                + " FROM " + tbl_sales
                + " WHERE " + col_created_by + " = '" + helperSalesParam.getCreated_by() + "' "
                + " AND " + col_machine_name + " = '" + helperSalesParam.getMachine_name() + "' "
                + " AND " + col_completed + " = '" + sCompleted + "' "
                + " AND " + col_var_hdr_id + " IS NULL "
                + " GROUP BY " + col_item_id
                + ", " + col_item_name
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + ", " + col_sort_order_id;

        String query_variants = "SELECT sum(" + 0 + ") AS " + col_qty
                + ", sum(" + col_selling_price + ")  AS " + col_selling_price
                + ", " + col_item_id
                + ", substr(" + col_item_name + ", 1, 3) AS " + col_item_name
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + ", " + col_sort_order_id
                + " FROM " + tbl_sales
                + " WHERE " + col_created_by + " = '" + helperSalesParam.getCreated_by() + "' "
                + " AND " + col_machine_name + " = '" + helperSalesParam.getMachine_name() + "' "
                + " AND " + col_completed + " = '" + sCompleted + "' "
                + " AND " + col_var_hdr_id + " IS NOT NULL "
                + " GROUP BY " + col_item_id
                + ", substr(" + col_item_name + ", 1, 3) "
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + ", " + col_sort_order_id
                + " ORDER BY " + col_sort_order_id;

        String query_all = "SELECT sum(" + col_qty + ") AS " + col_qty
                + ", sum(" + col_selling_price + ")  AS " + col_selling_price
                + ", " + col_item_id
                + ", GROUP_CONCAT( "+ col_item_name + ", '>') AS " + col_item_name
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + " FROM "
                + " ( "
                + query_item + " union all " + query_variants
                + " ) "
                + " GROUP BY " + col_item_id
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed;

        List<HelperSales> helperSales= new LinkedList<HelperSales>();
        helperSales.clear();

        String query = "SELECT (" + col_qty + ") || '(' || (" + col_selling_price + "/" + col_qty + ") || '.00)'  AS " + col_qty
                + ", " + col_selling_price
                + ", " + col_item_id
                + ", " + col_item_name
                + ", " + col_machine_name
                + ", " + col_created_by
                + ", " + col_transaction_per_entry
                + ", " + col_completed
                + " FROM "
                + " ( "
                + query_all
                + " ) "
                + " ORDER BY " + col_transaction_per_entry + " DESC";

        Log.d("listSummaryPreCompleted", query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        HelperSales helperSale = null;

        if (cursor.moveToFirst()) {
            do {
                helperSale = new HelperSales();
                helperSale.setQty(cursor.getString(0));
                helperSale.setSelling_price(cursor.getString(1));
                helperSale.setItem_id(Integer.parseInt(cursor.getString(2)));
                helperSale.setItem_name(cursor.getString(3));
                helperSale.setMachine_name(cursor.getString(4));
                helperSale.setCreated_by(cursor.getString(5));
                helperSale.setTransaction_per_entry(Integer.parseInt(cursor.getString(6)));
                helperSale.setCompleted(cursor.getString(7));
                helperSales.add(helperSale);
            } while (cursor.moveToNext());
        }

        Log.d("listSummaryPreCompleted", query);

        db.close();

        return helperSales;
    }

    public List<HelperSales> listHelperSalesDefaultVariants(HelperItem helperItem){

        List<HelperSales> helperSales= new LinkedList<HelperSales>();
        helperSales.clear();

        String query = "SELECT b.*  FROM " + tbl_variants_links + " a "
                + ", " + tbl_variants_dtls + " b "
                + " WHERE a." + col_item_id + " = " + helperItem.getItem_id()
                + " AND a." + col_var_hdr_id + " = b." + col_var_hdr_id
                + " AND b." + col_var_dtls_default + " = 'Y'";

        Log.d("listHSDeftVar", query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        HelperSales helperSale = null;

        if (cursor.moveToFirst()) {
            do {
                helperSale = new HelperSales();
                helperSale.setItem_id(helperItem.getItem_id());
                helperSale.setMachine_name("pos1");
                helperSale.setVar_hdr_id("" + cursor.getInt( 0 ));
                helperSale.setVar_dtls_id("" + cursor.getInt( 1 ));
                helperSale.setItem_name(cursor.getString(3));
                helperSale.setQty("" + 1);
                helperSale.setSelling_price(cursor.getString(4));
                helperSale.setCreated_by("admin");
                helperSale.setCompleted("W");
                helperSale.setTransaction_counter(nextTransactionCounter());
                helperSale.setTransaction_per_entry(nextTransactionPerEntry());
                if (variantExists(helperSale)){
                    helperSale.setSort_order_id(getSortOrderIdVariant(helperSale));
                } else {
                    helperSale.setSort_order_id(nextSortOrderIdVariant(helperSale));
                }

                helperSales.add(helperSale);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperSales;


    }

    public List<HelperSales> listHelperSalesDefaultVariants(HelperSales helperSalePar){


        String query = "SELECT b.*  FROM " + tbl_variants_links + " a "
                + ", " + tbl_variants_dtls + " b "
                + " WHERE a." + col_item_id + " = " + helperSalePar.getItem_id()
                + " AND a." + col_var_hdr_id + " = b." + col_var_hdr_id
                + " AND b." + col_var_dtls_default + " = 'Y'";

        Log.d("listHSDeftVar", query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        List<HelperSales> helperSales= new LinkedList<HelperSales>();
        helperSales.clear();
        HelperSales helperSale = null;

        if (cursor.moveToFirst()) {
            do {
                helperSale = new HelperSales();
                helperSale.setItem_id(helperSalePar.getItem_id());
                helperSale.setMachine_name(helperSalePar.getMachine_name());
                helperSale.setVar_hdr_id("" + cursor.getInt( 0 ));
                helperSale.setVar_dtls_id("" + cursor.getInt( 1 ));
                helperSale.setItem_name(cursor.getString(3));
                helperSale.setQty("" + 1);
                helperSale.setSelling_price(cursor.getString(4));
                helperSale.setCreated_by(helperSalePar.getCreated_by());
                helperSale.setCompleted(helperSalePar.getCompleted());
                helperSales.add(helperSale);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperSales;

    }

    public List<HelperSales> listSummarySalesPerDay(){
        List<HelperSales> helperSales = new LinkedList<>();
        helperSales.clear();

        String query = "SELECT sum(" + col_selling_price + ") AS " + col_selling_price
                + ", (" + " CASE "
                            + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Jan' THEN '01' "
                            + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Feb' THEN '02' "
                            + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Mar' THEN '03' "
                            + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Apr' THEN '04' "
                            + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'May' THEN '05' "
                            + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Jun' THEN '06' "
                            + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Jul' THEN '07' "
                            + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Aug' THEN '08' "
                            + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Sep' THEN '09' "
                            + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Oct' THEN '10' "
                            + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Nov' THEN '11' "
                            + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Dec' THEN '12' "
                            + " ELSE '01' "
                        + " END "
                        + " || "
                        + " CASE "
                            + " WHEN LENGTH("+ col_date + ") = 12 THEN SUBSTR(" + col_date + ", 5, 2 ) "
                            + " ELSE  '0' || SUBSTR( " + col_date + ", 5, 1) "
                        + " END  "
                    + ")"
                + " FROM " + tbl_sales
                + " WHERE " + col_created_by + " = 'admin'"
                + " AND " + col_machine_name + " = 'pos1'"
                + " AND " + " CASE "
                                + " WHEN LENGTH("+ col_date + ") = 12 THEN SUBSTR(" + col_date + ", 9, 4 ) "
                                + " ELSE  SUBSTR( " + col_date + ", 8, 4) "
                                + " END "
                                + " || '-' || "
                                + " CASE "
                                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Jan' THEN '01' "
                                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Feb' THEN '02' "
                                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Mar' THEN '03' "
                                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Apr' THEN '04' "
                                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'May' THEN '05' "
                                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Jun' THEN '06' "
                                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Jul' THEN '07' "
                                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Aug' THEN '08' "
                                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Sep' THEN '09' "
                                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Oct' THEN '10' "
                                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Nov' THEN '11' "
                                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Dec' THEN '12' "
                                + " ELSE '01' "
                                + " END "
                                + " || '-' || "
                                + " CASE "
                                + " WHEN LENGTH("+ col_date + ") = 12 THEN SUBSTR(" + col_date + ", 5, 2 ) "
                                + " ELSE  '0' || SUBSTR( " + col_date + ", 5, 1) "
                                + " END  " + " >= " + " (SELECT DATETIME('now', '-91 day'))"
                + " GROUP BY " + col_date
                + " ORDER BY 2 "
                ;

        Log.d("listSummarySalesPerDay", query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        HelperSales helperSale = null;
        int row_num = 0;

        if (cursor.moveToFirst()) {
            do {
                helperSale = new HelperSales();
                helperSale.setSelling_price(cursor.getString(0));
                helperSale.setItem_name(cursor.getString(1));
                helperSale.setQty("" + row_num);
                helperSales.add(helperSale);
                row_num += 1;

                //Log.d("listSummarySalesPerDay1", cursor.getString(0));
                //Log.d("listSummarySalesPerDay2", cursor.getString(1));
                //Log.d("listSummarySalesPerDay3", "" + row_num );

            } while (cursor.moveToNext());
        }

        db.close();

        return helperSales;
    }

    public List<HelperSales> listStocksPricePerDay(){
        //set to helpersales so that we can merge this with salesperday later
        List<HelperSales> helperSales = new LinkedList<>();
        helperSales.clear();

        String query = "SELECT sum("
                                + " (CASE "
                                        + " WHEN " + col_in_out + "='Out' THEN -" + col_cost
                                        + " ELSE " + col_cost
                                    + " END) "
                                + ") AS " + col_cost
                + ", (" + " CASE "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ col_time + ") = 12 THEN SUBSTR(" + col_time + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + col_time + ", 5, 1) "
                + " END  "
                + ")"
                + " FROM " + tbl_stocks_history
                + " WHERE 1=1"
                ///+ " AND " + col_username + " = 'admin'"
                + " AND " + col_cost + " IS NOT NULL "
                + " AND " + " CASE "
                + " WHEN LENGTH("+ col_time + ") = 12 THEN SUBSTR(" + col_time + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + col_time + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ col_time + ") = 12 THEN SUBSTR(" + col_time + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + col_time + ", 5, 1) "
                + " END  " + " >= " + " (SELECT DATETIME('now', '-31 day'))"
                // date
                + " AND " + " CASE "
                + " WHEN LENGTH("+ col_time + ") = 12 THEN SUBSTR(" + col_time + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + col_time + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ col_time + ") = 12 THEN SUBSTR(" + col_time + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + col_time + ", 5, 1) "
                + " END  " + " >= " + " (SELECT DATETIME('now', '-91 day'))"
                + " GROUP BY " + col_time
                + " ORDER BY 2 "
                ;

        Log.d("listStocksPricePerDay", query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        HelperSales helperSale = null;
        int row_num = 0;

        if (cursor.moveToFirst()) {
            do {
                helperSale = new HelperSales();
                helperSale.setSelling_price(cursor.getString(0));
                helperSale.setItem_name(cursor.getString(1));
                helperSale.setQty("" + row_num);
                helperSales.add(helperSale);
                row_num += 1;

            } while (cursor.moveToNext());
        }

        db.close();

        return helperSales;
    }

    public int sumCostStocksPerDay(String s_day){

        int s_return = 0;

        String query = "SELECT sum("
                + " (CASE "
                + " WHEN " + col_in_out + "='Out' THEN -" + col_cost
                + " ELSE " + col_cost
                + " END) "
                + ") AS " + col_cost
                + ", (" + " CASE "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ col_time + ") = 12 THEN SUBSTR(" + col_time + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + col_time + ", 5, 1) "
                + " END  "
                + ")"
                + " FROM " + tbl_stocks_history
                + " WHERE 1=1"
                ///+ " AND " + "+ col_username + " = 'admin'"
                + " AND " + col_cost + " IS NOT NULL "
                + " AND " + " (" + " CASE "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ col_time + ") = 12 THEN SUBSTR(" + col_time + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + col_time + ", 5, 1) "
                + " END  "
                + ")"
                + " = "
                + "'" + s_day + "'"
                // date
                + " AND " + " CASE "
                + " WHEN LENGTH("+ col_time + ") = 12 THEN SUBSTR(" + col_time + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + col_time + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ col_time + ") = 12 THEN SUBSTR(" + col_time + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + col_time + ", 5, 1) "
                + " END  " + " >= " + " (SELECT DATETIME('now', '-91 day'))"
                + " GROUP BY " + col_time
                + " ORDER BY 2 "
                ;

        Log.d("listStocksPricePerDay", query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            s_return = cursor.getInt(0);
        }

        db.close();

        return s_return;
    }


    public List<HelperSales> listSalesVsStocksPerDay(){
        //set to helpersales so that we can merge this with salesperday later
        List<HelperSales> helperSales = new LinkedList<>();
        helperSales.clear();

        String query_sales = "SELECT sum(" + col_selling_price + ") AS " + col_selling_price
                + ", (" + " CASE "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ col_date + ") = 12 THEN SUBSTR(" + col_date + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + col_date + ", 5, 1) "
                + " END  "
                + ")" + col_date
                + " FROM " + tbl_sales
                + " WHERE " + col_created_by + " = 'admin'"
                + " AND " + col_machine_name + " = 'pos1'"
                + " AND " + " CASE "
                + " WHEN LENGTH("+ col_date + ") = 12 THEN SUBSTR(" + col_date + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + col_date + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + col_date + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ col_date + ") = 12 THEN SUBSTR(" + col_date + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + col_date + ", 5, 1) "
                + " END  " + " >= " + " (SELECT DATETIME('now', '-31 day'))"
                + " GROUP BY " + col_date
                //+ " ORDER BY 2 "
                ;

        String query_stocks = "SELECT sum("
                + " (CASE "
                + " WHEN " + col_in_out + "='Out' THEN -" + col_cost
                + " ELSE " + col_cost
                + " END) "
                + ") AS " + col_cost
                + ", (" + " CASE "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ col_time + ") = 12 THEN SUBSTR(" + col_time + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + col_time + ", 5, 1) "
                + " END  "
                + ")"
                + " FROM " + tbl_stocks_history
                + " WHERE " + col_username + " = 'admin'"
                + " AND " + col_cost + " IS NOT NULL "
                + " AND " + " CASE "
                + " WHEN LENGTH("+ col_time + ") = 12 THEN SUBSTR(" + col_time + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + col_time + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + col_time + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ col_time + ") = 12 THEN SUBSTR(" + col_time + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + col_time + ", 5, 1) "
                + " END  " + " >= " + " (SELECT DATETIME('now', '-31 day'))"
                + " GROUP BY " + col_time
                //+ " ORDER BY 2 "
                ;

        String query = "SELECT SUM( " + col_selling_price + ") AS " + col_selling_price
                        + ", " + col_date
                        + " FROM "
                        + " ( " + query_sales + " union all " + query_stocks + " ) "
                        + " GROUP BY " + col_date
                        + " ORDER BY 2 "
                        ;


        Log.d("listStocksPricePerDay", query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        HelperSales helperSale = null;
        int row_num = 0;

        if (cursor.moveToFirst()) {
            do {
                helperSale = new HelperSales();
                helperSale.setSelling_price(cursor.getString(0));
                helperSale.setItem_name(cursor.getString(1));
                helperSale.setQty("" + row_num);
                helperSales.add(helperSale);
                row_num += 1;

            } while (cursor.moveToNext());
        }

        db.close();

        return helperSales;
    }





    public int mmMonth(String mon){
        switch (mon){
            case "Jan":
                return 1;
            case "Feb":
                return 2;
            case "Mar":
                return 3;
            case "Apr":
                return 4;
            case "May":
                return 5;
            case "Jun":
                return 6;
            case "Jul":
                return 7;
            case "Aug":
                return 8;
            case "Sep":
                return 9;
            case "Oct":
                return 10;
            case "Nov":
                return 11;
            case "Dec":
                return 12;
            default:
                return 1;
        }
    }

    //SALES end

    //CHANGES start
    public void refreshChanges(List<HelperChanges> helperChanges){

        deleteAllChanges();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < helperChanges.size(); i++) {
            contentValues.put(col_change_all, helperChanges.get(i).getChange_all());
            contentValues.put(col_stock_names, helperChanges.get(i).getStock_names());
            contentValues.put(col_category, helperChanges.get(i).getCategory());
            contentValues.put(col_items, helperChanges.get(i).getItems());
            contentValues.put(col_variants_links, helperChanges.get(i).getVariants_links());
            contentValues.put(col_variants_hdr, helperChanges.get(i).getVariants_hdr());
            contentValues.put(col_variants_dtls, helperChanges.get(i).getVariants_dtls());
            contentValues.put(col_composite_links, helperChanges.get(i).getComposite_links());
            contentValues.put(col_stock_histories, helperChanges.get(i).getStock_histories());

            long result = db.insert(tbl_changes, null, contentValues);
            if (result == -1) {
                Log.d("refreshChanges", "failed insert");
            } else {
                Log.d("refreshChanges", "success insert");
            }
        }

        db.close();

    }

    public void deleteAllChanges(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tbl_changes;
        db.execSQL(query);
    }

    public boolean dbChanged(){
        String query = "SELECT * FROM " + tbl_changes + " WHERE " + col_change_all + " = 'Y'";
        SQLiteDatabase db = this.getWritableDatabase();
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

    public boolean dbChangedStockNames(){
        String query = "SELECT * FROM " + tbl_changes + " WHERE " + col_stock_names + " = 'Y'";
        SQLiteDatabase db = this.getWritableDatabase();
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

    public boolean dbChangedCategory(){
        String query = "SELECT * FROM " + tbl_changes + " WHERE " + col_category + " = 'Y'";
        SQLiteDatabase db = this.getWritableDatabase();
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

    public boolean dbChangedItems(){
        String query = "SELECT * FROM " + tbl_changes + " WHERE " + col_items + " = 'Y'";
        SQLiteDatabase db = this.getWritableDatabase();
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

    public boolean dbChangedVariantsLinks(){
        String query = "SELECT * FROM " + tbl_changes + " WHERE " + col_variants_links + " = 'Y'";
        SQLiteDatabase db = this.getWritableDatabase();
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

    public boolean dbChangedVariantsHdr(){
        String query = "SELECT * FROM " + tbl_changes + " WHERE " + col_variants_hdr + " = 'Y'";
        SQLiteDatabase db = this.getWritableDatabase();
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

    public boolean dbChangedVariantsDtls(){
        String query = "SELECT * FROM " + tbl_changes + " WHERE " + col_variants_dtls + " = 'Y'";
        SQLiteDatabase db = this.getWritableDatabase();
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

    public boolean dbChangedCompositeLinks(){
        String query = "SELECT * FROM " + tbl_changes + " WHERE " + col_composite_links + " = 'Y'";
        SQLiteDatabase db = this.getWritableDatabase();
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

    public boolean dbChangedStockHistories(){
        String query = "SELECT * FROM " + tbl_changes + " WHERE " + col_stock_histories + " = 'Y'";
        SQLiteDatabase db = this.getWritableDatabase();
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

    //CHANGES end

    //COMPOSITE_LINKS start
    public void refreshCompositeLinks(List<HelperCompositeLinks> helperCompositeLinks){

        deleteAllCompositeLinks();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < helperCompositeLinks.size(); i++) {
            contentValues.put(col_composite_link_id, helperCompositeLinks.get(i).getComposite_link_id());
            contentValues.put(col_item_id, helperCompositeLinks.get(i).getItem_id());
            contentValues.put(col_stock_id, helperCompositeLinks.get(i).getStock_id());
            contentValues.put(col_var_hdr_id, helperCompositeLinks.get(i).getVar_hdr_id());
            contentValues.put(col_var_dtls_id, helperCompositeLinks.get(i).getVar_dtls_id());
            contentValues.put(col_qty, helperCompositeLinks.get(i).getQty());
            contentValues.put(col_unit, helperCompositeLinks.get(i).getUnit());

            Log.d("refreshCompositeLinks", helperCompositeLinks.get(i).getItem_id() + " --- " + helperCompositeLinks.get(i).getStock_id() +  " >>>> " + helperCompositeLinks.get(i).getVar_hdr_id() );

            long result = db.insert(tbl_composite_links, null, contentValues);
            if (result == -1) {
                Log.d("refreshCompositeLinks", "failed insert");
            } else {
                Log.d("refreshCompositeLinks", "success insert");
            }
        }

        db.close();

    }

    public void deleteAllCompositeLinks(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tbl_composite_links;
        db.execSQL(query);
    }

    public List<HelperCompositeLinks> listCompositeLinks(){

        List<HelperCompositeLinks> helperCompositeLinks = new LinkedList<HelperCompositeLinks>();
        helperCompositeLinks.clear();

        String query = "SELECT * FROM " + tbl_composite_links ;
        SQLiteDatabase db = this.getWritableDatabase();
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
                helperCompositeLinks.add(helperCompositeLink);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperCompositeLinks;
    }

    public List<HelperCompositeLinks> listCompositeLinks(int var_hdr_id, int vard_dtls_id){

        List<HelperCompositeLinks> helperCompositeLinks = new LinkedList<HelperCompositeLinks>();
        helperCompositeLinks.clear();

        String query = "SELECT * FROM " + tbl_composite_links
                + " WHERE " + col_var_hdr_id + " = " + var_hdr_id
                + " AND " + col_var_dtls_id + " = " + vard_dtls_id;
        SQLiteDatabase db = this.getWritableDatabase();
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
                helperCompositeLinks.add(helperCompositeLink);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperCompositeLinks;
    }

    public List<HelperCompositeLinks> listCompositeLinks(int item_id){

        List<HelperCompositeLinks> helperCompositeLinks = new LinkedList<HelperCompositeLinks>();
        helperCompositeLinks.clear();

        String query = "SELECT * FROM " + tbl_composite_links
                + " WHERE " + col_item_id + " = " + item_id;
        SQLiteDatabase db = this.getWritableDatabase();
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
                helperCompositeLinks.add(helperCompositeLink);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperCompositeLinks;
    }
    //COMPOSITE_LINKS end

    //DINE_IN_OUT start

    public void deleteAllDineInOut(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tbl_dine_in_out;
        db.execSQL(query);
    }

    public void insertDefaultDineInOut(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        deleteAllDineInOut();

        contentValues.put(col_dine_in_out, "Y");
        long result = db.insert(tbl_dine_in_out, null, contentValues);
        if (result == -1) {
            Log.d("insertDefaultDineInOut", "failed insert");
        } else {
            Log.d("insertDefaultDineInOut", "success insert");
        }
    }

    public boolean dineExists(){
        String query = "SELECT * FROM " + tbl_dine_in_out;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.close();
            return true;
        }

        insertDefaultDineInOut();

        db.close();

        return false;
    }

    public boolean dineIn(){
        String query = "SELECT * FROM " + tbl_dine_in_out + " WHERE " + col_dine_in_out + " = 'Y'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.close();
            return true;
        }
        db.close();

        return false;
    }

    public void updateDineInOut(String dine_in_out){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE " + tbl_dine_in_out + " SET "
                + col_dine_in_out + " = '" + dine_in_out + "' ";
        Log.d("updateDineInOut", query);
        db.execSQL(query);
    }

    //DINE_IN_OUT end

    //MAINTAIN start
    //Stock_id different name ( stock_names vs stocks_history )

    public boolean maintainStockNames(){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean ok = true;

        String query = "SELECT " + " a.ROWID"
                + ", a." + col_stock_id
                + ", a." + COL_STOCK_NAME
                + ", b." + col_stock_id
                + ", b." + COL_STOCK_NAME
                + " FROM " + tbl_stocks_history + " a " + ", " + TBL_STOCK_NAMES + " b"
                + " WHERE a." + col_stock_id + " = b." + col_stock_id
                + " AND a." + COL_STOCK_NAME + " <> b." + COL_STOCK_NAME
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

    //MAINTAIN end

}
