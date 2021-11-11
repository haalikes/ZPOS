package com.watata.zpos.dbdmlddl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.watata.zpos.DbNames;
import com.watata.zpos.ddlclass.HelperFPDtls;
import com.watata.zpos.ddlclass.HelperCsvLinks;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DbCsvLinks {

    DbNames dbNames;
    //HelperCsvLinks dbNames;

    public DbCsvLinks(DbNames dbNames) {
        this.dbNames = dbNames;
    }

    public void createTable(SQLiteDatabase db){
        try {
            String createTable = "CREATE TABLE " + dbNames.getTbl_csv_links()
                    + " ( "
                           + dbNames.getCol_csv_link_id()          + " TEXT "
                    + " ," + dbNames.getCol_csv_link_name()        + " TEXT "
                    + " ," + dbNames.getCol_z_link_item_id()       + " TEXT "
                    + " ," + dbNames.getCol_z_link_var_hdr_id()    + " TEXT "
                    + " ," + dbNames.getCol_z_link_var_dtls_id()   + " TEXT "
                    + " ," + dbNames.getCol_csv_link_source()      + " TEXT "
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
            db.execSQL("DROP TABLE IF EXISTS " + dbNames.getTbl_csv_links());
    }

    public void refreshCSVLinks(List<HelperCsvLinks> lHelperCsvLinks, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();

        String query = "DELETE FROM " + dbNames.getTbl_csv_links();
        db.execSQL(query);

        for (int i=0; i < lHelperCsvLinks.size(); i++) {

            contentValues.put(dbNames.getCol_csv_link_id(), lHelperCsvLinks.get(i).getCsv_link_id());
            contentValues.put(dbNames.getCol_csv_link_name(), lHelperCsvLinks.get(i).getCsv_link_name());
            contentValues.put(dbNames.getCol_z_link_item_id(), lHelperCsvLinks.get(i).getZ_link_item_id());
            contentValues.put(dbNames.getCol_z_link_var_hdr_id(), lHelperCsvLinks.get(i).getZ_link_var_hdr_id());
            contentValues.put(dbNames.getCol_z_link_var_dtls_id(), lHelperCsvLinks.get(i).getZ_link_var_dtls_id());
            contentValues.put(dbNames.getCol_csv_link_source(), lHelperCsvLinks.get(i).getCsv_link_source());
            long result = db.insert(dbNames.getTbl_csv_links(), null, contentValues);
            if (result == -1) {
                Log.d("texto=", "refreshCSVLinks failed insert");
            } else {
                Log.d("texto=", "refreshCSVLinks success insert");
            }
        }

        db.close();
    }

    public boolean addCSVLinks(HelperCsvLinks helperCsvLinks, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();

        contentValues.put(dbNames.getCol_csv_link_id(), helperCsvLinks.getCsv_link_id());
        contentValues.put(dbNames.getCol_csv_link_name(), helperCsvLinks.getCsv_link_name());
        contentValues.put(dbNames.getCol_z_link_item_id(), helperCsvLinks.getZ_link_item_id());
        contentValues.put(dbNames.getCol_z_link_var_hdr_id(), helperCsvLinks.getZ_link_var_hdr_id());
        contentValues.put(dbNames.getCol_z_link_var_dtls_id(), helperCsvLinks.getZ_link_var_dtls_id());
        contentValues.put(dbNames.getCol_csv_link_source(), helperCsvLinks.getCsv_link_source());
        long result = db.insert(dbNames.getTbl_csv_links(), null, contentValues);
        db.close();

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteCSVLinks(SQLiteDatabase db){
        String query = "DELETE FROM " + dbNames.getTbl_csv_links();
        db.execSQL(query);
        db.close();
    }

    public String getCsv_Link_Item_Id(String link_name, String link_source, SQLiteDatabase db){
        String item_id = "";
        String query =    "SELECT " + dbNames.getCol_csv_link_id()
                + " FROM " + dbNames.getTbl_csv_links()
                + " WHERE " + dbNames.getCol_csv_link_name() + " = '" + link_name + "'"
                + " AND " + dbNames.getCol_z_link_var_hdr_id() + " is null "
                + " AND " + dbNames.getCol_z_link_var_dtls_id() + " is null "
                + " AND " + dbNames.getCol_csv_link_source() + " = '" + link_source + "' "
                ;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            item_id = cursor.getString(0);
        }

        db.close();

        return item_id;
    }

    public List<String> getVarDtls(String item_id, String link_source, SQLiteDatabase db){
        List<String> listLink_values = new LinkedList<>();
        String query =  "SELECT " + dbNames.getCol_z_link_var_dtls_id()
                + " FROM " + dbNames.getTbl_csv_links()
                + " WHERE " + dbNames.getCol_csv_link_id() + " = '" + item_id + "'"
                + " AND " + dbNames.getCol_z_link_var_hdr_id() + " is not null "
                + " AND " + dbNames.getCol_z_link_var_dtls_id() + " is not null "
                + " AND " + dbNames.getCol_csv_link_source() + " = '" + link_source + "' "
                ;

        Log.d("texto=", "query=" + query );

        Cursor cursor = db.rawQuery(query, null);
        listLink_values.clear();

        if (cursor.moveToFirst()) {
            do {
                listLink_values.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        db.close();

        return listLink_values;
    }

    public List<HelperCsvLinks> listHelperCsvLinks(SQLiteDatabase db){

        List<HelperCsvLinks> helperCsvLinks = new LinkedList<>();
        helperCsvLinks.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_csv_links();
        Cursor cursor = db.rawQuery(query, null);
        HelperCsvLinks helperCsvLink = null;

        if (cursor.moveToFirst()) {
            do {
                helperCsvLink = new HelperCsvLinks();
                helperCsvLink.setCsv_link_id(cursor.getString(0));
                helperCsvLink.setCsv_link_name(cursor.getString(1));
                helperCsvLink.setZ_link_item_id(cursor.getString(2));
                helperCsvLink.setZ_link_var_hdr_id(cursor.getString(3));
                helperCsvLink.setZ_link_var_dtls_id(cursor.getString(4));
                helperCsvLink.setCsv_link_source(cursor.getString(5));
                helperCsvLinks.add(helperCsvLink);
            } while (cursor.moveToNext());
        }

        db.close();

        return helperCsvLinks;
    }

    public HelperCsvLinks helperCsvLinksItemId(String link_name, String link_source, SQLiteDatabase db){

        HelperCsvLinks helperCsvLink = new HelperCsvLinks();


        String query = "SELECT * FROM " + dbNames.getTbl_csv_links()
                + " WHERE " + dbNames.getCol_csv_link_name() + " = '" + link_name + "'"
                + " AND " + dbNames.getCol_z_link_var_hdr_id() + " is null "
                + " AND " + dbNames.getCol_z_link_var_dtls_id() + " is null "
                + " AND " + dbNames.getCol_csv_link_source() + " = '" + link_source + "' "
                ;
        Log.d("texto=", "helperCsvLinksItemId         query=" + query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            helperCsvLink.setCsv_link_id(cursor.getString(0));
            helperCsvLink.setCsv_link_name(cursor.getString(1));
            helperCsvLink.setZ_link_item_id(cursor.getString(2));
            helperCsvLink.setZ_link_var_hdr_id(cursor.getString(3));
            helperCsvLink.setZ_link_var_dtls_id(cursor.getString(4));
            helperCsvLink.setCsv_link_source(cursor.getString(5));
        }

        db.close();

        return helperCsvLink;
    }

    public HelperCsvLinks helperCsvLinksVarDtls(SQLiteDatabase db, String item_id, String link_name, String link_source){

        List<HelperCsvLinks> helperCsvLinks = new LinkedList<>();
        helperCsvLinks.clear();

        String query = "SELECT * FROM " + dbNames.getTbl_csv_links()
                + " WHERE " + dbNames.getCol_csv_link_name() + " = '" + link_name + "'"
                + " AND " + dbNames.getCol_z_link_item_id() + " = '" + item_id + "'"
                + " AND " + dbNames.getCol_z_link_var_hdr_id() + " is not null "
                + " AND " + dbNames.getCol_z_link_var_dtls_id() + " is not null "
                + " AND " + dbNames.getCol_csv_link_source() + " = '" + link_source + "' "
                ;

        Log.d("listHelperCsvLink", "listHelperCsvLinksVarDtls         query=" + query);
        Cursor cursor = db.rawQuery(query, null);
        HelperCsvLinks helperCsvLink = null;

        if (cursor.moveToFirst()) {
            ///do {
                helperCsvLink = new HelperCsvLinks();
                helperCsvLink.setCsv_link_id(cursor.getString(0));
                helperCsvLink.setCsv_link_name(cursor.getString(1));
                helperCsvLink.setZ_link_item_id(cursor.getString(2));
                helperCsvLink.setZ_link_var_hdr_id(cursor.getString(3));
                helperCsvLink.setZ_link_var_dtls_id(cursor.getString(4));
                helperCsvLink.setCsv_link_source(cursor.getString(5));
                ///helperCsvLinks.add(helperCsvLink);
            ///} while (cursor.moveToNext());
        }

        db.close();

        return helperCsvLink;
    }

    public List<HelperCsvLinks> listHelperCsvLinksConvertStringToCsvLinks(SQLiteDatabase db, String item_id, List<String> var_mod, String link_source){

        List<HelperCsvLinks> helperCsvLinks = new LinkedList<>();
        helperCsvLinks.clear();

        //convert string var mod to csvlinks
        for (int i=0;i<var_mod.size();i++){
            helperCsvLinks.add(helperCsvLinksVarDtls(var_mod.get(i), item_id, link_source, db));
        }

        //Add not selected but default variants
        helperCsvLinks = listHelperCsvLinksNotSelected(db, helperCsvLinks);

        db.close();

        return helperCsvLinks;
    }

    private HelperCsvLinks helperCsvLinksVarDtls(String link_name, String item_id, String link_source, SQLiteDatabase db){


        String query = "SELECT * FROM " + dbNames.getTbl_csv_links()
                + " WHERE " + dbNames.getCol_csv_link_name() + " = '" + link_name + "'"
                + " AND " + dbNames.getCol_z_link_item_id() + " = '" + item_id + "'"
                + " AND " + dbNames.getCol_z_link_var_hdr_id() + " is not null "
                + " AND " + dbNames.getCol_z_link_var_dtls_id() + " is not null "
                + " AND " + dbNames.getCol_csv_link_source() + " = '" + link_source + "' "
                ;

        Cursor cursor = db.rawQuery(query, null);
        HelperCsvLinks helperCsvLink = new HelperCsvLinks();

        if (cursor.moveToFirst()) {
                helperCsvLink.setCsv_link_id(cursor.getString(0));
                helperCsvLink.setCsv_link_name(cursor.getString(1));
                helperCsvLink.setZ_link_item_id(cursor.getString(2));
                helperCsvLink.setZ_link_var_hdr_id(cursor.getString(3));
                helperCsvLink.setZ_link_var_dtls_id(cursor.getString(4));
                helperCsvLink.setCsv_link_source(cursor.getString(5));
        }

        return helperCsvLink;
    }

    public List<String> removeIfSameVarHdr(SQLiteDatabase db, HelperCsvLinks helperCsvLinkItemId, List<String> listVariantsModifiers, String link_source){
        List<String> lVariantsModifiers = new LinkedList<>();
        lVariantsModifiers.clear();
        String item_id = "";
        String var_mod = "";
        String var_hdr_id = "temp";
        List<HelperCsvLinks> listHelperCsvLinksVarDtls = new LinkedList<>();

        listHelperCsvLinksVarDtls.clear();

        item_id = "'" + helperCsvLinkItemId.getZ_link_item_id() + "',";
        for (int i=0;i<listVariantsModifiers.size(); i++){
            listHelperCsvLinksVarDtls.add(helperCsvLinksVarDtls(listVariantsModifiers.get(i), helperCsvLinkItemId.getZ_link_item_id(), link_source, db));
        }
        for (int i=0;i<listHelperCsvLinksVarDtls.size();i++){
            var_mod = var_mod + "'" + listHelperCsvLinksVarDtls.get(i).getZ_link_var_dtls_id() + "',";
        }

        item_id = item_id.substring(0, item_id.length()-1 );
        var_mod = var_mod.substring(0, var_mod.length()-1 );

        String query = "SELECT * FROM " + dbNames.getTbl_csv_links()
                + " WHERE " + dbNames.getCol_z_link_item_id() + " in (" + item_id + ") "
                + " AND " + dbNames.getCol_z_link_var_dtls_id() + " in (" + var_mod + ") "
                + " AND " + dbNames.getCol_csv_link_source() + " = '" + link_source + "' "
                + " ORDER BY 4 "
                ;

        Log.d("texto=", "removeIfSameVarHdr         query=" + query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Log.d("texto=", "var_hdr_id=" + var_hdr_id + " cursor=" + cursor.getString(3));
                if(!var_hdr_id.equals(cursor.getString(3))){
                    lVariantsModifiers.add(cursor.getString(1));
                }
                var_hdr_id = cursor.getString(3);
            } while (cursor.moveToNext());
        }

        db.close();

        Log.d("texto=", "lVariantsModifiers size=" +  lVariantsModifiers.size() + " = " + lVariantsModifiers.toString());

        return lVariantsModifiers;
    }

    private List<HelperCsvLinks> listHelperCsvLinksNotSelected(SQLiteDatabase db, List<HelperCsvLinks> listHelperCsvLinksSelected){
        String var_hdr_ids = "";
        String item_id = "";
        List<HelperCsvLinks> listHelperCsvLinksToReturn = new LinkedList<>();
        listHelperCsvLinksToReturn.clear();

        Log.d("listHelperCsvLink=", "size before =" + listHelperCsvLinksSelected.size());


        for (int i=0;i<listHelperCsvLinksSelected.size();i++){
            var_hdr_ids = var_hdr_ids + "'" + listHelperCsvLinksSelected.get(i).getZ_link_var_hdr_id() + "',";
            item_id = listHelperCsvLinksSelected.get(i).getZ_link_item_id();
            listHelperCsvLinksToReturn.add(listHelperCsvLinksSelected.get(i));
        }

        var_hdr_ids = var_hdr_ids.substring(0, var_hdr_ids.length()-1 );

        String query = "SELECT a." + dbNames.getCol_var_dtls_name()
                + ", b." + dbNames.getCol_item_id()
                + ", a." + dbNames.getCol_var_hdr_id()
                + ", a." + dbNames.getCol_var_dtls_id()
                + " FROM " + dbNames.getTbl_variants_dtls() + " a "
                + ", " + dbNames.getTbl_variants_links() + " b "
                + " WHERE a." + dbNames.getCol_var_hdr_id() + " = b." + dbNames.getCol_var_hdr_id()
                + " AND a." + dbNames.getCol_var_hdr_id() + " not in (" + var_hdr_ids + ")"
                + " AND b." + dbNames.getCol_item_id() + " = '" + item_id + "'"
                + " AND a." + dbNames.getCol_var_dtls_default() + " = 'Y'"
                ;

        Log.d("texto=", "listHelperCsvLinksVarDtls         query=" + query);
        Cursor cursor = db.rawQuery(query, null);

        HelperCsvLinks helperCsvLink = null;
        if (cursor.moveToFirst()) {
            do {
                helperCsvLink = new HelperCsvLinks();
                helperCsvLink.setCsv_link_name(cursor.getString(0));
                helperCsvLink.setZ_link_item_id(cursor.getString(1));
                helperCsvLink.setZ_link_var_hdr_id(cursor.getString(2));
                helperCsvLink.setZ_link_var_dtls_id(cursor.getString(3));
                listHelperCsvLinksToReturn.add(helperCsvLink);
            } while (cursor.moveToNext());
        }

        Log.d("listHelperCsvLink=", "size after =" + listHelperCsvLinksToReturn.size());

        return listHelperCsvLinksToReturn;


    }

}
