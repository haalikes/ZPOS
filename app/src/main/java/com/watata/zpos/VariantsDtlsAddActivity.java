package com.watata.zpos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VariantsDtlsAddActivity extends AppCompatActivity {

    Button saveBtn, cancelBtn;
    ImageView imageView;
    EditText editName, editSellingPrice;
    CheckBox checkBoxDefault;
    //int item_id, cat_id, var_link_id, stock_link_id;
    HelperVariantsDtls helperVariantsDtlsPrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variants_dtls_add);

        helperVariantsDtlsPrev = (HelperVariantsDtls) getIntent().getSerializableExtra("helperVariantsDtls");

        setupXmlIds();
        setupListeners();
        copyValues();
    }

    public void setupXmlIds(){
        saveBtn = findViewById(R.id.save);
        cancelBtn = findViewById(R.id.back);
        imageView = findViewById(R.id.image);
        editName = findViewById(R.id.name);
        editSellingPrice = findViewById(R.id.selling_price);
        checkBoxDefault = findViewById(R.id.var_dtls_default);
    }

    public void setupListeners(){
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editName.getText().toString().equals("") && !editName.getText().toString().equals("Click to Add")){

                    HelperVariantsDtls resulhelperVariantsDtls = new HelperVariantsDtls();
                    resulhelperVariantsDtls.setVar_dtls_id(helperVariantsDtlsPrev.getVar_dtls_id());
                    resulhelperVariantsDtls.setVar_hdr_id(helperVariantsDtlsPrev.getVar_hdr_id());
                    resulhelperVariantsDtls.setVar_dtls_name(editName.getText().toString());
                    resulhelperVariantsDtls.setVar_dtls_image(imageView.getTag().toString());
                    resulhelperVariantsDtls.setVar_selling_price("" + editSellingPrice.getText());
                    if (checkBoxDefault.isChecked()){
                        resulhelperVariantsDtls.setVar_dtls_default("Y");
                    }

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("resulhelperVariantsDtls", resulhelperVariantsDtls);

                    addFB(resulhelperVariantsDtls);

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
                openVariantsDtlsIconsActivity();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void copyValues(){
        editName.setText(helperVariantsDtlsPrev.getVar_dtls_name());
        if (helperVariantsDtlsPrev.getVar_dtls_image() != null){
            if (!helperVariantsDtlsPrev.getVar_dtls_image().equals("0")){
                imageView.setImageResource(getImageId(this, helperVariantsDtlsPrev.getVar_dtls_image()));
                imageView.setTag(helperVariantsDtlsPrev.getVar_dtls_image());
            } else {
                imageView.setImageResource(R.drawable.ic_no_icon);
                imageView.setTag("0");
            }
        } else {
            imageView.setImageResource(R.drawable.ic_no_icon);
            imageView.setTag("0");
        }

        editSellingPrice.setText(helperVariantsDtlsPrev.getVar_selling_price());

        checkBoxDefault.setChecked(false);
        if (helperVariantsDtlsPrev.getVar_dtls_default() != null){
            if (helperVariantsDtlsPrev.getVar_dtls_default().equals("Y")){
                checkBoxDefault.setChecked(true);
            }
        }

    }

    public void addFB(HelperVariantsDtls helperVariantsDtls){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("variants_dtls");
        reference.child("" + helperVariantsDtls.getVar_dtls_id()).setValue(helperVariantsDtls);

        ChangesFB changesFB = new ChangesFB();
        changesFB.ChangesVariantsDtls();

    }

    public void openVariantsDtlsIconsActivity() {
        ///Intent intent = new Intent(this, VariantsDtlsIconsActivity.class);
        Intent intent = new Intent(this, ItemIconsActivity.class);
        //intent.putExtra("username", username.getText().toString());
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK) {

                if (data.getStringExtra("iconSelected") != "0"
                        && data.getStringExtra("iconSelected")  != "ic_no_icon"
                        && data.getStringExtra("iconSelected")  != "ic_add" ) {
                    imageView.setImageResource(getImageId(this, data.getStringExtra("iconSelected" )));
                    imageView.setTag(data.getStringExtra("iconSelected" ));
                } else {
                    imageView.setImageResource(R.drawable.ic_no_icon);
                    imageView.setTag("0");
                }

            }
            if (resultCode == RESULT_CANCELED) {
                popMessage("No icon selected");
            }
        }

    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
    }

}