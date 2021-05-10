package com.watata.zpos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class VariantsDtlsEditActivity extends AppCompatActivity {


    float scale;
    int dp80, dp1, dp6, dp12, dp16, dp20, dp25, dp35;

    final LinearLayout.LayoutParams lpMatchMatch = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
    );

    final LinearLayout.LayoutParams lpWrapWrap = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    final LinearLayout.LayoutParams lpWrapWrap1f = new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
    );

    LinearLayout.LayoutParams lp80dp;

    LinearLayout.LayoutParams lp35dp;


    GridLayout gGrid;
    ImageView gImageView;
    TextView gTextView, gnTextView, glasttextView;
    CardView addCardView;
    String screen_size;

    List<HelperVariantsDtls> listVarDtls = new LinkedList<HelperVariantsDtls>();
    List<HelperVariantsDtls> listVarDtlsAll = new LinkedList<HelperVariantsDtls>();
    HelperVariantsHdr helperVariantsHdrPrev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variants_dtls_edit);


        initializeVariables();

        helperVariantsHdrPrev = (HelperVariantsHdr) getIntent().getSerializableExtra("helperVariantsHdr");

        setupXmlIds();
        setupListeners();
        queryListAll();
        autoPopulate();

        if (listVarDtls.size() == 0) {
            populateGrid();
        }

    }


    public void initializeVariables(){
        scale = this.getResources().getDisplayMetrics().density;
        dp80 = (int) (80 * scale + 0.5f);
        dp1 = (int) (1 * scale + 0.5f);
        dp6 = (int) (6 * scale + 0.5f);
        dp12 = (int) (12 * scale + 0.5f);
        dp16 = (int) (16 * scale + 0.5f);
        dp20 = (int) (20 * scale + 0.5f);
        dp25 = (int) (25 * scale + 0.5f);
        dp35 = (int) (35 * scale + 0.5f);

        lp80dp = new LinearLayout.LayoutParams( dp80, dp80);
        lp35dp = new LinearLayout.LayoutParams( dp35, dp35);
    }

    public void setupXmlIds(){
        gGrid = findViewById(R.id.grid);
        addCardView = findViewById(R.id.addcardview);
        screen_size = getResources().getString(R.string.screen_type);
    }

    public void setupListeners(){
        addCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gGrid.removeView(addCardView);
                HelperVariantsDtls helperVariantsDtls = new HelperVariantsDtls();
                helperVariantsDtls.setVar_hdr_id(helperVariantsHdrPrev.getVar_hdr_id());

                if (listVarDtlsAll.size() == 0){
                    helperVariantsDtls.setVar_dtls_id(0);
                    gGrid.addView(completeCardView(helperVariantsDtls));
                    setGridColumn();
                    gGrid.addView(addCardView);
                } else {
                    if (listVarDtls.size() == 0){
                        helperVariantsDtls.setVar_dtls_id(listVarDtlsAll.get(listVarDtlsAll.size()-1).getVar_dtls_id() + 1);
                        gGrid.addView(completeCardView(helperVariantsDtls));
                        setGridColumn();
                        gGrid.addView(addCardView);
                    } else {
                        if (!glasttextView.getText().equals("") && !glasttextView.getText().equals("Click to Add") ){
                            helperVariantsDtls.setVar_dtls_id(listVarDtlsAll.get(listVarDtlsAll.size()-1).getVar_dtls_id() + 1);
                            gGrid.addView(completeCardView(helperVariantsDtls));
                            setGridColumn();
                            gGrid.addView(addCardView);
                        } else {
                            popMessage("Please fill all the items before adding.");
                            gGrid.addView(addCardView);
                        }
                    }
                }

            }
        });
    }

    public void setGridColumn(){
        switch(screen_size){
            case "phone":
                switch(listVarDtls.size()){
                    case 0:
                        gGrid.setColumnCount(1);
                        break;
                    default:
                        gGrid.setColumnCount(2);
                        break;
                }
                break;
            default:
                switch(listVarDtls.size()) {
                    case 0:
                        gGrid.setColumnCount(1);
                        break;
                    case 1:
                        gGrid.setColumnCount(2);
                        break;
                    case 2:
                        gGrid.setColumnCount(3);
                        break;
                    default:
                        gGrid.setColumnCount(4);
                        break;
                }
                break;
        }
    }

    public void autoPopulate(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("variants_dtls").orderByChild("var_hdr_id").equalTo(helperVariantsHdrPrev.getVar_hdr_id());
        //Query applesQuery = ref.child("items").child(cat_name);

        applesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listVarDtls.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listVarDtls.add(snapshot.getValue(HelperVariantsDtls.class));
                    }
                    populateGrid();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void queryListAll(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("variants_dtls");

        applesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listVarDtlsAll.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listVarDtlsAll.add(snapshot.getValue(HelperVariantsDtls.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void populateGrid(){
        gGrid.removeAllViews();
        for (int i = 0; i < listVarDtls.size(); i++){
            gGrid.addView(completeCardView(listVarDtls.get(i)));
        }
        setGridColumn();
        gGrid.addView(addCardView);
    }

    public CardView completeCardView(HelperVariantsDtls helperVariantsDtls){
        TextView itextView, itemNameTextView;
        RelativeLayout relativeLayout;
        LinearLayout linearLayout;
        CardView cardView;
        ImageView delImage;

        itextView = iconTextView(helperVariantsDtls);
        itemNameTextView = itemNameTextView(helperVariantsDtls);
        relativeLayout = createRelativeLayout();
        linearLayout = createLinearLayout();
        cardView = createCardView();
        delImage = delImageVIew(helperVariantsDtls,cardView);

        //addViews
        relativeLayout.addView(itextView);
        relativeLayout.addView(iconImageView(helperVariantsDtls, itemNameTextView, itextView));

        linearLayout.addView(relativeLayout);
        linearLayout.addView(itemNameTextView);
        cardView.addView(delImage);
        cardView.addView(linearLayout);

        return cardView;
    }

    public TextView iconTextView(HelperVariantsDtls helperVariantsDtls){
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lp80dp);
        textView.setTextSize(dp20);
        textView.setGravity(Gravity.CENTER);
        if (helperVariantsDtls.getVar_dtls_name() != null){
            textView.setText(helperVariantsDtls.getVar_dtls_name().substring(0,1));
        }
        return textView;
    }

    public TextView itemNameTextView(HelperVariantsDtls helperVariantsDtls){
        TextView textView = new TextView(this);
        textView.setLayoutParams(lpWrapWrap);
        textView.setHint("Click to Add");
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);
        if (helperVariantsDtls.getVar_dtls_name() != null){
            textView.setText(helperVariantsDtls.getVar_dtls_name());
        }
        glasttextView = textView;
        return textView;
    }

    public RelativeLayout createRelativeLayout(){
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(lpWrapWrap);
        return relativeLayout;
    }

    public LinearLayout createLinearLayout(){
        //LinearLayout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(lpMatchMatch);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(dp16, dp16, dp16, dp16);
        return linearLayout;
    }

    public CardView createCardView(){
        CardView cardView = new CardView(this);
        cardView.setCardElevation(dp6);
        cardView.setRadius(dp12);
        cardView.setPadding(dp6,dp6,dp6,dp6);
        GridLayout.LayoutParams parem = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
        parem.setMargins(dp12,dp12,dp12,dp12);
        cardView.setLayoutParams(parem);
        return cardView;
    }

    public ImageView delImageVIew(final HelperVariantsDtls helperVariantsDtls, final CardView cardView){
        int dp35 = (int) (35 * scale + 0.5f);
        LinearLayout.LayoutParams lp35dp = new LinearLayout.LayoutParams( dp35, dp35);

        //imageView delete
        ImageView delImmage = new ImageView(this);
        lp35dp.weight = 1.0f;
        lp35dp.gravity = Gravity.RIGHT;
        delImmage.setLayoutParams(lp35dp);
        delImmage.setImageResource(R.drawable.ic_delete);

        delImmage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirm");
                builder.setMessage("Delete " + helperVariantsDtls.getVar_dtls_name() + "?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delItem(helperVariantsDtls, cardView);
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

        return delImmage;
    }

    public void delItem(HelperVariantsDtls helperVariantsDtls, CardView cardView){
        delItemFB(helperVariantsDtls);
        gGrid.removeView(cardView);
        if (listVarDtls.size() == 0) {
            populateGrid();
        }

    }

    public void delItemFB(final HelperVariantsDtls helperVariantsDtls) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("variants_dtls").orderByChild("var_dtls_id").equalTo(helperVariantsDtls.getVar_dtls_id());
        //Query applesQuery = ref.child("items").child(helperItem.getCat_name()).orderByChild("item_id").equalTo(helperItem.getItem_id());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                ChangesFB changesFB = new ChangesFB();
                changesFB.ChangesVariantsDtls();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e("FB Error", "onCancelled", databaseError.toException());
            }
        });
    }

    public ImageView iconImageView(final HelperVariantsDtls helperVariantsDtls, final TextView itemNameTextView, final TextView iconTextView){
        //ImageView
        final ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(lp80dp);
        if (helperVariantsDtls.getVar_dtls_image() != null){
            if (!helperVariantsDtls.getVar_dtls_image().equals("0")) {
                imageView.setImageResource(Integer.parseInt(helperVariantsDtls.getVar_dtls_image()));
                imageView.setTag(Integer.parseInt(helperVariantsDtls.getVar_dtls_image()));

            } else {
                if (!helperVariantsDtls.getVar_dtls_name().equals("")){
                    helperVariantsDtls.setVar_dtls_image("" + R.drawable.ic_no_icon);
                    imageView.setImageResource(R.drawable.ic_no_icon);
                    imageView.setTag(R.drawable.ic_no_icon);
                } else {
                    helperVariantsDtls.setVar_dtls_image("" + R.drawable.ic_add);
                    imageView.setImageResource(R.drawable.ic_add);
                    imageView.setTag(R.drawable.ic_add);
                }
            }
        } else {
            if (helperVariantsDtls.getVar_dtls_name() != null){
                helperVariantsDtls.setVar_dtls_image("" + R.drawable.ic_no_icon);
                imageView.setImageResource(R.drawable.ic_no_icon);
                imageView.setTag(R.drawable.ic_no_icon);
            } else {
                helperVariantsDtls.setVar_dtls_image("" + R.drawable.ic_add);
                imageView.setImageResource(R.drawable.ic_add);
                imageView.setTag(R.drawable.ic_add);
            }
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVariantsDtlsAddActivity(helperVariantsDtls, imageView, itemNameTextView, iconTextView);
            }
        });

        return imageView;
    }

    public void openVariantsDtlsAddActivity(HelperVariantsDtls helperVariantsDtls, ImageView imageView, TextView textView, TextView ntextView) {
        if (helperVariantsDtls.getVar_dtls_image() != null){
            if (helperVariantsDtls.getVar_dtls_image().equals("" + R.drawable.ic_add)) {
                helperVariantsDtls.setVar_dtls_image("0");
            }
        }

        Intent intent = new Intent(this, VariantsDtlsAddActivity.class);
        intent.putExtra("helperVariantsDtls", helperVariantsDtls);
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
                /*
                HelperItem resultHelperItem = (HelperItem) data.getSerializableExtra("resulthelperItem");

                if (resultHelperItem.getItem_image() != null){
                    if ( !resultHelperItem.getItem_image().equals("0") &&
                            !resultHelperItem.getItem_image().equals("" + R.drawable.ic_no_icon) &&
                            !resultHelperItem.getItem_image().equals("" + R.drawable.ic_add) ) {
                        gImageView.setImageResource(Integer.parseInt(resultHelperItem.getItem_image()));
                        gImageView.setTag(resultHelperItem.getItem_image());
                        gTextView.setText(resultHelperItem.getItem_name());
                    } else {
                        gTextView.setText(resultHelperItem.getItem_name());
                        gnTextView.setText(( resultHelperItem.getItem_name()).substring(0,1));
                        gImageView.setImageResource(R.drawable.ic_no_icon);
                        gImageView.setTag(R.drawable.ic_no_icon);
                    }
                } else {
                    gTextView.setText(resultHelperItem.getItem_name());
                    gnTextView.setText(( resultHelperItem.getItem_name()).substring(0,1));
                    gImageView.setImageResource(R.drawable.ic_no_icon);
                    gImageView.setTag(R.drawable.ic_no_icon);
                }
                */
            }
            if (resultCode == RESULT_CANCELED) {
                popMessage("Nothing selected");
            }
        }
    }

    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_LONG).show();
    }

}