package com.watata.zpos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PaidActivity extends AppCompatActivity {

    float scale;
    String screen_size;
    TextView gHeader;
    private int final_bill, final_cash;
    Button bClose;

    LinearLayout llCash, llPaid;
    TableLayout tblCash, tblPaid;
    LayoutSummary layoutSummary;
    HelperDatabase helperDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid);

        layoutSummary = new LayoutSummary(this);
        helperDatabase  = new HelperDatabase(this);
        final_bill = getIntent().getIntExtra("final_bill",0);
        final_cash = getIntent().getIntExtra("final_cash",0);

        setupXmlIds();
        setupListeners();

        startLayoutPaid();
        startLayoutCash();

    }


    public void setupXmlIds(){
        screen_size = getResources().getString(R.string.screen_type);
        scale = this.getResources().getDisplayMetrics().density;
        gHeader = findViewById(R.id.header);

        llCash = findViewById(R.id.llcash);
        llPaid = findViewById(R.id.llpaid);

        tblCash = findViewById(R.id.tblcash);
        tblPaid = findViewById(R.id.tblpaid);

        bClose = findViewById(R.id.close);

    }

    public void setupListeners(){
        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void startLayoutCash(){
        tblCash.removeAllViews();
        TableRow tableRow = new TableRow(this);
        TextView textBill = new TextView(this);
        textBill.setText("Total bill:" + final_bill);
        TextView textCash = new TextView(this);
        textBill.setText("Total cash:" + final_cash);

        tableRow.addView(textBill);
        tableRow.addView(textCash);
        tblCash.addView(tableRow);
    }

    public void startLayoutPaid(){
        layoutSummary.setContext(this);
        layoutSummary.setTableSales(tblPaid);
        layoutSummary.setScreen_size(screen_size);
        layoutSummary.setScale(scale);
        layoutSummary.setIn_payment(true);
        layoutSummary.setDisplay_dine_in_out(true);
        HelperSales helperSales = new HelperSales();
        helperSales.setCreated_by("admin");
        helperSales.setMachine_name("pos1");
        helperSales.setCompleted("N");
        layoutSummary.populateSummarySales();
    }

}