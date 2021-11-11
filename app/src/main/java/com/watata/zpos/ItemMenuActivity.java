package com.watata.zpos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.watata.zpos.ddlclass.HelperSales;

public class ItemMenuActivity extends AppCompatActivity {
    float scale;
    String screen_size, all_date;
    int cat_id;
    TextView gHeader;

    //LAYOUTSUMMARY
    TableLayout tableSales;
    LayoutSummary layoutSummary;

    //CARDVIEWS
    GridLayout gGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_menu);

        cat_id = getIntent().getIntExtra("cat_id", 0);
        all_date = getIntent().getStringExtra("all_date");

        setupXmlIds();
        setupListeners();

        //SALES start
        startLayoutSales();
        //SALES end

        //CARDVIEWS start
        startLayoutCardViews();
        //CARDVIEWS end

    }

    public void setupXmlIds(){
        gGrid = findViewById(R.id.grid);
        screen_size = getResources().getString(R.string.screen_type);
        tableSales = findViewById(R.id.tblHistoryDtls);
        gHeader = findViewById(R.id.header);
    }

    public void setupListeners(){
        gHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gHeader.setBackgroundResource(R.color.white);
                gHeader.setTextColor(Color.GRAY);
                onBackPressed();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK) {

            }
            if (resultCode == RESULT_CANCELED) {
                popMessage("Nothing selected");
            }

            ///layoutSummary.populateSales();
            HelperSales helperSales = new HelperSales();
            helperSales.setCreated_by("admin");
            helperSales.setMachine_name("pos1");
            layoutSummary.populateSales(helperSales);
            ///layoutSummary.populateSummarySales();

            //cardview start
            startLayoutCardViews();
            //cardview end
        }
    }

    //SALES start
    public void startLayoutSales(){
        layoutSummary = new LayoutSummary(this);
        layoutSummary.setContext(this);
        layoutSummary.setTableSales(tableSales);
        layoutSummary.setScreen_size(screen_size);
        layoutSummary.setScale(scale);
        layoutSummary.setAll_date(all_date);
        HelperSales helperSales = new HelperSales();
        helperSales.setCreated_by("admin");
        helperSales.setMachine_name("pos1");
        layoutSummary.populateSales(helperSales);
        ///layoutSummary.populateSummarySales();
    }

    //SALES end


    //CARDVIEWS start

    public void startLayoutCardViews(){
        LayoutCardViewsItems layoutCardViewsItems = new LayoutCardViewsItems();
        layoutCardViewsItems.setContext(this);
        scale = this.getResources().getDisplayMetrics().density;
        layoutCardViewsItems.setScale(scale);
        layoutCardViewsItems.setScreen_size(screen_size);
        layoutCardViewsItems.setAll_date(all_date);
        layoutCardViewsItems.setCat_id(cat_id);
        layoutCardViewsItems.setgGrid(gGrid);
        layoutCardViewsItems.setLayoutSummary(layoutSummary);
        layoutCardViewsItems.startCardViews();
    }

    //CARDVIEWS end


    @Override
    public void onBackPressed() {
        final HelperDatabase helperDatabase = new HelperDatabase(this);

        if(helperDatabase.notInCartExists("admin", "pos1")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm");
            builder.setMessage("Item not in cart will be deleted?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    helperDatabase.deleteNotInCart("admin", "pos1");
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
        } else {
            finish();
        }

    }


    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_LONG).show();
    }

}