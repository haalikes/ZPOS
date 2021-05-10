package com.watata.zpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class VariantsHdrIconsActivity extends AppCompatActivity {

    ImageView iDimsun, iPizza, iIcedrop, iDine, iIcecream, iBlock, iBlank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variants_hdr_icons);

        setupXmlIds();
        setupListeners();
    }



    public void setupXmlIds(){
        iDimsun = findViewById(R.id.dimsun);
        iPizza = findViewById(R.id.pizza);
        iIcedrop = findViewById(R.id.icedrop);
        iDine = findViewById(R.id.dine);
        iIcecream = findViewById(R.id.icecream);
        iBlock = findViewById(R.id.block);
        iBlank = findViewById(R.id.blank);

    }

    public void setupListeners(){
        iDimsun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconSelected(R.drawable.dimsun);
            }
        });

        iPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconSelected(R.drawable.pizza);
            }
        });

        iIcedrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconSelected(R.drawable.icedrop);
            }
        });

        iDine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconSelected(R.drawable.dine);
            }
        });

        iIcecream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconSelected(R.drawable.icecream);
            }
        });

        iBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconSelected(R.drawable.ic_block);
            }
        });

        iBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconSelected(0);
            }
        });

    }

    public void iconSelected(int drawable){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("iconSelected", drawable);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}