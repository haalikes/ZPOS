package com.watata.zpos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoryAddActivity extends AppCompatActivity {

    Button saveBtn, cancelBtn, itemEditBtn;
    ImageView iCatSelected;
    EditText eCatName;
    //String id;
    int cat_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);


        setupXmlIds();
        eCatName.setText(getIntent().getStringExtra("catName"));
        iCatSelected.setImageResource(getIntent().getIntExtra("catImage", 0));
        iCatSelected.setTag(getIntent().getIntExtra("catImage", 0));
        cat_id = getIntent().getIntExtra("catId",0);
        setupListeners();


    }

    public void setupXmlIds(){
        saveBtn = findViewById(R.id.save);
        cancelBtn = findViewById(R.id.back);
        iCatSelected = findViewById(R.id.imagecategory);
        eCatName = findViewById(R.id.catname);
        itemEditBtn = findViewById(R.id.itemedit);
    }

    public void setupListeners(){
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!eCatName.getText().toString().equals("") && !eCatName.getText().toString().equals("Click to Add")){
                    Intent resultIntent = new Intent();
                    Integer resource = (Integer) iCatSelected.getTag();
                    resultIntent.putExtra("catCurrentIcon", resource);
                    resultIntent.putExtra("catCurrentName", eCatName.getText().toString() );

                    HelperCategory helperCategory = new HelperCategory(cat_id, resource.toString(), eCatName.getText().toString());
                    addCategoryFB(helperCategory);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    popMessage("Please enter Category Name.");
                    eCatName.setText("");
                }

            }
        });

        iCatSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryIconsActivity();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        itemEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemEditActivity();
            }
        });

    }

    public void openCategoryIconsActivity() {
        Intent intent = new Intent(this, CategoryIconsActivity.class);
        //intent.putExtra("username", username.getText().toString());
        startActivityForResult(intent, 1);
    }

    public void openItemEditActivity() {
        Intent intent = new Intent(this, ItemEditActivity.class);
        //Intent intent = new Intent(this, TestActivity.class);
        //intent.putExtra("cat_name", eCatName.getText().toString());
        intent.putExtra("cat_id", cat_id);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK) {

                if (data.getIntExtra("catIconSelected", 0) != 0
                        && data.getIntExtra("catIconSelected", 0)  != R.drawable.ic_no_icon
                        && data.getIntExtra("catIconSelected", 0)  != R.drawable.ic_add ) {
                    iCatSelected.setImageResource(data.getIntExtra("catIconSelected", 0 ));
                    iCatSelected.setTag(data.getIntExtra("catIconSelected", 0 ));
                } else {
                    //int id = getResources().getIdentifier("watata.clabsme.zinventory:drawable/" + "ic_block", null, null);
                    //iCatSelected.setImageResource(id);
                    iCatSelected.setImageResource(0);
                    iCatSelected.setTag(0);
                }

            }
            if (resultCode == RESULT_CANCELED) {
                popMessage("Nothing selected");
            }
        }
    }

    public void addCategoryFB(HelperCategory helperCategory){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("category");
        reference.child("" + helperCategory.getCat_id()).setValue(helperCategory);

        ChangesFB changesFB = new ChangesFB();
        changesFB.ChangesInCategory();
    }

    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
    }

}