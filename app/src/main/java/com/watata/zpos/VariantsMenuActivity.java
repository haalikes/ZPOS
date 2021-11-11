package com.watata.zpos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.watata.zpos.ddlclass.HelperItem;
import com.watata.zpos.ddlclass.HelperSales;
import com.watata.zpos.ddlclass.HelperVariantsLinks;

public class VariantsMenuActivity extends AppCompatActivity {

    float scale;
    String screen_size, all_date;
    int item_id;
    TextView gHeader;

    LinearLayout linearLayout;

    //LAYOUTSUMMARY
    TableLayout tableSales;
    LayoutSummary layoutSummary;

    HelperVariantsLinks helperVariantsLinksPrev;
    HelperItem helperItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variants_menu);


        helperItem = (HelperItem) getIntent().getSerializableExtra("helperItem");
        all_date = getIntent().getStringExtra("all_date");
        item_id = helperItem.getItem_id();

        setupXmlIds();
        setupListeners();

        startLayoutSales();
        startLayoutVariantsDtls();

    }

    public void setupXmlIds(){
        linearLayout = findViewById(R.id.llvariantsdetails);
        screen_size = getResources().getString(R.string.screen_type);
        tableSales = findViewById(R.id.tblHistoryDtls);
        scale = this.getResources().getDisplayMetrics().density;
        gHeader = findViewById(R.id.header);
    }

    public void startLayoutSales(){
        layoutSummary = new LayoutSummary(this);
        layoutSummary.setContext(this);
        layoutSummary.setTableSales(tableSales);
        layoutSummary.setScreen_size(screen_size);
        layoutSummary.setAll_date(all_date);
        layoutSummary.setScale(scale);
        layoutSummary.setDisplay_dine_in_out(false);
        HelperSales helperSales = new HelperSales();
        helperSales.setCreated_by("admin");
        helperSales.setMachine_name("pos1");
        helperSales.setCompleted("W");
        layoutSummary.populateSales(helperSales);
    }

    //CARDVIEWS start

    public void startLayoutVariantsDtls(){
        LayoutVariantsDtls layoutVariantsDtls = new LayoutVariantsDtls();
        layoutVariantsDtls.setContext(this);
        layoutVariantsDtls.setScale(scale);
        layoutVariantsDtls.setScreen_size(screen_size);
        layoutVariantsDtls.setAll_date(all_date);
        layoutVariantsDtls.setItem_id(item_id);
        layoutVariantsDtls.setItem_image(helperItem.getItem_image());
        layoutVariantsDtls.setItem_name(helperItem.getItem_name());
        layoutVariantsDtls.setSelling_price(helperItem.getItem_selling_price());
        layoutVariantsDtls.setLinearLayout(linearLayout);
        layoutVariantsDtls.setLayoutSummary(layoutSummary);
        layoutVariantsDtls.setHelperItemMenu(helperItem);
        layoutVariantsDtls.startLayoutVariantsDtls();
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
                    gHeader.setBackgroundResource(R.color.transFull);
                    gHeader.setTextColor(Color.WHITE);
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            finish();
        }

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


    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_LONG).show();
    }


}