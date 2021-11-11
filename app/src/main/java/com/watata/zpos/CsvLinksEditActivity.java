package com.watata.zpos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.watata.zpos.ddlclass.HelperCsvLinks;
import com.watata.zpos.ddlclass.HelperCsvLinksSorter;
import com.watata.zpos.ddlclass.HelperItem;
import com.watata.zpos.ddlclass.HelperSales;
import com.watata.zpos.ddlclass.HelperStockNames;
import com.watata.zpos.ddlclass.HelperStockNamesSorter;
import com.watata.zpos.ddlclass.HelperTwoString;
import com.watata.zpos.ddlclass.HelperVariantsDtls;
import com.watata.zpos.ddlclass.HelperVariantsLinks;
import com.watata.zpos.display.DisplayObjects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CsvLinksEditActivity extends AppCompatActivity {

    //addVariantCsvBtn need to add variants details

    float scale;
    Button backBtn, linkDatabtn;
    Spinner spinner_csv, spinner_db, spinner_link_source;
    TableLayout tableLayout;
    ProgressBar progressBar;
    final private String link_fp = "FP";
    final private String link_lv = "LV";
    final private String modifier_fp = ",";
    final private String modifier_lv = ";";

    HelperDatabase helperDatabase = new HelperDatabase(this);
    List<HelperItem> listHelperItems = new LinkedList<>();
    List<HelperVariantsLinks> listHelperVariantsLinks = new LinkedList<>();
    List<HelperVariantsDtls> listHelperVariantsDtls = new LinkedList<>();
    HashMap<String, String> hmCsvName = new HashMap<>();
    HashMap<String, String> hmDbName = new HashMap<>();
    List<HelperCsvLinks> listHelperCsvLinks = new LinkedList<>();

    private List<String> listCsvItemName  = new LinkedList<>(), listCsvVariantModifier = new LinkedList<>();

    private int rCode = 1;
    private String pathFile;
    private EditText etFile;

    DataHelper dataHelper = new DataHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv_links_edit);

        initializeVariables();

        setupXmlIds();
        setupListeners();
        processDbList();
        getFbCsvLinks();

    }

    private void initializeVariables() {
        scale = this.getResources().getDisplayMetrics().density;
    }

    public void setupXmlIds(){
        backBtn = (Button) findViewById(R.id.btn_back);
        linkDatabtn = (Button) findViewById(R.id.btn_link_data);
        spinner_csv = findViewById(R.id.spinner_csv);
        spinner_db = findViewById(R.id.spinner_db);
        spinner_link_source = findViewById(R.id.spinner_link_source);
        etFile = findViewById(R.id.etFile);

        progressBar = findViewById(R.id.progressBar);
        tableLayout = findViewById(R.id.tblHistoryDtls);

    }

    public void setupListeners(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        linkDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("texto=", hmCsvName.get(spinner_csv.getSelectedItem().toString()) + " --- " + hmDbName.get(spinner_db.getSelectedItem().toString()));
                Log.d("texto=", "item_id=" + getValue(hmDbName.get(spinner_db.getSelectedItem().toString()), 1)
                        + " var_hdr_id=" + getValue(hmDbName.get(spinner_db.getSelectedItem().toString()), 2)
                        + " var_dtls_id=" + getValue(hmDbName.get(spinner_db.getSelectedItem().toString()), 3)
                );


                HelperCsvLinks helperCsvLink = new HelperCsvLinks(
                        "" + ( Integer.parseInt(listHelperCsvLinks.get(listHelperCsvLinks.size()-1).getCsv_link_id()) + 1 )
                        ,hmCsvName.get(spinner_csv.getSelectedItem().toString())
                        ,getValue(hmDbName.get(spinner_db.getSelectedItem().toString()), 1)
                        ,getValue(hmDbName.get(spinner_db.getSelectedItem().toString()), 2)
                        ,getValue(hmDbName.get(spinner_db.getSelectedItem().toString()), 3)
                        ,spinner_link_source.getSelectedItem().toString()
                );
                //PUSH start
                //DatabaseReference reference = FirebaseDatabase.getInstance().getReference("stock_names").push();
                //reference.setValue(helperStockNames);
                //PUSH end

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("csv_links");
                reference.child("" + helperCsvLink.getCsv_link_id()).setValue(helperCsvLink);

                ChangesFB changesFB = new ChangesFB();
                changesFB.ChangesCSVLinks();


                popMessage("Link added.");

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
                if(spinner_link_source.getSelectedItem().toString().equals(link_lv)){
                    processFileLv();
                } else if (spinner_link_source.getSelectedItem().toString().equals(link_fp)){
                    processFileFP();
                }

            }
        }
    }

    public void openFileChooser(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("*/*");
        startActivityForResult(intent,rCode);
    }

    public void processFileLv(){

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
        List<String> listCsvName = new LinkedList<>();
        listCsvName.clear();

        final String sLoySeparator = ",";
        boolean bHeader = true;

        pathFile = pathFile.substring(pathFile.lastIndexOf(":") + 1);
        etFile.setText(pathFile);
        File file = new File(pathFile);

        String buffRead = "";


        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while((buffRead  = reader.readLine()) != null){
                listVariantsModifiers.clear();

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
                            Log.d("texto=", "modifiers=" + modifiers + " variant=" + variant);
                            listVariantsModifiers = getModifiers(modifiers, modifier_lv);
                            listVariantsModifiers.add(variant);

                            Log.d("texto=", "Total modifiers size=" + listVariantsModifiers.size());

                            qty = getSubstring(getSubstring(buffRead, sLoySeparator, int_qty_occurrence), ".", 0);
                            //Log.d("texto=", "item_name=" + item_name + " variant=" + variant + " modifiers=" + modifiers + " qty=" + qty);

                                    Log.d("texto=", " INSERT Item");
                                    listCsvName.add(item_name);

                                    //insert var_dtls
                                    for (int k=0; k < listVariantsModifiers.size(); k++){
                                        Log.d("texto=", " INSERT Variants " + listVariantsModifiers.get(k) + " size=" + listVariantsModifiers.size());
                                        listCsvName.add(item_name + "," + listVariantsModifiers.get(k));
                                    }

                        }
                    }
                }
                bHeader = false;
            }

            Log.d("texto=", "success");
            addSpinnerCsv(listCsvName);

        } catch(Exception e){
            Log.d("texto=", "error " + e.getMessage());
            System.out.println( e.getMessage());
        }
    }

    public void processFileFP(){

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
        List<String> listCsvName = new LinkedList<>();
        listCsvName.clear();

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
                            Log.d("lOneItemOrder", "i=" + i + " value=" + lOneItemOrder.get(i));
                            tempQty = lOneItemOrder.get(i).substring(0,lOneItemOrder.get(i).indexOf(" ")).trim();
                            Log.d("lOneItemOrder", "tempQty=" + tempQty);
                            tempS = lOneItemOrder.get(i).substring(lOneItemOrder.get(i).indexOf(" ")).trim();
                            Log.d("lOneItemOrder", "tempS=" + tempS);
                            for (int j=0;j<Integer.parseInt(tempQty);j++){
                                tempVariant = null;
                                tempModifiers = null;
                                listVariantsModifiers.clear();

                                Log.d("lOneItemOrder", "(=" + tempS.indexOf("(") + " [=" + tempS.indexOf("["));
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
                                listCsvName.add(tempItemName);

                                if (tempModifiers!=null){
                                    listVariantsModifiers = getModifiers(tempModifiers,modifier_fp);
                                } else if (tempVariant!=null){
                                    listVariantsModifiers.add(tempVariant);
                                } else if (tempModifiers==null && tempVariant==null){
                                    listVariantsModifiers.add(tempItemName);
                                }
                                for (int k=0; k < listVariantsModifiers.size(); k++){
                                    Log.d("texto=", " INSERT Variants " + listVariantsModifiers.get(k) + " size=" + listVariantsModifiers.size());
                                    listCsvName.add(tempItemName + "," + listVariantsModifiers.get(k));
                                }
                            }
                            Log.d("lOneItemOrder=", "lOneItemOrder=" + lOneItemOrder.get(i));
                            Log.d("lOneItemOrder=", "tempQty=" + tempQty);
                            Log.d("lOneItemOrder=", "tempItemName=" + tempItemName);
                            Log.d("lOneItemOrder=", "tempVariant=" + tempVariant);
                            Log.d("lOneItemOrder=", "tempModifiers=" + tempModifiers);
                        }
                    }
                }
                bHeader = false;
            }

            Log.d("texto=", "success");
            addSpinnerCsv(listCsvName);

        } catch(Exception e){
            Log.d("texto=", "error " + e.getMessage());
            System.out.println( e.getMessage());
        }
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

        ///if (s.indexOf(";") > 0){
        if (s.length()>0) {
            newS = newS + ";";
            for (int i = 0; i < countChar(s, ";") + 1; i++) {
                listMod.add(newS.substring(0, newS.indexOf(";")));
                newS = newS.substring(newS.indexOf(";") + 1);
                newS = newS.trim();
            }
        }
        ///}

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

    public void processDbList(){
        List<String> listDbName = new LinkedList<>();
        listDbName.clear();

        List<HelperTwoString> helperTwoStrings = helperDatabase.listHelperTwoStrings();
        for (int i = 0; i < helperTwoStrings.size(); i++){
            listDbName.add(helperTwoStrings.get(i).getFirst_string());
            hmDbName.put(helperTwoStrings.get(i).getFirst_string(), helperTwoStrings.get(i).getSecond_string());
        }
        addSpinnerDb(listDbName);
    }

    private void addSpinnerCsv(List<String> listCsvName){
        List<String> listAppVariant = new LinkedList<>();
        listAppVariant.clear();

        listAppVariant = dataHelper.listSortUnique(listCsvName);
        for (int i=0; i < listAppVariant.size(); i++){
            if(listAppVariant.get(i).indexOf(",")>0){
                hmCsvName.put(listAppVariant.get(i), listAppVariant.get(i).substring(listAppVariant.get(i).indexOf(",")+1));
            } else {
                hmCsvName.put(listAppVariant.get(i), listAppVariant.get(i));
            }
        }
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listAppVariant);
        spinner_csv.setAdapter(itemAdapter);
    }

    private void addSpinnerDb(List<String> listCsvName){
        List<String> listAppVariant = new LinkedList<>();
        listAppVariant.clear();

        listAppVariant = dataHelper.listSortUnique(listCsvName);
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listAppVariant);
        spinner_db.setAdapter(itemAdapter);
    }

    public void getFbCsvLinks(){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("csv_links");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listHelperCsvLinks.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listHelperCsvLinks.add(snapshot.getValue(HelperCsvLinks.class));
                    }
                    populateFbCsvLinks(listHelperCsvLinks);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public String getValue(String s, int n){
        String newS = s;
        String value = null;
        if (newS.indexOf(",")>0) {
            newS = newS + ",";
            for (int i = 0; i < n; i++) {
                value = newS.substring(0, newS.indexOf(","));
                newS = newS.substring(newS.indexOf(",") + 1);
            }
        } else {
            if (n==1){
                value = s;
            }
        }

        return value;
    }
    
    public void populateFbCsvLinks(List<HelperCsvLinks> listHelperCsvLinks){
        DisplayObjects displayObjects = new DisplayObjects();
        
        tableLayout.removeAllViews();

        // Header
        TableRow tableRowHdr = new TableRow(this);
        tableRowHdr.setWeightSum(17);
        tableLayout.addView(tableRowHdr);
        //columns
        TableRow.LayoutParams param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(displayObjects.createTextViewTable( this,"Del?", "header" ), param0);

        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(displayObjects.createTextViewTable( this,"Item Name", "header" ), param0);

        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(displayObjects.createTextViewTable( this,"Var Name", "header" ), param0);

        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6);
        tableRowHdr.addView(displayObjects.createTextViewTable( this,"Csv Name", "header" ), param0);

        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(displayObjects.createTextViewTable( this,"Source", "header" ), param0);


        List<HelperCsvLinksSorter> listHelperCsvLinksSorter = new LinkedList<>();
        listHelperCsvLinksSorter.clear();
        HelperCsvLinksSorter helperCsvLinksSorter;
        for(int i = 0; i < listHelperCsvLinks.size(); i++){
            helperCsvLinksSorter = new HelperCsvLinksSorter();
            helperCsvLinksSorter.setCsv_link_id(listHelperCsvLinks.get(i).getCsv_link_id());
            helperCsvLinksSorter.setCsv_link_name(listHelperCsvLinks.get(i).getCsv_link_name());
            helperCsvLinksSorter.setZ_link_item_id(helperDatabase.getItemName(listHelperCsvLinks.get(i).getZ_link_item_id()));
            helperCsvLinksSorter.setZ_link_var_dtls_id(helperDatabase.getVarDtlsName(listHelperCsvLinks.get(i).getZ_link_var_dtls_id()));
            helperCsvLinksSorter.setCsv_link_source(listHelperCsvLinks.get(i).getCsv_link_source());
            listHelperCsvLinksSorter.add(helperCsvLinksSorter);

        }

        Collections.sort(listHelperCsvLinksSorter, new Comparator<HelperCsvLinksSorter>(){
            public int compare(HelperCsvLinksSorter obj1, HelperCsvLinksSorter obj2) {
                // ## Ascending order
                //return obj1.getStock_name().compareToIgnoreCase(obj2.getStock_name()); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                // ## Descending order
                return obj2.getZ_link_item_id().compareToIgnoreCase(obj1.getZ_link_item_id()); // To compare string values
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
            }
        });

        for (int row = listHelperCsvLinksSorter.size() - 1; row >= 0 ; row--){
            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(15);
            tableLayout.addView(tableRow);

            //columns

            TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);

            ImageView delImage;
            delImage = delImageVIew(listHelperCsvLinksSorter.get(row));
            tableRow.addView(delImage, param);

            param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
            tableRow.addView(displayObjects.createTextViewTable(this, "" + listHelperCsvLinksSorter.get(row).getZ_link_item_id()), param);

            param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
            tableRow.addView(displayObjects.createTextViewTable(this, "" + listHelperCsvLinksSorter.get(row).getZ_link_var_dtls_id()), param);

            param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6);
            tableRow.addView(displayObjects.createTextViewTable(this, "" + listHelperCsvLinksSorter.get(row).getCsv_link_name()), param);

            param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
            tableRow.addView(displayObjects.createTextViewTable(this, "" + listHelperCsvLinksSorter.get(row).getCsv_link_source()), param);

        }
    }

    public ImageView delImageVIew(final HelperCsvLinksSorter helperCsvLinksSorter){
        int dp35 = (int) (35 * scale + 0.5f);
        LinearLayout.LayoutParams lp35dp = new LinearLayout.LayoutParams( dp35, dp35);

        //imageView delete
        ImageView delImmage = new ImageView(this);
        lp35dp.weight = 1.0f;
        lp35dp.gravity = Gravity.RIGHT;
        delImmage.setLayoutParams(lp35dp);
        delImmage.setImageResource(R.drawable.ic_delete);


        delImmage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirm");
                builder.setMessage("Delete " + helperCsvLinksSorter.getCsv_link_source() + " " + helperCsvLinksSorter.getZ_link_item_id() + " " + helperCsvLinksSorter.getZ_link_var_dtls_id() + "?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRecord(helperCsvLinksSorter);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        return delImmage;
    }

    public void deleteRecord(HelperCsvLinksSorter helperCsvLinksSorter){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("csv_links").orderByChild("csv_link_id").equalTo(helperCsvLinksSorter.getCsv_link_id());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                ChangesFB changesFB = new ChangesFB();
                changesFB.ChangesCSVLinks();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_LONG).show();
    }

}