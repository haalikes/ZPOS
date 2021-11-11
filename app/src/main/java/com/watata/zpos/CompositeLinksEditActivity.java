package com.watata.zpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.watata.zpos.ddlclass.HelperStockNames;
import com.watata.zpos.ddlclass.HelperVariantsDtls;
import com.watata.zpos.ddlclass.HelperVariantsHdr;
import com.watata.zpos.ddlclass.HelperVariantsLinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CompositeLinksEditActivity extends AppCompatActivity {

    int item_id;
    String item_name;
    float scale;
    Button addBtn, backBtn;
    Spinner spinner, spinnerStockNamesForVar, sItemUnit;
    TableLayout tlIncByVar, tlCompositeLinks;
    ProgressBar progressBar, progressBarIncByVar;
    CheckBox inc_by_var, cbItemReq;
    TextView tvThisPage;
    LinearLayout llVariantsSpinner;

    EditText item_qty; //, item_unit;

    ///List<EditText> listQtyVarCompDtls = new LinkedList<>();
    ///List<EditText> listUnitVarCompDtls = new LinkedList<>();


    List<HelperCompositeLinks> listHelperCompositeLinks = new LinkedList<>();
    List<HelperStockNames> listHelperStockNames = new LinkedList<>();
    List<HelperVariantsHdr> listHelperVariantsHdrs = new LinkedList<>();
    List<HelperVariantsDtls> listHelperVariantsDtls = new LinkedList<>();
    List<HelperVariantsLinks> listHelperVariantsLinks = new LinkedList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composite_links_edit);

        item_id = getIntent().getIntExtra("item_id", 0);
        item_name = getIntent().getStringExtra("item_name");

        initializeVariables();

        setupXmlIds();

        fillLists();
        setupListeners();

    }

    public void initializeVariables() {
        scale = this.getResources().getDisplayMetrics().density;
    }

    public void setupXmlIds(){
        addBtn = findViewById(R.id.add);
        backBtn = findViewById(R.id.back);
        item_qty= findViewById(R.id.item_stocks_qty);
        //item_unit = findViewById(R.id.item_stocks_unit);
        sItemUnit = findViewById(R.id.sunit);
        tlIncByVar = findViewById(R.id.tblIncByVar);
        tlCompositeLinks = findViewById(R.id.composite_links);
        inc_by_var = findViewById(R.id.inc_by_var);
        progressBar = findViewById(R.id.progressBar);
        progressBarIncByVar = findViewById(R.id.progressBarIncByVar);
        progressBarIncByVar.setVisibility(View.GONE);
        spinner = findViewById(R.id.item_stocks_spinner);
        tvThisPage = findViewById(R.id.tvThisPage);
        llVariantsSpinner = findViewById(R.id.llVariantsSpinner);
        spinnerStockNamesForVar = findViewById(R.id.spinnerStockNamesForVar);
        cbItemReq = findViewById(R.id.cbItemReq);
        cbItemReq.setChecked(true);

        tvThisPage.setText("Composite of " + item_name);


        String[] list = new String[]{"pcs", "g", "m", "ml", "part", "pack"};
        ArrayAdapter<String> createdAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        sItemUnit.setAdapter(createdAdapter);

    }

    public void setupListeners(){
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperCompositeLinks helperCompositeLink = new HelperCompositeLinks();
                helperCompositeLink.setItem_id(item_id);
                helperCompositeLink.setStock_id(getStockId(spinner.getSelectedItem().toString()));
                helperCompositeLink.setQty(item_qty.getText().toString());
                //helperCompositeLink.setUnit(item_unit.getText().toString());
                helperCompositeLink.setUnit(sItemUnit.getSelectedItem().toString());
                if (!cbItemReq.isChecked()){
                    helperCompositeLink.setReq("N");
                }
                if (inc_by_var.isChecked()){
                    helperCompositeLink.setInc_by_var("Y");
                }
                addCompositeLinks(helperCompositeLink);
            }
        });

        inc_by_var.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inc_by_var.isChecked()){
                    findQtyUnitVarDtls();
                    llVariantsSpinner.setVisibility(View.VISIBLE);
                } else {
                    tlIncByVar.removeAllViews();
                    llVariantsSpinner.setVisibility(View.GONE);
                }
            }
        });

        spinnerStockNamesForVar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                findQtyUnitVarDtls();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cbItemReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    public void findQtyUnitVarDtls(){
        tlIncByVar.removeAllViews();


        // Header
        TableRow tableRowHdr = new TableRow(this);
        tableRowHdr.setWeightSum(24);
        tlIncByVar.addView(tableRowHdr);
        //columns
        TableRow.LayoutParams param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6);

        tableRowHdr.addView(createTextViewTable("Variant Cat", "header" ), param0);

        tableRowHdr.addView(createTextViewTable("Variant Dtls", "header" ), param0);

        tableRowHdr.addView(createTextViewTable("Stock Name", "header" ), param0);

        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(createTextViewTable("Qty", "header" ), param0);
        tableRowHdr.addView(createTextViewTable("Unit", "header" ), param0);
        tableRowHdr.addView(createTextViewTable("Req", "header" ), param0);
        tableRowHdr.addView(createTextViewTable("(+)", "header" ), param0);

        for (int i = 0; i < listHelperVariantsLinks.size(); i++){
            if (listHelperVariantsLinks.get(i).getItem_id() == item_id){
                for ( int j = 0; j < listHelperVariantsDtls.size(); j++){
                    if (listHelperVariantsDtls.get(j).getVar_hdr_id() == listHelperVariantsLinks.get(i).getVar_hdr_id()){
                        populateQtyUnitVarDtls(listHelperVariantsDtls.get(j));
                    }
                }
            }
        }
    }

    public void populateQtyUnitVarDtls(HelperVariantsDtls helperVariantsDtl){

            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(28);
            tlIncByVar.addView(tableRow);

            //columns

            TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6);
            tableRow.addView(createTextViewTable(getVarHdrName(helperVariantsDtl.getVar_hdr_id())), param);
            tableRow.addView(createTextViewTable(helperVariantsDtl.getVar_dtls_name()), param);

            tableRow.addView(createTextViewTable(spinnerStockNamesForVar.getSelectedItem().toString()), param);

            param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);

            EditText etQty, etUnit;
            //Spinner sUnit;

            etQty = createEditTextTable(getCompLinkQty(helperVariantsDtl.getVar_hdr_id(), helperVariantsDtl.getVar_dtls_id()));
            ///listQtyVarCompDtls.add(editTextQty);
            tableRow.addView(etQty, param);

            etUnit = createEditTextTable(getCompLinkUnit(helperVariantsDtl.getVar_hdr_id(), helperVariantsDtl.getVar_dtls_id()));
            ///listUnitVarCompDtls.add(editTextUnit);
            tableRow.addView(etUnit, param);
            //sUnit = createSpinnerUnit(getCompLinkUnit(helperVariantsDtl.getVar_hdr_id(), helperVariantsDtl.getVar_dtls_id()));
            //tableRow.addView(sUnit, param);

            CheckBox cbOptional = new CheckBox(this);
            cbOptional.setChecked(true);
            tableRow.addView(cbOptional, param);

            ImageView addImage;
            HelperCompositeLinks helperCompositeLink = new HelperCompositeLinks();
            helperCompositeLink.setItem_id(item_id);
            helperCompositeLink.setStock_id(getStockId(spinnerStockNamesForVar.getSelectedItem().toString()));
            helperCompositeLink.setVar_hdr_id("" + helperVariantsDtl.getVar_hdr_id());
            helperCompositeLink.setVar_dtls_id("" + helperVariantsDtl.getVar_dtls_id());
            addImage = addImageVIew(helperCompositeLink, etQty, etUnit, cbOptional);
            tableRow.addView(addImage, param);
    }

    private void autoCompositeLinks() {
        progressBar.setVisibility(View.VISIBLE);
        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("composite_links");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = reference.child("composite_links").orderByChild("item_id").equalTo(item_id);
        applesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listHelperCompositeLinks.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listHelperCompositeLinks.add(snapshot.getValue(HelperCompositeLinks.class));
                    }
                    populateCompositeLinks();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void fillLists() {
        fillStockNames();
        fillVarHdr();

    }

    public void fillStockNames(){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("stock_names");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listHelperStockNames.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listHelperStockNames.add(snapshot.getValue(HelperStockNames.class));
                    }
                    populateSpinner();
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void fillVarHdr(){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("variants_hdr");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listHelperVariantsHdrs.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listHelperVariantsHdrs.add(snapshot.getValue(HelperVariantsHdr.class));
                    }
                    fillVarDtls();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void fillVarDtls(){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("variants_dtls");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listHelperVariantsDtls.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listHelperVariantsDtls.add(snapshot.getValue(HelperVariantsDtls.class));
                    }

                    fillVarLinks();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void fillVarLinks(){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("variants_links");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listHelperVariantsLinks.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listHelperVariantsLinks.add(snapshot.getValue(HelperVariantsLinks.class));
                    }
                }
                autoCompositeLinks();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    public void populateCompositeLinks(){
        tlCompositeLinks.removeAllViews();


        // Header
        TableRow tableRowHdr = new TableRow(this);
        tableRowHdr.setWeightSum(32);
        tlCompositeLinks.addView(tableRowHdr);
        //columns
        TableRow.LayoutParams param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(createTextViewTable("Del?", "header" ), param0);

        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6);
        tableRowHdr.addView(createTextViewTable("Item Name", "header" ), param0);
        tableRowHdr.addView(createTextViewTable("Variant Cat", "header" ), param0);
        tableRowHdr.addView(createTextViewTable("Variant Sub", "header" ), param0);
        tableRowHdr.addView(createTextViewTable("Stock Name", "header" ), param0);

        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(createTextViewTable("Qty", "header" ), param0);
        tableRowHdr.addView(createTextViewTable("Unit", "header" ), param0);
        tableRowHdr.addView(createTextViewTable("Req", "header" ), param0);

        for (int row = listHelperCompositeLinks.size() - 1; row >= 0 ; row--){
            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(32);
            tlCompositeLinks.addView(tableRow);

            //columns

            TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
            ImageView delImage;
            delImage = delImageVIew(listHelperCompositeLinks.get(row));
            tableRow.addView(delImage, param);

            param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6);
            tableRow.addView(createTextViewTable(item_name), param);
            if(listHelperCompositeLinks.get(row).getVar_hdr_id() != null){
                tableRow.addView(createTextViewTable(getVarHdrName(Integer.parseInt(listHelperCompositeLinks.get(row).getVar_hdr_id()))), param);
                tableRow.addView(createTextViewTable(getVarDtlsName(Integer.parseInt(listHelperCompositeLinks.get(row).getVar_dtls_id()))), param);
            } else {
                tableRow.addView(createTextViewTable("-"), param);
                tableRow.addView(createTextViewTable("-"), param);
            }
            tableRow.addView(createTextViewTable(getStockName(listHelperCompositeLinks.get(row).getStock_id())), param);

            param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
            tableRow.addView(createTextViewTable(listHelperCompositeLinks.get(row).getQty()), param);
            tableRow.addView(createTextViewTable(listHelperCompositeLinks.get(row).getUnit()), param);
            Log.d("populateCompositeLinks", "getReq=" + listHelperCompositeLinks.get(row).getReq());
            if (listHelperCompositeLinks.get(row).getReq() != null){
                tableRow.addView(createTextViewTable(listHelperCompositeLinks.get(row).getReq()), param);
            } else {
                tableRow.addView(createTextViewTable("Y"), param);
            }

        }


    }

    private TextView createTextViewTable(String sText, String sRowType){
        TextView textView = new TextView(this);
        textView.setText(sText);
        textView.setPadding(30, 10, 30, 10);

        if (sRowType == "header") {
            textView.setBackgroundResource(R.drawable.custom_textview_hdr);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
        } else {
            if (sRowType == "out") {
                textView.setBackgroundResource(R.drawable.custom_text_out);
            } else {
                textView.setBackgroundResource(R.drawable.custom_text_in);
            }
        }

        textView.setTextSize(18);
        return(textView);
    }

    private TextView createTextViewTable(String sText){
        TextView textView = new TextView(this);
        textView.setText(sText);
        textView.setPadding(30, 10, 30, 10);
        textView.setBackgroundResource(R.drawable.custom_text_in);
        textView.setTextSize(18);
        return(textView);
    }

    private EditText createEditTextTable(String sText){
        EditText editText = new EditText(this);
        editText.setText(sText);
        editText.setPadding(30, 10, 30, 10);
        editText.setBackgroundResource(R.drawable.custom_text_in);
        editText.setTextSize(18);

        return(editText);
    }

    private Spinner createSpinnerUnit(String sText){
        Spinner spinner = new Spinner(this);
        String[] list = new String[]{"pcs", "part", "kilo", "pack", "gal"};
        ArrayAdapter<String> createdAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(createdAdapter);

        switch(sText){
            case "pcs":
                spinner.setSelection(0);
                break;
            case "part":
                spinner.setSelection(1);
                break;
            case "kilo":
                spinner.setSelection(2);
                break;
            case "pack":
                spinner.setSelection(3);
                break;
            case "gal":
                spinner.setSelection(4);
                break;
            default:
                break;
        }

        return spinner;
    }

    public ImageView delImageVIew(final HelperCompositeLinks helperCompositeLink){
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
                builder.setMessage("Delete comp_link_id=" + helperCompositeLink.getComposite_link_id() + "?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCompositeLink(helperCompositeLink);
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

    public ImageView addImageVIew(final HelperCompositeLinks helperCompositeLink, final EditText etQty, final EditText etUnit, final CheckBox cbReq){
        int dp35 = (int) (35 * scale + 0.5f);
        LinearLayout.LayoutParams lp35dp = new LinearLayout.LayoutParams( dp35, dp35);

        //imageView add
        ImageView addImmage = new ImageView(this);
        lp35dp.weight = 1.0f;
        lp35dp.gravity = Gravity.RIGHT;
        addImmage.setLayoutParams(lp35dp);
        addImmage.setImageResource(R.drawable.ic_add);


        addImmage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                helperCompositeLink.setQty(etQty.getText().toString());
                helperCompositeLink.setUnit(etUnit.getText().toString());
                if (!cbReq.isChecked()) helperCompositeLink.setReq("N");
                addCompositeLinks(helperCompositeLink);

            }
        });

        return addImmage;
    }

    public void deleteCompositeLink(HelperCompositeLinks helperCompositeLink){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("composite_links").orderByChild("composite_link_id").equalTo(helperCompositeLink.getComposite_link_id());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                ChangesFB changesFB = new ChangesFB();
                changesFB.ChangesCompositeLinks();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    public String getStockName(int stock_id){
        for (int i = 0; i < listHelperStockNames.size(); i++){
            if (listHelperStockNames.get(i).getStock_id() == stock_id){
                return listHelperStockNames.get(i).getStock_name();
            }
        }

        return null;
    }

    public int getStockId(String stock_name){
        for (int i = 0; i < listHelperStockNames.size(); i++){
            if (listHelperStockNames.get(i).getStock_name().equals(stock_name)){
                return listHelperStockNames.get(i).getStock_id();
            }
        }

        return 999999;
    }

    public String getVarHdrName(int var_hdr_id){
        for (int i = 0; i < listHelperVariantsHdrs.size(); i++){
            if (listHelperVariantsHdrs.get(i).getVar_hdr_id() == var_hdr_id){
                return listHelperVariantsHdrs.get(i).getVar_hdr_name();
            }
        }

        return null;
    }

    public String getVarDtlsName(int var_dtls_id){
        for (int i = 0; i < listHelperVariantsDtls.size(); i++){
            if (listHelperVariantsDtls.get(i).getVar_dtls_id() == var_dtls_id){
                return listHelperVariantsDtls.get(i).getVar_dtls_name();
            }
        }

        return null;
    }

    private void populateSpinner() {
        List<String> arrStockNames = new ArrayList<>();
        arrStockNames.clear();

        for (int i = 0; i < listHelperStockNames.size(); i++){
            arrStockNames.add(listHelperStockNames.get(i).getStock_name());
        }

        Collections.sort(arrStockNames);

        popMessage("listHelperStockNames=" + listHelperStockNames.size());

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrStockNames);
        spinner.setAdapter(itemAdapter);
        spinnerStockNamesForVar.setAdapter(itemAdapter);

    }

    public String getCompLinkQty(int var_hdr_id, int var_dtls_id){
        for (int i = 0; i < listHelperCompositeLinks.size(); i++){
            if(listHelperCompositeLinks.get(i).getStock_id() == getStockId(spinnerStockNamesForVar.getSelectedItem().toString())
                && listHelperCompositeLinks.get(i).getItem_id() == item_id && listHelperCompositeLinks.get(i).getVar_hdr_id() != null ){
               if ( Integer.parseInt(listHelperCompositeLinks.get(i).getVar_hdr_id()) == var_hdr_id
                        && Integer.parseInt(listHelperCompositeLinks.get(i).getVar_dtls_id()) == var_dtls_id
               ){
                    return listHelperCompositeLinks.get(i).getQty();
                }
            }
        }
        return null;
    }

    public String getCompLinkUnit(int var_hdr_id, int var_dtls_id){
        for (int i = 0; i < listHelperCompositeLinks.size(); i++){
            if(listHelperCompositeLinks.get(i).getStock_id() == getStockId(spinnerStockNamesForVar.getSelectedItem().toString())
                    && listHelperCompositeLinks.get(i).getItem_id() == item_id && listHelperCompositeLinks.get(i).getVar_hdr_id() != null ){
                if ( Integer.parseInt(listHelperCompositeLinks.get(i).getVar_hdr_id()) == var_hdr_id
                        && Integer.parseInt(listHelperCompositeLinks.get(i).getVar_dtls_id()) == var_dtls_id
                ){
                    return listHelperCompositeLinks.get(i).getUnit();
                }
            }

        }
        return null;
    }

    public void addCompositeLinks(final HelperCompositeLinks helperCompositeLink){

        //ALL start
        final List<HelperCompositeLinks> listHelperCompositeLinksAll = new LinkedList<>();

        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = reference.child("composite_links");
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listHelperCompositeLinksAll.clear();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("composite_links");

                if (dataSnapshot.exists()){

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listHelperCompositeLinksAll.add(snapshot.getValue(HelperCompositeLinks.class));
                    }
                    helperCompositeLink.setComposite_link_id(listHelperCompositeLinksAll.get(listHelperCompositeLinksAll.size()-1).getComposite_link_id() + 1);

                    reference.child("" + helperCompositeLink.getComposite_link_id()).setValue(helperCompositeLink);

                    ChangesFB changesFB = new ChangesFB();
                    changesFB.ChangesCompositeLinks();

                    popMessage("Added item=" +  helperCompositeLink.getItem_id() + " linkid=" + helperCompositeLink.getComposite_link_id() );

                }  else {
                    if (!dataSnapshot.exists()) {
                        helperCompositeLink.setComposite_link_id(0);
                        reference.child("" + helperCompositeLink.getComposite_link_id()).setValue(helperCompositeLink);

                        ChangesFB changesFB = new ChangesFB();
                        changesFB.ChangesCompositeLinks();

                        popMessage("New Added item=" +  helperCompositeLink.getItem_id() + " linkid=" + helperCompositeLink.getComposite_link_id() );
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
        //ALL end

    }


    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
    }

}