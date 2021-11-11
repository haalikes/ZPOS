package com.watata.zpos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.watata.zpos.ddlclass.HelperSales;

import java.util.LinkedList;
import java.util.List;

public class CategoryMenuActivity extends AppCompatActivity {

    float scale;
    GridLayout gCategories;
    ImageView gImageView;
    TextView gTextView, gnTextView, glasttextView, gHeader;
    List<HelperCategory> listCategory = new LinkedList<HelperCategory>();

    String screen_size, all_date;

    HelperDatabase helperDatabase = new HelperDatabase(this);

    //LAYOUTSUMMARY
    TableLayout tableSales;
    //LayoutSummary layoutSummary;

    List<HelperSales> listHelperSales = new LinkedList<HelperSales>();
    HelperSales helperSalesInit = new HelperSales();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_menu);

        scale = this.getResources().getDisplayMetrics().density;
        all_date = getIntent().getStringExtra("all_date");

        setupXmlIds();
        setupListeners();
        autoPopulateCategories();
        noItemsInFB();

        //SALES start
        startLayoutSales();

        //helperSalesInit.setCreated_by("admin");
        //populateSales(helperSalesInit);

        //SALES end
    }


    public void setupXmlIds(){
        gCategories = findViewById(R.id.catgrid);
        screen_size = getResources().getString(R.string.screen_type);
        tableSales = findViewById(R.id.tblHistoryDtls);
        gHeader = findViewById(R.id.header);
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

    public void noItemsInFB(){
        if (listCategory.size() == 0){
            gCategories.removeAllViews();
            populateOthers();
        }

    }

    public void autoPopulateCategories() {

        final ProgressBar progressBar = new ProgressBar(this);

        gCategories.removeAllViews();
        progressBar.setVisibility(View.VISIBLE);
        gCategories.addView(progressBar);

        listCategory.clear();
        //HelperDatabase helperDatabase = new HelperDatabase(this);
        listCategory = helperDatabase.listCategory();
        progressBar.setVisibility(View.GONE);
        gCategories.removeView(progressBar);

        populateCategory();
        populateOthers();
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
        cardView.setBackgroundColor(getResources().getColor(R.color.transFull));

        LinearLayout.LayoutParams lpMatchMatch = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        LinearLayout.LayoutParams lpWrapWrap = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        LinearLayout.LayoutParams lp80dp = new LinearLayout.LayoutParams( dp80, dp80);

        //LinearLayout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(lpMatchMatch);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(dp16, dp16, dp16, dp16);
        linearLayout.setClickable(true);
        linearLayout.setBackground(getResources().getDrawable(R.drawable.custom_ll_category, getTheme()));

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
            imageView.setImageResource(getImageId(this, catImg));
            imageView.setTag((catImg));

        } else {
            if (!catName.equals("")){
                imageView.setImageResource(R.drawable.ic_no_icon);
                imageView.setTag("0");
                itextView.setText(catName.substring(0,1));
            } else {
                imageView.setImageResource(R.drawable.ic_add);
                imageView.setTag("ic_add");
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
        cardView.addView(linearLayout);

        gCategories.addView(cardView);

        /*
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemMenuActivity(iId, imageView, textView, itextView);
            }
        });

         */

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemMenuActivity(iId, imageView, textView, itextView);
            }
        });

    }

    public void openItemMenuActivity(int id, ImageView imageView, TextView textView, TextView ntextView) {
        if (imageView.getTag().equals("ic_add")) {
            imageView.setTag("ic_add");
        }


        Intent intent = new Intent(this, ItemMenuActivity.class);
        intent.putExtra("cat_id", id);
        intent.putExtra("all_date", all_date);
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
            }
            if (resultCode == RESULT_CANCELED) {
                popMessage("Nothing selected");
            }

            LayoutSummary layoutSummary = new LayoutSummary(this);
            layoutSummary.setContext(this);
            layoutSummary.setTableSales(tableSales);
            layoutSummary.setScreen_size(screen_size);
            layoutSummary.setScale(scale);
            layoutSummary.setIn_category(true);
            layoutSummary.populateSummarySales();
        }
    }

    //SALES start
    public void startLayoutSales(){
        LayoutSummary layoutSummary = new LayoutSummary(this);
        layoutSummary.setContext(this);
        layoutSummary.setTableSales(tableSales);
        layoutSummary.setScreen_size(screen_size);
        layoutSummary.setScale(scale);
        layoutSummary.setIn_category(true);
        /*
        HelperSales helperSales = new HelperSales();
        helperSales.setCreated_by("admin");
        helperSales.setMachine_name("pos1");
        */
        ///layoutSummary.populateSales(helperSales);
        layoutSummary.populateSummarySales();
    }
    //SALES end

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_LONG).show();
    }
}