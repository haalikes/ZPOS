package com.watata.zpos;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;
import com.watata.zpos.ddlclass.HelperStockHistory;
import com.watata.zpos.ddlclass.HelperStockNames;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class StocksInputOnlyActivity extends AppCompatActivity {

    Spinner sInOut, sStockName, sCreatedBy;
    EditText eQty, eCost, eCostPerOrder;
    Button addBtn, historyBtn, backBtn;
    TableLayout tableLayoutStockHistory;
    List<HelperStockNames> listStockNames = new LinkedList<HelperStockNames>();
    List<HelperStockHistory> listStockHistory = new ArrayList<HelperStockHistory>();
    String screen_size, all_date;
    HelperDatabase helperDatabase = new HelperDatabase(this);
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);


        all_date = getIntent().getStringExtra("all_date");

        setupXmlIds();
        setupBtnListneres();

        createDropList();
        //autoPopulateHistory();

    }


    public void setupXmlIds(){

        sInOut = findViewById(R.id.in_out);
        sStockName = findViewById(R.id.stock_name);
        eQty = findViewById(R.id.eQty);
        eCost = findViewById(R.id.eCost);
        sCreatedBy = findViewById(R.id.createdby);
        addBtn = findViewById(R.id.add);
        historyBtn = findViewById(R.id.view);
        backBtn = findViewById(R.id.back);
        tableLayoutStockHistory = findViewById(R.id.tblHistoryDtls);
        screen_size = getResources().getString(R.string.screen_type);
        progressBar = findViewById(R.id.progressBar);

    }

    public void setupBtnListneres(){
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (historyBtn.getText().equals("Remaining")){
                    populateStocksRemaining();
                    historyBtn.setText("History");
                } else {
                    autoPopulateHistory();
                    historyBtn.setText("Remaining");
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!eQty.getText().toString().equals("0") && !eQty.getText().toString().equals("")){

                    String sDateOnly = all_date;
                    if(sDateOnly==null){
                        Date currentTime = Calendar.getInstance().getTime();
                        sDateOnly = DateFormat.getDateInstance().format(currentTime);
                    }

                    HelperStockHistory stockHistory = new HelperStockHistory(
                            sInOut.getSelectedItem().toString(),
                            helperDatabase.getStockId(sStockName.getSelectedItem().toString()),
                            sStockName.getSelectedItem().toString(),
                            eQty.getText().toString(),
                            helperDatabase.getStockNameMeasuredUsed(sStockName.getSelectedItem().toString()),
                            sDateOnly,
                            sCreatedBy.getSelectedItem().toString(),
                            eCost.getText().toString()
                    );
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("stocks_history").push();
                    reference.setValue(stockHistory);

                    ChangesFB changesFB = new ChangesFB();
                    changesFB.ChangesStockHistories();

                    popMessage(sStockName.getSelectedItem().toString() + " added" + sDateOnly);

                    eQty.setText("");

                } else {
                    popMessage("Cannot save when quantity is 0.");
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void autoPopulateHistory(){
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("stocks_history");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listStockHistory.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listStockHistory.add(snapshot.getValue(HelperStockHistory.class));
                    }
                    helperDatabase.refreshStocksHistory(listStockHistory);
                    populateStocksHistory();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void populateStocksRemaining(){
        tableLayoutStockHistory.removeAllViews();

        // Header
        TableRow tableRowHdr = new TableRow(this);
        tableRowHdr.setWeightSum(11);
        tableLayoutStockHistory.addView(tableRowHdr);
        //columns
        TableRow.LayoutParams param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 7);
        tableRowHdr.addView(createTextViewTable("Item Name" , "header"), param0);
        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(createTextViewTable("Remaining", "header" ), param0);
        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(createTextViewTable("Unit Used", "header" ), param0);

        /*
        HashSet<String> hItemName = new HashSet<String>();

        //get unique item
        for (int i = listStockHistory.size() - 1; i >= 0; i--) {
            hItemName.add("" + listStockHistory.get(i).getStock_name());
        }


        for (String itemName : hItemName){
            int stocksRemaining = 0;
            String measureName = "";
            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(11);
            tableLayoutStockHistory.addView(tableRow);

            for(int i = 0; i< listStockHistory.size(); i++){
                if (listStockHistory.get(i).getStock_name().equals(itemName)){
                    if (listStockHistory.get(i).getIn_out().equals("Out")){
                        stocksRemaining -= Integer.parseInt(listStockHistory.get(i).getQty());
                    } else {
                        stocksRemaining += Integer.parseInt(listStockHistory.get(i).getQty());
                    }
                    measureName = listStockHistory.get(i).getMeasure_used();
                }
            }

            //columns
            TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 7);

            if (stocksRemaining==0){
                tableRow.addView(createTextViewTable("" + itemName, "out"), param);
                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
                tableRow.addView(createTextViewTable("" + stocksRemaining, "out"), param);
                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
                tableRow.addView(createTextViewTable("" + measureName, "out"), param);
            } else {
                if (stocksRemaining<0){
                    tableRow.addView(createTextViewTable("" + itemName, "negative"), param);
                    param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
                    tableRow.addView(createTextViewTable("" + stocksRemaining, "negative"), param);
                    param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
                    tableRow.addView(createTextViewTable("" + measureName, "negative"), param);
                } else {
                    tableRow.addView(createTextViewTable("" + itemName), param);
                    param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
                    tableRow.addView(createTextViewTable("" + stocksRemaining), param);
                    param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
                    tableRow.addView(createTextViewTable("" + measureName), param);
                }

            }

        }


        */

        List<HelperStockHistory> listHelperStockHistRem = new LinkedList<>();
        listHelperStockHistRem.clear();
        listHelperStockHistRem = helperDatabase.listHelperStockHistRem();
        //columns
        TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 7);

        for (int i = 0; i < listHelperStockHistRem.size(); i++){
            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(11);
            tableLayoutStockHistory.addView(tableRow);

            if (listHelperStockHistRem.get(i).getQty().equals("0")){
                tableRow.addView(createTextViewTable("" + listHelperStockHistRem.get(i).getStock_name(), "out"), param);
                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
                tableRow.addView(createTextViewTable("" + listHelperStockHistRem.get(i).getQty(), "out"), param);
                tableRow.addView(createTextViewTable("" + listHelperStockHistRem.get(i).getMeasure_used(), "out"), param);
            } else {
                if (Integer.parseInt(listHelperStockHistRem.get(i).getQty())<0){
                    tableRow.addView(createTextViewTable("" + listHelperStockHistRem.get(i).getStock_name(), "negative"), param);
                    param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
                    tableRow.addView(createTextViewTable("" + listHelperStockHistRem.get(i).getQty(), "negative"), param);
                    tableRow.addView(createTextViewTable("" + listHelperStockHistRem.get(i).getMeasure_used(), "negative"), param);
                } else {
                    tableRow.addView(createTextViewTable("" + listHelperStockHistRem.get(i).getStock_name()), param);
                    param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
                    tableRow.addView(createTextViewTable("" + listHelperStockHistRem.get(i).getQty()), param);
                    tableRow.addView(createTextViewTable("" + listHelperStockHistRem.get(i).getMeasure_used()), param);
                }

            }
        }

    }

    public void populateStocksHistory(){
        tableLayoutStockHistory.removeAllViews();

        // Header
        TableRow tableRowHdr = new TableRow(this);
        tableRowHdr.setWeightSum(13);
        tableLayoutStockHistory.addView(tableRowHdr);
        //columns
        TableRow.LayoutParams param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        tableRowHdr.addView(createTextViewTable("In Out", "header" ), param0);
        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6);
        tableRowHdr.addView(createTextViewTable("Item Name", "header" ), param0);
        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        tableRowHdr.addView(createTextViewTable("Qty", "header" ), param0);
        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(createTextViewTable("Unit Used", "header" ), param0);
        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdr.addView(createTextViewTable("Created Date", "header" ), param0);
        param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        tableRowHdr.addView(createTextViewTable("Created By", "header" ), param0);


        for (int row = listStockHistory.size() - 1; row >= 0 ; row--){
            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(13);
            tableLayoutStockHistory.addView(tableRow);

            //columns
            if (listStockHistory.get(row).getIn_out().equals("Out")){
                TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
                tableRow.addView(createTextViewTable("" + listStockHistory.get(row).getIn_out(), "out" ), param);
                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6);
                tableRow.addView(createTextViewTable("" + listStockHistory.get(row).getStock_name(), "out" ), param);
                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
                tableRow.addView(createTextViewTable("" + listStockHistory.get(row).getQty(), "out" ), param);
                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
                tableRow.addView(createTextViewTable("" + listStockHistory.get(row).getMeasure_used(), "out" ), param);
                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
                tableRow.addView(createTextViewTable("" + listStockHistory.get(row).getTime(), "out" ), param);
                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
                tableRow.addView(createTextViewTable("" + listStockHistory.get(row).getUsername(), "out" ), param);
            } else {
                TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
                tableRow.addView(createTextViewTable("" + listStockHistory.get(row).getIn_out()), param);
                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6);
                tableRow.addView(createTextViewTable("" + listStockHistory.get(row).getStock_name()), param);
                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
                tableRow.addView(createTextViewTable("" + listStockHistory.get(row).getQty()), param);
                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
                tableRow.addView(createTextViewTable("" + listStockHistory.get(row).getMeasure_used()), param);
                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
                tableRow.addView(createTextViewTable("" + listStockHistory.get(row).getTime()), param);
                param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
                tableRow.addView(createTextViewTable("" + listStockHistory.get(row).getUsername()), param);
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
                if (sRowType == "negative"){
                    textView.setBackgroundResource(R.drawable.custom_text_negative);
                } else {
                    textView.setBackgroundResource(R.drawable.custom_text_in);
                }

            }
        }

        if (screen_size == "phone") {
            textView.setTextSize(12);
        } else {
            textView.setTextSize(18);
        }
        return(textView);
    }

    private TextView createTextViewTable(String sText){
        TextView textView = new TextView(this);
        textView.setText(sText);
        textView.setPadding(30, 10, 30, 10);
        textView.setBackgroundResource(R.drawable.custom_text_in);


        if (screen_size == "phone") {
            textView.setTextSize(12);
        } else {
            textView.setTextSize(18);
        }
        return(textView);
    }

    private void createDropList(){
        String[] inout = new String[]{"In", "Out"};
        ArrayAdapter<String> inoutAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, inout);
        sInOut.setAdapter(inoutAdapter);

        List<String> listStockNames = new LinkedList<String>();

        this.listStockNames.clear();
        this.listStockNames = helperDatabase.listStockNames();

        for (int i = 0; i < this.listStockNames.size(); i++){
            listStockNames.add(this.listStockNames.get(i).getStock_name());
        }

        Collections.sort(listStockNames);

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listStockNames);
        sStockName.setAdapter(itemAdapter);

        String[] created = new String[]{"Mar", "Jao", "Sei", "Law", "Exp"};
        ArrayAdapter<String> createdAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, created);
        sCreatedBy.setAdapter(createdAdapter);

    }

    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_LONG).show();
    }


}