package com.watata.zpos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.watata.zpos.ddlclass.HelperCsvLinks;
import com.watata.zpos.ddlclass.HelperSales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class ProcessExportFileActivity extends AppCompatActivity {

    private EditText etFile;
    private int rCode = 1;
    private String pathFile;
    final private String link_fp = "FP";
    final private String link_lv = "LV";
    final private String modifier_fp = ",";
    final private String modifier_lv = ";";
    Button btnLoyverse;
    final String sLoySeparatorDtls = "\"";
    HelperDatabase helperDatabase = new HelperDatabase(this);


    String buffGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_export_file);

        etFile = findViewById(R.id.etFile);
        btnLoyverse = findViewById(R.id.loyverseBtn);

        btnLoyverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pathFile != null) {
                    openLoyverseFile();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null)
            return;

        if (requestCode ==rCode){
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    pathFile = uri.getPath();
                }
        }
    }



    public void openFileChooser(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("*/*");
        startActivityForResult(intent,rCode);
    }

    public void openLoyverseFile(){

        //location
        final int int_date_occurrece = 0
                , int_receipt_type_occurrence = 2
                , int_item_name_occurrence = 5
                , int_variant_occurrence = 6
                , int_modifiers_occurrence = 7
                , int_qty_occurrence = 8
                ;
        final String sale_type = "Sale";

        //fields
        String date, receipt_type, item_name, variant, modifiers, qty;
        List<String> listVariantsModifiers = new LinkedList<>();
        List<HelperCsvLinks> listHelperCsvLinksItemId = new LinkedList<>();
        List<HelperCsvLinks> listHelperCsvLinksVardtls = new LinkedList<>();
        HelperCsvLinks helperCsvLinkItemId = new HelperCsvLinks();
        HelperCsvLinks helperCsvLinksVarDtls = new HelperCsvLinks();

        List<String> list_item_id = new LinkedList<>();


        final String sLoySeparator = ",";
        boolean bHeader = true;

        pathFile = pathFile.substring(pathFile.lastIndexOf(":") + 1);
        etFile.setText(pathFile);
        File file = new File(pathFile);

        String buffRead = "";
        int intCount = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while((buffRead  = reader.readLine()) != null){
                listVariantsModifiers.clear();
                buffGlobal = buffRead;


                //exclude header and FP
                if (!bHeader){
                    buffRead = getProperString(buffRead);
                    Log.d("texto=", "bufffffffffffffffffRead after=" + buffRead);

                    //fields
                    variant = getSubstring(buffRead, sLoySeparator, int_variant_occurrence);

                    if (variant.indexOf("FP") < 0){

                        //fields
                        receipt_type = getSubstring(buffRead, sLoySeparator, int_receipt_type_occurrence);
                        if (receipt_type.equals(sale_type)){

                            //fields
                            date = getSubstring(buffRead, sLoySeparator, int_date_occurrece);
                            date = getConvertDate(date.substring(0,2), date.substring(3,5), date.substring(6,10));
                            item_name = getSubstring(buffRead, sLoySeparator, int_item_name_occurrence);
                            modifiers = getSubstring(buffRead, sLoySeparator, int_modifiers_occurrence);
                            listVariantsModifiers = getModifiers(modifiers,modifier_lv);
                            listVariantsModifiers.add(variant);
                            qty = getSubstring(getSubstring(buffRead, sLoySeparator, int_qty_occurrence), ".", 0);


                            helperCsvLinkItemId = helperDatabase.helperCsvLinksItemId(item_name, link_lv);
                            if (helperCsvLinkItemId.getZ_link_item_id()!=null){
                                listVariantsModifiers = helperDatabase.removeIfSameVarHdr(helperCsvLinkItemId, listVariantsModifiers, link_lv);
                                listHelperCsvLinksVardtls.clear();
                                listHelperCsvLinksVardtls = helperDatabase.listHelperCsvLinksConvertStringToCsvLinks(helperCsvLinkItemId.getZ_link_item_id(), listVariantsModifiers, link_lv);

                                for (int i=0; i < Integer.parseInt(qty); i++){
                                    Log.d("texto=", " INSERT SALESSSSSSSSSSSSSSSSSS");
                                    HelperSales helperSalesItem = insertItemSale(helperCsvLinkItemId.getZ_link_item_id(), date);

                                    //insert var_dtls
                                    for (int k=0;k<listHelperCsvLinksVardtls.size();k++){
                                        insertVariantsSale(helperCsvLinkItemId.getZ_link_item_id(), date, listHelperCsvLinksVardtls.get(k).getZ_link_var_dtls_id(), helperSalesItem);
                                    }
                                }
                            } else {
                                Log.d("texto=", "No setup in CSV link for item_name=" + item_name);
                            }
                        }

                    }
                }

                bHeader = false;
            }

            Log.d("texto=", "success");


        } catch(Exception e){
            Log.d("texto=", "error buffGlobal=" + buffGlobal + e.getMessage());
            System.out.println( e.getMessage());
        }
    }

    public void openFoodPandaFile(View view){
        //location
        final int int_order_id = 1
                , int_order_status = 4
                , int_date_occurrece = 5
                , int_qty_item_variant_modifiers = 24
                ;
        final String accepted_order = "Delivered";

        //fields
        String order_id, order_status, date, qty_item_variant_modifiers;
        List<String> listVariantsModifiers = new LinkedList<>();
        List<HelperCsvLinks> listHelperCsvLinksVardtls = new LinkedList<>();
        HelperCsvLinks helperCsvLinkItemId = new HelperCsvLinks();

        List<String> list_item_id = new LinkedList<>();


        final String sLoySeparator = ",";
        boolean bHeader = true;

        pathFile = pathFile.substring(pathFile.lastIndexOf(":") + 1);
        etFile.setText(pathFile);
        File file = new File(pathFile);

        String buffRead = "";
        int intCount = 0;


        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while((buffRead  = reader.readLine()) != null){
                listVariantsModifiers.clear();
                buffGlobal = buffRead;


                //exclude header and FP
                if (!bHeader){
                    buffRead = getProperString(buffRead);
                    Log.d("texto=", "bufffffffffffffffffRead after=" + buffRead);

                    //fields
                    order_status = getSubstring(buffRead, sLoySeparator, int_order_status);

                    if (order_status.equals(accepted_order)){
                        //fields
                        order_id = getSubstring(buffRead, sLoySeparator, int_order_id);
                        date = getSubstring(buffRead, sLoySeparator, int_date_occurrece);
                        date = getConvertDate(date.substring(9,10), date.substring(6,7), date.substring(0,4));
                        qty_item_variant_modifiers = getSubstring(buffRead, sLoySeparator, int_qty_item_variant_modifiers);

                        //3 Fried Noodles With Siomai (Siomai) [1 Sweet Chili Sauce; 1 Beef Teriyaki Sauce; 1 Pork Siomai]; 4 Pork Siomai 4pcs
                        String s_qty, s_item_name, s_variant, s_modifiers;

                        qty_item_variant_modifiers = commaVariantsModifiers(qty_item_variant_modifiers);
                        List<String> lOneItemOrder = new LinkedList<>();
                        lOneItemOrder = listOneItemOrder(qty_item_variant_modifiers);

                        String tempS = "";
                        String tempQty, tempItemName = null, tempVariant = null, tempModifiers = null;
                        tempS = qty_item_variant_modifiers;
                        //1 Z6. Fries & Drinks + Fried Noodles With Toppings [1 Sour Cream, 1 Chicken Balls, 1 Beef Teriyaki Sauce, 1 Barbeque Sauce];

                        for (int i=0;i<lOneItemOrder.size();i++){
                            tempQty = lOneItemOrder.get(i).substring(0,lOneItemOrder.get(i).indexOf(" ")).trim();
                            tempS = lOneItemOrder.get(i).substring(lOneItemOrder.get(i).indexOf(" ")).trim();

                            tempVariant = null;
                            tempModifiers = null;
                            listVariantsModifiers.clear();

                            if (tempS.indexOf("(")>0 && tempS.indexOf("[")>0){
                                if (tempS.indexOf("(")<tempS.indexOf("[")){
                                    tempItemName = tempS.substring(0,tempS.indexOf("(")).trim();
                                    tempVariant = tempS.substring(tempS.indexOf("(")+1,tempS.indexOf(")")).trim();
                                    tempModifiers = tempS.substring(tempS.indexOf("[")+1,tempS.indexOf("]")).trim();
                                } else {
                                    tempItemName = tempS.substring(0,tempS.indexOf("[")).trim();
                                    tempVariant = tempS.substring(tempS.indexOf("(")+1,tempS.indexOf(")")).trim();
                                    tempModifiers = tempS.substring(tempS.indexOf("[")+1,tempS.indexOf("]")).trim();
                                }
                            } else if (tempS.indexOf("(")>0){
                                tempItemName = tempS.substring(0,tempS.indexOf("(")).trim();
                                tempVariant = tempS.substring(tempS.indexOf("(")+1,tempS.indexOf(")")).trim();
                            } else if (tempS.indexOf("[")>0){
                                tempItemName = tempS.substring(0,tempS.indexOf("[")).trim();
                                tempModifiers = tempS.substring(tempS.indexOf("[")+1,tempS.indexOf("]")).trim();
                            } else {
                                tempItemName = tempS.trim();
                            }

                            if (tempModifiers!=null){
                                listVariantsModifiers = getModifiers(tempModifiers,modifier_fp);
                            } else if (tempVariant!=null){
                                listVariantsModifiers.add(tempVariant);
                            } else if (tempModifiers==null && tempVariant==null){
                                listVariantsModifiers.add(tempItemName);
                            }

                            helperCsvLinkItemId = helperDatabase.helperCsvLinksItemId(tempItemName, link_fp);

                            if (helperCsvLinkItemId!=null){
                                listVariantsModifiers = helperDatabase.removeIfSameVarHdr(helperCsvLinkItemId, listVariantsModifiers, link_fp);
                                listHelperCsvLinksVardtls.clear();
                                listHelperCsvLinksVardtls = helperDatabase.listHelperCsvLinksConvertStringToCsvLinks(helperCsvLinkItemId.getZ_link_item_id(), listVariantsModifiers, link_fp);


                                for (int j=0;j<Integer.parseInt(tempQty);j++){
                                    Log.d("texto=", " INSERT SALESSSSSSSSSSSSSSSSSS");
                                    HelperSales helperSalesItem = insertItemSale(helperCsvLinkItemId.getZ_link_item_id(), date);

                                    //insert var_dtls
                                    for (int k=0;k<listHelperCsvLinksVardtls.size();k++){
                                        insertVariantsSale(helperCsvLinkItemId.getZ_link_item_id(), date, listHelperCsvLinksVardtls.get(i).getZ_link_var_dtls_id(), helperSalesItem);
                                    }

                                }
                                Log.d("lOneItemOrder=", "lOneItemOrder=" + lOneItemOrder.get(i));
                                Log.d("lOneItemOrder=", "tempQty=" + tempQty);
                                Log.d("lOneItemOrder=", "tempItemName=" + tempItemName);
                                Log.d("lOneItemOrder=", "tempVariant=" + tempVariant);
                                Log.d("lOneItemOrder=", "tempModifiers=" + tempModifiers);
                            } else {
                                Log.d("texto=", "No setup in CSV link for item_name=" + tempItemName);
                            }

                        }
                    }
                }
                bHeader = false;
            }

            Log.d("texto=", "success");

        } catch(Exception e){
            Log.d("texto=", "error buffGlobal=" + buffGlobal + e.getMessage());
            System.out.println( e.getMessage());
        }
    }

    public List<String> listOneItemOrder(String s){
        List<String> lOneItemOrder = new LinkedList<>();
        String newS = s;
        if (newS.indexOf(";")>0){
            while(newS.indexOf(";")>0){
                lOneItemOrder.add(newS.substring(0,newS.indexOf(";")).trim());
                newS = newS.substring(newS.indexOf(";")+1).trim();
            }
        } else {
            lOneItemOrder.add(newS);
        }
        return lOneItemOrder;
    }

    public String commaVariantsModifiers(String s){
        Log.d("texto=", "commaVariantsModifiers start");
        String newS = s + ";";
        String outS = null;
        Boolean variant = false, modifier = false;

        //3 Fried Noodles With Siomai (Siomai) [1 Sweet Chili Sauce; 1 Beef Teriyaki Sauce; 1 Pork Siomai]; 4 Pork Siomai 4pcs
        //comma the variants first
        for (int i=0;i<s.length();i++){
            if(newS.substring(i,i+1).equals("(") && !variant){
                variant = true;
            }
            if(newS.substring(i,i+1).equals(")") && variant){
                variant = false;
            }

            if(variant){
                if(newS.substring(i,i+1).equals(";")){
                    newS = newS.substring(0,i) + "," + newS.substring(i+1);
                }
            }

        }

        for (int i=0;i<s.length();i++){
            if(newS.substring(i,i+1).equals("[") && !variant){
                variant = true;
            }
            if(newS.substring(i,i+1).equals("]") && variant){
                variant = false;
            }

            if(variant){
                if(newS.substring(i,i+1).equals(";")){
                    newS = newS.substring(0,i) + "," + newS.substring(i+1);
                }
            }

        }

        outS = newS;
        Log.d("texto=", "commaVariantsModifiers end");
        return outS;
    }

    /*
    public void openFoodPandaFile(View view){

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, PackageManager.PERMISSION_GRANTED);

        File file = new File(etFile.getText().toString());
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String buffRead = "";
            String texto = "";
            while((buffRead  = reader.readLine()) != null){
                texto += buffRead;
            }


            Log.d("openFoodPandaFile", "success");

        } catch(Exception e){
            Log.d("openFoodPandaFile", "error");
            System.out.println( e.getMessage());
        }
    }
    */

    public void writeFile(View view){
        try {
            FileOutputStream fileOutputStream = openFileOutput("item.csv", MODE_PRIVATE);
            fileOutputStream.write("watata".getBytes());
            fileOutputStream.close();

            etFile.setText("location=" + this.getFilesDir());
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){

        }
    }

    public void readFile(View view){
        try {
            FileInputStream fileInputStream = openFileInput("item.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            String lines;
            while ((lines=bufferedReader.readLine()) != null){
                stringBuffer.append(lines + "\n");


            }
            Log.d("readFile", stringBuffer.toString());
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getProperString(String s){
        boolean startQuote = false;
        String returnS= "";
        String sSingle;

        if (s.indexOf("\"") > 0){
            for(int i=0; i < s.length(); i++) {
                sSingle = s.substring(i,i+1);

                if(sSingle.equals("\"")){
                    if (startQuote){
                        startQuote = false;
                    } else {
                        startQuote = true;
                    }
                }

                if (startQuote){
                    if (sSingle.equals(",")){
                        sSingle = ";";
                    }
                }
                if (!sSingle.equals("\"")){
                    returnS = returnS + sSingle;
                }

            }

            //Log.d("texto=", "returnS=" + returnS);

            return returnS;
        } else {
            return s;
        }


    }

    public String getSubstring(String s, String separator, int occurance){
        String newS = s;

        if (newS.indexOf(separator)>0){
            if (occurance!=0){
                for (int i = 0; i < occurance; i++) {
                    newS = newS.substring(newS.indexOf(separator)+1);
                }
            }

            newS = newS.substring(0, newS.indexOf(separator));
        }

        return newS;
    }

    public int countChar(String s, String c)
    {
        String sNew = s;
        int count = 0;

        for(int i=0; i < s.length(); i++) {
            if(sNew.substring(0,1).equals(c)){
                count++;
            }
            sNew = sNew.substring(1);
        }
        return count;
    }

    public List<String> getItems(String s, String separator){
        List<String> listItems = new LinkedList<>();
        listItems.clear();
        int startItem = 0, endItem;
        String items = s;

        //sample data: Cheese, Pork Siomai, Sour Cream


        if (countChar(s, separator) > 0){
            for (int i = 0; i < countChar(s, separator); i++){
                if (items.indexOf(separator) > 0){
                    endItem = items.indexOf(separator);
                } else {
                    endItem = items.length();
                }
                listItems.add(items.substring(startItem, endItem));
                if (items.length() > 1){
                    items = items.substring(endItem+1);
                }
            }
        } else {
            listItems.add(items);
        }

        return listItems;
    }

    private String getConvertDate(String dd, String mm, String yyyy){
        String mon;
        int mmInt = Integer.parseInt(mm);

        switch(mmInt){
            case 0:
                mon = "Jan";
                break;
            case 1:
                mon = "Feb";
                break;
            case 2:
                mon = "Mar";
                break;
            case 3:
                mon = "Apr";
                break;
            case 4:
                mon = "May";
                break;
            case 5:
                mon = "Jun";
                break;
            case 6:
                mon = "Jul";
                break;
            case 7:
                mon = "Aug";
                break;
            case 8:
                mon = "Sep";
                break;
            case 9:
                mon = "Oct";
                break;
            case 10:
                mon = "Nov";
                break;
            case 11:
                mon = "Dec";
                break;
            default:
                mon = "Jan";
                break;
        }

        return mon + " " + dd + ", " + yyyy;

    }

    /*
    private List<String> getModifiers(String s){
        String newS = s;
        List<String> listMod = new LinkedList<>();

        if (s.indexOf(";") > 0){
            newS = newS + ";";
            for(int i=0; i<countChar(s,";");i++){
                listMod.add( newS.substring(0,newS.indexOf(";")));
                newS = newS.substring(newS.indexOf(";"));
            }
        }

        return listMod;
    }
    */

    private List<String> getModifiers(String s, String s_sep){
        String newS = s;
        List<String> listMod = new LinkedList<>();

        ///if (s.indexOf(";") > 0){
        if (s.length()>0) {
            newS = newS + s_sep;
            for (int i = 0; i < countChar(s, s_sep) + 1; i++) {
                listMod.add(newS.substring(0, newS.indexOf(s_sep)));
                newS = newS.substring(newS.indexOf(s_sep) + 1);
                newS = newS.trim();
            }
        }
        ///}

        return listMod;
    }

    private HelperSales insertItemSale(String item_id, String date){



        //insert item
        HelperSales helperSale = new HelperSales();
        helperSale.setItem_id(Integer.parseInt(item_id));
        helperSale.setMachine_name("pos1");
        helperSale.setItem_name(helperDatabase.getItemName(item_id));
        helperSale.setQty("1");
        helperSale.setSelling_price(helperDatabase.getItemSellingPrice(item_id));
        helperSale.setDate(date);
        helperSale.setCreated_by("admin");
        helperSale.setCompleted("N"); //to detect variant exists
        helperSale.setTransaction_counter(helperDatabase.nextTransactionCounter());
        helperSale.setTransaction_per_entry(helperDatabase.nextTransactionPerEntry());
        helperSale.setSort_order_id(helperDatabase.getSortOrderIdItem(helperSale));
        helperDatabase.insertSaleNew(helperSale);
        //helperDatabase.updateDuplicateVariants(helperSale);
        helperDatabase.addToCart(helperSale);

        return helperSale;

    }

    private void insertVariantsSale(String item_id, String date, String var_dtls_id, HelperSales helperSalesItem){

        HelperSales helperSaleVar = new HelperSales();
        helperSaleVar.setItem_id(Integer.parseInt(item_id));
        helperSaleVar.setItem_name(helperDatabase.getVarDtlsName(var_dtls_id));
        helperSaleVar.setSelling_price(helperDatabase.getVarDtlsSellingPrice(var_dtls_id));
        helperSaleVar.setMachine_name("pos1");
        helperSaleVar.setDate(date);
        helperSaleVar.setCreated_by("admin");
        helperSaleVar.setCompleted("N"); //to detect variant exists
        helperSaleVar.setQty("1");
        helperSaleVar.setVar_hdr_id(helperDatabase.getVarHdrId(var_dtls_id));
        helperSaleVar.setVar_dtls_id(var_dtls_id);
        helperSaleVar.setTransaction_counter(helperSalesItem.getTransaction_counter());
        helperSaleVar.setTransaction_per_entry(helperSalesItem.getTransaction_per_entry());
        //helperSaleVar.setSort_order_id(helperSalesItem.getSort_order_id());

        //helperSaleVar.setTransaction_counter(helperDatabase.nextTransactionCounter());
        //helperSaleVar.setTransaction_per_entry(helperDatabase.nextTransactionPerEntry());
        if (helperDatabase.variantExists(helperSaleVar)) {
            helperSaleVar.setSort_order_id(helperDatabase.getSortOrderIdVariant(helperSaleVar));
        } else {
            helperSaleVar.setSort_order_id(helperDatabase.nextSortOrderIdVariant(helperSaleVar));
        }
        helperDatabase.insertSaleNew(helperSaleVar);
        //helperDatabase.updateDuplicateVariants(helperSale);
        helperDatabase.addToCart(helperSaleVar);
    }

}