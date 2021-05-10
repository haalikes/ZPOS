package com.watata.zpos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class CategoryEditActivity extends AppCompatActivity {

    GridLayout gCategories;
    ImageView gImageView;
    TextView gTextView, gnTextView, glasttextView;
    CardView addCardView;
    List<HelperCategory> listCategory = new LinkedList<HelperCategory>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);

        setupXmlIds();
        setupListeners();
        autoPopulateCategories();
        noItemsInFB();
    }

    public void setupXmlIds(){
        gCategories = findViewById(R.id.catgrid);
        addCardView = findViewById(R.id.addcardview);
    }

    public void setupListeners(){
        addCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!glasttextView.getText().equals("") && !glasttextView.getText().equals("Click to Add") && listCategory.size()!=0){
                    gCategories.removeView(addCardView);
                    createCategory(listCategory.get(listCategory.size()-1).getCat_id() + 1, "" + 0, "" );
                    gCategories.addView(addCardView);
                } else {
                    popMessage("Please fill all the category before adding.");
                }
            }
        });
    }

    public void noItemsInFB(){
        if (listCategory.size() == 0){
            gCategories.removeAllViews();
            populateOthers();
            gCategories.addView(addCardView);
        }

    }

    public void autoPopulateCategories() {

        final ProgressBar progressBar = new ProgressBar(this);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("category");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    gCategories.removeAllViews();
                    progressBar.setVisibility(View.VISIBLE);
                    gCategories.addView(progressBar);

                    listCategory.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listCategory.add(snapshot.getValue(HelperCategory.class));
                    }
                    progressBar.setVisibility(View.GONE);
                    gCategories.removeView(progressBar);

                    populateCategory();
                    populateOthers();
                    gCategories.addView(addCardView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                gCategories.removeView(progressBar);
            }
        });

    }

    public void populateCategory() {
        for (int i = 0; i < listCategory.size(); i++){
            createCategory(listCategory.get(i).getCat_id(), listCategory.get(i).getCat_image(), listCategory.get(i).getCat_name());
        }
    }

    public void populateOthers(){
        if ( listCategory.size() < 3){
            for (int i = listCategory.size(); i < 4; i ++) {
                createCategory(0, "" + 0, "");
            }
        }
    }

    public void createCategory(int id, String catImg, String catName) {
        final float scale = this.getResources().getDisplayMetrics().density;
        final String packageName = "watata.clabsme.zinventory";
        final int iId = id;
        final String scat_name = catName;
        int dp80 = (int) (80 * scale + 0.5f);
        int dp1 = (int) (1 * scale + 0.5f);
        int dp6 = (int) (6 * scale + 0.5f);
        int dp12 = (int) (12 * scale + 0.5f);
        int dp16 = (int) (16 * scale + 0.5f);
        int dp20 = (int) (20 * scale + 0.5f);
        int dp25 = (int) (25 * scale + 0.5f);
        int dp35 = (int) (35 * scale + 0.5f);

        //Cardview
        final CardView cardView = new CardView(this);
        //cardView.setBackgroundResource(R.drawable.rounded_corner_little);

        //cardView.setBackgroundResource(R.color.transRedSuper);
        //cardView.setCardBackgroundColor(getResources().getColor(R.color.transRedSuper));

        cardView.setCardElevation(dp6);
        cardView.setRadius(dp12);
        cardView.setPadding(dp6,dp6,dp6,dp6);
        GridLayout.LayoutParams parem = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
        parem.setMargins(dp12,dp12,dp12,dp12);
        cardView.setLayoutParams(parem);

        LinearLayout.LayoutParams lpMatchMatch = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        LinearLayout.LayoutParams lpWrapWrap = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        LinearLayout.LayoutParams lp80dp = new LinearLayout.LayoutParams( dp80, dp80);

        LinearLayout.LayoutParams lp35dp = new LinearLayout.LayoutParams( dp35, dp35);

        //imageView delete
        ImageView imageDel = new ImageView(this);
        lp35dp.weight = 1.0f;
        lp35dp.gravity = Gravity.RIGHT;
        imageDel.setLayoutParams(lp35dp);
        imageDel.setImageResource(R.drawable.ic_delete);

        //LinearLayout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(lpMatchMatch);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(dp16, dp16, dp16, dp16);

        //RelativeLayout
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(lpWrapWrap);

        //TextView No Icon
        final TextView itextView = new TextView(this);
        itextView.setLayoutParams(lp80dp);
        itextView.setTextSize(dp20);
        itextView.setGravity(Gravity.CENTER);

        //ImageView
        final ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(lp80dp);
        if (!catImg.equals("0")) {
            //int id = getResources().getIdentifier(packageName + ":drawable/" + catImg, null, null);
            imageView.setImageResource(Integer.parseInt(catImg));
            imageView.setTag(Integer.parseInt(catImg));

        } else {
            if (!catName.equals("")){
                imageView.setImageResource(R.drawable.ic_no_icon);
                imageView.setTag(R.drawable.ic_no_icon);
                itextView.setText(catName.substring(0,1));
            } else {
                imageView.setImageResource(R.drawable.ic_add);
                imageView.setTag(R.drawable.ic_add);
            }

        }


        //TextView
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lpWrapWrap);
        textView.setHint("Click to Add");
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);
        if (!catName.equals("")){
            textView.setText(catName);
        }
        glasttextView = textView;

        //addViews
        relativeLayout.addView(itextView);
        relativeLayout.addView(imageView);

        linearLayout.addView(relativeLayout);
        linearLayout.addView(textView);
        cardView.addView(imageDel);
        cardView.addView(linearLayout);

        gCategories.addView(cardView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryAddActivity(iId, imageView, textView, itextView);
            }
        });

        imageDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirm");
                builder.setMessage("Delete " + scat_name + "? Items will also be deleted for this category.");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delCategory(iId, cardView);
                        dialog.dismiss();
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


            }
        });
    }

    public void openCategoryAddActivity(int id, ImageView imageView, TextView textView, TextView ntextView) {
        if (imageView.getTag().equals(R.drawable.ic_add)) {
            imageView.setTag(0);
        }


        Intent intent = new Intent(this, CategoryAddActivity.class);
        intent.putExtra("catName", textView.getText().toString());
        Integer resource = (Integer) imageView.getTag();
        intent.putExtra("catImage", resource);
        intent.putExtra("catId", id);
        startActivityForResult(intent, 1);

        gImageView = imageView;
        gTextView = textView;
        gnTextView = ntextView;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK) {
                if (data.getIntExtra("catCurrentIcon", 0) != 0
                        && data.getIntExtra("catCurrentIcon", 0)  != R.drawable.ic_no_icon
                        && data.getIntExtra("catCurrentIcon", 0)  != R.drawable.ic_add ) {
                    gImageView.setImageResource(data.getIntExtra("catCurrentIcon", 0 ));
                    gImageView.setTag(data.getIntExtra("catCurrentIcon", 0));
                    gTextView.setText(data.getStringExtra("catCurrentName" ));

                } else {
                    gTextView.setText(data.getStringExtra("catCurrentName" ));
                    gnTextView.setText(( data.getStringExtra("catCurrentName" )).substring(0,1));
                    gImageView.setImageResource(R.drawable.ic_no_icon);
                    gImageView.setTag(R.drawable.ic_no_icon);
                }
            }
            if (resultCode == RESULT_CANCELED) {
                popMessage("Nothing selected");
            }
        }
    }

    public void delCategory(int id, CardView cardView){
        if (listCategory.size() > 4){
            deleteCategoryFB(id);
            gCategories.removeView(cardView);
        } else {
            Log.d( "delCategory", "Cannot delete id=" + id );
            popMessage("You cannot delete another category. Minimum is 4.");
        }

    }

    public void deleteCategoryFB(final int id) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("category").orderByChild("cat_id").equalTo(id);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                    ChangesFB changesFB = new ChangesFB();
                    changesFB.ChangesInCategory();

                    deleteItemsFB(id);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e("FB Error", "onCancelled", databaseError.toException());
            }
        });

    }

    public void deleteItemsFB(int cat_id){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("items").orderByChild("cat_id").equalTo(cat_id);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                    ChangesFB changesFB = new ChangesFB();
                    changesFB.ChangesItems();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e("FB Error", "onCancelled", databaseError.toException());
            }
        });
    }

    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_LONG).show();
    }
}