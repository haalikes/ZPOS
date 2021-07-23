package com.watata.zpos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ItemAddActivity extends AppCompatActivity {

    Button saveBtn, cancelBtn, varLinkBtn, stockLinkBtn;
    ImageView item_image;
    EditText item_name, item_selling_price;
    int item_id, cat_id, var_link_id, stock_link_id;
    HelperItem helperItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);

        setupXmlIds();

        //cat_name = getIntent().getStringExtra("cat_name");
        cat_id = getIntent().getIntExtra("cat_id", 0);
        helperItem = (HelperItem) getIntent().getSerializableExtra("helperItem");

        setupListeners();
        copyValues();

    }

    public void setupXmlIds(){
        saveBtn = findViewById(R.id.save);
        cancelBtn = findViewById(R.id.back);
        item_image = findViewById(R.id.item_image);
        item_name = findViewById(R.id.item_name);
        item_selling_price = findViewById(R.id.item_selling_price);
        varLinkBtn = findViewById(R.id.var_link);
        stockLinkBtn = findViewById(R.id.stock_link);
    }

    public void copyValues(){
        item_id = helperItem.getItem_id();
        item_name.setText(helperItem.getItem_name());
        if (helperItem.getItem_image() != null){
            if (!helperItem.getItem_image().equals("0")){
                item_image.setImageResource(getImageId(this, helperItem.getItem_image()));
                item_image.setTag(helperItem.getItem_image());
            } else {
                item_image.setImageResource(R.drawable.ic_no_icon);
                item_image.setTag("0");
            }
        } else {
            item_image.setImageResource(R.drawable.ic_no_icon);
            item_image.setTag("0");
        }

        item_selling_price.setText(helperItem.getItem_selling_price());

    }

    public void setupListeners(){
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!item_name.getText().toString().equals("") && !item_name.getText().toString().equals("Click to Add")){

                    HelperItem resulthelperItem = new HelperItem();
                    resulthelperItem.setItem_id(item_id);
                    resulthelperItem.setCat_id(cat_id);
                    resulthelperItem.setItem_name(item_name.getText().toString());
                    resulthelperItem.setItem_image(item_image.getTag().toString());
                    resulthelperItem.setItem_selling_price("" + item_selling_price.getText());

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("cat_id", cat_id);
                    resultIntent.putExtra("resulthelperItem", resulthelperItem);

                    addItemFB(resulthelperItem);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    popMessage("Please enter Item Name.");
                    item_name.setText("");
                }

            }
        });

        item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemIconsActivity();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        varLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVariantsLinkEditActivity();
            }
        });

        stockLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCompositeLinkEditActivity();
            }
        });

    }

    public void openItemIconsActivity() {
        Intent intent = new Intent(this, ItemIconsActivity.class);
        //intent.putExtra("username", username.getText().toString());
        startActivityForResult(intent, 1);
    }

    public void openVariantsLinkEditActivity() {
        Intent intent = new Intent(this, VariantsLinksEditActivity.class);
        intent.putExtra("item_id", item_id);
        startActivityForResult(intent, 2);
    }

    public void openCompositeLinkEditActivity() {
        Intent intent = new Intent(this, CompositeLinksEditActivity.class);
        intent.putExtra("item_id", item_id);
        intent.putExtra("item_name", helperItem.getItem_name());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK) {

                //itemIconSelected
                if (data.getStringExtra("iconSelected") != "0"
                        && data.getStringExtra("iconSelected")  != "ic_no_icon"
                        && data.getStringExtra("iconSelected")  != "ic_add" ) {
                    item_image.setImageResource(getImageId(this, data.getStringExtra("iconSelected" )));
                    item_image.setTag(data.getStringExtra("iconSelected" ));
                } else {
                    item_image.setImageResource(R.drawable.ic_no_icon);
                    item_image.setTag("0");
                }

            }
            if (resultCode == RESULT_CANCELED) {
                popMessage("No icon selected");
            }
        }

        if (requestCode == 2){
            if (resultCode == RESULT_OK) {

                if (data.getIntExtra("var_link_id", 0) != 0 ) {
                    var_link_id = data.getIntExtra("var_link_id", 0);
                } else {
                    var_link_id = 0;
                }

            }
            if (resultCode == RESULT_CANCELED) {
                popMessage("No variants selected");
            }
        }

    }

    public void addItemFB(HelperItem helperItem){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("items");
        reference.child("" + helperItem.getItem_id()).setValue(helperItem);

        ChangesFB changesFB = new ChangesFB();
        changesFB.ChangesItems();

        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference("items").push();
        //reference.setValue(helperItem);

        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference("items").child(helperItem.getCat_name());
        //reference.child("" + helperItem.getItem_id()).setValue(helperItem);
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }


    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
    }

}