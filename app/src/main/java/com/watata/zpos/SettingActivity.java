package com.watata.zpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SettingActivity extends AppCompatActivity {

    ImageView iEditStockName, iCategoryEdit, iEditVariants, iEditCsvLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setupXmlIds();
        setupBtnListneres();

    }


    public void setupXmlIds(){
        iEditStockName = findViewById(R.id.itemnamesetting);
        iCategoryEdit = findViewById(R.id.editcategory);
        iEditVariants = findViewById(R.id.editvariants);
        iEditCsvLinks = findViewById(R.id.aditcsvlinks);
    }

    public void setupBtnListneres(){
        iEditStockName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStockNamesEditActivity();
            }
        });

        iCategoryEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryEditActivity();
            }
        });

        iEditVariants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVariantsHdrEditActivity();
            }
        });

        iEditCsvLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCsvLinksEditActivity();
            }
        });
    }

    public void openStockNamesEditActivity() {
        Intent intent = new Intent(this, StockNamesEditActivity.class);
        startActivity(intent);
    }

    public void openCategoryEditActivity() {
        Intent intent = new Intent(this, CategoryEditActivity.class);
        startActivity(intent);
    }

    public void openVariantsHdrEditActivity() {
        Intent intent = new Intent(this, VariantsHdrEditActivity.class);
        startActivity(intent);
    }

    public void openCsvLinksEditActivity() {
        Intent intent = new Intent(this, CsvLinksEditActivity.class);
        startActivity(intent);
    }
}