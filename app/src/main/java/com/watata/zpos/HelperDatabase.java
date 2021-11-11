package com.watata.zpos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.watata.zpos.dbdmlddl.DbCategory;
import com.watata.zpos.dbdmlddl.DbChanges;
import com.watata.zpos.dbdmlddl.DbCompositeLinks;
import com.watata.zpos.dbdmlddl.DbCsvLinks;
import com.watata.zpos.dbdmlddl.DbDineInOut;
import com.watata.zpos.dbdmlddl.DbFpDetails;
import com.watata.zpos.dbdmlddl.DbItems;
import com.watata.zpos.dbdmlddl.DbSales;
import com.watata.zpos.dbdmlddl.DbStockNames;
import com.watata.zpos.dbdmlddl.DbStocksHistory;
import com.watata.zpos.dbdmlddl.DbVariantsDtls;
import com.watata.zpos.dbdmlddl.DbVariantsHdr;
import com.watata.zpos.dbdmlddl.DbVariantsLinks;
import com.watata.zpos.ddlclass.HelperCsvLinks;
import com.watata.zpos.ddlclass.HelperFPDtls;
import com.watata.zpos.ddlclass.HelperItem;
import com.watata.zpos.ddlclass.HelperSales;
import com.watata.zpos.ddlclass.HelperStockHistory;
import com.watata.zpos.ddlclass.HelperStockNames;
import com.watata.zpos.ddlclass.HelperTwoString;
import com.watata.zpos.ddlclass.HelperVariantsDtls;
import com.watata.zpos.ddlclass.HelperVariantsHdr;
import com.watata.zpos.ddlclass.HelperVariantsLinks;

import java.io.IOException;
import java.util.List;

public class HelperDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 85;
    private static final String DB_NAME = "ZPOS_TBLS";

    DbNames dbNames = new DbNames();
    DbStockNames        dbStockNames        = new DbStockNames(dbNames);
    DbStocksHistory     dbStocksHistory     = new DbStocksHistory(dbNames);
    DbCategory          dbCategory          = new DbCategory(dbNames);
    DbItems             dbItems             = new DbItems(dbNames);
    DbVariantsLinks     dbVariantsLinks     = new DbVariantsLinks(dbNames);
    DbVariantsHdr       dbVariantsHdr       = new DbVariantsHdr(dbNames);
    DbVariantsDtls      dbVariantsDtls      = new DbVariantsDtls(dbNames);
    DbSales             dbSales             = new DbSales(dbNames);
    DbChanges           dbChanges           = new DbChanges(dbNames);
    DbCompositeLinks    dbCompositeLinks    = new DbCompositeLinks(dbNames);
    DbFpDetails         dbFpDetails         = new DbFpDetails(dbNames);
    DbCsvLinks          dbCsvLinks          = new DbCsvLinks(dbNames);
    DbDineInOut         dbDineInOut         = new DbDineInOut(dbNames);


    public HelperDatabase(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            createTables(db);
        } catch (SQLiteException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            //new on upgrade, drop, remove create in onupgrade
            dropTables(db);

            //create
            createTables(db);

        }

    }

    public void createTables(SQLiteDatabase db){
        //create
        dbStockNames.createTable(db);
        dbStocksHistory.createTable(db);
        dbCategory.createTable(db);
        dbItems.createTable(db);
        dbVariantsLinks.createTable(db);
        dbVariantsHdr.createTable(db);
        dbVariantsDtls.createTable(db);
        dbSales.createTable(db);
        dbChanges.createTable(db);
        dbCompositeLinks.createTable(db);
        dbFpDetails.createTable(db);
        dbCsvLinks.createTable(db);
        dbDineInOut.createTable(db);

    }

    public void dropTables(SQLiteDatabase db){
        dbStockNames.onUpgrade(db);
        dbStocksHistory.onUpgrade(db);
        dbCategory.onUpgrade(db);
        dbItems.onUpgrade(db);
        dbVariantsLinks.onUpgrade(db);
        dbVariantsHdr.onUpgrade(db);
        dbVariantsDtls.onUpgrade(db);
        dbSales.onUpgrade(db);
        dbChanges.onUpgrade(db);
        dbCompositeLinks.onUpgrade(db);
        dbFpDetails.onUpgrade(db);
        dbCsvLinks.onUpgrade(db);
        dbDineInOut.onUpgrade(db);
    }

    //STOCK_NAMES start
    public List<HelperStockNames> listStockNames(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStockNames.listStockNames(db);
    }
    public void refreshStockNames(List<HelperStockNames> stockNames){
        SQLiteDatabase db = this.getWritableDatabase();
        dbStockNames.refreshStockNames(stockNames, db);
    }
    public boolean addStock(HelperStockNames stockNames){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStockNames.addStock(stockNames, db);
    }
    public String getStockNameMeasuredUsed(String stock_name){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStockNames.getStockNameMeasuredUsed(stock_name, db);
    }
    public String getStockName(int stock_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStockNames.getStockName(stock_id, db);
    }
    public String getItemName(String item_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStockNames.getItemName(item_id, db);
    }
    public String getItemSellingPrice(String item_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStockNames.getItemSellingPrice(item_id, db);
    }
    public void deleteStockNames(){
        SQLiteDatabase db = this.getWritableDatabase();
        dbStockNames.deleteStockNames(db);
    }
    //STOCK_NAMES end

    //STOCKS_HISTORY start
    public List<HelperStockHistory> listStocksHistory(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.listStocksHistory(db);
    }
    public void refreshStocksHistory(List<HelperStockHistory> listHelperStockHistories){
        SQLiteDatabase db = this.getWritableDatabase();
        dbStocksHistory.refreshStocksHistory(listHelperStockHistories, db);
    }
    public boolean addStocksHistory(HelperStockHistory helperStockHistory){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.addStocksHistory(helperStockHistory, db);
    }
    public void deleteStocksHistory(){
        SQLiteDatabase db = this.getWritableDatabase();
        dbStocksHistory.deleteStocksHistory(db);
    }
    public List<HelperStockHistory> listHelperStockHistRem(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.listHelperStockHistRem(db);
    }
    public boolean optionalComposite(){
        return dbStocksHistory.optionalComposite();
    }
    public boolean decQtyOnSale(){
        return dbStocksHistory.decQtyOnSale();
    }
    public boolean stocksOk(int stock_id, SQLiteDatabase db, int needed_qty, String needed_unit){
        return dbStocksHistory.stocksOk(stock_id, db, needed_qty, needed_unit);
    }
    public boolean stocksOk(HelperItem helperItem){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.stocksOk(helperItem, db);
    }
    public boolean stocksOk(HelperItem helperItem, int item_id, int var_hdr_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.stocksOk(helperItem, item_id, var_hdr_id, db);
    }
    public boolean stocksOk(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.stocksOk(helperSale, db);
    }
    public int stocksAvailable(HelperItem helperItem){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.stocksAvailable(helperItem, db);
    }
    public int stocksAvailable(HelperSales helperSales){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.stocksAvailable(helperSales, db);
    }
    public int stocksAvailable(HelperItem helperItem, String item_id, String var_hdr_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.stocksAvailable(helperItem, item_id, var_hdr_id, db);
    }
    public int stocksAvailableVarOnly(HelperItem helperItem, String item_id, String var_hdr_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.stocksAvailableVarOnly(helperItem, item_id, var_hdr_id, db);
    }
    public List<String> listOfStocks(HelperItem helperItem){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.listOfStocks(helperItem, db);
    }
    public List<String> listOfStocks(HelperItem helperItem, String item_id, String var_hdr_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.listOfStocks(helperItem, item_id, var_hdr_id, db);
    }
    public String outOfStockName(int stock_id, SQLiteDatabase db, int needed_qty, String needed_unit) {
        return dbStocksHistory.outOfStockName(stock_id, db, needed_qty, needed_unit);
    }
    public List<String> listOutOfStocks(HelperItem helperItem){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.listOutOfStocks(helperItem, db);
    }
    public List<String> listOutOfStocks(HelperItem helperItem, int item_id, int var_hdr_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.listOutOfStocks(helperItem, item_id, var_hdr_id, db);
    }
    public List<String> listOutOfStocks(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.listOutOfStocks(helperSale, db);
    }
    public int priceInItemMenu(HelperItem helperItem){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.priceInItemMenu(helperItem, db);
    }
    public int getStockId(String stock_name){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.getStockId(stock_name, db);
    }
    public List<HelperStockHistory> listStockUsedInPreFinishSale(List<HelperSales> helperSales){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStocksHistory.listStockUsedInPreFinishSale(helperSales, db);
    }
    //STOCKS_HISTORY end

    //CATEGORY start
    public List<HelperCategory> listCategory(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCategory.listCategory(db);
    }
    public int categoryMaxId(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCategory.categoryMaxId(db);
    }
    public int categoryCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCategory.categoryCount(db);
    }
    public boolean addCategory(HelperCategory helperCategory){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCategory.addCategory(helperCategory, db);
    }
    public boolean existsCategory(int cat_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCategory.existsCategory(cat_id, db);
    }
    public void updateCategory(int cat_id, String cat_image, String cat_name){
        SQLiteDatabase db = this.getWritableDatabase();
        dbCategory.updateCategory(cat_id, cat_image, cat_name, db);
    }
    public void deleteCategory(int cat_id){
        SQLiteDatabase db = this.getWritableDatabase();
        dbCategory.deleteCategory(cat_id, db);
    }
    public void deleteAllCategory(){
        SQLiteDatabase db = this.getWritableDatabase();
        dbCategory.deleteAllCategory(db);
    }
    public void refreshCategory(List<HelperCategory> categories){
        SQLiteDatabase db = this.getWritableDatabase();
        dbCategory.refreshCategory(categories, db);
    }
    //CATEGORY end

    //ITEMS start
    public void refreshItems(List<HelperItem> helperItems){
        SQLiteDatabase db = this.getWritableDatabase();
        dbItems.refreshItems(helperItems, db);
    }
    public void deleteAllItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        dbItems.deleteAllItems(db);
    }
    public List<HelperItem> listItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbItems.listItems(db);
    }
    public List<HelperItem> listItems(int cat_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbItems.listItems(cat_id, db);
    }
    public List<String> listItemNameVariants(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbItems.listItemNameVariants(db);
    }
    public List<HelperTwoString> listHelperTwoStrings(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbItems.listHelperTwoStrings(db);
    }
    //ITEMS end

    //VARIANTS_LINKS start
    public void refreshVariantsLinks(List<HelperVariantsLinks> helperVariantsLinks){
        SQLiteDatabase db = this.getWritableDatabase();
        dbVariantsLinks.refreshVariantsLinks(helperVariantsLinks, db);
    }
    public void deleteAllVariantsLinks(){
        SQLiteDatabase db = this.getWritableDatabase();
        dbVariantsLinks.deleteAllVariantsLinks(db);
    }
    public List<HelperVariantsLinks> listVariantsLinks(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbVariantsLinks.listVariantsLinks(db);
    }
    public List<HelperVariantsLinks> listVariantsLinks(int item_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbVariantsLinks.listVariantsLinks(item_id, db);
    }
    public boolean variantsExists(int item_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbVariantsLinks.variantsExists(item_id, db);
    }
    //VARIANTS_LINKS end

    //VARIANTS_HDR start
    public void refreshVariantsHdr(List<HelperVariantsHdr> helperVariantsHdr){
        SQLiteDatabase db = this.getWritableDatabase();
        dbVariantsHdr.refreshVariantsHdr(helperVariantsHdr, db);
    }
    public void deleteAllVariantsHdr(){
        SQLiteDatabase db = this.getWritableDatabase();
        dbVariantsHdr.deleteAllVariantsHdr(db);
    }
    public List<HelperVariantsHdr> listVariantsHdr(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbVariantsHdr.listVariantsHdr(db);
    }
    public List<HelperVariantsHdr> listVariantsHdr(List<HelperVariantsLinks> helperVariantsLinks){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbVariantsHdr.listVariantsHdr(helperVariantsLinks, db);
    }
    //VARIANTS_HDR end

    //VARIANTS_DTLS start
    public void refreshVariantsDtls(List<HelperVariantsDtls> helperVariantsDtls){
        SQLiteDatabase db = this.getWritableDatabase();
        dbVariantsDtls.refreshVariantsDtls(helperVariantsDtls, db);
    }
    public List<HelperVariantsDtls> listVariantsDtls(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbVariantsDtls.listVariantsDtls(db);
    }
    public List<HelperVariantsDtls> listVariantsDtls(int var_hdr_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbVariantsDtls.listVariantsDtls(var_hdr_id, db);
    }
    public List<HelperVariantsDtls> listVariantsDtls(List<HelperVariantsLinks> helperVariantsLinks){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbVariantsDtls.listVariantsDtls(helperVariantsLinks, db);
    }
    public boolean compositeRequired(int var_hdr_id, int var_dtls_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbVariantsDtls.compositeRequired(var_hdr_id, var_dtls_id, db );
    }
    public String getVarHdrId(String var_dtls_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbVariantsDtls.getVarHdrId(var_dtls_id, db);
    }
    public String getVarDtlsName(String var_dtls_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbVariantsDtls.getVarDtlsName(var_dtls_id, db);
    }
    public String getVarDtlsSellingPrice(String var_dtls_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbVariantsDtls.getVarDtlsSellingPric(var_dtls_id, db);
    }
    //VARIANTS_DTLS end

    //SALES start
    //Cannot delete completed sales
    public void refreshSales(List<HelperSales> listHelperSales){
        SQLiteDatabase db = this.getWritableDatabase();
        dbSales.refreshSales(listHelperSales, db);
    }
    public void deleteSalesW(){
        SQLiteDatabase db = this.getWritableDatabase();
        dbSales.deleteSalesW(db);
    }
    public void deleteSaleTesting(){
        SQLiteDatabase db = this.getWritableDatabase();
        dbSales.deleteSales(db);
    }
    public void deleteVariant(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        dbSales.deleteVariant(helperSale, db);
    }
    public void deleteSale(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        dbSales.deleteSale(helperSale, db);
    }
    public void addToCart(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        dbSales.addToCart(helperSale, db);
    }
    public List<HelperSales> listPreFinishSale(HelperSales helperSaleParam){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.listPreFinishSale(helperSaleParam, db);
    }
    public void finishSale(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        dbSales.finishSale(helperSale, db);
    }
    public void insertSaleNew(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        dbSales.insertSaleNew(helperSale, db);
    }
    public void deleteNotInCart(String created_by, String machine_name){
        SQLiteDatabase db = this.getWritableDatabase();
        dbSales.deleteNotInCart(created_by, machine_name, db);
    }
    public boolean notInCartExists(String created_by, String machine_name){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.notInCartExists(created_by, machine_name, db);
    }
    public boolean itemExists(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.itemExists(helperSale, db);
    }
    public boolean variantExists(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.variantExists(helperSale, db);
    }
    public int nextSortOrderIdNoItem(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.nextSortOrderIdNoItem(helperSale, db);
    }
    public int getSortOrderIdItem(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.getSortOrderIdItem(helperSale, db);
    }
    public int getSortOrderIdVariant(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.getSortOrderIdVariant(helperSale, db);
    }
    public int maxTransactionId(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.maxTransactionId(db);
    }
    public int nextTransactionCounter(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.nextTransactionCounter(db);
    }
    public int nextTransactionPerEntry(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.nextTransactionPerEntry(db);
    }
    public int nextSortOrderIdVariant(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.nextSortOrderIdVariant(helperSale, db);
    }
    public int sumItemsPerTranPerEntry(HelperSales helperSale){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.sumItemsPerTranPerEntry(helperSale, db);
    }
    public List<HelperSales> listVariantsInItem(HelperSales helperSalesParam){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.listVariantsInItem(helperSalesParam, db);
    }
    public List<HelperSales> listPreCompletedSales(HelperSales helperSalesParam){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.listPreCompletedSales(helperSalesParam, db);
    }
    public List<HelperSales> listSummaryPreCompletedSalesNewLine(HelperSales helperSalesParam){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.listSummaryPreCompletedSalesNewLine(helperSalesParam, db);
    }
    public List<HelperSales> listHelperSalesDefaultVariants(HelperSales helperSalePar){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.listHelperSalesDefaultVariants(helperSalePar, db);
    }
    public List<HelperSales> listSummarySalesPerDay(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.listSummarySalesPerDay(db);
    }
    public List<HelperSales> listEODGraphFPWeekly(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.listEODGraphFPWeekly(db);
    }
    public int sumCostStocksPerDay(String s_day){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.sumCostStocksPerDay(s_day, db);
    }
    public int estimatedCostPerDay(String s_day){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.estimatedCostPerDay(s_day, db);
    }
    public int estimatedCostPerWeek(String s_week, String s_month){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.estimatedCostPerWeek(s_week, s_month, db);
    }
    public int estimatedReducedSales(String s_day){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.estimatedReducedSales(s_day, db);
    }
    public int estimatedFPSales(String s_day){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.estimatedFPSales(s_day, db);
    }
    public int paymentAdviceWeekly(String s_week, String s_month){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbSales.paymentAdviceWeekly(s_week, s_month, db);
    }
    //SALES end

    //CHANGES start
    public void refreshChanges(List<HelperChanges> helperChanges){
        SQLiteDatabase db = this.getWritableDatabase();
        dbChanges.refreshChanges(helperChanges, db);
    }
    public boolean dbChanged(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbChanges.dbChanged(db);
    }
    public boolean dbChangedStockNames(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbChanges.dbChangedStockNames(db);
    }
    public boolean dbChangedCategory(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbChanges.dbChangedCategory(db);
    }
    public boolean dbChangedItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbChanges.dbChangedItems(db);
    }
    public boolean dbChangedVariantsLinks(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbChanges.dbChangedVariantsLinks(db);
    }
    public boolean dbChangedVariantsHdr(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbChanges.dbChangedVariantsHdr(db);
    }
    public boolean dbChangedVariantsDtls(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbChanges.dbChangedVariantsDtls(db);
    }
    public boolean dbChangedCompositeLinks(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbChanges.dbChangedCompositeLinks(db);
    }
    public boolean dbChangedStockHistories(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbChanges.dbChangedStockHistories(db);
    }
    public boolean dbChangedSales(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbChanges.dbChangedSales(db);
    }
    public boolean dbChangedFPDtls(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbChanges.dbChangedFPDtls(db);
    }
    //CHANGES end

    //COMPOSITE_LINKS start
    public void refreshCompositeLinks(List<HelperCompositeLinks> helperCompositeLinks){
        SQLiteDatabase db = this.getWritableDatabase();
        dbCompositeLinks.refreshCompositeLinks(helperCompositeLinks, db);
    }
    public void deleteAllCompositeLinks(){
        SQLiteDatabase db = this.getWritableDatabase();
        dbCompositeLinks.deleteAllCompositeLinks(db);
    }
    public List<HelperCompositeLinks> listCompositeLinks(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCompositeLinks.listCompositeLinks(db);
    }
    public List<HelperCompositeLinks> listCompositeLinks(int var_hdr_id, int var_dtls_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCompositeLinks.listCompositeLinks(var_hdr_id, var_dtls_id, db);
    }
    public List<HelperCompositeLinks> listCompositeLinks(int item_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCompositeLinks.listCompositeLinks(item_id, db);
    }
    //COMPOSITE_LINKS end

    //DINE_IN_OUT start
    public void deleteAllDineInOut(){
        SQLiteDatabase db = this.getWritableDatabase();
        dbDineInOut.deleteAllDineInOut(db);
    }
    public void insertDefaultDineInOut(){
        SQLiteDatabase db = this.getWritableDatabase();
        dbDineInOut.insertDefaultDineInOut(db);
    }
    public boolean dineExists(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbDineInOut.dineExists(db);
    }
    public boolean dineIn(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbDineInOut.dineIn(db);
    }
    public void updateDineInOut(String dine_in_out){
        SQLiteDatabase db = this.getWritableDatabase();
        dbDineInOut.updateDineInOut(dine_in_out, db);
    }
    //DINE_IN_OUT end

    //FP_DTLS start
    public List<HelperFPDtls> listHelperFPDtls(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbFpDetails.listHelperFPDtls(db);
    }
    public void refreshFPDtls(List<HelperFPDtls> listHelperFPDtls){
        SQLiteDatabase db = this.getWritableDatabase();
        dbFpDetails.refreshFPDtls(listHelperFPDtls, db);
    }
    public boolean addFPDtls(HelperFPDtls helperFPDtl){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbFpDetails.addFPDtls(helperFPDtl, db);
    }
    public void deleteFPDtls(){
        SQLiteDatabase db = this.getWritableDatabase();
        dbFpDetails.deleteFPDtls(db);
    }
    //FP_DTLS end

    //CSV_LINKS start
    public List<HelperCsvLinks> listHelperCsvLinks(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCsvLinks.listHelperCsvLinks(db);
    }
    public void refreshCSVLinks(List<HelperCsvLinks> listHelperCsvLinks){
        SQLiteDatabase db = this.getWritableDatabase();
        dbCsvLinks.refreshCSVLinks(listHelperCsvLinks, db);
    }
    public boolean addCSVLinks(HelperCsvLinks helperCsvLinks){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCsvLinks.addCSVLinks(helperCsvLinks, db);
    }
    public void deleteCSVLinks(){
        SQLiteDatabase db = this.getWritableDatabase();
        dbCsvLinks.deleteCSVLinks(db);
    }
    public String getCsv_Link_Item_Id(String link_name, String link_source){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCsvLinks.getCsv_Link_Item_Id(link_name, link_source, db);
    }
    public HelperCsvLinks helperCsvLinksItemId(String link_name, String link_source){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCsvLinks.helperCsvLinksItemId(link_name, link_source, db);
    }
    public HelperCsvLinks helperCsvLinksVarDtls(String item_id, String link_name, String link_source){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCsvLinks.helperCsvLinksVarDtls(db, item_id, link_name, link_source);
    }
    public List<HelperCsvLinks> listHelperCsvLinksConvertStringToCsvLinks(String item_id, List<String> var_mod, String link_source){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCsvLinks.listHelperCsvLinksConvertStringToCsvLinks(db, item_id, var_mod, link_source);
    }
    public List<String> removeIfSameVarHdr(HelperCsvLinks helperCsvLink, List<String> listVariantsModifiers, String link_source) {
        SQLiteDatabase db = this.getWritableDatabase();
        return dbCsvLinks.removeIfSameVarHdr(db, helperCsvLink, listVariantsModifiers, link_source);
    }
        public boolean dbChangedCsvLinks(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbChanges.dbChanged(db);
    }
    //CSV_LINKS end

    //MAINTAIN start
    //Stock_id different name ( stock_names vs stocks_history )
    //tbfixed later, for now message will appear when stock_names have different name in stocks_history
    public boolean maintainStockNames(){
        SQLiteDatabase db = this.getWritableDatabase();
        return dbStockNames.maintainStockNames(db);
    }
    //MAINTAIN end

}
