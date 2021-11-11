package com.watata.zpos.dbdmlddl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.watata.zpos.DbNames;
import com.watata.zpos.ddlclass.HelperItem;
import com.watata.zpos.ddlclass.HelperSales;
import com.watata.zpos.ddlclass.HelperStockHistory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DbStocksHistory {
    DbNames dbNames;

    public DbStocksHistory(DbNames dbNames) {
        this.dbNames = dbNames;
    }

    public void createTable(SQLiteDatabase db){
        try {
            String createTable = "CREATE TABLE " + dbNames.getTbl_stocks_history()
                    + " ( "
                           + dbNames.getCol_in_out() + " TEXT "
                    + " ," + dbNames.getCol_stock_id() + " INTEGER "
                    + " ," + dbNames.getColStockName() + " TEXT "
                    + " ," + dbNames.getCol_qty() + " NUMBER "
                    + " ," + dbNames.getColMeasureUsed() + " TEXT "
                    + " ," + dbNames.getCol_time() + " TEXT "
                    + " ," + dbNames.getCol_username() + " TEXT "
                    + " ," + dbNames.getCol_cost() + " TEXT "
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
        db.execSQL("DROP TABLE IF EXISTS " + dbNames.getTbl_stocks_history());
    }


    public List<HelperStockHistory> listStocksHistory(SQLiteDatabase db){

        List<HelperStockHistory> helperStockHistories = new LinkedList<HelperStockHistory>();
        helperStockHistories.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_stocks_history();
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

    public void refreshStocksHistory(List<HelperStockHistory> listHelperStockHistories, SQLiteDatabase db){
        deleteStocksHistory(db);

        ContentValues contentValues = new ContentValues();

        for (int i=0; i < listHelperStockHistories.size(); i++) {

            Log.d("refreshStockNames", listHelperStockHistories.get(i).getStock_id() + ">>" + listHelperStockHistories.get(i).getStock_name() + "--" + listHelperStockHistories.get(i).getMeasure_used());

            contentValues.put(dbNames.getCol_in_out(), listHelperStockHistories.get(i).getIn_out());
            contentValues.put(dbNames.getCol_stock_id(), listHelperStockHistories.get(i).getStock_id());
            contentValues.put(dbNames.getColStockName(), listHelperStockHistories.get(i).getStock_name());
            contentValues.put(dbNames.getCol_qty(), listHelperStockHistories.get(i).getQty());
            contentValues.put(dbNames.getColMeasureUsed(), listHelperStockHistories.get(i).getMeasure_used());
            contentValues.put(dbNames.getCol_time(), listHelperStockHistories.get(i).getTime());
            contentValues.put(dbNames.getCol_username(), listHelperStockHistories.get(i).getUsername());
            contentValues.put(dbNames.getCol_cost(), listHelperStockHistories.get(i).getCost());
            long result = db.insert(dbNames.getTbl_stocks_history(), null, contentValues);
            if (result == -1) {
                Log.d("refreshStockNames", "failed insert");
            } else {
                Log.d("refreshStockNames", "success insert");
            }
        }

        db.close();
    }

    public boolean addStocksHistory(HelperStockHistory helperStockHistory, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();

        contentValues.put(dbNames.getCol_in_out(), helperStockHistory.getIn_out());
        contentValues.put(dbNames.getColStockName(), helperStockHistory.getStock_name());
        contentValues.put(dbNames.getCol_qty(), helperStockHistory.getQty());
        contentValues.put(dbNames.getColMeasureUsed(), helperStockHistory.getMeasure_used());
        contentValues.put(dbNames.getCol_time(), helperStockHistory.getTime());
        contentValues.put(dbNames.getCol_username(), helperStockHistory.getUsername());
        long result = db.insert(dbNames.getTblStockNames(), null, contentValues);
        db.close();

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteStocksHistory(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_stocks_history();
        db.execSQL(query);
    }

    public List<HelperStockHistory> listHelperStockHistRem(SQLiteDatabase db){

        List<HelperStockHistory> listHelperStockHistories = new LinkedList<HelperStockHistory>();
        listHelperStockHistories.clear();

        String query = "SELECT " + dbNames.getColStockName()
                + ", " + " SUM( CASE " + dbNames.getCol_in_out() + " WHEN 'Out' THEN -" + dbNames.getCol_qty() + " ELSE " + dbNames.getCol_qty() + " END) AS remaining "
                + ", " + dbNames.getColMeasureUsed()
                + " FROM " + dbNames.getTbl_stocks_history()
                + " GROUP BY " + dbNames.getColStockName()
                + ", " + dbNames.getColMeasureUsed();
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
        //product names like Food Panda, None FP, Grab Food will not insert "out" in dbNames.getTbl_stocks_history()

        return false;
    }

    public boolean stocksOk(int stock_id, SQLiteDatabase db, int needed_qty, String needed_unit){
        String base_unit = "";

        String query_base_unit = "SELECT " + dbNames.getColMeasureUsed()
                + " FROM " + dbNames.getTblStockNames()
                + " WHERE " + dbNames.getCol_stock_id() + " = " + stock_id;
        Cursor cursor_base_unit = db.rawQuery(query_base_unit, null);
        if (cursor_base_unit.moveToFirst()){
            base_unit = cursor_base_unit.getString(0);
        }

        if(!base_unit.equals("")){

            String query;

            if (base_unit.equals(needed_unit)){
                query = "SELECT SUM(CASE WHEN "+ dbNames.getCol_in_out() + " = 'Out' THEN -" + dbNames.getCol_qty() + " ELSE " + dbNames.getCol_qty() + " END) stocks"
                        + ", " + dbNames.getColMeasureUsed()
                        + " FROM " + dbNames.getTbl_stocks_history()
                        + " WHERE " + dbNames.getCol_stock_id() + " = " + stock_id
                        + " GROUP BY " +  dbNames.getColMeasureUsed()
                ;
            } else {
                query = "SELECT SUM(CASE WHEN "+ dbNames.getCol_in_out() + " = 'Out' THEN -" + dbNames.getCol_qty() + " ELSE " + dbNames.getCol_qty() + " END) stocks"
                        + ", " + dbNames.getColMeasureUsed()
                        + " FROM " + dbNames.getTbl_stocks_history()
                        + " WHERE " + dbNames.getCol_stock_id() + " = " + stock_id
                        + " AND " + dbNames.getColMeasureUsed() + " = '" + base_unit + "'"
                        + " GROUP BY " + dbNames.getColMeasureUsed()
                ;
            }


            Log.d("stocksOkActual", query);

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                //if (cursor.getString(1).equals(needed_unit)){
                if (base_unit.equals(needed_unit)){
                    //EXISTS start
                    int ordered_qty = 0;
                    String query_item = "SELECT SUM(a." + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                            + " FROM " + dbNames.getTbl_composite_links() + " a, " + dbNames.getTbl_sales() + " b "
                            + " WHERE a." + dbNames.getCol_stock_id() + " = " + stock_id
                            + " AND a." + dbNames.getCol_unit() + " = '" + needed_unit + "' "
                            + " AND a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                            + " AND a." + dbNames.getCol_var_hdr_id() +  " is null "
                            + " AND a." + dbNames.getCol_var_dtls_id() + " is null "
                            + " AND b." + dbNames.getCol_var_hdr_id() +  " is null "
                            + " AND b." + dbNames.getCol_var_dtls_id() + " is null "
                            + " AND b." + dbNames.getCol_created_by() + " = 'admin'"
                            + " AND b." + dbNames.getCol_machine_name() + " = 'pos1'"
                            + " AND b." + dbNames.getCol_completed() + " IN ( 'W', 'N' )"
                            ;

                    String query_variants = "SELECT SUM(a." + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                            + " FROM " + dbNames.getTbl_composite_links() + " a, " + dbNames.getTbl_sales() + " b "
                            + " WHERE a." + dbNames.getCol_stock_id() + " = " + stock_id
                            + " AND a." + dbNames.getCol_unit() + " = '" + needed_unit + "' "
                            //+ " AND a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                            + " AND a." + dbNames.getCol_var_hdr_id() +  " = b." + dbNames.getCol_var_hdr_id()
                            + " AND a." + dbNames.getCol_var_dtls_id() + " = b." + dbNames.getCol_var_dtls_id()
                            + " AND b." + dbNames.getCol_created_by() + " = 'admin'"
                            + " AND b." + dbNames.getCol_machine_name() + " = 'pos1'"
                            + " AND b." + dbNames.getCol_completed() + " IN ( 'W', 'N' )"
                            ;

                    String query_exists = "SELECT SUM(" + dbNames.getCol_qty() + ") AS " + dbNames.getCol_qty()
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

    public boolean stocksOk(HelperItem helperItem, SQLiteDatabase db){
        //check for item only
        String query = "SELECT " + dbNames.getCol_stock_id()
                + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id() + " = " + helperItem.getItem_id()
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        Log.d("stocksOkchekcItems", query);


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

    public boolean stocksOk(HelperItem helperItem, int item_id, int var_hdr_id, SQLiteDatabase db){
        //check for items variants

        String query_item = "SELECT " + dbNames.getCol_stock_id()
                + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id() + " = " + item_id
                + " AND " + dbNames.getCol_var_hdr_id() +  " is null "
                + " AND " + dbNames.getCol_var_dtls_id() + " is null "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query_variants = "SELECT " + dbNames.getCol_stock_id()
                + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id() + " = " + item_id
                + " AND " + dbNames.getCol_var_hdr_id() +  " = " + var_hdr_id
                + " AND " + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query = "SELECT " + dbNames.getCol_stock_id()
                + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM "
                + " ( "
                + query_item + " union all " + query_variants
                + " ) "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        Log.d("stocksOk", "item with variants " + query);


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

    public boolean stocksOk(HelperSales helperSale, SQLiteDatabase db){
        String query;

        if (helperSale.getVar_hdr_id()!=null){
            String query_item = "SELECT " + dbNames.getCol_stock_id()
                    + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                    + ", " + dbNames.getCol_unit()
                    + " FROM " + dbNames.getTbl_composite_links()
                    + " WHERE " + dbNames.getCol_item_id() + " = " + helperSale.getItem_id()
                    + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                    + " GROUP BY " + dbNames.getCol_stock_id()
                    + ", " + dbNames.getCol_unit()
                    ;

            String query_variants = "SELECT " + dbNames.getCol_stock_id()
                    + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                    + ", " + dbNames.getCol_unit()
                    + " FROM " + dbNames.getTbl_composite_links()
                    + " WHERE " + dbNames.getCol_item_id() + " = " + helperSale.getItem_id()
                    + " AND " + dbNames.getCol_var_hdr_id() + " = " + helperSale.getVar_hdr_id()
                    + " AND " + dbNames.getCol_var_dtls_id() + " = " + helperSale.getVar_dtls_id()
                    + " GROUP BY " + dbNames.getCol_stock_id()
                    + ", " + dbNames.getCol_unit()
                    ;

            query = "SELECT " + dbNames.getCol_stock_id()
                    + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                    + ", " + dbNames.getCol_unit()
                    + " FROM "
                    + " ( "
                    + query_item + " union all " + query_variants
                    + " ) "
                    + " GROUP BY " + dbNames.getCol_stock_id()
                    + ", " + dbNames.getCol_unit()
            ;



        } else {
            query = "SELECT " + dbNames.getCol_stock_id()
                    + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                    + ", " + dbNames.getCol_unit()
                    + " FROM " + dbNames.getTbl_composite_links()
                    + " WHERE " + dbNames.getCol_item_id() + " = " + helperSale.getItem_id()
                    + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                    + " GROUP BY " + dbNames.getCol_stock_id()
                    + ", " + dbNames.getCol_unit()
            ;
        }


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

    public int stocksAvailable(HelperItem helperItem, SQLiteDatabase db){
        //display only the minimum possible order


        //check for item only
        String query = "SELECT " + dbNames.getCol_stock_id()
                + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id() + " = " + helperItem.getItem_id()
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        //REMAINING ORDERS query
        String query_sub0 = "SELECT " + dbNames.getCol_stock_id()
                + ", SUM(  " + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id()  + " =  " + helperItem.getItem_id()
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query_sub1 = "SELECT (CASE WHEN a." + dbNames.getCol_in_out() + "= 'Out' THEN -a." + dbNames.getCol_qty()
                + " ELSE a." + dbNames.getCol_qty()
                + " END) " + dbNames.getCol_qty()
                + ", a." + dbNames.getColMeasureUsed() + " " + dbNames.getCol_unit()
                + ", a." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_stocks_history() + " a "
                + ", ( " + query_sub0 + " ) b "
                + " WHERE a." + dbNames.getCol_stock_id() + "= b." + dbNames.getCol_stock_id()
                + " union all "
                + " SELECT -b." + dbNames.getCol_qty()
                + ", b." + dbNames.getCol_unit()
                + ", b." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_sales() + " a "
                + ", " + dbNames.getTbl_composite_links() + " b "
                + " WHERE 1=1 "
                ///+ " AND b." + dbNames.getCol_item_id() + " = " + helperItem.getItem_id()
                + " AND a." + dbNames.getCol_completed() + " IN ( 'W', 'N' )"
                + " AND a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND a." + dbNames.getCol_var_hdr_id() + " is NULL "
                + " AND b." + dbNames.getCol_var_hdr_id() + " is NULL "
                + " AND EXISTS ( SELECT 1 "
                + " FROM " + dbNames.getTbl_composite_links() + " aa "
                + " WHERE b." + dbNames.getCol_stock_id() + " = aa." + dbNames.getCol_stock_id()
                + " AND aa." + dbNames.getCol_item_id() + " = " + helperItem.getItem_id()
                + " AND aa." + dbNames.getCol_var_hdr_id() + " is NULL ) "
                + " union all "
                + "SELECT 0 " + dbNames.getCol_qty()
                + ", c." + dbNames.getCol_unit()
                + ", c." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_composite_links() + " c "
                + " WHERE c." + dbNames.getCol_item_id()  + " =  " + helperItem.getItem_id()
                + " AND c." + dbNames.getCol_var_hdr_id() + " IS NULL "
                ;

        String query_sub2 = "SELECT SUM( aa." + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", aa." + dbNames.getCol_unit()
                + ", aa." + dbNames.getCol_stock_id()
                + " FROM (" + query_sub1 + " ) aa "
                + " GROUP BY aa." + dbNames.getCol_stock_id()
                + ", aa." + dbNames.getCol_unit()
                ;

        String query_sub31 = "SELECT "
                + "(CASE WHEN x." + dbNames.getCol_qty() + "=0 THEN 1 ELSE x." + dbNames.getCol_qty() + " END) AS " + dbNames.getCol_qty()
                + " FROM " + dbNames.getTbl_composite_links() + " x "
                + " WHERE x." + dbNames.getCol_item_id() + " = " + helperItem.getItem_id()
                + " AND x." + dbNames.getCol_var_hdr_id() + " is null "
                + " AND x." + dbNames.getCol_stock_id() + " = ww." + dbNames.getCol_stock_id()
                ;

        String query_sub3 = "SELECT ww." + dbNames.getCol_qty()
                + " / "
                + " ( " + query_sub31 + " ) " + dbNames.getCol_qty()
                + " FROM ( " + query_sub2 + " ) ww "
                ;

        String query_sub4 = "SELECT MIN( uu." + dbNames.getCol_qty() + " )" + dbNames.getCol_qty()
                + " FROM (" + query_sub3 + " ) uu"
                ;

        Log.d("stocksOkRemainingOrderi", query_sub4);


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

    public int stocksAvailable(HelperSales helperSales, SQLiteDatabase db){
        //display only the minimum possible order

        HelperItem helperItem = new HelperItem();
        helperItem.setItem_id(Integer.parseInt(helperSales.getVar_dtls_id()));
        return ( stocksAvailable(helperItem, "" + helperSales.getItem_id(), helperSales.getVar_hdr_id(), db));

    }

    public int stocksAvailable(HelperItem helperItem, String item_id, String var_hdr_id, SQLiteDatabase db){
        //display only the minimum possible order

        //REMAINING ORDERS query

        String query_sub0item = "SELECT " + dbNames.getCol_stock_id()
                + ", SUM(  " + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id()  + " =  " + item_id
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query_sub100var = "SELECT " + dbNames.getCol_stock_id()
                + ", SUM(  " + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id()  + " =  " + item_id
                + " AND " + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id
                + " AND " + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query_sub1000itemvar = "SELECT " + dbNames.getCol_stock_id()
                + ", SUM( " + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", "  + dbNames.getCol_unit()
                + " FROM ( " + query_sub0item + " UNION ALL " + query_sub100var + " ) "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query_sub1000var = "SELECT " + dbNames.getCol_stock_id()
                + ", SUM( " + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", "  + dbNames.getCol_unit()
                + " FROM ( " + query_sub100var + " ) "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query_sub101itemvar = "SELECT (CASE WHEN a." + dbNames.getCol_in_out() + "= 'Out' THEN -a." + dbNames.getCol_qty()
                + " ELSE a." + dbNames.getCol_qty()
                + " END) " + dbNames.getCol_qty()
                + ", a." + dbNames.getColMeasureUsed() + " " + dbNames.getCol_unit()
                + ", a." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_stocks_history() + " a "
                + ", ( " + query_sub1000itemvar + " ) b "
                + " WHERE a." + dbNames.getCol_stock_id() + "= b." + dbNames.getCol_stock_id()
                + " union all "
                + " SELECT -b." + dbNames.getCol_qty()
                + ", b." + dbNames.getCol_unit()
                + ", b." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_sales() + " a "
                + ", " + dbNames.getTbl_composite_links() + " b "
                + " WHERE a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND a." + dbNames.getCol_completed() + " IN ( 'W', 'N' )"
                + " AND a." + dbNames.getCol_var_hdr_id() + " = b." + dbNames.getCol_var_hdr_id()
                + " AND a." + dbNames.getCol_var_dtls_id() + " = b." + dbNames.getCol_var_dtls_id()
                + " AND EXISTS ( SELECT 1 "
                + " FROM " + dbNames.getTbl_composite_links() + " aa "
                + " WHERE b." + dbNames.getCol_stock_id() + " = aa." + dbNames.getCol_stock_id()
                + " AND aa." + dbNames.getCol_item_id() + " = " + item_id
                + " AND aa." + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id
                + " AND aa." + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                + " ) "
                + " union all "
                + " SELECT -b." + dbNames.getCol_qty()
                + ", b." + dbNames.getCol_unit()
                + ", b." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_sales() + " a "
                + ", " + dbNames.getTbl_composite_links() + " b "
                + " WHERE 1=1 "
                + " AND a." + dbNames.getCol_completed() + " IN ( 'W', 'N' )"
                + " AND a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND a." + dbNames.getCol_var_hdr_id() + " is NULL "
                + " AND b." + dbNames.getCol_var_hdr_id() + " is NULL "
                + " AND EXISTS ( SELECT 1 "
                + " FROM " + dbNames.getTbl_composite_links() + " aa "
                + " WHERE b." + dbNames.getCol_stock_id() + " = aa." + dbNames.getCol_stock_id()
                + " AND aa." + dbNames.getCol_item_id() + " = " + item_id
                + " AND aa." + dbNames.getCol_var_hdr_id() + " is NULL ) "
                + " union all "
                + "SELECT 0 " + dbNames.getCol_qty()
                + ", c." + dbNames.getCol_unit()
                + ", c." + dbNames.getCol_stock_id()
                + " FROM ( " + query_sub0item + " UNION ALL " + query_sub100var + " ) c "
                ;

        String query_sub101var = "SELECT (CASE WHEN a." + dbNames.getCol_in_out() + "= 'Out' THEN -a." + dbNames.getCol_qty()
                + " ELSE a." + dbNames.getCol_qty()
                + " END) " + dbNames.getCol_qty()
                + ", a." + dbNames.getColMeasureUsed() + " " + dbNames.getCol_unit()
                + ", a." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_stocks_history() + " a "
                + ", ( " + query_sub1000var + " ) b "
                + " WHERE a." + dbNames.getCol_stock_id() + "= b." + dbNames.getCol_stock_id()
                + " union all "
                + " SELECT -b." + dbNames.getCol_qty()
                + ", b." + dbNames.getCol_unit()
                + ", b." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_sales() + " a "
                + ", " + dbNames.getTbl_composite_links() + " b "
                + " WHERE a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND a." + dbNames.getCol_completed() + " IN ( 'W', 'N' )"
                + " AND a." + dbNames.getCol_var_hdr_id() + " = b." + dbNames.getCol_var_hdr_id()
                + " AND a." + dbNames.getCol_var_dtls_id() + " = b." + dbNames.getCol_var_dtls_id()
                + " AND EXISTS ( SELECT 1 "
                + " FROM " + dbNames.getTbl_composite_links() + " aa "
                + " WHERE b." + dbNames.getCol_stock_id() + " = aa." + dbNames.getCol_stock_id()
                + " AND aa." + dbNames.getCol_item_id() + " = " + item_id
                + " AND aa." + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id
                + " AND aa." + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                + " ) "
                + " union all "
                + "SELECT 0 " + dbNames.getCol_qty()
                + ", c." + dbNames.getCol_unit()
                + ", c." + dbNames.getCol_stock_id()
                + " FROM ( " + query_sub100var + " ) c "
                ;

        String query_sub102itemvar = "SELECT SUM( aa." + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", aa." + dbNames.getCol_unit()
                + ", aa." + dbNames.getCol_stock_id()
                + " FROM (" + query_sub101itemvar + " ) aa "
                + " GROUP BY aa." + dbNames.getCol_stock_id()
                + ", aa." + dbNames.getCol_unit()
                ;

        String query_sub102var = "SELECT SUM( aa." + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", aa." + dbNames.getCol_unit()
                + ", aa." + dbNames.getCol_stock_id()
                + " FROM (" + query_sub101var + " ) aa "
                + " GROUP BY aa." + dbNames.getCol_stock_id()
                + ", aa." + dbNames.getCol_unit()
                ;

        String query_sub31item = "SELECT "
                + "(CASE WHEN x." + dbNames.getCol_qty() + "=0 THEN 1 ELSE x." + dbNames.getCol_qty() + " END) AS " + dbNames.getCol_qty()
                //+ dbNames.getCol_qty()
                + " FROM " + dbNames.getTbl_composite_links() + " x "
                + " WHERE x." + dbNames.getCol_item_id() + " = " + item_id
                + " AND x." + dbNames.getCol_var_hdr_id() + " is null "
                + " AND x." + dbNames.getCol_stock_id() + " = ww." + dbNames.getCol_stock_id()
                ;

        String query_sub131var = "SELECT "
                ///+ "(CASE WHEN x." + dbNames.getCol_qty() + "=0 THEN 1 ELSE x." + dbNames.getCol_qty() + " END) AS " + dbNames.getCol_qty()
                + dbNames.getCol_qty()
                + " FROM " + dbNames.getTbl_composite_links() + " x "
                + " WHERE x." + dbNames.getCol_item_id() + " = " + item_id
                + " AND x." + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id
                + " AND x." + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                + " AND x." + dbNames.getCol_stock_id() + " = ww." + dbNames.getCol_stock_id()
                ;

        String query_1031itemvar = "SELECT SUM(" + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + " FROM (" + query_sub31item + " UNION ALL " + query_sub131var + " ) ";

        String query_1031var = "SELECT SUM(" + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + " FROM (" + query_sub131var + " ) ";

        String query_sub103itemvar = "SELECT ww." + dbNames.getCol_qty()
                + " / "
                + " ( " + query_1031itemvar + " ) " + dbNames.getCol_qty()
                + " FROM ( " + query_sub102itemvar + " ) ww "
                ;

        String query_sub103var = "SELECT ww." + dbNames.getCol_qty()
                + " / "
                + " ( " + query_1031var + " ) " + dbNames.getCol_qty()
                + " FROM ( " + query_sub102var + " ) ww "
                ;

        String query_sub104itemvar = "SELECT MIN( uu." + dbNames.getCol_qty() + " )" + dbNames.getCol_qty()
                + " FROM (" + query_sub103itemvar + " ) uu"
                ;

        String query_sub104var = "SELECT MIN( uu." + dbNames.getCol_qty() + " )" + dbNames.getCol_qty()
                + " FROM (" + query_sub103var + " ) uu"
                ;

        Log.d("stocksOkRemainingOrderv", query_sub104itemvar);


        //Cursor cursor = db.rawQuery(query, null);
        Cursor cursor = db.rawQuery(query_sub104itemvar, null);

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

    public int stocksAvailableVarOnly(HelperItem helperItem, String item_id, String var_hdr_id, SQLiteDatabase db){
        //display only the minimum possible order

        //REMAINING ORDERS query

        String query_sub0item = "SELECT " + dbNames.getCol_stock_id()
                + ", SUM(  " + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id()  + " =  " + item_id
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query_sub100var = "SELECT " + dbNames.getCol_stock_id()
                + ", SUM(  " + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id()  + " =  " + item_id
                + " AND " + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id
                + " AND " + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query_sub1000itemvar = "SELECT " + dbNames.getCol_stock_id()
                + ", SUM( " + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", "  + dbNames.getCol_unit()
                + " FROM ( " + query_sub0item + " UNION ALL " + query_sub100var + " ) "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query_sub1000var = "SELECT " + dbNames.getCol_stock_id()
                + ", SUM( " + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", "  + dbNames.getCol_unit()
                + " FROM ( " + query_sub100var + " ) "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query_sub101itemvar = "SELECT (CASE WHEN a." + dbNames.getCol_in_out() + "= 'Out' THEN -a." + dbNames.getCol_qty()
                + " ELSE a." + dbNames.getCol_qty()
                + " END) " + dbNames.getCol_qty()
                + ", a." + dbNames.getColMeasureUsed() + " " + dbNames.getCol_unit()
                + ", a." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_stocks_history() + " a "
                + ", ( " + query_sub1000itemvar + " ) b "
                + " WHERE a." + dbNames.getCol_stock_id() + "= b." + dbNames.getCol_stock_id()
                + " union all "
                + " SELECT -b." + dbNames.getCol_qty()
                + ", b." + dbNames.getCol_unit()
                + ", b." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_sales() + " a "
                + ", " + dbNames.getTbl_composite_links() + " b "
                + " WHERE a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND a." + dbNames.getCol_completed() + " IN ( 'W', 'N' )"
                + " AND a." + dbNames.getCol_var_hdr_id() + " = b." + dbNames.getCol_var_hdr_id()
                + " AND a." + dbNames.getCol_var_dtls_id() + " = b." + dbNames.getCol_var_dtls_id()
                + " AND EXISTS ( SELECT 1 "
                + " FROM " + dbNames.getTbl_composite_links() + " aa "
                + " WHERE b." + dbNames.getCol_stock_id() + " = aa." + dbNames.getCol_stock_id()
                + " AND aa." + dbNames.getCol_item_id() + " = " + item_id
                + " AND aa." + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id
                + " AND aa." + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                + " ) "
                + " union all "
                + " SELECT -b." + dbNames.getCol_qty()
                + ", b." + dbNames.getCol_unit()
                + ", b." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_sales() + " a "
                + ", " + dbNames.getTbl_composite_links() + " b "
                + " WHERE 1=1 "
                + " AND a." + dbNames.getCol_completed() + " IN ( 'W', 'N' )"
                + " AND a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND a." + dbNames.getCol_var_hdr_id() + " is NULL "
                + " AND b." + dbNames.getCol_var_hdr_id() + " is NULL "
                + " AND EXISTS ( SELECT 1 "
                + " FROM " + dbNames.getTbl_composite_links() + " aa "
                + " WHERE b." + dbNames.getCol_stock_id() + " = aa." + dbNames.getCol_stock_id()
                + " AND aa." + dbNames.getCol_item_id() + " = " + item_id
                + " AND aa." + dbNames.getCol_var_hdr_id() + " is NULL ) "
                + " union all "
                + "SELECT 0 " + dbNames.getCol_qty()
                + ", c." + dbNames.getCol_unit()
                + ", c." + dbNames.getCol_stock_id()
                + " FROM ( " + query_sub0item + " UNION ALL " + query_sub100var + " ) c "
                ;

        String query_sub101var = "SELECT (CASE WHEN a." + dbNames.getCol_in_out() + "= 'Out' THEN -a." + dbNames.getCol_qty()
                + " ELSE a." + dbNames.getCol_qty()
                + " END) " + dbNames.getCol_qty()
                + ", a." + dbNames.getColMeasureUsed() + " " + dbNames.getCol_unit()
                + ", a." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_stocks_history() + " a "
                + ", ( " + query_sub1000var + " ) b "
                + " WHERE a." + dbNames.getCol_stock_id() + "= b." + dbNames.getCol_stock_id()
                + " union all "
                + " SELECT -b." + dbNames.getCol_qty()
                + ", b." + dbNames.getCol_unit()
                + ", b." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_sales() + " a "
                + ", " + dbNames.getTbl_composite_links() + " b "
                + " WHERE a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND a." + dbNames.getCol_completed() + " IN ( 'W', 'N' )"
                + " AND a." + dbNames.getCol_var_hdr_id() + " = b." + dbNames.getCol_var_hdr_id()
                + " AND a." + dbNames.getCol_var_dtls_id() + " = b." + dbNames.getCol_var_dtls_id()
                + " AND EXISTS ( SELECT 1 "
                + " FROM " + dbNames.getTbl_composite_links() + " aa "
                + " WHERE b." + dbNames.getCol_stock_id() + " = aa." + dbNames.getCol_stock_id()
                + " AND aa." + dbNames.getCol_item_id() + " = " + item_id
                + " AND aa." + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id
                + " AND aa." + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                + " ) "
                + " union all "
                + "SELECT 0 " + dbNames.getCol_qty()
                + ", c." + dbNames.getCol_unit()
                + ", c." + dbNames.getCol_stock_id()
                + " FROM ( " + query_sub100var + " ) c "
                ;

        String query_sub102itemvar = "SELECT SUM( aa." + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", aa." + dbNames.getCol_unit()
                + ", aa." + dbNames.getCol_stock_id()
                + " FROM (" + query_sub101itemvar + " ) aa "
                + " GROUP BY aa." + dbNames.getCol_stock_id()
                + ", aa." + dbNames.getCol_unit()
                ;

        String query_sub102var = "SELECT SUM( aa." + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", aa." + dbNames.getCol_unit()
                + ", aa." + dbNames.getCol_stock_id()
                + " FROM (" + query_sub101var + " ) aa "
                + " GROUP BY aa." + dbNames.getCol_stock_id()
                + ", aa." + dbNames.getCol_unit()
                ;

        String query_sub31item = "SELECT "
                + "(CASE WHEN x." + dbNames.getCol_qty() + "=0 THEN 1 ELSE x." + dbNames.getCol_qty() + " END) AS " + dbNames.getCol_qty()
                //+ dbNames.getCol_qty()
                + " FROM " + dbNames.getTbl_composite_links() + " x "
                + " WHERE x." + dbNames.getCol_item_id() + " = " + item_id
                + " AND x." + dbNames.getCol_var_hdr_id() + " is null "
                + " AND x." + dbNames.getCol_stock_id() + " = ww." + dbNames.getCol_stock_id()
                ;

        String query_sub131var = "SELECT "
                ///+ "(CASE WHEN x." + dbNames.getCol_qty() + "=0 THEN 1 ELSE x." + dbNames.getCol_qty() + " END) AS " + dbNames.getCol_qty()
                + dbNames.getCol_qty()
                + " FROM " + dbNames.getTbl_composite_links() + " x "
                + " WHERE x." + dbNames.getCol_item_id() + " = " + item_id
                + " AND x." + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id
                + " AND x." + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                + " AND x." + dbNames.getCol_stock_id() + " = ww." + dbNames.getCol_stock_id()
                ;

        String query_1031itemvar = "SELECT SUM(" + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + " FROM (" + query_sub31item + " UNION ALL " + query_sub131var + " ) ";

        String query_1031var = "SELECT SUM(" + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + " FROM (" + query_sub131var + " ) ";

        String query_sub103itemvar = "SELECT ww." + dbNames.getCol_qty()
                + " / "
                + " ( " + query_1031itemvar + " ) " + dbNames.getCol_qty()
                + " FROM ( " + query_sub102itemvar + " ) ww "
                ;

        String query_sub103var = "SELECT ww." + dbNames.getCol_qty()
                + " / "
                + " ( " + query_1031var + " ) " + dbNames.getCol_qty()
                + " FROM ( " + query_sub102var + " ) ww "
                ;

        String query_sub104itemvar = "SELECT MIN( uu." + dbNames.getCol_qty() + " )" + dbNames.getCol_qty()
                + " FROM (" + query_sub103itemvar + " ) uu"
                ;

        String query_sub104var = "SELECT MIN( uu." + dbNames.getCol_qty() + " )" + dbNames.getCol_qty()
                + " FROM (" + query_sub103var + " ) uu"
                ;

        Log.d("stocksOkRemainingOrderv", query_sub104itemvar);


        //Cursor cursor = db.rawQuery(query, null);
        Cursor cursor = db.rawQuery(query_sub104var, null);

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



    public List<String> listOfStocks(HelperItem helperItem, SQLiteDatabase db){
        List<String> listStockNames = new LinkedList<>();
        listStockNames.clear();
        String stockName;

        //REMAINING ORDERS query
        String query_sub0 = "SELECT " + dbNames.getCol_stock_id()
                + ", SUM(  " + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id()  + " =  " + helperItem.getItem_id()
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query_sub1 = "SELECT (CASE WHEN a." + dbNames.getCol_in_out() + "= 'Out' THEN -a." + dbNames.getCol_qty()
                + " ELSE a." + dbNames.getCol_qty()
                + " END) " + dbNames.getCol_qty()
                + ", a." + dbNames.getColMeasureUsed() + " " + dbNames.getCol_unit()
                + ", a." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_stocks_history() + " a "
                + ", ( " + query_sub0 + " ) b "
                + " WHERE a." + dbNames.getCol_stock_id() + "= b." + dbNames.getCol_stock_id()
                + " union all "
                + " SELECT -b." + dbNames.getCol_qty()
                + ", b." + dbNames.getCol_unit()
                + ", b." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_sales() + " a "
                + ", " + dbNames.getTbl_composite_links() + " b "
                + " WHERE 1=1 "
                + " AND a." + dbNames.getCol_completed() + " IN ( 'W', 'N' )"
                + " AND a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND a." + dbNames.getCol_var_hdr_id() + " is NULL "
                + " AND b." + dbNames.getCol_var_hdr_id() + " is NULL "
                + " AND EXISTS ( SELECT 1 "
                + " FROM " + dbNames.getTbl_composite_links() + " aa "
                + " WHERE b." + dbNames.getCol_stock_id() + " = aa." + dbNames.getCol_stock_id()
                + " AND aa." + dbNames.getCol_item_id() + " = " + helperItem.getItem_id()
                + " AND aa." + dbNames.getCol_var_hdr_id() + " is NULL ) "
                + " union all "
                + "SELECT 0 " + dbNames.getCol_qty()
                + ", c." + dbNames.getCol_unit()
                + ", c." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_composite_links() + " c "
                + " WHERE c." + dbNames.getCol_item_id()  + " =  " + helperItem.getItem_id()
                + " AND c." + dbNames.getCol_var_hdr_id() + " IS NULL "
                ;

        String query_sub2 = "SELECT SUM( aa." + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", aa." + dbNames.getCol_unit()
                + ", aa." + dbNames.getCol_stock_id()
                + " FROM (" + query_sub1 + " ) aa "
                + " GROUP BY aa." + dbNames.getCol_stock_id()
                + ", aa." + dbNames.getCol_unit()
                ;

        String query_sub31 = "SELECT "
                + "(CASE WHEN x." + dbNames.getCol_qty() + "=0 THEN 1 ELSE x." + dbNames.getCol_qty() + " END) AS " + dbNames.getCol_qty()
                + " FROM " + dbNames.getTbl_composite_links() + " x "
                + " WHERE x." + dbNames.getCol_item_id() + " = " + helperItem.getItem_id()
                + " AND x." + dbNames.getCol_stock_id() + " = ww." + dbNames.getCol_stock_id()
                ;

        String query_sub32 = "SELECT " + dbNames.getColStockName()
                + " FROM " + dbNames.getTblStockNames()
                + " WHERE " + dbNames.getCol_stock_id() + " = ww." + dbNames.getCol_stock_id()
                ;

        String query_sub3 = "SELECT ww." + dbNames.getCol_qty()
                + " / "
                + " ( " + query_sub31 + " ) orders "
                + ", " + " ( " + query_sub32 + " ) stock_name "
                + ", ww." + dbNames.getCol_qty()
                + ", ww." + dbNames.getCol_unit()
                + ", ww." + dbNames.getCol_stock_id()
                + " FROM ( " + query_sub2 + " ) ww "
                ;


        Cursor cursor = db.rawQuery(query_sub3, null);
        if (cursor.moveToFirst()) {
            do {
                stockName = String.format("% 3d", Integer.parseInt(cursor.getString(0))) + " order rem. - " + cursor.getString(1) + " (" + cursor.getString(2) + ") " + cursor.getString(3) + " avl";

                if(stockName!=null){
                    listStockNames.add(stockName);
                }
            } while (cursor.moveToNext());
        }
        db.close();
        return listStockNames;

    }

    public List<String> listOfStocks(HelperItem helperItem, String item_id, String var_hdr_id, SQLiteDatabase db){
        List<String> listStockNames = new LinkedList<>();
        listStockNames.clear();
        String stockName;

        //REMAINING ORDERS query
        String query_sub0 = "SELECT " + dbNames.getCol_stock_id()
                + ", SUM(  " + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id()  + " =  " + item_id
                + " AND " + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id
                + " AND " + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query_sub1 = "SELECT (CASE WHEN a." + dbNames.getCol_in_out() + "= 'Out' THEN -a." + dbNames.getCol_qty()
                + " ELSE a." + dbNames.getCol_qty()
                + " END) " + dbNames.getCol_qty()
                + ", a." + dbNames.getColMeasureUsed() + " " + dbNames.getCol_unit()
                + ", a." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_stocks_history() + " a "
                + ", ( " + query_sub0 + " ) b "
                + " WHERE a." + dbNames.getCol_stock_id() + "= b." + dbNames.getCol_stock_id()
                + " union all "
                + " SELECT -b." + dbNames.getCol_qty()
                + ", b." + dbNames.getCol_unit()
                + ", b." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_sales() + " a "
                + ", " + dbNames.getTbl_composite_links() + " b "
                + " WHERE 1=1 "
                + " AND a." + dbNames.getCol_completed() + " IN ( 'W', 'N' )"
                + " AND a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND a." + dbNames.getCol_var_hdr_id() + " = b." + dbNames.getCol_var_hdr_id()
                + " AND a." + dbNames.getCol_var_dtls_id() + " = b." + dbNames.getCol_var_dtls_id()
                + " AND EXISTS ( SELECT 1 "
                + " FROM " + dbNames.getTbl_composite_links() + " aa "
                + " WHERE b." + dbNames.getCol_stock_id() + " = aa." + dbNames.getCol_stock_id()
                + " AND aa." + dbNames.getCol_item_id() + " = " + item_id
                + " AND aa." + dbNames.getCol_var_hdr_id() + " = " +  var_hdr_id
                + " AND aa." + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                + " ) "
                + " union all "
                + "SELECT 0 " + dbNames.getCol_qty()
                + ", c." + dbNames.getCol_unit()
                + ", c." + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTbl_composite_links() + " c "
                + " WHERE c." + dbNames.getCol_item_id()  + " =  " + item_id
                + " AND c." + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id
                + " AND c." + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                ;

        String query_sub2 = "SELECT SUM( aa." + dbNames.getCol_qty() + " ) " + dbNames.getCol_qty()
                + ", aa." + dbNames.getCol_unit()
                + ", aa." + dbNames.getCol_stock_id()
                + " FROM (" + query_sub1 + " ) aa "
                + " GROUP BY aa." + dbNames.getCol_stock_id()
                + ", aa." + dbNames.getCol_unit()
                ;

        String query_sub31 = "SELECT "
                + "(CASE WHEN x." + dbNames.getCol_qty() + "=0 THEN 1 ELSE x." + dbNames.getCol_qty() + " END) AS " + dbNames.getCol_qty()
                + " FROM " + dbNames.getTbl_composite_links() + " x "
                + " WHERE x." + dbNames.getCol_item_id() + " = " + item_id
                + " AND x." + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id
                + " AND x." + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                + " AND x." + dbNames.getCol_stock_id() + " = ww." + dbNames.getCol_stock_id()
                ;

        String query_sub32 = "SELECT " + dbNames.getColStockName()
                + " FROM " + dbNames.getTblStockNames()
                + " WHERE " + dbNames.getCol_stock_id() + " = ww." + dbNames.getCol_stock_id()
                ;

        String query_sub3 = "SELECT ww." + dbNames.getCol_qty()
                + " / "
                + " ( " + query_sub31 + " ) orders "
                + ", " + " ( " + query_sub32 + " ) stock_name "
                + ", ww." + dbNames.getCol_qty()
                + ", ww." + dbNames.getCol_unit()
                + ", ww." + dbNames.getCol_stock_id()
                + " FROM ( " + query_sub2 + " ) ww "
                ;


        Cursor cursor = db.rawQuery(query_sub3, null);
        if (cursor.moveToFirst()) {
            do {
                stockName = String.format("% 3d", Integer.parseInt(cursor.getString(0))) + " order rem. - " + cursor.getString(1) + " (" + cursor.getString(2) + ") " + cursor.getString(3) + " avl";

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
        String query_check_unit = "SELECT COUNT(DISTINCT " + dbNames.getColMeasureUsed() + ") cnt"
                + " FROM " + dbNames.getTbl_stocks_history()
                + " WHERE " + dbNames.getCol_stock_id() + " = " + stock_id;
        Cursor cursor_check_unit = db.rawQuery(query_check_unit, null);

        if (cursor_check_unit.moveToFirst()) {
            check_unit = cursor_check_unit.getInt(0);
            Log.d("outOfStockName", "" + check_unit);
            if (check_unit == 0) {
                String query_name = "SELECT " + dbNames.getColStockName() + " FROM " + dbNames.getTblStockNames() + " WHERE " + dbNames.getCol_stock_id() + " = '" + stock_id + "'";
                Cursor cursor_name = db.rawQuery(query_name, null);
                if (cursor_name.moveToFirst()) {
                    return cursor_name.getString(0) + " (0)stocks";
                }

            }
        }

        String query_base_unit = "SELECT " + dbNames.getColMeasureUsed()
                + " FROM " + dbNames.getTblStockNames()
                + " WHERE " + dbNames.getCol_stock_id() + " = " + stock_id;
        Cursor cursor_base_unit = db.rawQuery(query_base_unit, null);
        if (cursor_base_unit.moveToFirst()) {
            base_unit = cursor_base_unit.getString(0);
        }

        if (!base_unit.equals("")){

            String query;

            if (base_unit.equals(needed_unit)) {
                query = "SELECT SUM(CASE WHEN " + dbNames.getCol_in_out() + " = 'Out' THEN -" + dbNames.getCol_qty() + " ELSE " + dbNames.getCol_qty() + " END) stocks"
                        + ", " + dbNames.getColStockName()
                        + ", " + dbNames.getColMeasureUsed()
                        + " FROM " + dbNames.getTbl_stocks_history()
                        + " WHERE " + dbNames.getCol_stock_id() + " = " + stock_id
                        + " GROUP BY " + dbNames.getColStockName()
                        + ", " + dbNames.getColMeasureUsed()
                ;
            } else {
                query = "SELECT SUM(CASE WHEN " + dbNames.getCol_in_out() + " = 'Out' THEN -" + dbNames.getCol_qty() + " ELSE " + dbNames.getCol_qty() + " END) stocks"
                        + ", " + dbNames.getColStockName()
                        + ", " + dbNames.getColMeasureUsed()
                        + " FROM " + dbNames.getTbl_stocks_history()
                        + " WHERE " + dbNames.getCol_stock_id() + " = " + stock_id
                        + " AND " + dbNames.getColMeasureUsed() + " = '" + base_unit + "'"
                        + " GROUP BY " + dbNames.getColStockName()
                        + ", " + dbNames.getColMeasureUsed()
                ;
            }

            Log.d("outOfStockName", "" + query);

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                //if (cursor.getString(2).equals(needed_unit)){
                if (base_unit.equals(needed_unit)) {
                    //EXISTS start
                    int ordered_qty = 0;
                    String query_item = "SELECT SUM(a." + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                            + " FROM " + dbNames.getTbl_composite_links() + " a, " + dbNames.getTbl_sales() + " b "
                            + " WHERE a." + dbNames.getCol_stock_id() + " = " + stock_id
                            + " AND a." + dbNames.getCol_unit() + " = '" + needed_unit + "' "
                            + " AND a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                            + " AND a." + dbNames.getCol_var_hdr_id() + " is null "
                            + " AND a." + dbNames.getCol_var_dtls_id() + " is null "
                            + " AND b." + dbNames.getCol_var_hdr_id() + " is null "
                            + " AND b." + dbNames.getCol_var_dtls_id() + " is null "
                            + " AND b." + dbNames.getCol_created_by() + " = 'admin'"
                            + " AND b." + dbNames.getCol_machine_name() + " = 'pos1'"
                            + " AND b." + dbNames.getCol_completed() + " IN ( 'W', 'N' )";

                    String query_variants = "SELECT SUM(a." + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                            + " FROM " + dbNames.getTbl_composite_links() + " a, " + dbNames.getTbl_sales() + " b "
                            + " WHERE " + dbNames.getCol_stock_id() + " = " + stock_id
                            //+ " AND a." + dbNames.getCol_unit() + " = '" + needed_unit + "' "
                            + " AND a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                            + " AND a." + dbNames.getCol_var_hdr_id() + " = b." + dbNames.getCol_var_hdr_id()
                            + " AND a." + dbNames.getCol_var_dtls_id() + " = b." + dbNames.getCol_var_dtls_id()
                            + " AND b." + dbNames.getCol_created_by() + " = 'admin'"
                            + " AND b." + dbNames.getCol_machine_name() + " = 'pos1'"
                            + " AND b." + dbNames.getCol_completed() + " IN ( 'W', 'N' )";

                    String query_exists = "SELECT SUM(" + dbNames.getCol_qty() + ") AS " + dbNames.getCol_qty()
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


    public List<String> listOutOfStocks(HelperItem helperItem, SQLiteDatabase db){
        List<String> listOutStockNames = new LinkedList<>();
        listOutStockNames.clear();
        String stockName;

        //check for item only
        String query = "SELECT " + dbNames.getCol_stock_id()
                + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id() + " = " + helperItem.getItem_id()
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

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

    public List<String> listOutOfStocks(HelperItem helperItem, int item_id, int var_hdr_id, SQLiteDatabase db){
        List<String> listOutStockNames = new LinkedList<>();
        listOutStockNames.clear();
        String stockName;

        //check for items variants
        String query_item = "SELECT " + dbNames.getCol_stock_id()
                + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id() + " = " + item_id
                + " AND " + dbNames.getCol_var_hdr_id() +  " is null "
                + " AND " + dbNames.getCol_var_dtls_id() + " is null "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query_variants = "SELECT " + dbNames.getCol_stock_id()
                + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM " + dbNames.getTbl_composite_links()
                + " WHERE " + dbNames.getCol_item_id() + " = " + item_id
                + " AND " + dbNames.getCol_var_hdr_id() +  " = " + var_hdr_id
                + " AND " + dbNames.getCol_var_dtls_id() + " = " + helperItem.getItem_id()
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        String query = "SELECT " + dbNames.getCol_stock_id()
                + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_unit()
                + " FROM "
                + " ( "
                + query_item + " union all " + query_variants
                + " ) "
                + " GROUP BY " + dbNames.getCol_stock_id()
                + ", " + dbNames.getCol_unit()
                ;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Log.d("listOutOfStocks", "cursor1=" + cursor.getInt(0) + " cursor2=" + cursor.getInt(1) + " cursor3=" + cursor.getString(2));
                stockName = outOfStockName(cursor.getInt(0), db, cursor.getInt(1), cursor.getString(2));
                if(stockName!=null){
                    listOutStockNames.add(stockName);
                }
            } while (cursor.moveToNext());

        }
        db.close();
        return listOutStockNames;
    }

    public List<String> listOutOfStocks(HelperSales helperSale, SQLiteDatabase db){
        List<String> listOutStockNames = new LinkedList<>();
        listOutStockNames.clear();
        String stockName;

        String query;
        if (helperSale.getVar_hdr_id()!=null){
            String query_item = "SELECT " + dbNames.getCol_stock_id()
                    + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                    + ", " + dbNames.getCol_unit()
                    + " FROM " + dbNames.getTbl_composite_links()
                    + " WHERE " + dbNames.getCol_item_id() + " = " + helperSale.getItem_id()
                    + " AND " + dbNames.getCol_var_hdr_id() + " is null "
                    + " AND " + dbNames.getCol_var_dtls_id() + " is null "
                    + " GROUP BY " + dbNames.getCol_stock_id()
                    + ", " + dbNames.getCol_unit()
                    ;

            String query_variants = "SELECT " + dbNames.getCol_stock_id()
                    + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                    + ", " + dbNames.getCol_unit()
                    + " FROM " + dbNames.getTbl_composite_links()
                    + " WHERE " + dbNames.getCol_item_id() + " = " + helperSale.getItem_id()
                    + " AND " + dbNames.getCol_var_hdr_id() + " = " + helperSale.getVar_hdr_id()
                    + " AND " + dbNames.getCol_var_dtls_id() + " = " + helperSale.getVar_dtls_id()
                    + " GROUP BY " + dbNames.getCol_stock_id()
                    + ", " + dbNames.getCol_unit()
                    ;

            query = "SELECT " + dbNames.getCol_stock_id()
                    + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                    + ", " + dbNames.getCol_unit()
                    + " FROM "
                    + " ( "
                    + query_item + " union all " + query_variants
                    + " ) "
                    + " GROUP BY " + dbNames.getCol_stock_id()
                    + ", " + dbNames.getCol_unit()
            ;
        } else {
            query = "SELECT " + dbNames.getCol_stock_id()
                    + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                    + ", " + dbNames.getCol_unit()
                    + " FROM " + dbNames.getTbl_composite_links()
                    + " WHERE " + dbNames.getCol_item_id() + " = " + helperSale.getItem_id()
                    + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                    + " GROUP BY " + dbNames.getCol_stock_id()
                    + ", " + dbNames.getCol_unit()
            ;
        }


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

    public int priceInItemMenu(HelperItem helperItem, SQLiteDatabase db){
        String query = "SELECT MAX(CAST(a." + dbNames.getCol_var_selling_price()
                + " as INT)) "
                + " FROM " + dbNames.getTbl_variants_dtls() + " a, " + dbNames.getTbl_variants_links() + " b "
                + " WHERE a." + dbNames.getCol_var_hdr_id() + " = b." + dbNames.getCol_var_hdr_id()
                + " AND b." + dbNames.getCol_item_id() + " = " + helperItem.getItem_id();
        ;

        Log.d("priceInItemMenu", query);


        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int getStockId(String stock_name, SQLiteDatabase db){

        String query = "SELECT " + dbNames.getCol_stock_id()
                + " FROM " + dbNames.getTblStockNames()
                + " WHERE " + dbNames.getColStockName() + " = '" + stock_name + "'";
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }



    public List<HelperStockHistory> listStockUsedInPreFinishSale(List<HelperSales> helperSales, SQLiteDatabase db){
        List<HelperStockHistory> helperStockHistories = new LinkedList<>();
        helperStockHistories.clear();
        String query;

        for(int i = 0; i < helperSales.size(); i++){
            if(helperSales.get(i).getVar_hdr_id()==null){
                query = "SELECT " + dbNames.getCol_stock_id()
                        + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                        + ", " + dbNames.getCol_unit()
                        + " FROM " + dbNames.getTbl_composite_links()
                        + " WHERE " + dbNames.getCol_item_id() + " = " + helperSales.get(i).getItem_id()
                        + " AND " + dbNames.getCol_var_hdr_id() + " is null "
                        + " AND " + dbNames.getCol_var_dtls_id() + " is null "
                        + " GROUP BY " + dbNames.getCol_stock_id()
                        + ", " + dbNames.getCol_unit()
                ;
            } else {
                query = "SELECT " + dbNames.getCol_stock_id()
                        + ", " + "SUM(" + dbNames.getCol_qty() + ") " + dbNames.getCol_qty()
                        + ", " + dbNames.getCol_unit()
                        + " FROM " + dbNames.getTbl_composite_links()
                        + " WHERE " + dbNames.getCol_item_id() + " = " + helperSales.get(i).getItem_id()
                        + " AND " + dbNames.getCol_var_hdr_id() + " = " + helperSales.get(i).getVar_hdr_id()
                        + " AND " + dbNames.getCol_var_dtls_id() + " = " + helperSales.get(i).getVar_dtls_id()
                        + " GROUP BY " + dbNames.getCol_stock_id()
                        + ", " + dbNames.getCol_unit()
                ;
            }
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToFirst()){
                do{
                    HelperStockHistory helperStockHistory = new HelperStockHistory();
                    helperStockHistory.setStock_id(cursor.getInt(0));
                    helperStockHistory.setIn_out("Out");

                    String query_stock_name = "SELECT " + dbNames.getColStockName() + " FROM " + dbNames.getTblStockNames() + " WHERE " + dbNames.getCol_stock_id() + " = '" + cursor.getInt(0) + "'";
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
    
}
