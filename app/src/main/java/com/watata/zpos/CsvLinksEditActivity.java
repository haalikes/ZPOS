package com.watata.zpos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class CsvLinksEditActivity extends AppCompatActivity {

    float scale;
    Button backBtn, addItemCsvBtn, addVariantCsvBtn, listVariantBtn;
    Spinner spinner_app_item_name, spinner_app_variant, spinner_csv_item_name, spinner_csv_variant;
    TableLayout tableLayout;
    ProgressBar progressBar;

    List<HelperItem> listHelperItems = new LinkedList<>();
    List<HelperCsvLinks> listHelperCsvLinks = new LinkedList<>();
    List<HelperVariantsLinks> listHelperVariantsLinks = new LinkedList<>();
    List<HelperVariantsDtls> listHelperVariantsDtls = new LinkedList<>();

    private List<String> listCsvItemName  = new LinkedList<>(), listCsvVariantModifier = new LinkedList<>();

    private int rCode = 1;
    private String pathFile;

    DataHelper dataHelper = new DataHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv_links_edit);

        initializeVariables();

        setupXmlIds();
        setupListeners();
        addListFBCSVLinks();
        populateListItemName();
        populateListVariantsLinks();
        populateListVariantsDtls();

    }

    private void initializeVariables() {
        scale = this.getResources().getDisplayMetrics().density;
    }

    public void setupXmlIds(){
        backBtn = (Button) findViewById(R.id.back);
        addItemCsvBtn = findViewById(R.id.add_link_csv_item);
        addVariantCsvBtn = findViewById(R.id.add_link_csv_variant);
        listVariantBtn = findViewById(R.id.list_variant_btn);
        spinner_app_item_name = findViewById(R.id.spinner_app_item_name);
        spinner_app_variant = findViewById(R.id.spinner_app_variant);
        spinner_csv_item_name = findViewById(R.id.spinner_csv_item_name);
        spinner_csv_variant = findViewById(R.id.spinner_csv_variant);
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

        addItemCsvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperCsvLinks helperCsvLinks;
                if (listHelperCsvLinks.size()==0){
                    helperCsvLinks = new HelperCsvLinks(
                            "1"
                            ,"item_id"
                            , "" + getItemId()
                            , spinner_csv_item_name.getSelectedItem().toString());
                } else {
                    helperCsvLinks = new HelperCsvLinks(
                            "" + ( Integer.parseInt(listHelperCsvLinks.get(listHelperCsvLinks.size()-1).getCsv_link_id()) + 1 )
                            ,"item_id"
                            , "" + getItemId()
                            , spinner_csv_item_name.getSelectedItem().toString());
                }


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("csv_links");
                reference.child("" + helperCsvLinks.getCsv_link_id()).setValue(helperCsvLinks);

                ChangesFB changesFB = new ChangesFB();
                changesFB.ChangesCSVLinks();

                dataHelper.popMessage(spinner_csv_item_name.getSelectedItem().toString() + " added.", view.getContext());
            }
        });

        addVariantCsvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperCsvLinks helperCsvLinks;
                if (listHelperCsvLinks.size()==0){
                    helperCsvLinks = new HelperCsvLinks(
                            "1"
                            ,"var_dtls_id"
                            , "" + getItemId()
                            , spinner_csv_variant.getSelectedItem().toString());
                } else {
                    helperCsvLinks = new HelperCsvLinks(
                            listHelperCsvLinks.get(listHelperCsvLinks.size()-1).getCsv_link_id() + 1
                            ,"var_dtls_id"
                            , "" + getItemId()
                            , spinner_csv_variant.getSelectedItem().toString());
                }


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("csv_links");
                reference.child("" + helperCsvLinks.getCsv_link_id()).setValue(helperCsvLinks);

                ChangesFB changesFB = new ChangesFB();
                changesFB.ChangesCSVLinks();

                dataHelper.popMessage(spinner_csv_variant.getSelectedItem().toString() + " added.", view.getContext());
            }
        });

        listVariantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoyverseFileVariant(spinner_csv_item_name.getSelectedItem().toString());
            }
        });

        spinner_app_item_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (!spinner_app_item_name.getSelectedItem().toString().isEmpty()){
                    addSpinnerCsvVariant("" + getItemId());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        spinner_csv_item_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                Log.d("spinner", "onselected");
                if (!spinner_csv_item_name.getSelectedItem().toString().isEmpty()){
                    openLoyverseFileVariant(spinner_csv_item_name.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.d("spinner", "onnothing");
                // your code here
            }

        });

    }

    public void addListFBCSVLinks(){
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
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void populateListItemName(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("items");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                if (dataSnapshot.exists()){
                    listHelperItems.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listHelperItems.add(snapshot.getValue(HelperItem.class));
                    }
                    addSpinnerAppItem();
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void populateListVariantsLinks(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("variants_links");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                if (dataSnapshot.exists()){
                    listHelperVariantsLinks.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listHelperVariantsLinks.add(snapshot.getValue(HelperVariantsLinks.class));
                    }
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void populateListVariantsDtls(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("variants_dtls");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                if (dataSnapshot.exists()){
                    listHelperVariantsDtls.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listHelperVariantsDtls.add(snapshot.getValue(HelperVariantsDtls.class));
                    }
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
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

    public void openLoyverseFile(){
        //location
        final int int_date_occurrece = 0
                , int_receipt_type_occurrence = 2
                , int_category_occurrence = 3
                , int_item_name_occurrence = 5
                , int_variant_occurrence = 6
                , int_modifiers_occurrence = 7
                , int_qty_occurrence = 8
                ;
        final String sale_type = "Sale";

        //fields
        String csvDate, csvCategory, csvReceiptType, csvItemName, csvVariant, csvModifier, csvQty;
        List<String> listCsvModifier = new LinkedList<>();

        final String sLoySeparator = ",";
        boolean bHeader = true;
        pathFile = pathFile.substring(pathFile.lastIndexOf(":") + 1);
        File file = new File(pathFile);
        String buffRead = "";

        listCsvItemName.clear();
        listCsvVariantModifier.clear();


        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while((buffRead  = reader.readLine()) != null){
                listCsvModifier.clear();

                //exclude header and FP
                if (!bHeader){
                    buffRead = dataHelper.removeDoubleQuoteInCsv(buffRead);

                    //fields
                    csvVariant = dataHelper.getSubString(buffRead, sLoySeparator, int_variant_occurrence);

                    if (csvVariant.indexOf("FP") < 0){

                        //fields
                        csvReceiptType = dataHelper.getSubString(buffRead, sLoySeparator, int_receipt_type_occurrence);
                        if (csvReceiptType.equals(sale_type)){

                            //fields
                            csvCategory = dataHelper.getSubString(buffRead, sLoySeparator, int_category_occurrence);
                            csvItemName = dataHelper.getSubString(buffRead, sLoySeparator, int_item_name_occurrence);
                            csvModifier = dataHelper.getSubString(buffRead, sLoySeparator, int_modifiers_occurrence);
                            listCsvModifier = dataHelper.listStringFrSemi(csvModifier);

                            listCsvItemName.add(csvCategory + "-" +csvItemName);
                            listCsvVariantModifier.add(csvVariant);
                            for (int i=0; i < listCsvModifier.size(); i++){
                                listCsvVariantModifier.add(listCsvModifier.get(i));
                            }

                            Log.d("texto=", "item_name=" + csvItemName + " variant=" + csvVariant + " modifiers=" + csvModifier );
                        }

                    }
                }

                bHeader = false;
            }

            Log.d("texto=", "success");
            sortListsUnique();

        } catch(Exception e){
            dataHelper.popMessage("Error file format", this);
            Log.d("texto=", "error buffGlobal=" + e.getMessage());
            System.out.println( e.getMessage());
        }
    }

    public void openLoyverseFileVariant(String ref_item_name){
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
        String csvDate, csvReceiptType, csvItemName, csvVariant, csvModifier, csvQty;
        List<String> listCsvModifier = new LinkedList<>();

        final String sLoySeparator = ",";
        boolean bHeader = true;
        pathFile = pathFile.substring(pathFile.lastIndexOf(":") + 1);
        File file = new File(pathFile);
        String buffRead = "";

        listCsvVariantModifier.clear();


        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while((buffRead  = reader.readLine()) != null){
                listCsvModifier.clear();

                //exclude header and FP
                if (!bHeader){
                    buffRead = dataHelper.removeDoubleQuoteInCsv(buffRead);

                    //fields
                    csvVariant = dataHelper.getSubString(buffRead, sLoySeparator, int_variant_occurrence);

                    if (csvVariant.indexOf("FP") < 0){

                        //fields
                        csvReceiptType = dataHelper.getSubString(buffRead, sLoySeparator, int_receipt_type_occurrence);
                        if (csvReceiptType.equals(sale_type)){

                            //fields
                            csvItemName = dataHelper.getSubString(buffRead, sLoySeparator, int_item_name_occurrence);
                            Log.d("texto2=", "item_name=" + csvItemName + " ref_item_name=" + ref_item_name );
                            if (csvItemName.equals(ref_item_name)){
                                csvModifier = dataHelper.getSubString(buffRead, sLoySeparator, int_modifiers_occurrence);
                                listCsvModifier = dataHelper.listStringFrSemi(csvModifier);

                                listCsvVariantModifier.add(csvVariant);
                                for (int i=0; i < listCsvModifier.size(); i++){
                                    listCsvVariantModifier.add(listCsvModifier.get(i));
                                }
                                Log.d("texto2=", "item_name=" + csvItemName );
                            }


                        }

                    }
                }

                bHeader = false;
            }

            Log.d("texto2=", "success");
            sortListsUniqueVariant();

        } catch(Exception e){
            dataHelper.popMessage("Error file format", this);
            Log.d("texto2=", "error buffGlobal=" + e.getMessage());
            System.out.println( e.getMessage());
        }
    }

    public void openFileChooser(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("*/*");
        startActivityForResult(intent,rCode);
    }

    private void sortListsUnique(){
        listCsvItemName = dataHelper.listSortUnique(listCsvItemName);
        ArrayAdapter<String> itemAdapter_item = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listCsvItemName);
        spinner_csv_item_name.setAdapter(itemAdapter_item);
    }

    private void sortListsUniqueVariant(){
        listCsvVariantModifier = dataHelper.listSortUnique(listCsvVariantModifier);
        ArrayAdapter<String> itemAdapter_variant = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listCsvVariantModifier);
        spinner_csv_variant.setAdapter(itemAdapter_variant);
    }

    private void addSpinnerAppItem(){
        List<String> listAppItemName = new LinkedList<>();
        listAppItemName.clear();

        for (int i=0;i<listHelperItems.size();i++){
            listAppItemName.add(listHelperItems.get(i).getItem_name());
        }
        listAppItemName = dataHelper.listSortUnique(listAppItemName);
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listAppItemName);
        spinner_app_item_name.setAdapter(itemAdapter);
    }

    private int getItemId(){
        for (int i=0; i< listHelperItems.size(); i++){
            if (spinner_app_item_name.getSelectedItem().toString().equals(listHelperItems.get(i).getItem_name())){
                return listHelperItems.get(i).getItem_id();
            }
        }
        return 0;
    }

    private void addSpinnerCsvVariant(String item_id){
        List<String> listAppVariant = new LinkedList<>();
        listAppVariant.clear();
        String tmp_item_id, tmp_var_hdr_id;

        for (int i=0;i<listHelperVariantsLinks.size();i++){
            tmp_item_id = "" + listHelperVariantsLinks.get(i).getItem_id();
            if (item_id.equals(tmp_item_id)){
                for (int j=0; j<listHelperVariantsDtls.size(); j++){
                    tmp_var_hdr_id = "" + listHelperVariantsLinks.get(i).getVar_hdr_id();
                    if (tmp_var_hdr_id.equals("" + listHelperVariantsDtls.get(j).getVar_hdr_id())){
                        listAppVariant.add("" + listHelperVariantsDtls.get(j).getVar_dtls_name());
                    }
                }
            }
        }
        listAppVariant = dataHelper.listSortUnique(listAppVariant);
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listAppVariant);
        spinner_app_variant.setAdapter(itemAdapter);
    }

}