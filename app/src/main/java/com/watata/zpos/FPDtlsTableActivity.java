package com.watata.zpos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class FPDtlsTableActivity extends AppCompatActivity {

    //new for this activity
    HelperDatabase helperDatabase = new HelperDatabase(this);

    //default
    float scale;
    ProgressBar progressBar;
    Button backBtn;
    TableLayout tlFPDtls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpdtls_table);


        initializeVariables();
        setupXmlIds();
        setupListeners();
        genMainLoop();
    }

    public void initializeVariables() {
        scale = this.getResources().getDisplayMetrics().density;
    }

    public void setupXmlIds(){
        backBtn = (Button) findViewById(R.id.back);
        progressBar = findViewById(R.id.progressBar);
        tlFPDtls = findViewById(R.id.tblHistoryDtls);
    }

    public void setupListeners(){

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void genMainLoop(){
        List<HelperSales> listHelperSales = new LinkedList<>();
        listHelperSales.clear();


        listHelperSales = helperDatabase.listEODGraphFPWeekly();
        populateTable(listHelperSales);
    }

    public void populateTable(final List<HelperSales> listHelperSales){
        tlFPDtls.removeAllViews();

        // Header
        TableRow tableRowHdr = new TableRow(this);
        tableRowHdr.setWeightSum(14);
        tlFPDtls.addView(tableRowHdr);
        //columns
        TableRow.LayoutParams param2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        TableRow.LayoutParams param4 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 4);



        tableRowHdr.addView(createTextViewTable("Week", "header" ), param2);
        tableRowHdr.addView(createTextViewTable("EOD FP", "header" ), param4);
        tableRowHdr.addView(createTextViewTable("PmtAdvice", "header" ), param4);
        tableRowHdr.addView(createTextViewTable("Cost", "header" ), param4);

        for (int row = listHelperSales.size() - 1; row >= 0 ; row--){
            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(14);
            tlFPDtls.addView(tableRow);

            //columns

            tableRow.addView(createTextViewTable("" + listHelperSales.get(row).getCreated_by() + "-" + listHelperSales.get(row).getItem_name()), param2);
            tableRow.addView(createTextViewTable("" + listHelperSales.get(row).getSelling_price()), param4);
            tableRow.addView(createTextViewTable("" + helperDatabase.paymentAdviceWeekly(listHelperSales.get(row).getItem_name(), listHelperSales.get(row).getCreated_by())), param4);
            tableRow.addView(createTextViewTable("" + helperDatabase.estimatedCostPerWeek(listHelperSales.get(row).getItem_name(), listHelperSales.get(row).getCreated_by())), param4);

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
        textView.setGravity(Gravity.RIGHT);


        textView.setTextSize(18);

        return(textView);
    }

}