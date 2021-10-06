package com.watata.zpos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ProcessExportFileActivity extends AppCompatActivity {

    private EditText etFile;
    private int rCode = 1;
    private String pathFile;
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
                    openLoyverseFile();
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
                            listVariantsModifiers = getModifiers(modifiers);
                            listVariantsModifiers.add(variant);
                            qty = getSubstring(getSubstring(buffRead, sLoySeparator, int_qty_occurrence), ".", 0);

                            for (int i=0; i < Integer.parseInt(qty); i++){
                                //insert item_id
                                List<String> list_item_id = helperDatabase.listLinkTypeValues("item_id", item_name);
                                Log.d("texto=", "list_item_id size=" + list_item_id.size() + " i=" + i);
                                for (int j=0; j < list_item_id.size(); j++){
                                    insertItemSale(list_item_id.get(j), date);
                                    //insert var_dtls
                                    for (int k=0; k < listVariantsModifiers.size(); k++){
                                        List<String> list_var_dtls_id = helperDatabase.listLinkTypeValues("var_dtls_id", listVariantsModifiers.get(j));
                                        for (int l=0; l < list_var_dtls_id.size(); l++){
                                            insertVariantsSale(list_item_id.get(j), date, list_var_dtls_id.get(k));
                                        }
                                    }
                                }
                            }
                            Log.d("texto=", "item_name=" + item_name + " variant=" + variant + " modifiers=" + modifiers + " qty=" + qty);
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

            Log.d("texto=", "returnS=" + returnS);

            return returnS;
        } else {
            return s;
        }


    }

    public String getSubstring(String s, String separator, int occurance){
        String newS = s;

        if (occurance!=0){
            for (int i = 0; i < occurance; i++) {
                    newS = newS.substring(newS.indexOf(separator)+1);
            }
        }

        newS = newS.substring(0, newS.indexOf(separator));

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

    private void insertItemSale(String item_id, String date){
        Log.d("texto=", "insertItemSale item_id=" + item_id + " date=" + date );

        //insert item
        HelperSales helperSale = new HelperSales();
        helperSale.setItem_id(Integer.parseInt(item_id));
        helperSale.setMachine_name("pos1");
        helperSale.setItem_name(helperDatabase.getItemName(item_id));
        helperSale.setQty("1");
        helperSale.setSelling_price(helperDatabase.getItemSellingPrice(item_id));
        helperSale.setDate(date);
        helperSale.setCreated_by("admin");
        helperSale.setCompleted("W");
        helperSale.setTransaction_counter(helperDatabase.nextTransactionCounter());
        helperSale.setTransaction_per_entry(helperDatabase.nextTransactionPerEntry());
        helperSale.setSort_order_id(helperDatabase.getSortOrderIdItem(helperSale));
        helperDatabase.insertSaleNew(helperSale);
    }

    private void insertVariantsSale(String item_id, String date, String var_dtls_id){

        Log.d("texto=", "insertVariantsSale item_id=" + item_id + " date=" + date + " var_dtls_id=" + var_dtls_id);

        HelperSales helperSaleVar = new HelperSales();
        helperSaleVar.setItem_id(Integer.parseInt(item_id));
        helperSaleVar.setItem_name(helperDatabase.getItemName(item_id));
        helperSaleVar.setSelling_price(helperDatabase.getItemSellingPrice(item_id));
        helperSaleVar.setMachine_name("pos1");
        helperSaleVar.setCreated_by("admin");
        helperSaleVar.setCompleted("W");
        helperSaleVar.setQty("1");
        helperSaleVar.setVar_hdr_id(helperDatabase.getVarHdrId(var_dtls_id));
        helperSaleVar.setVar_dtls_id(var_dtls_id);
        helperSaleVar.setTransaction_counter(helperDatabase.nextTransactionCounter());
        helperSaleVar.setTransaction_per_entry(helperDatabase.nextTransactionPerEntry());
        if (helperDatabase.variantExists(helperSaleVar)) {
            helperSaleVar.setSort_order_id(helperDatabase.getSortOrderIdVariant(helperSaleVar));
        } else {
            helperSaleVar.setSort_order_id(helperDatabase.nextSortOrderIdVariant(helperSaleVar));
        }
    }

}