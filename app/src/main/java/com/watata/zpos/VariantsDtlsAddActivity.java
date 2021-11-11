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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.watata.zpos.ddlclass.HelperVariantsDtls;

public class VariantsDtlsAddActivity extends AppCompatActivity {

    Button saveBtn, cancelBtn;
    ImageView imageView;
    EditText editName, editSellingPrice;
    CheckBox checkBoxDefault, cbCompositeRequired;
    //int item_id, cat_id, var_link_id, stock_link_id;
    HelperVariantsDtls helperVariantsDtlsPrev;
    Boolean bVarHasComposite;

    //checking
    //List<HelperCompositeLinks> listCompositeLinks = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variants_dtls_add);

        helperVariantsDtlsPrev = (HelperVariantsDtls) getIntent().getSerializableExtra("helperVariantsDtls");
        initialize();
        setupXmlIds();
        setupListeners();
        copyValues();
    }

    public void initialize(){
        bVarHasComposite = false;
        checkCompositeLink( "" + helperVariantsDtlsPrev.getVar_dtls_id());
    }

    public void setupXmlIds(){
        saveBtn = findViewById(R.id.save);
        cancelBtn = findViewById(R.id.back);
        imageView = findViewById(R.id.image);
        editName = findViewById(R.id.name);
        editSellingPrice = findViewById(R.id.selling_price);
        checkBoxDefault = findViewById(R.id.var_dtls_default);
        cbCompositeRequired = findViewById(R.id.composite_required);
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

                    if (!cbCompositeRequired.isChecked()){
                        resulhelperVariantsDtls.setComposite_required("N");
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

        cbCompositeRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Log.d("checkCompositeLink", "size=" + listCompositeLinks.size());

                for (int i=0; i < listCompositeLinks.size(); i++){
                    Log.d("checkCompositeLink", "composite_link_id=" + listCompositeLinks.get(i).getComposite_link_id());
                    Log.d("checkCompositeLink", "item_id=" + listCompositeLinks.get(i).getItem_id());
                    Log.d("checkCompositeLink", "var_hdr_id=" + listCompositeLinks.get(i).getVar_hdr_id());
                    Log.d("checkCompositeLink", "var_dtls_id=" + listCompositeLinks.get(i).getVar_dtls_id());
                    Log.d("checkCompositeLink", "unit=" + listCompositeLinks.get(i).getUnit());
                }
                */
                if (!cbCompositeRequired.isChecked()){
                    if (bVarHasComposite){
                        popMessage("Cannot uncheck. Delete the composite of the variants in 1 or more items.");
                        cbCompositeRequired.setChecked(true);
                    }
                }
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


        cbCompositeRequired.setChecked(true);
        if (helperVariantsDtlsPrev.getComposite_required() != null) {
            if (helperVariantsDtlsPrev.getComposite_required().equals("N")) {
                cbCompositeRequired.setChecked(false);
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

    public void checkCompositeLink(String var_dtls_id){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("composite_links").orderByChild("var_dtls_id").equalTo(var_dtls_id);
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        //listCompositeLinks.add(appleSnapshot.getValue(HelperCompositeLinks.class));
                        bVarHasComposite = true;
                    }

                    popMessage("Done checking");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                popMessage("Cannot connect to server - checkCompositeLink");
            }
        });

    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
    }

}