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

public class DbSales {

    DbNames dbNames;

    public DbSales(DbNames dbNames) {
        this.dbNames = dbNames;
    }

    public void createTable(SQLiteDatabase db){
        try {
            String createTable = "CREATE TABLE " + dbNames.getTbl_sales() 
                    + " ( " 
                           + dbNames.getCol_transaction_id() + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + " ," + dbNames.getCol_transaction_counter() + " INTEGER "
                    + " ," + dbNames.getCol_transaction_per_entry() + " INTEGER "
                    + " ," + dbNames.getCol_item_id() + " INTEGER "
                    + " ," + dbNames.getCol_sort_order_id() + " INTEGER "
                    + " ," + dbNames.getCol_machine_name() + " TEXT "
                    + " ," + dbNames.getCol_var_hdr_id() + " TEXT "
                    + " ," + dbNames.getCol_var_dtls_id() + " TEXT "
                    + " ," + dbNames.getCol_item_name() + " TEXT "
                    + " ," + dbNames.getCol_qty() + " TEXT "
                    + " ," + dbNames.getCol_selling_price() + " TEXT "
                    + " ," + dbNames.getCol_date() + " TEXT "
                    + " ," + dbNames.getCol_created_by() + " TEXT "
                    + " ," + dbNames.getCol_completed() + " TEXT "
                    + " ," + dbNames.getCol_dine_in_out() + " TEXT "
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
        db.execSQL("DROP TABLE IF EXISTS " + dbNames.getTbl_sales());
    }


    public void refreshSales(List<HelperSales> listHelperSales, SQLiteDatabase db){
        deleteSales(db);
        ContentValues contentValues = new ContentValues();

        for (int i=0; i < listHelperSales.size(); i++) {
            contentValues.put(dbNames.getCol_transaction_id(), listHelperSales.get(i).getTransaction_id());
            contentValues.put(dbNames.getCol_transaction_counter(), listHelperSales.get(i).getTransaction_counter());
            contentValues.put(dbNames.getCol_transaction_per_entry(), listHelperSales.get(i).getTransaction_per_entry());
            contentValues.put(dbNames.getCol_item_id(), listHelperSales.get(i).getItem_id());
            contentValues.put(dbNames.getCol_sort_order_id(), listHelperSales.get(i).getSort_order_id());
            contentValues.put(dbNames.getCol_machine_name(), listHelperSales.get(i).getMachine_name());
            contentValues.put(dbNames.getCol_var_hdr_id(), listHelperSales.get(i).getVar_dtls_id());
            contentValues.put(dbNames.getCol_var_dtls_id(), listHelperSales.get(i).getVar_dtls_id());
            contentValues.put(dbNames.getCol_item_name(), listHelperSales.get(i).getItem_name());
            contentValues.put(dbNames.getCol_qty(), listHelperSales.get(i).getQty());
            contentValues.put(dbNames.getCol_selling_price(), listHelperSales.get(i).getSelling_price());
            contentValues.put(dbNames.getCol_date(), listHelperSales.get(i).getDate());
            contentValues.put(dbNames.getCol_created_by(), listHelperSales.get(i).getCreated_by());
            contentValues.put(dbNames.getCol_completed(), listHelperSales.get(i).getCompleted());
            contentValues.put(dbNames.getCol_dine_in_out(), listHelperSales.get(i).getDine_in_out());

            long result = db.insert(dbNames.getTbl_sales(), null, contentValues);
            if (result == -1) {
                Log.d("refreshSales", "failed sales refresh");
            } else {
                Log.d("refreshSales", "success sales refresh");
            }
        }

        db.close();
        
    }

    public void deleteSales(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_sales();
        db.execSQL(query);
    }

    public void deleteSalesW(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_sales() 
                + " WHERE " + dbNames.getCol_completed() + " = 'W'";
        db.execSQL(query);
    }

    public void deleteVariant(HelperSales helperSale, SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_transaction_per_entry() + " = '" + helperSale.getTransaction_per_entry() + "'"
                + " AND " + dbNames.getCol_var_hdr_id() + " = '" + helperSale.getVar_hdr_id() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + dbNames.getCol_completed() + " = '" + helperSale.getCompleted() + "'"
                ;
        Log.d("query", query);
        db.execSQL(query);
    }

    public void deleteSale(HelperSales helperSale, SQLiteDatabase db){
        String query = "";
        if (helperSale.getVar_dtls_id() != null){
            query = "DELETE FROM " + dbNames.getTbl_sales()
                    + " WHERE ROWID = ("
                    + " SELECT ROWID FROM " + dbNames.getTbl_sales()
                    + " WHERE " + dbNames.getCol_transaction_per_entry() + " = '" + helperSale.getTransaction_per_entry() + "'"
                    + " AND " + dbNames.getCol_item_id() + " = '" + helperSale.getItem_id() + "'"
                    + " AND " + dbNames.getCol_var_dtls_id() + " = '" + helperSale.getVar_dtls_id() + "'"
                    + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                    + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                    + " AND " + dbNames.getCol_completed() + " = '" + helperSale.getCompleted() + "'"
                    + " limit 1"
                    + ")";
        } else {
            query = "DELETE FROM " + dbNames.getTbl_sales()
                    + " WHERE ROWID = ("
                    + " SELECT ROWID FROM " + dbNames.getTbl_sales()
                    + " WHERE " + dbNames.getCol_transaction_per_entry() + " = '" + helperSale.getTransaction_per_entry() + "'"
                    + " AND " + dbNames.getCol_item_id() + " = '" + helperSale.getItem_id() + "'"
                    + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                    + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                    + " AND " + dbNames.getCol_completed() + " = '" + helperSale.getCompleted() + "'"
                    + " limit 1"
                    + ")";
        }
        Log.d("deleteSale", query);
        db.execSQL(query);
    }

    public void addToCart(HelperSales helperSale, SQLiteDatabase db){

        String query = "UPDATE " + dbNames.getTbl_sales() + " SET "
                + dbNames.getCol_completed() + " = 'N'"
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + dbNames.getCol_completed() + " = 'W'";
        Log.d("query", query);
        db.execSQL(query);

        updateDuplicateVariants(helperSale, db);

    }

    public void updateDuplicateVariants(HelperSales helperSale, SQLiteDatabase db){

        
        //MERGE START with variants( ITEM also included )
        String query_concat = "SELECT " + dbNames.getCol_transaction_per_entry()
                + ", (" + dbNames.getCol_item_id()
                + " || '-'"
                + " || " + dbNames.getCol_var_hdr_id()
                + " || " + dbNames.getCol_var_dtls_id() + " ) AS concat_columns "
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + dbNames.getCol_completed() + " = 'N'"
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NOT NULL "
                + " ORDER BY 2, 1"
                ;

        String query = "SELECT " + dbNames.getCol_transaction_per_entry()
                + ", GROUP_CONCAT( DISTINCT concat_columns ) AS gconcat_columns "
                + " FROM " + " ( " + query_concat + " ) "
                + " GROUP BY " + dbNames.getCol_transaction_per_entry()
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
        String query_item_only = "SELECT " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_item_id()
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + dbNames.getCol_item_id() + " = " + helperSale.getItem_id()
                + " AND " + dbNames.getCol_completed() + " = 'N'"
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                + " EXCEPT "
                + "SELECT " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_item_id()
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + dbNames.getCol_item_id() + " = " + helperSale.getItem_id()
                + " AND " + dbNames.getCol_completed() + " = 'N'"
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NOT NULL "
                + " ORDER BY 2, 1"
                ;

        String query_item_only_all = "SELECT " + dbNames.getCol_transaction_per_entry()
                + ", GROUP_CONCAT( DISTINCT " + dbNames.getCol_item_id() + " ) AS gconcat_columns "
                + " FROM " + " ( " + query_item_only + " ) "
                + " GROUP BY " + dbNames.getCol_transaction_per_entry()
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
        String query_update_item = "UPDATE " + dbNames.getTbl_sales()
                + " SET " + dbNames.getCol_transaction_per_entry() + " = " + base_per_entry
                + " WHERE " + dbNames.getCol_transaction_per_entry() + " in (" + all_entry + ") "
                + " AND " + dbNames.getCol_item_id() + " = " + item_id_dup
                + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + dbNames.getCol_completed() + " = 'N'";
        ///Log.d("updateDuplicateVariantsUpdate", query_update_item);
        db.execSQL(query_update_item);
    }

    public boolean haveOtherVariants(String per_entry, String item_id, String var_hdr_id, String var_hdr_dtls_id, SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_completed() + " = 'N'"
                + " AND " + dbNames.getCol_transaction_per_entry() + " = " + per_entry
                + " AND " + dbNames.getCol_item_id() + " = " + item_id
                + " AND " + dbNames.getCol_var_hdr_id() + " = " + var_hdr_id
                + " AND " + dbNames.getCol_var_dtls_id() + " != " + var_hdr_dtls_id
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL ";
        ;
        
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

    public List<HelperStockHistory> listStocksAddedToCart(HelperSales helperSaleIn, SQLiteDatabase db){
        List<HelperStockHistory> helperStockHistories = new LinkedList<>();
        helperStockHistories.clear();

        String query_item = "SELECT 'Out' " + dbNames.getCol_in_out()
                + ", ( SELECT " + dbNames.getColStockName() + " FROM " + dbNames.getTblStockNames() + " c WHERE c." + dbNames.getCol_stock_id() + " = b." + dbNames.getCol_stock_id() + " ) AS " + dbNames.getColStockName()
                + ", b." + dbNames.getCol_qty()
                + ", b." + dbNames.getCol_unit()
                + ", a." + dbNames.getCol_date()
                + ", a." + dbNames.getCol_created_by()
                + " FROM " + dbNames.getTbl_sales() + " AS a, " + dbNames.getTbl_composite_links() + " AS b"
                + " WHERE a." + dbNames.getCol_created_by() + " = '" + helperSaleIn.getCreated_by() + "'"
                + " AND a." + dbNames.getCol_machine_name() + " = '" + helperSaleIn.getMachine_name() + "'"
                + " AND a." + dbNames.getCol_completed() + " = 'N'"
                + " AND a." + dbNames.getCol_var_hdr_id() + " is null "
                + " AND a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND b." + dbNames.getCol_var_hdr_id() + " is null "
                ;

        String query_var = "SELECT 'Out' " + dbNames.getCol_in_out()
                + ", ( SELECT " + dbNames.getColStockName() + " FROM " + dbNames.getTblStockNames() + " c WHERE c." + dbNames.getCol_stock_id() + " = b." + dbNames.getCol_stock_id() + " ) AS " + dbNames.getColStockName()
                + ", b." + dbNames.getCol_qty()
                + ", b." + dbNames.getCol_unit()
                + ", a." + dbNames.getCol_date()
                + ", a." + dbNames.getCol_created_by()
                + " FROM " + dbNames.getTbl_sales() + " AS a, " + dbNames.getTbl_composite_links() + " AS b"
                + " WHERE a." + dbNames.getCol_created_by() + " = '" + helperSaleIn.getCreated_by() + "'"
                + " AND a." + dbNames.getCol_machine_name() + " = '" + helperSaleIn.getMachine_name() + "'"
                + " AND a." + dbNames.getCol_completed() + " = 'N'"
                + " AND a." + dbNames.getCol_var_hdr_id() + " = b." + dbNames.getCol_var_hdr_id()
                + " AND a." + dbNames.getCol_var_dtls_id() + " = b." + dbNames.getCol_var_dtls_id()
                + " AND a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                ;

        String query = query_item + " union all " + query_var;

        Log.d("listStocksAddedToCart", query);

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

    public List<HelperSales> listAddedToCart(HelperSales helperSaleIn, SQLiteDatabase db){
        List<HelperSales> helperSales = new LinkedList<>();
        helperSales.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSaleIn.getCreated_by() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSaleIn.getMachine_name() + "'"
                + " AND " + dbNames.getCol_completed() + " = 'N'";
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

    public List<HelperSales> listPreFinishSale(HelperSales helperSaleParam, SQLiteDatabase db){
        List<HelperSales> HelperSales = new LinkedList<HelperSales>();
        HelperSales.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSaleParam.getCreated_by() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSaleParam.getMachine_name() + "'"
                + " AND " + dbNames.getCol_completed() + " = 'N'";
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
                helperSale.setCompleted("Y"); //set completed, finishSale will be called
                HelperSales.add(helperSale);
            } while (cursor.moveToNext());
        }

        db.close();

        return HelperSales;
    }


    public void finishSale(HelperSales helperSale, SQLiteDatabase db){
        String query = "UPDATE " + dbNames.getTbl_sales() + " SET "
                + dbNames.getCol_completed() + " = 'Y'"
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + dbNames.getCol_completed() + " = 'N'";
        Log.d("query", query);
        db.execSQL(query);
    }

    public void insertSaleNew(HelperSales helperSale, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();

        //contentValues.put(dbNames.getCol_transaction_id(), helperSale.getTransaction_id());
        contentValues.put(dbNames.getCol_transaction_counter(), helperSale.getTransaction_counter());
        contentValues.put(dbNames.getCol_transaction_per_entry(), helperSale.getTransaction_per_entry());
        contentValues.put(dbNames.getCol_item_id(), helperSale.getItem_id());
        contentValues.put(dbNames.getCol_sort_order_id(), helperSale.getSort_order_id());
        contentValues.put(dbNames.getCol_machine_name(), helperSale.getMachine_name());
        contentValues.put(dbNames.getCol_var_hdr_id(), helperSale.getVar_hdr_id());
        contentValues.put(dbNames.getCol_var_dtls_id(), helperSale.getVar_dtls_id());
        contentValues.put(dbNames.getCol_item_name(), helperSale.getItem_name());
        contentValues.put(dbNames.getCol_qty(), helperSale.getQty());
        contentValues.put(dbNames.getCol_selling_price(), helperSale.getSelling_price());
        contentValues.put(dbNames.getCol_date(), helperSale.getDate());
        contentValues.put(dbNames.getCol_created_by(), helperSale.getCreated_by());
        contentValues.put(dbNames.getCol_completed(), helperSale.getCompleted());
        contentValues.put(dbNames.getCol_dine_in_out(), helperSale.getDine_in_out());
        long result = db.insert(dbNames.getTbl_sales(), null, contentValues);
        if (result == -1) {
            Log.d("insertSaleNew", "failed insert sale");
        } else {
            Log.d("insertSaleNew", "success insert SAAAAAAAAALLLLLLLLLLLEEEEEEEEE");
        }


        db.close();
    }

    public HelperSales insertSaleNRet(HelperSales helperSales, SQLiteDatabase db){
        //only dbNames.getCol_completed() = 'W'
        if(helperSales.getCompleted().equals("W")){
            helperSales = mergeSameHelperSales(helperSales, db);
        }

        ContentValues contentValues = new ContentValues();

        //contentValues.put(dbNames.getCol_transaction_id(), helperSales.getTransaction_id());
        contentValues.put(dbNames.getCol_transaction_counter(), helperSales.getTransaction_counter());
        contentValues.put(dbNames.getCol_transaction_per_entry(), helperSales.getTransaction_per_entry());
        contentValues.put(dbNames.getCol_item_id(), helperSales.getItem_id());
        contentValues.put(dbNames.getCol_sort_order_id(), helperSales.getSort_order_id());
        contentValues.put(dbNames.getCol_machine_name(), helperSales.getMachine_name());
        contentValues.put(dbNames.getCol_var_hdr_id(), helperSales.getVar_hdr_id());
        contentValues.put(dbNames.getCol_var_dtls_id(), helperSales.getVar_dtls_id());
        contentValues.put(dbNames.getCol_item_name(), helperSales.getItem_name());
        contentValues.put(dbNames.getCol_qty(), helperSales.getQty());
        contentValues.put(dbNames.getCol_selling_price(), helperSales.getSelling_price());
        contentValues.put(dbNames.getCol_date(), helperSales.getDate());
        contentValues.put(dbNames.getCol_created_by(), helperSales.getCreated_by());
        contentValues.put(dbNames.getCol_completed(), helperSales.getCompleted());
        contentValues.put(dbNames.getCol_dine_in_out(), helperSales.getDine_in_out());
        long result = db.insert(dbNames.getTbl_sales(), null, contentValues);
        if (result == -1) {
            Log.d("insertSale", "failed insert sale");
        } else {
            Log.d("insertSale", "success insert sale");
        }


        db.close();

        return helperSales;
    }

    public void insertSalexxx(HelperSales helperSales, SQLiteDatabase db){
        //only dbNames.getCol_completed() = 'W'
        if(helperSales.getCompleted().equals("W")){
            helperSales = mergeSameHelperSales(helperSales, db);
        }

        ContentValues contentValues = new ContentValues();

        //contentValues.put(dbNames.getCol_transaction_id(), helperSales.getTransaction_id());
        contentValues.put(dbNames.getCol_transaction_counter(), helperSales.getTransaction_counter());
        contentValues.put(dbNames.getCol_transaction_per_entry(), helperSales.getTransaction_per_entry());
        contentValues.put(dbNames.getCol_item_id(), helperSales.getItem_id());
        contentValues.put(dbNames.getCol_sort_order_id(), helperSales.getSort_order_id());
        contentValues.put(dbNames.getCol_machine_name(), helperSales.getMachine_name());
        contentValues.put(dbNames.getCol_var_hdr_id(), helperSales.getVar_hdr_id());
        contentValues.put(dbNames.getCol_var_dtls_id(), helperSales.getVar_dtls_id());
        contentValues.put(dbNames.getCol_item_name(), helperSales.getItem_name());
        contentValues.put(dbNames.getCol_qty(), helperSales.getQty());
        contentValues.put(dbNames.getCol_selling_price(), helperSales.getSelling_price());
        contentValues.put(dbNames.getCol_date(), helperSales.getDate());
        contentValues.put(dbNames.getCol_created_by(), helperSales.getCreated_by());
        contentValues.put(dbNames.getCol_completed(), helperSales.getCompleted());
        contentValues.put(dbNames.getCol_dine_in_out(), helperSales.getDine_in_out());
        long result = db.insert(dbNames.getTbl_sales(), null, contentValues);
        if (result == -1) {
            Log.d("insertSale", "failed insert sale");
        } else {
            Log.d("insertSale", "success insert sale");
        }


        db.close();
    }

    public HelperSales mergeSameHelperSales(HelperSales helperSale, SQLiteDatabase db){

        String query;

        if(helperSale.getVar_hdr_id()!= null){
            query = "SELECT DISTINCT " + dbNames.getCol_transaction_per_entry()
                    + ", " + dbNames.getCol_sort_order_id()
                    +  " FROM " + dbNames.getTbl_sales()
                    + " WHERE " + dbNames.getCol_item_id() + " = '" + helperSale.getItem_id() + "'"
                    + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                    + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                    + " AND " + dbNames.getCol_completed() + " = '" + helperSale.getCompleted() + "'"
                    + " AND " + dbNames.getCol_var_hdr_id() + " = " + helperSale.getVar_hdr_id()
                    + " AND " + dbNames.getCol_var_dtls_id() + " = " + helperSale.getVar_dtls_id();
        } else {
            query = "SELECT DISTINCT " + dbNames.getCol_transaction_per_entry()
                    + ", " + dbNames.getCol_sort_order_id()
                    +  " FROM " + dbNames.getTbl_sales()
                    + " WHERE " + dbNames.getCol_item_id() + " = '" + helperSale.getItem_id() + "'"
                    + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                    + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                    + " AND " + dbNames.getCol_completed() + " = '" + helperSale.getCompleted() + "'"
                    + " AND " + dbNames.getCol_var_hdr_id() + " is null";
        }

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            helperSale.setTransaction_per_entry(cursor.getInt(0));
            helperSale.setSort_order_id(cursor.getInt(1));
        }

        db.close();

        return helperSale;
    }



    public void deleteNotInCart(String created_by, String machine_name, SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_completed() + " = 'W'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + machine_name + "'"
                + " AND " + dbNames.getCol_created_by() + " = '" + created_by + "'"
                ;
        db.execSQL(query);
    }

    public boolean notInCartExists(String created_by, String machine_name, SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_completed() + " = 'W'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + machine_name + "'"
                + " AND " + dbNames.getCol_created_by() + " = '" + created_by + "'"
                ;
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

    public boolean itemExists(HelperSales helperSale, SQLiteDatabase db){
        String query = "SELECT * FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_item_id() + " = '" + helperSale.getItem_id() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + dbNames.getCol_completed() + " = '" + helperSale.getCompleted() + "'"
                + " AND " + dbNames.getCol_transaction_per_entry() + " = '" + helperSale.getTransaction_per_entry() + "'"
                ;
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



    public boolean variantExists(HelperSales helperSale, SQLiteDatabase db){
        String query;

        if (helperSale.getVar_hdr_id()!=null){
            query = "SELECT * FROM " + dbNames.getTbl_sales()
                    + " WHERE " + dbNames.getCol_transaction_per_entry() + " = '" + helperSale.getTransaction_per_entry() + "'"
                    + " AND " + dbNames.getCol_item_id() + " = '" + helperSale.getItem_id() + "'"
                    + " AND " + dbNames.getCol_var_hdr_id() + " = '" + helperSale.getVar_hdr_id() + "'"
                    + " AND " + dbNames.getCol_var_dtls_id() + " = '" + helperSale.getVar_dtls_id() + "'"
                    + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                    + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                    + " AND " + dbNames.getCol_completed() + " = '" + helperSale.getCompleted() + "'"
            ;
        } else {
            query = "SELECT * FROM " + dbNames.getTbl_sales()
                    + " WHERE " + dbNames.getCol_transaction_per_entry() + " = '" + helperSale.getTransaction_per_entry() + "'"
                    + " AND " + dbNames.getCol_item_id() + " = '" + helperSale.getItem_id() + "'"
                    + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                    + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                    + " AND " + dbNames.getCol_completed() + " = '" + helperSale.getCompleted() + "'"
            ;
        }

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

    public int nextSortOrderIdNoItem(HelperSales helperSale, SQLiteDatabase db){
        String query = "SELECT MAX(" + dbNames.getCol_sort_order_id() + " ) + " + dbNames.getSale_sorter() + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_sort_order_id() + " % " + dbNames.getSale_sorter() + " = 0"
                + " AND " + dbNames.getCol_transaction_per_entry() + " = '" + helperSale.getTransaction_per_entry() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + dbNames.getCol_completed() + " = '" + helperSale.getCompleted() + "'"
                ;
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int getSortOrderIdItem(HelperSales helperSale, SQLiteDatabase db){
        String query = "SELECT MAX(" + dbNames.getCol_sort_order_id() + " ) FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_sort_order_id() + " % " + dbNames.getSale_sorter() + " = 0"
                + " AND " + dbNames.getCol_transaction_per_entry() + " = '" + helperSale.getTransaction_per_entry() + "'"
                + " AND " + dbNames.getCol_item_id() + " = '" + helperSale.getItem_id() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + dbNames.getCol_completed() + " = '" + helperSale.getCompleted() + "'"
                ;
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int getSortOrderIdVariant(HelperSales helperSale, SQLiteDatabase db){
        String query = "SELECT MAX(" + dbNames.getCol_sort_order_id() + ") FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_transaction_per_entry() + " = '" + helperSale.getTransaction_per_entry() + "'"
                + " AND " + dbNames.getCol_item_id() + " = '" + helperSale.getItem_id() + "'"
                + " AND " + dbNames.getCol_var_dtls_id() + " = '" + helperSale.getVar_dtls_id() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + dbNames.getCol_completed() + " = '" + helperSale.getCompleted() + "'"
                ;
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int maxTransactionId(SQLiteDatabase db){
        String query = "SELECT MAX(" + dbNames.getCol_transaction_id() + " ) FROM " + dbNames.getTbl_sales();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int maxTransactionPerEntry(SQLiteDatabase db){
        String query = "SELECT MAX(" + dbNames.getCol_transaction_per_entry() + " ) FROM " + dbNames.getTbl_sales();
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }


    public int maxTransactionPerEntryCompN(HelperSales helperSale, SQLiteDatabase db){

        String query = "SELECT MAX(" + dbNames.getCol_transaction_per_entry() + " ) FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "' "
                + " AND " + dbNames.getCol_item_id() + " = '" + helperSale.getItem_id() + "' "
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "' "
                + " AND " + dbNames.getCol_completed() + " = 'N' "
                + " AND " + dbNames.getCol_var_hdr_id() + " is not null"
                ;
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int nextTransactionCounter(SQLiteDatabase db){
        String query = "SELECT MAX(" + dbNames.getCol_transaction_counter() + " ) + 1 FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_completed() + " = 'Y'"
                ;
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int nextTransactionPerEntry(SQLiteDatabase db){
        //modify this, to check first if HelperSale does not exists yet, then get new transaction_per_entry no.
        //                              if exists, then reuse that transaction_per_entry no.
        // ex. customer say 1 reg fries cheese
        //                  1 pizza
        //                  1 reg fries cheese
        // display should should be
        //                  2 reg fries cheese
        //                  1 pizza

        String query = "SELECT MAX(" + dbNames.getCol_transaction_per_entry() + " ) + 1 FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_completed() + " = 'N'"
                ;
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int addToMaxSortOrderIdVariant(HelperSales helperSale, SQLiteDatabase db){
        //int to be used is the rownum in table links how it was inserted
        String query = "SELECT  * FROM " + dbNames.getTbl_variants_links()
                + " WHERE " + dbNames.getCol_item_id() + " = '" + helperSale.getItem_id() + "'"
                + " ORDER BY ROWID"
                ;
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

    public int nextSortOrderIdVariant(HelperSales helperSale, SQLiteDatabase db){

        int int_add = addToMaxSortOrderIdVariant(helperSale, db);

        String query = "SELECT MAX(" + dbNames.getCol_sort_order_id() + " ) + " + int_add + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_transaction_per_entry() + " = '" + helperSale.getTransaction_per_entry() + "'"
                + " AND " + dbNames.getCol_item_id() + " = '" + helperSale.getItem_id() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + dbNames.getCol_completed() + " = '" + helperSale.getCompleted() + "'"
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                ;
        Log.d("nextSortOrderIdVariant", query );

        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        return id;
    }

    public int sumItemsPerTranPerEntry(HelperSales helperSale, SQLiteDatabase db){
        //no variant data
        String query = "SELECT sum(" + dbNames.getCol_qty() +  ") FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_transaction_per_entry() + " = '" + helperSale.getTransaction_per_entry() + "'"
                + " AND " + dbNames.getCol_item_id() + " = '" + helperSale.getItem_id() + "'"
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSale.getMachine_name() + "'"
                + " AND " + dbNames.getCol_created_by() + " = '" + helperSale.getCreated_by() + "'"
                + " AND " + dbNames.getCol_completed() + " = '" + helperSale.getCompleted() + "'"
                + " AND " + dbNames.getCol_var_hdr_id() + " is null"
                ;
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        db.close();

        Log.d("sumItemsPerTranPerEntry", query + " id=" + id);

        return id;
    }

    public boolean existingItemVarCompN(HelperSales helperSaleIn, SQLiteDatabase db){

        String query = "SELECT 1 "
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSaleIn.getCreated_by() + "' "
                + " AND " + dbNames.getCol_item_id() + " = '" + helperSaleIn.getItem_id() + "' "
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSaleIn.getMachine_name() + "' "
                + " AND " + dbNames.getCol_transaction_per_entry() + " = '" + helperSaleIn.getTransaction_per_entry() + "' "
                + " AND " + dbNames.getCol_completed() + " = 'N' "
                + " AND " + dbNames.getCol_var_hdr_id() + " is not null"
                ;

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

    public List<HelperSales> listLastSelectedVar(HelperSales helperSaleIn, int intLastTransactionPerEntry, SQLiteDatabase db){
        //last selected variants after add to cart
        List<HelperSales> helperSales= new LinkedList<HelperSales>();
        helperSales.clear();

        String query = "SELECT DISTINCT " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_var_dtls_id()
                + ", " + dbNames.getCol_var_hdr_id()
                + ", " + dbNames.getCol_completed()
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSaleIn.getCreated_by() + "' "
                + " AND " + dbNames.getCol_item_id() + " = '" + helperSaleIn.getItem_id() + "' "
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSaleIn.getMachine_name() + "' "
                + " AND " + dbNames.getCol_transaction_per_entry() + " = '" + intLastTransactionPerEntry + "' "
                + " AND " + dbNames.getCol_completed() + " = 'N' "
                + " AND " + dbNames.getCol_var_hdr_id() + " is not null"
                ;
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

    public List<HelperSales> listVariantsInItemNew(HelperSales helperSaleIn, SQLiteDatabase db){

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

        String query = "SELECT DISTINCT " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_var_dtls_id()
                + ", " + dbNames.getCol_var_hdr_id()
                + ", " + dbNames.getCol_completed()
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSaleIn.getCreated_by() + "' "
                + " AND " + dbNames.getCol_item_id() + " = '" + helperSaleIn.getItem_id() + "' "
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSaleIn.getMachine_name() + "' "
                + " AND " + dbNames.getCol_transaction_per_entry() + " = '" + helperSaleIn.getTransaction_per_entry() + "' "
                + " AND " + dbNames.getCol_completed() + " = '" + sCompleted + "' "
                + " AND " + dbNames.getCol_var_hdr_id() + " is not null"
                ;
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

    public List<HelperSales> listVariantsInItem(HelperSales helperSalesParam, SQLiteDatabase db){

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

        String query = "SELECT DISTINCT " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_var_dtls_id()
                + ", " + dbNames.getCol_var_hdr_id()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_item_id()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_completed()
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSalesParam.getCreated_by() + "' "
                + " AND " + dbNames.getCol_item_id() + " = '" + helperSalesParam.getItem_id() + "' "
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSalesParam.getMachine_name() + "' "
                + " AND " + dbNames.getCol_transaction_per_entry() + " = '" + helperSalesParam.getTransaction_per_entry() + "' "
                + " AND " + dbNames.getCol_completed() + " = '" + sCompleted + "' "
                + " AND " + dbNames.getCol_var_hdr_id() + " is not null"
                ;
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

    public List<HelperSales> listPreCompletedSales(HelperSales helperSalesParam, SQLiteDatabase db){

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

        String query = "SELECT sum(" + dbNames.getCol_qty() + ") || '(' ||" +  dbNames.getCol_selling_price() + "||'.00)' AS " + dbNames.getCol_qty()
                + ", sum(" + dbNames.getCol_qty() + ") * " + dbNames.getCol_selling_price() + " AS " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_item_id()
                + ", " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_sort_order_id()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + ", " + dbNames.getCol_var_dtls_id()
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSalesParam.getCreated_by() + "' "
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSalesParam.getMachine_name() + "' "
                + " AND " + dbNames.getCol_completed() + " = '" + sCompleted + "' "
                + " GROUP BY " + dbNames.getCol_item_id()
                + ", " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_sort_order_id()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + ", " + dbNames.getCol_var_dtls_id()
                + " ORDER BY "
                + dbNames.getCol_transaction_per_entry()  + " DESC " + ",  "
                + dbNames.getCol_sort_order_id()  + " DESC ";
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

    public List<HelperSales> listSummaryPreCompletedSalesNewLine(HelperSales helperSalesParam, SQLiteDatabase db){

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

        String query_item = "SELECT sum(" + dbNames.getCol_qty() + ") AS " + dbNames.getCol_qty()
                + ", sum(" + dbNames.getCol_selling_price() + ") AS " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_item_id()
                + ", " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + ", " + dbNames.getCol_sort_order_id()
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSalesParam.getCreated_by() + "' "
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSalesParam.getMachine_name() + "' "
                + " AND " + dbNames.getCol_completed() + " = '" + sCompleted + "' "
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                + " GROUP BY " + dbNames.getCol_item_id()
                + ", " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + ", " + dbNames.getCol_sort_order_id();

        String query_variants = "SELECT sum(" + 0 + ") AS " + dbNames.getCol_qty()
                + ", sum(" + dbNames.getCol_selling_price() + ")  AS " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_item_id()
                + ", ('\n' || '   >' || " + dbNames.getCol_item_name() + ") AS " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + ", " + dbNames.getCol_sort_order_id()
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSalesParam.getCreated_by() + "' "
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSalesParam.getMachine_name() + "' "
                + " AND " + dbNames.getCol_completed() + " = '" + sCompleted + "' "
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NOT NULL "
                + " GROUP BY " + dbNames.getCol_item_id()
                + ", ('\n' || '   >' || " + dbNames.getCol_item_name() + ") "
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + ", " + dbNames.getCol_sort_order_id()
                + " ORDER BY " + dbNames.getCol_sort_order_id() + " ";

        String query_all = "SELECT sum(" + dbNames.getCol_qty() + ") AS " + dbNames.getCol_qty()
                + ", sum(" + dbNames.getCol_selling_price() + ")  AS " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_item_id()
                + ", GROUP_CONCAT( "+ dbNames.getCol_item_name() + ", ' ') AS " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + " FROM "
                + " ( "
                + query_item + " union all " + query_variants
                + " ) "
                + " GROUP BY " + dbNames.getCol_item_id()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed();

        List<HelperSales> helperSales= new LinkedList<HelperSales>();
        helperSales.clear();

        String query = "SELECT (" + dbNames.getCol_qty() + ") || '(' || (" + dbNames.getCol_selling_price() + "/" + dbNames.getCol_qty() + ") || '.00)'  AS " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_item_id()
                + ", " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + " FROM "
                + " ( "
                + query_all
                + " ) "
                + " ORDER BY " + dbNames.getCol_transaction_per_entry() + " DESC";

        Log.d("listSummaryPreCompleted", query);

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

    public List<HelperSales> listSummaryPreCompletedSales(HelperSales helperSalesParam, SQLiteDatabase db){

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

        String query_item = "SELECT sum(" + dbNames.getCol_qty() + ") AS " + dbNames.getCol_qty()
                + ", sum(" + dbNames.getCol_selling_price() + ") AS " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_item_id()
                + ", " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + ", " + dbNames.getCol_sort_order_id()
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSalesParam.getCreated_by() + "' "
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSalesParam.getMachine_name() + "' "
                + " AND " + dbNames.getCol_completed() + " = '" + sCompleted + "' "
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NULL "
                + " GROUP BY " + dbNames.getCol_item_id()
                + ", " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + ", " + dbNames.getCol_sort_order_id();

        String query_variants = "SELECT sum(" + 0 + ") AS " + dbNames.getCol_qty()
                + ", sum(" + dbNames.getCol_selling_price() + ")  AS " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_item_id()
                + ", substr(" + dbNames.getCol_item_name() + ", 1, 3) AS " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + ", " + dbNames.getCol_sort_order_id()
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = '" + helperSalesParam.getCreated_by() + "' "
                + " AND " + dbNames.getCol_machine_name() + " = '" + helperSalesParam.getMachine_name() + "' "
                + " AND " + dbNames.getCol_completed() + " = '" + sCompleted + "' "
                + " AND " + dbNames.getCol_var_hdr_id() + " IS NOT NULL "
                + " GROUP BY " + dbNames.getCol_item_id()
                + ", substr(" + dbNames.getCol_item_name() + ", 1, 3) "
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + ", " + dbNames.getCol_sort_order_id()
                + " ORDER BY " + dbNames.getCol_sort_order_id() + " ";

        String query_all = "SELECT sum(" + dbNames.getCol_qty() + ") AS " + dbNames.getCol_qty()
                + ", sum(" + dbNames.getCol_selling_price() + ")  AS " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_item_id()
                + ", GROUP_CONCAT( "+ dbNames.getCol_item_name() + ", '>') AS " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + " FROM "
                + " ( "
                + query_item + " union all " + query_variants
                + " ) "
                + " GROUP BY " + dbNames.getCol_item_id()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed();

        List<HelperSales> helperSales= new LinkedList<HelperSales>();
        helperSales.clear();

        String query = "SELECT (" + dbNames.getCol_qty() + ") || '(' || (" + dbNames.getCol_selling_price() + "/" + dbNames.getCol_qty() + ") || '.00)'  AS " + dbNames.getCol_qty()
                + ", " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_item_id()
                + ", " + dbNames.getCol_item_name()
                + ", " + dbNames.getCol_machine_name()
                + ", " + dbNames.getCol_created_by()
                + ", " + dbNames.getCol_transaction_per_entry()
                + ", " + dbNames.getCol_completed()
                + " FROM "
                + " ( "
                + query_all
                + " ) "
                + " ORDER BY " + dbNames.getCol_transaction_per_entry() + " DESC";

        Log.d("listSummaryPreCompleted", query);

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

    public List<HelperSales> listHelperSalesDefaultVariants(HelperItem helperItem, SQLiteDatabase db){

        List<HelperSales> helperSales= new LinkedList<HelperSales>();
        helperSales.clear();

        String query = "SELECT b.*  FROM " + dbNames.getTbl_variants_links() + " a "
                + ", " + dbNames.getTbl_variants_dtls() + " b "
                + " WHERE a." + dbNames.getCol_item_id() + " = " + helperItem.getItem_id()
                + " AND a." + dbNames.getCol_var_hdr_id() + " = b." + dbNames.getCol_var_hdr_id()
                + " AND b." + dbNames.getCol_var_dtls_default() + " = 'Y'";

        Log.d("listHSDeftVar", query);

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
                helperSale.setTransaction_counter(nextTransactionCounter(db));
                helperSale.setTransaction_per_entry(nextTransactionPerEntry(db));
                if (variantExists(helperSale, db)){
                    helperSale.setSort_order_id(getSortOrderIdVariant(helperSale, db));
                } else {
                    helperSale.setSort_order_id(nextSortOrderIdVariant(helperSale, db));
                }

                helperSales.add(helperSale);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperSales;


    }

    public List<HelperSales> listHelperSalesDefaultVariants(HelperSales helperSalePar, SQLiteDatabase db){


        String query = "SELECT b.*  FROM " + dbNames.getTbl_variants_links() + " a "
                + ", " + dbNames.getTbl_variants_dtls() + " b "
                + " WHERE a." + dbNames.getCol_item_id() + " = " + helperSalePar.getItem_id()
                + " AND a." + dbNames.getCol_var_hdr_id() + " = b." + dbNames.getCol_var_hdr_id()
                + " AND b." + dbNames.getCol_var_dtls_default() + " = 'Y'";

        Log.d("listHSDeftVar", query);

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

    public List<HelperSales> listSummarySalesPerDay(SQLiteDatabase db){
        List<HelperSales> helperSales = new LinkedList<>();
        helperSales.clear();

        String query = "SELECT sum(" + dbNames.getCol_selling_price() + ") AS " + dbNames.getCol_selling_price()
                + ", ("
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_date() + ", 5, 1) "
                + " END  "
                + ")"
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = 'admin'"
                + " AND " + dbNames.getCol_machine_name() + " = 'pos1'"
                + " AND " + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_date() + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + dbNames.getCol_date() + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_date() + ", 5, 1) "
                + " END  " + " >= " + " (SELECT DATETIME('now', '-" + dbNames.getDays_covered() + " day'))"
                + " GROUP BY " + dbNames.getCol_date()
                + " ORDER BY 2 "
                ;

        Log.d("listSummarySalesPerDay", query);

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

    public List<HelperSales> listEODGraphFP(SQLiteDatabase db){
        List<HelperSales> helperSales = new LinkedList<>();
        helperSales.clear();

        String query = "SELECT sum(" + dbNames.getCol_fp_end_of_day_total() + ") AS " + dbNames.getCol_fp_end_of_day_total()
                + ", ("
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_fp_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_fp_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_fp_date() + ", 5, 1) "
                + " END  "
                + ")"
                + " FROM " + dbNames.getTbl_fp_dtls()
                + " WHERE "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_fp_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_fp_date() + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + dbNames.getCol_fp_date() + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_fp_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_fp_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_fp_date() + ", 5, 1) "
                + " END  " + " >= " + " (SELECT DATETIME('now', '-" + dbNames.getDays_covered() + " day'))"
                + " GROUP BY " + dbNames.getCol_fp_date()
                + " ORDER BY 2 "
                ;

        Log.d("listEODGraphFP", query);

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

    public List<HelperSales> listEODGraphFPWeekly(SQLiteDatabase db){
        List<HelperSales> helperSales = new LinkedList<>();
        helperSales.clear();

        String query = "SELECT sum(" + dbNames.getCol_fp_end_of_day_total() + ") AS " + dbNames.getCol_fp_end_of_day_total()
                + ", strftime( '%W', "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_fp_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_fp_date() + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + dbNames.getCol_fp_date() + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_fp_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_fp_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_fp_date() + ", 5, 1) "
                + " END  "
                + ")"
                + ", "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " FROM " + dbNames.getTbl_fp_dtls()
                + " WHERE "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_fp_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_fp_date() + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + dbNames.getCol_fp_date() + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_fp_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_fp_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_fp_date() + ", 5, 1) "
                + " END  " + " >= " + " (SELECT DATETIME('now', '-" + dbNames.getDays_covered() + " day'))"
                + " GROUP BY "
                + " strftime( '%W', "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_fp_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_fp_date() + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + dbNames.getCol_fp_date() + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_fp_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_fp_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_fp_date() + ", 5, 1) "
                + " END  "
                + ")"
                + ", "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " ORDER BY 2 "
                ;

        Log.d("listEODGraphFP", query);

        Cursor cursor = db.rawQuery(query, null);
        HelperSales helperSale = null;
        int row_num = 0;

        if (cursor.moveToFirst()) {
            do {
                helperSale = new HelperSales();
                helperSale.setSelling_price(cursor.getString(0));
                helperSale.setItem_name(cursor.getString(1));   //week
                helperSale.setCreated_by(cursor.getString(2));  //month
                helperSale.setQty("" + row_num);
                helperSales.add(helperSale);
                row_num += 1;

            } while (cursor.moveToNext());
        }

        db.close();

        return helperSales;
    }


    public List<HelperSales> listStocksPricePerDay(SQLiteDatabase db){
        //set to helpersales so that we can merge this with salesperday later
        List<HelperSales> helperSales = new LinkedList<>();
        helperSales.clear();

        String query = "SELECT sum("
                + " (CASE "
                + " WHEN " + dbNames.getCol_in_out() + "='IN' THEN -" + dbNames.getCol_cost()
                + " ELSE " + dbNames.getCol_cost()
                + " END) "
                + ") AS " + dbNames.getCol_cost()
                + ", (" + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_time() + ") = 12 THEN SUBSTR(" + dbNames.getCol_time() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_time() + ", 5, 1) "
                + " END  "
                + ")"
                + " FROM " + dbNames.getTbl_stocks_history()
                + " WHERE " + dbNames.getCol_username() + " = 'admin'"
                + " AND " + dbNames.getCol_cost() + " IS NOT NULL "
                + " AND " + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_time() + ") = 12 THEN SUBSTR(" + dbNames.getCol_time() + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + dbNames.getCol_time() + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_time() + ") = 12 THEN SUBSTR(" + dbNames.getCol_time() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_time() + ", 5, 1) "
                + " END  " + " >= " + " (SELECT DATETIME('now', '-" + dbNames.getDays_covered() + " day'))"
                + " GROUP BY " + dbNames.getCol_time()
                + " ORDER BY 2 "
                ;

        Log.d("listStocksPricePerDay", query);

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

    public int sumCostStocksPerDay(String s_day, SQLiteDatabase db){

        int s_return = 0;

        String query = "SELECT sum("
                + " (CASE "
                + " WHEN " + dbNames.getCol_in_out() + "='IN' THEN -" + dbNames.getCol_cost()
                + " ELSE " + dbNames.getCol_cost()
                + " END) "
                + ") AS " + dbNames.getCol_cost()
                + ", (" + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_time() + ") = 12 THEN SUBSTR(" + dbNames.getCol_time() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_time() + ", 5, 1) "
                + " END  "
                + ")"
                + " FROM " + dbNames.getTbl_stocks_history()
                + " WHERE 1=1 "
                ///+ dbNames.getCol_username() + " = 'admin'"
                + " AND " + dbNames.getCol_cost() + " IS NOT NULL "
                + " AND "
                + " (" + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_time() + ") = 12 THEN SUBSTR(" + dbNames.getCol_time() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_time() + ", 5, 1) "
                + " END  "
                + ") "
                + " = " + "'" + s_day + "'"
                + " GROUP BY " + dbNames.getCol_time()
                + " ORDER BY 2 "
                ;

        Log.d("listStocksPricePerDay", query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            s_return = cursor.getInt(0);
        }

        db.close();

        return s_return;
    }

    public int estimatedCostPerDay(String s_day, SQLiteDatabase db){

        int s_return = 0;

        String query = " SELECT ROUND( (SUM(estimated_cost * 1.0000)), 0) "
                + " FROM ( SELECT (SELECT ROUND(( SUM(aa." + dbNames.getCol_cost() + " * 1.0000) / SUM(aa." + dbNames.getCol_qty() + " * 1.0000) ), 4) "
                + "                FROM   " + dbNames.getTbl_stocks_history() + " aa "
                + "                WHERE  aa." + dbNames.getCol_cost() + " IS NOT NULL "
                + "                       AND aa." + dbNames.getCol_cost() + " != 0 "
                + "                       AND aa." + dbNames.getCol_stock_id() + " = b." + dbNames.getCol_stock_id()
                + "                       ORDER BY ROWID DESC "
                + "                       LIMIT 1 "
                + "             ) * 1.0000 * b." + dbNames.getCol_qty() + " AS estimated_cost "
                + "        FROM " + dbNames.getTbl_sales() + " a, "
                + "               " + dbNames.getTbl_composite_links() + " b "
                + "        WHERE  a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + "               AND a." + dbNames.getCol_var_hdr_id() + " IS NULL "
                + "               AND b." + dbNames.getCol_var_hdr_id() + " IS NULL"
                + " AND "
                + " (" + " CASE "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH(a."+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(a." + dbNames.getCol_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR(a." + dbNames.getCol_date() + ", 5, 1) "
                + " END  "
                + ") "
                + " = " + "'" + s_day + "'"
                + "        UNION ALL "
                + "        SELECT (SELECT ROUND(( SUM(aa." + dbNames.getCol_cost() + " * 1.0000) / SUM(aa." + dbNames.getCol_qty() + " * 1.0000) ), 4)"
                + "                FROM " + dbNames.getTbl_stocks_history() + " aa "
                + "                WHERE  aa." + dbNames.getCol_cost() + " IS NOT NULL "
                + "                       AND aa." + dbNames.getCol_cost() + " != 0 "
                + "                       AND aa." + dbNames.getCol_stock_id() + "= b." + dbNames.getCol_stock_id()
                + "                ORDER BY ROWID DESC"
                + "                LIMIT 1"
                + "             ) * 1.0000 * b." + dbNames.getCol_qty() + " AS estimated_cost "
                + "        FROM " + dbNames.getTbl_sales() + " a "
                + "           , " + dbNames.getTbl_composite_links() + " b "
                + "        WHERE  a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + "               AND a." + dbNames.getCol_var_hdr_id() + " = b." + dbNames.getCol_var_hdr_id()
                + "               AND a." + dbNames.getCol_var_dtls_id() + "= b." + dbNames.getCol_var_dtls_id()
                + " AND "
                + " (" + " CASE "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(a." + dbNames.getCol_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH(a."+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(a." + dbNames.getCol_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR(a." + dbNames.getCol_date() + ", 5, 1) "
                + " END  "
                + ") "
                + " = " + "'" + s_day + "'"
                + ")";

        Log.d("estimatedCostPerDay", query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            s_return = cursor.getInt(0);
        }

        db.close();

        return s_return;
    }

    public int estimatedCostPerWeek(String s_week, String s_month, SQLiteDatabase db){

        int s_return = 0;

        String query = " SELECT ROUND( (SUM(estimated_cost * 1.0000)), 0) "
                + " FROM ( SELECT (SELECT ROUND(( SUM(aa." + dbNames.getCol_cost() + " * 1.0000) / SUM(aa." + dbNames.getCol_qty() + " * 1.0000) ), 4) "
                + "                FROM   " + dbNames.getTbl_stocks_history() + " aa "
                + "                WHERE  aa." + dbNames.getCol_cost() + " IS NOT NULL "
                + "                       AND aa." + dbNames.getCol_cost() + " != 0 "
                + "                       AND aa." + dbNames.getCol_stock_id() + " = b." + dbNames.getCol_stock_id()
                + "                       ORDER BY ROWID DESC "
                + "                       LIMIT 1 "
                + "             ) * 1.0000 * b." + dbNames.getCol_qty() + " AS estimated_cost "
                + "        FROM " + dbNames.getTbl_sales() + " a, "
                + "               " + dbNames.getTbl_composite_links() + " b "
                + "        WHERE  a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + "               AND a." + dbNames.getCol_var_hdr_id() + " IS NULL "
                + "               AND b." + dbNames.getCol_var_hdr_id() + " IS NULL"
                + " AND "
                + " strftime( '%W', "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_date() + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + dbNames.getCol_date() + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_date() + ", 5, 1) "
                + " END  "
                + ")"
                + " = " + "'" + s_week + "'"
                + "        UNION ALL "
                + "        SELECT (SELECT ROUND(( SUM(aa." + dbNames.getCol_cost() + " * 1.0000) / SUM(aa." + dbNames.getCol_qty() + " * 1.0000) ), 4)"
                + "                FROM " + dbNames.getTbl_stocks_history() + " aa "
                + "                WHERE  aa." + dbNames.getCol_cost() + " IS NOT NULL "
                + "                       AND aa." + dbNames.getCol_cost() + " != 0 "
                + "                       AND aa." + dbNames.getCol_stock_id() + "= b." + dbNames.getCol_stock_id()
                + "                ORDER BY ROWID DESC"
                + "                LIMIT 1"
                + "             ) * 1.0000 * b." + dbNames.getCol_qty() + " AS estimated_cost "
                + "        FROM " + dbNames.getTbl_sales() + " a "
                + "           , " + dbNames.getTbl_composite_links() + " b "
                + "        WHERE  a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + "               AND a." + dbNames.getCol_var_hdr_id() + " = b." + dbNames.getCol_var_hdr_id()
                + "               AND a." + dbNames.getCol_var_dtls_id() + "= b." + dbNames.getCol_var_dtls_id()
                + " AND "
                + " strftime( '%W', "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_date() + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + dbNames.getCol_date() + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_date() + ", 5, 1) "
                + " END  "
                + ")"
                + " = " + "'" + s_week + "'"
                + " AND "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " = " + "'" + s_month + "'"
                + ")";

        Log.d("estimatedCostPerDay", query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            s_return = cursor.getInt(0);
        }

        db.close();

        return s_return;
    }


    public int estimatedReducedSales(String s_day, SQLiteDatabase db){

        int s_return = 0;

        String query = " SELECT SUM(" + dbNames.getCol_selling_price() + " ) "
                + " FROM ( "
                + " SELECT sum( "
                + dbNames.getCol_selling_price()
                + ") AS " + dbNames.getCol_selling_price()
                + " FROM " + dbNames.getTbl_sales() + " a "
                + " WHERE " + dbNames.getCol_created_by() + " = 'admin'"
                + " AND " + dbNames.getCol_machine_name() + " = 'pos1'"
                + " AND NOT EXISTS ("
                + " SELECT 1 FROM " + dbNames.getTbl_sales() + " b "
                + " WHERE a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND a." + dbNames.getCol_date() + " = b." + dbNames.getCol_date()
                + " AND b." + dbNames.getCol_item_name() + " LIKE '%FP%'"
                +" )"
                + " AND "
                + " (" + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_date() + ", 5, 1) "
                + " END  "
                + ") "
                + " = " + "'" + s_day + "'"
                + " UNION ALL "
                + " SELECT sum( "
                + dbNames.getCol_selling_price() + " * 0.8 "
                + ") AS " + dbNames.getCol_selling_price()
                + " FROM " + dbNames.getTbl_sales() + " a "
                + " WHERE " + dbNames.getCol_created_by() + " = 'admin'"
                + " AND " + dbNames.getCol_machine_name() + " = 'pos1'"
                + " AND EXISTS ("
                + " SELECT 1 FROM " + dbNames.getTbl_sales() + " b "
                + " WHERE a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND a." + dbNames.getCol_date() + " = b." + dbNames.getCol_date()
                + " AND b." + dbNames.getCol_item_name() + " LIKE '%FP%'"
                +" )"
                + " AND "
                + " (" + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_date() + ", 5, 1) "
                + " END  "
                + ") "
                + " = " + "'" + s_day + "'"
                + " ) "
                ;

        Log.d("estimatedReducedSales", query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            s_return = cursor.getInt(0);
        }

        db.close();

        return s_return;
    }

    public int estimatedFPSales(String s_day, SQLiteDatabase db){

        int s_return = 0;

        String query = "SELECT sum( "
                + dbNames.getCol_selling_price() + " * 0.8 "
                + ") AS " + dbNames.getCol_selling_price()
                + " FROM " + dbNames.getTbl_sales() + " a "
                + " WHERE " + dbNames.getCol_created_by() + " = 'admin'"
                + " AND " + dbNames.getCol_machine_name() + " = 'pos1'"
                + " AND EXISTS ("
                + " SELECT 1 FROM " + dbNames.getTbl_sales() + " b "
                + " WHERE a." + dbNames.getCol_item_id() + " = b." + dbNames.getCol_item_id()
                + " AND a." + dbNames.getCol_date() + " = b." + dbNames.getCol_date()
                + " AND b." + dbNames.getCol_item_name() + " LIKE '%FP%'"
                +" )"
                + " AND "
                + " (" + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_date() + ", 5, 1) "
                + " END  "
                + ") "
                + " = " + "'" + s_day + "'";

        Log.d("estimatedFPSales", query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            s_return = cursor.getInt(0);
        }

        db.close();

        return s_return;
    }

    public int paymentAdvice(String s_day, SQLiteDatabase db){

        int s_return = 0;

        String query = "SELECT sum( "
                + dbNames.getCol_fp_payment_advice()
                + ") AS " + dbNames.getCol_fp_payment_advice()
                + " FROM " + dbNames.getTbl_fp_dtls() + " a "
                + " WHERE "
                + " (" + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_fp_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_fp_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_fp_date() + ", 5, 1) "
                + " END  "
                + ") "
                + " = " + "'" + s_day + "'";

        Log.d("estimatedFPSales", query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            s_return = cursor.getInt(0);
        }

        db.close();

        return s_return;
    }

    public int paymentAdviceWeekly(String s_week, String s_month, SQLiteDatabase db){

        int s_return = 0;

        String query = "SELECT sum( "
                + dbNames.getCol_fp_payment_advice()
                + ") AS " + dbNames.getCol_fp_payment_advice()
                + " FROM " + dbNames.getTbl_fp_dtls() + " a "
                + " WHERE "
                + " strftime( '%W', "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_fp_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_fp_date() + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + dbNames.getCol_fp_date() + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_fp_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_fp_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_fp_date() + ", 5, 1) "
                + " END  "
                + ")"
                + " = " + "'" + s_week + "'"
                + " AND "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_fp_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " = " + "'" + s_month + "'"
                ;

        Log.d("estimatedFPSales", query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            s_return = cursor.getInt(0);
        }

        db.close();

        return s_return;
    }

    public List<HelperSales> listSalesVsStocksPerDay(SQLiteDatabase db){
        //set to helpersales so that we can merge this with salesperday later
        List<HelperSales> helperSales = new LinkedList<>();
        helperSales.clear();

        String query_sales = "SELECT sum(" + dbNames.getCol_selling_price() + ") AS " + dbNames.getCol_selling_price()
                + ", (" + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_date() + ", 5, 1) "
                + " END  "
                + ")" + dbNames.getCol_date()
                + " FROM " + dbNames.getTbl_sales()
                + " WHERE " + dbNames.getCol_created_by() + " = 'admin'"
                + " AND " + dbNames.getCol_machine_name() + " = 'pos1'"
                + " AND " + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_date() + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + dbNames.getCol_date() + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_date() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_date() + ") = 12 THEN SUBSTR(" + dbNames.getCol_date() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_date() + ", 5, 1) "
                + " END  " + " >= " + " (SELECT DATETIME('now', '-" + dbNames.getDays_covered() + " day'))"
                + " GROUP BY " + dbNames.getCol_date()
                //+ " ORDER BY 2 "
                ;

        String query_stocks = "SELECT sum("
                + " (CASE "
                + " WHEN " + dbNames.getCol_in_out() + "='IN' THEN -" + dbNames.getCol_cost()
                + " ELSE " + dbNames.getCol_cost()
                + " END) "
                + ") AS " + dbNames.getCol_cost()
                + ", (" + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_time() + ") = 12 THEN SUBSTR(" + dbNames.getCol_time() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_time() + ", 5, 1) "
                + " END  "
                + ")"
                + " FROM " + dbNames.getTbl_stocks_history()
                + " WHERE " + dbNames.getCol_username() + " = 'admin'"
                + " AND " + dbNames.getCol_cost() + " IS NOT NULL "
                + " AND " + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_time() + ") = 12 THEN SUBSTR(" + dbNames.getCol_time() + ", 9, 4 ) "
                + " ELSE  SUBSTR( " + dbNames.getCol_time() + ", 8, 4) "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jan' THEN '01' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Feb' THEN '02' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Mar' THEN '03' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Apr' THEN '04' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'May' THEN '05' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jun' THEN '06' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Jul' THEN '07' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Aug' THEN '08' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Sep' THEN '09' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Oct' THEN '10' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Nov' THEN '11' "
                + " WHEN SUBSTR(" + dbNames.getCol_time() + ", 1, 3 ) = 'Dec' THEN '12' "
                + " ELSE '01' "
                + " END "
                + " || '-' || "
                + " CASE "
                + " WHEN LENGTH("+ dbNames.getCol_time() + ") = 12 THEN SUBSTR(" + dbNames.getCol_time() + ", 5, 2 ) "
                + " ELSE  '0' || SUBSTR( " + dbNames.getCol_time() + ", 5, 1) "
                + " END  " + " >= " + " (SELECT DATETIME('now', '-" + dbNames.getDays_covered() + " day'))"
                + " GROUP BY " + dbNames.getCol_time()
                //+ " ORDER BY 2 "
                ;

        String query = "SELECT SUM( " + dbNames.getCol_selling_price() + ") AS " + dbNames.getCol_selling_price()
                + ", " + dbNames.getCol_date()
                + " FROM "
                + " ( " + query_sales + " union all " + query_stocks + " ) "
                + " GROUP BY " + dbNames.getCol_date()
                + " ORDER BY 2 "
                ;


        Log.d("listStocksPricePerDay", query);

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
    
}
