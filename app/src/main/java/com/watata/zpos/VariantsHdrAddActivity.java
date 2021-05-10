package com.watata.zpos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VariantsHdrAddActivity extends AppCompatActivity {

    Button saveBtn, cancelBtn, editVariantsBtn;
    ImageView imageView;
    EditText editName;
    HelperVariantsHdr helperVariantsHdrPrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variants_hdr_add);

        helperVariantsHdrPrev = (HelperVariantsHdr) getIntent().getSerializableExtra("helperVariantsHdr");

        setupXmlIds();
        setupListeners();
        copyValues();
    }


    public void setupXmlIds(){
        saveBtn = findViewById(R.id.save);
        cancelBtn = findViewById(R.id.back);
        imageView = findViewById(R.id.image);
        editName = findViewById(R.id.name);
        editVariantsBtn = findViewById(R.id.editvariantsdtls);
    }

    public void setupListeners(){
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editName.getText().toString().equals("") && !editName.getText().toString().equals("Click to Add")){

                    String item_image_res = "" + imageView.getTag();

                    HelperVariantsHdr resulthelperVariantsHdr = new HelperVariantsHdr();
                    resulthelperVariantsHdr.setVar_hdr_id(helperVariantsHdrPrev.getVar_hdr_id());
                    resulthelperVariantsHdr.setVar_hdr_name(editName.getText().toString());
                    resulthelperVariantsHdr.setVar_hdr_image(item_image_res);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("resulthelperVariantsHdr", resulthelperVariantsHdr);

                    addFB(resulthelperVariantsHdr);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    popMessage("Please enter Item Name.");
                    editName.setText("");
                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVariantsHdrIconsActivity();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editVariantsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVariantsDtlsEditActivity();
            }
        });

    }

    public void copyValues(){
        editName.setText(helperVariantsHdrPrev.getVar_hdr_name());
        if (helperVariantsHdrPrev.getVar_hdr_image() != null){
            if (!helperVariantsHdrPrev.getVar_hdr_image().equals("0")){
                imageView.setImageResource(Integer.parseInt(helperVariantsHdrPrev.getVar_hdr_image()));
                imageView.setTag(helperVariantsHdrPrev.getVar_hdr_image());
            } else {
                imageView.setImageResource(R.drawable.ic_no_icon);
                imageView.setTag("" + R.drawable.ic_no_icon);
            }
        } else {
            imageView.setImageResource(R.drawable.ic_no_icon);
            imageView.setTag("" + R.drawable.ic_no_icon);
        }

    }

    public void addFB(HelperVariantsHdr helperVariantsHdr){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("variants_hdr");
        reference.child("" + helperVariantsHdr.getVar_hdr_id()).setValue(helperVariantsHdr);

        ChangesFB changesFB = new ChangesFB();
        changesFB.ChangesVariantsHdr();

    }

    public void openVariantsHdrIconsActivity() {
        Intent intent = new Intent(this, VariantsHdrIconsActivity.class);
        //intent.putExtra("username", username.getText().toString());
        startActivityForResult(intent, 1);
    }

    public void openVariantsDtlsEditActivity() {
        Intent intent = new Intent(this, VariantsDtlsEditActivity.class);
        intent.putExtra("helperVariantsHdr", helperVariantsHdrPrev);
        startActivityForResult(intent, 2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK) {

                if (data.getIntExtra("iconSelected", 0) != 0
                        && data.getIntExtra("iconSelected", 0)  != R.drawable.ic_no_icon
                        && data.getIntExtra("iconSelected", 0)  != R.drawable.ic_add ) {
                    imageView.setImageResource(data.getIntExtra("iconSelected", 0 ));
                    imageView.setTag(data.getIntExtra("iconSelected", 0 ));
                } else {
                    imageView.setImageResource(0);
                    imageView.setTag(0);
                }

            }
            if (resultCode == RESULT_CANCELED) {
                popMessage("No icon selected");
            }
        }

    }

    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
    }


}