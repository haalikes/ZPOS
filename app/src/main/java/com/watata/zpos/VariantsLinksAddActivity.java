package com.watata.zpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.watata.zpos.ddlclass.HelperVariantsHdr;
import com.watata.zpos.ddlclass.HelperVariantsLinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class VariantsLinksAddActivity extends AppCompatActivity {

    float scale;
    Button addBtn, backBtn;
    Spinner spinner;
    TableLayout tableLayout;
    ProgressBar progressBar;
    List<HelperVariantsLinks> listhelperVariantsLinks = new LinkedList<HelperVariantsLinks>();
    List<HelperVariantsHdr> listhelperVariantsHdrs = new LinkedList<HelperVariantsHdr>();
    List<String> listVariantsHdrName = new ArrayList<String>();
    int item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variants_links_add);

        item_id = getIntent().getIntExtra("item_id", 0);

        initializeVariables();

        setupXmlIds();
        setupListeners();


        autoVariantsHdr();
        autoVariantsLinks();


    }

    public void populateSpinner(){
        listVariantsHdrName.clear();
        boolean notfound;
        for(int i=0; i < listhelperVariantsHdrs.size(); i++){
            notfound = true;
            for(int j=0; j < listhelperVariantsLinks.size(); j++){
                if(listhelperVariantsHdrs.get(i).getVar_hdr_id() == listhelperVariantsLinks.get(j).getVar_hdr_id()){
                    notfound = false;
                }
            }
            if (notfound){
                listVariantsHdrName.add(listhelperVariantsHdrs.get(i).getVar_hdr_name());
            }
        }

        Collections.sort(listVariantsHdrName);

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listVariantsHdrName);
        spinner.setAdapter(itemAdapter);
    }

    public void initializeVariables() {
        scale = this.getResources().getDisplayMetrics().density;
    }

    public void setupXmlIds(){
        addBtn = (Button) findViewById(R.id.add);
        backBtn = (Button) findViewById(R.id.back);
        spinner = findViewById(R.id.variant_header);
        progressBar = findViewById(R.id.progressBar);
        tableLayout = findViewById(R.id.tblHistoryDtls);

    }

    public void setupListeners(){

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ALL start
                final List<HelperVariantsLinks> listHelperVariantsLinksAll = new LinkedList<>();

                progressBar.setVisibility(View.VISIBLE);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = reference.child("variants_links");
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listHelperVariantsLinksAll.clear();
                        HelperVariantsLinks helperVariantsLinks = new HelperVariantsLinks();
                        helperVariantsLinks.setItem_id(item_id);
                        helperVariantsLinks.setVar_hdr_id(getVariantHdrId(spinner.getSelectedItem().toString()));
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("variants_links");

                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                listHelperVariantsLinksAll.add(snapshot.getValue(HelperVariantsLinks.class));
                            }

                            helperVariantsLinks.setLink_id(listHelperVariantsLinksAll.get(listHelperVariantsLinksAll.size()-1).getLink_id() + 1);
                            reference.child("" + helperVariantsLinks.getLink_id()).setValue(helperVariantsLinks);

                            ChangesFB changesFB = new ChangesFB();
                            changesFB.ChangesInVariantsLinks();

                            popMessage("Added item=" +  helperVariantsLinks.getItem_id() + " linkid=" + helperVariantsLinks.getLink_id() );

                        } else {
                            if(!dataSnapshot.exists()){
                                helperVariantsLinks.setLink_id(0);
                                reference.child("" + helperVariantsLinks.getLink_id()).setValue(helperVariantsLinks);
                                popMessage("New Added item=" +  helperVariantsLinks.getItem_id() + " linkid=" + helperVariantsLinks.getLink_id() );
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
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void autoVariantsHdr(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("variants_hdr");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                if (dataSnapshot.exists()){
                    listhelperVariantsHdrs.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listhelperVariantsHdrs.add(snapshot.getValue(HelperVariantsHdr.class));
                    }
                    repopulateAll();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void autoVariantsLinks(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("variants_links").orderByChild("item_id").equalTo(item_id);
        applesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                if (dataSnapshot.exists()) {
                    listhelperVariantsLinks.clear();
                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        listhelperVariantsLinks.add(appleSnapshot.getValue(HelperVariantsLinks.class));
                    }
                    repopulateAll();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    public void repopulateAll(){
        populateSpinner();
        populateVariantsLinks();
    }

    public void populateVariantsLinks(){

        tableLayout.removeAllViews();

        TableRow tableRowGuide = new TableRow(this);
        TextView guide1 = new TextView(this);
        TextView guide2 = new TextView(this);
        guide2.setText("Delete all to sort menu Summary, (FIFO) ex. Fries>Reg>Che");
        tableRowGuide.addView(guide1);
        tableRowGuide.addView(guide2);
        tableLayout.addView(tableRowGuide);

        // Header
        TableRow tableRowHdr = new TableRow(this);
        tableRowHdr.setWeightSum(8);
        tableLayout.addView(tableRowHdr);
        //columns
        TableRow.LayoutParams param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(createTextViewTable("Del?", "header" ), param0);

        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6);
        tableRowHdr.addView(createTextViewTable("Variant Included", "header" ), param0);


        for (int row = listhelperVariantsLinks.size() - 1; row >= 0 ; row--){
            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(13);
            tableLayout.addView(tableRow);

            //columns

            TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);

            ImageView delImage;
            ///delImage = delImageVIew(listhelperVariantsLinks.get(row).getVar_hdr_id());
            delImage = delImageVIew(listhelperVariantsLinks.get(row));
            tableRow.addView(delImage, param);

            param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6);

            tableRow.addView(createTextViewTable(getVariantHdrName(listhelperVariantsLinks.get(row).getVar_hdr_id())), param);

            ImageView addImage;
            addImage = addImageVIew(listhelperVariantsLinks.get(row).getVar_hdr_id());
            tableRow.addView(addImage, param);

        }


    }

    public String getVariantHdrName(int var_hdr_id){
        for (int i = 0; i < listhelperVariantsHdrs.size(); i++) {
            if (var_hdr_id == listhelperVariantsHdrs.get(i).getVar_hdr_id()){
                return listhelperVariantsHdrs.get(i).getVar_hdr_name();
            }
        }
        return "" + var_hdr_id + " no var_hdr_id";
    }

    public int getVariantHdrId(String var_hdr_name){
        for (int i =0; i < listhelperVariantsHdrs.size(); i++){
            if (listhelperVariantsHdrs.get(i).getVar_hdr_name() == var_hdr_name){
                return listhelperVariantsHdrs.get(i).getVar_hdr_id();
            }
        }
        return 99999;
    }


    public ImageView delImageVIew(final HelperVariantsLinks helperVariantsLinks){
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
                builder.setMessage("Delete " + getVariantHdrName(helperVariantsLinks.getVar_hdr_id()) + "?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteVariantsLinks(helperVariantsLinks);
                        dialog.dismiss();
                        finish();
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


    public void deleteVariantsLinks(HelperVariantsLinks helperVariantsLinks){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("variants_links").orderByChild("link_id").equalTo(helperVariantsLinks.getLink_id());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                ChangesFB changesFB = new ChangesFB();
                changesFB.ChangesInVariantsLinks();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    public ImageView addImageVIew(final int var_hdr_id){
        int dp35 = (int) (35 * scale + 0.5f);
        LinearLayout.LayoutParams lp35dp = new LinearLayout.LayoutParams( dp35, dp35);

        //imageView add
        ImageView addImmage = new ImageView(this);
        lp35dp.weight = 1.0f;
        lp35dp.gravity = Gravity.RIGHT;
        addImmage.setLayoutParams(lp35dp);
        addImmage.setImageResource(R.drawable.ic_edit);


        addImmage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openVariantsDtlsEditActivity(var_hdr_id);

            }
        });

        return addImmage;
    }

    public void openVariantsDtlsEditActivity(int var_hdr_id) {
        Intent intent = new Intent(this, VariantsDtlsEditActivity.class);
        HelperVariantsHdr helperVariantsHdr = new HelperVariantsHdr();
        helperVariantsHdr.setVar_hdr_id(var_hdr_id);
        intent.putExtra("helperVariantsHdr", helperVariantsHdr);
        startActivityForResult(intent, 2);
    }



    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_LONG).show();
    }

}