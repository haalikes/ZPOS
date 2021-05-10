package com.watata.zpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class StockNamesEditActivity extends AppCompatActivity {
    float scale;
    EditText eStockName, eMeasureUsed;
    Button addBtn, backBtn;
    TableLayout tableLayoutStockNames;
    ProgressBar progressBar;
    List<HelperStockNames> listStockNames = new LinkedList<HelperStockNames>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_names_edit);

        initializeVariables();

        setupXmlIds();
        setupListeners();
        autoStockNames();
    }

    public void initializeVariables() {
        scale = this.getResources().getDisplayMetrics().density;
    }

    public void setupXmlIds(){
        addBtn = (Button) findViewById(R.id.add);
        backBtn = (Button) findViewById(R.id.back);
        eStockName = findViewById(R.id.item_name);
        eMeasureUsed = findViewById(R.id.measure_name);
        progressBar = findViewById(R.id.progressBar);
        tableLayoutStockNames = findViewById(R.id.tblHistoryDtls);

    }

    public void setupListeners(){

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperStockNames helperStockNames = new HelperStockNames(
                        listStockNames.get(listStockNames.size()-1).getStock_id() + 1
                        ,"" + eStockName.getText().toString()
                        , eMeasureUsed.getText().toString());
                //PUSH start
                //DatabaseReference reference = FirebaseDatabase.getInstance().getReference("stock_names").push();
                //reference.setValue(helperStockNames);
                //PUSH end

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("stock_names");
                reference.child("" + helperStockNames.getStock_id()).setValue(helperStockNames);

                ChangesFB changesFB = new ChangesFB();
                changesFB.ChangesInStockNames();

                popMessage(eStockName.getText().toString() + " added.");
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void deleteStockName(HelperStockNames helperStockName){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("stock_names").orderByChild("stock_id").equalTo(helperStockName.getStock_id());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    public void deleteStockName(HelperStockNamesSorter helperStockNamesSorter){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("stock_names").orderByChild("stock_id").equalTo(helperStockNamesSorter.getStock_id());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    public void autoStockNames(){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("stock_names");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listStockNames.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listStockNames.add(snapshot.getValue(HelperStockNames.class));
                    }
                    populateStockNames(listStockNames);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void populateStockNames(final List<HelperStockNames> listStockNames){
        tableLayoutStockNames.removeAllViews();

        // Header
        TableRow tableRowHdr = new TableRow(this);
        tableRowHdr.setWeightSum(13);
        tableLayoutStockNames.addView(tableRowHdr);
        //columns
        TableRow.LayoutParams param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(createTextViewTable("Del?", "header" ), param0);

        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6);
        tableRowHdr.addView(createTextViewTable("Product Name", "header" ), param0);

        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(createTextViewTable("Measure Used", "header" ), param0);


        List<HelperStockNamesSorter> listStockNamesDisplay = new LinkedList<>();
        listStockNamesDisplay.clear();
        HelperStockNamesSorter helperStockNamesSorter;
        for(int i = 0; i < listStockNames.size(); i++){
            helperStockNamesSorter = new HelperStockNamesSorter();
            helperStockNamesSorter.setStock_id(listStockNames.get(i).getStock_id());
            helperStockNamesSorter.setMeasure_used(listStockNames.get(i).getMeasure_used());
            helperStockNamesSorter.setStock_name(listStockNames.get(i).getStock_name());
            listStockNamesDisplay.add(helperStockNamesSorter);
        }

        Collections.sort(listStockNamesDisplay, new Comparator<HelperStockNamesSorter>(){
            public int compare(HelperStockNamesSorter obj1, HelperStockNamesSorter obj2) {
                // ## Ascending order
                //return obj1.getStock_name().compareToIgnoreCase(obj2.getStock_name()); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                // ## Descending order
                return obj2.getStock_name().compareToIgnoreCase(obj1.getStock_name()); // To compare string values
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
            }
        });

        for (int row = listStockNamesDisplay.size() - 1; row >= 0 ; row--){
            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(13);
            tableLayoutStockNames.addView(tableRow);

            //columns

                TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);

                ImageView delImage;
                delImage = delImageVIew(listStockNamesDisplay.get(row));
                tableRow.addView(delImage, param);

                tableRow.addView(createTextViewTable("" + listStockNamesDisplay.get(row).getStock_name()), param);

                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6);
                tableRow.addView(createTextViewTable("" + listStockNamesDisplay.get(row).getMeasure_used()), param);

        }


    }

    public ImageView delImageVIew(final HelperStockNames helperStockName){
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
                builder.setMessage("Delete " + helperStockName.getStock_name() + "?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteStockName(helperStockName);
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

    public ImageView delImageVIew(final HelperStockNamesSorter helperStockNamesSorter){
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
                builder.setMessage("Delete " + helperStockNamesSorter.getStock_name() + "?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteStockName(helperStockNamesSorter);
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

    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_LONG).show();
    }
}