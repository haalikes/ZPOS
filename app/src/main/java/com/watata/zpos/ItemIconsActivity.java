package com.watata.zpos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
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

import java.util.LinkedList;
import java.util.List;

public class ItemIconsActivity extends AppCompatActivity {

    ImageView iItem1, iItem2, iItem3, iItem4, iItem5, iIcedrop, iDine, iIcecream, iBlock, iBlank, iItem0, iItem01;

    List<String> iconList = new LinkedList<>();
    GridLayout iconGrid;

    private int dp80, dp1, dp6, dp12, dp16, dp20, dp25, dp35;
    private LinearLayout.LayoutParams lp80dp;
    private LinearLayout.LayoutParams lp35dp;
    private float scale;
    private final LinearLayout.LayoutParams lpWrapWrap = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    private final LinearLayout.LayoutParams lpMatchMatch = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_icons);


        setupXmlIds();

        initialize();
        populateGrid();
        setupListeners();

    }

    public void initialize(){
        iconList.clear();
        //first add is filename
        //second add is display Name

        iconList.add("zc");
        iconList.add("ZC");
        iconList.add("zczach");
        iconList.add("ZC Effect");

        iconList.add("streetvendo");
        iconList.add("Street Foods");
        iconList.add("friednoodles");
        iconList.add("Fried Noodles");
        iconList.add("squidballs");
        iconList.add("Squid Balls");
        iconList.add("chickenballs");
        iconList.add("Chicken Balls");
        iconList.add("fishballs");
        iconList.add("Fish Balls");
        iconList.add("dumpling");
        iconList.add("Dumplings");
        iconList.add("cheesesticks");
        iconList.add("Cheese Sticks");

        iconList.add("foodpanda");
        iconList.add("Food Panda");

        iconList.add("wings");
        iconList.add("Wings");
        iconList.add("wings3pcs");
        iconList.add("Wings 3pcs");
        iconList.add("wings6pcs");
        iconList.add("Wings 6pcs");
        iconList.add("wings12pcs");
        iconList.add("Wings 12pcs");

        iconList.add("burger");
        iconList.add("Burger");
        iconList.add("burgerplain");
        iconList.add("Burger");
        iconList.add("burgerx2patty");
        iconList.add("Double Patty");
        iconList.add("burgerwegg");
        iconList.add("With Egg");
        iconList.add("patty");
        iconList.add("Patty");

        iconList.add("pizza");
        iconList.add("Pizza");
        iconList.add("pizzetta");
        iconList.add("Pizzetta");
        iconList.add("pizzettaduo");
        iconList.add("Pizzetta Duo");
        iconList.add("pizzettabluecheese");
        iconList.add("Blue Cheese");
        iconList.add("pizzettabuffalochicken");
        iconList.add("Buf Chicken");

        iconList.add("fries");
        iconList.add("Fries");
        iconList.add("friesregular");
        iconList.add("Regular");
        iconList.add("friesmedium");
        iconList.add("Medium");
        iconList.add("frieslarge");
        iconList.add("Large");
        iconList.add("friesbiggie");
        iconList.add("Biggie");

        iconList.add("nachos");
        iconList.add("Nachos");
        iconList.add("nachosmedium");
        iconList.add("Medium");

        iconList.add("extras");
        iconList.add("Extras");
        iconList.add("water");
        iconList.add("Water");
        iconList.add("soda");
        iconList.add("Softdrinks");
        iconList.add("rice");
        iconList.add("Rice");

        iconList.add("icedrop");
        iconList.add("Ice Drop");
        iconList.add("dine");
        iconList.add("Dine In");
        iconList.add("icecream");
        iconList.add("Ice Cream");
        iconList.add("0");
        iconList.add("No Icon");

        scale = this.getResources().getDisplayMetrics().density;
        dp20 = (int) (20 * scale + 0.5f);
        dp80 = (int) (80 * scale + 0.5f);
        lp80dp = new LinearLayout.LayoutParams( dp80, dp80);
    }

    public void populateGrid(){
        iconGrid.removeAllViews();
        for (int i = 0; i < iconList.size(); i++){
            iconGrid.addView(completeCardView(iconList.get(i), iconList.get(i+1)));
            i++;
        }
    }

    public CardView completeCardView(String iconFileName, String iconName){
        TextView itemNameTextView;
        RelativeLayout relativeLayout;
        ImageView imageView;
        LinearLayout linearLayout;
        CardView cardView;

        itemNameTextView = createItemNameTextView(iconName);
        relativeLayout = createRelativeLayout();
        cardView = createCardView();

        //addViews
        imageView = createIconImageView(iconFileName);
        relativeLayout.addView(imageView);
        linearLayout = createLinearLayout(iconFileName);

        linearLayout.addView(relativeLayout);
        linearLayout.addView(itemNameTextView);
        cardView.addView(linearLayout);

        return cardView;
    }

    public TextView createItemNameTextView(String iconName){
        TextView textView = new TextView(this);
        textView.setLayoutParams(lpWrapWrap);
        textView.setHint(iconName);
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    public RelativeLayout createRelativeLayout(){
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(lpWrapWrap);
        return relativeLayout;
    }

    public CardView createCardView(){
        CardView cardView = new CardView(this);
        cardView.setCardElevation(dp6);
        cardView.setRadius(dp12);
        cardView.setPadding(dp6,dp6,dp6,dp6);
        GridLayout.LayoutParams parem = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
        parem.setMargins(dp12,dp12,dp12,dp12);
        cardView.setLayoutParams(parem);
        cardView.setBackgroundColor(this.getResources().getColor(R.color.transFull));
        return cardView;
    }

    public ImageView createIconImageView(String iconFileName){
        //ImageView
        final ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(lp80dp);

        imageView.setImageResource(getImageId(this, iconFileName));
        imageView.setTag(iconFileName);

        return imageView;
    }

    public LinearLayout createLinearLayout(String iconFileName){
        //LinearLayout
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(lpMatchMatch);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(dp16, dp16, dp16, dp16);
        linearLayout.setClickable(true);

        //linearLayout.setBackground(this.getResources().getDrawable(R.drawable.custom_ll_item, this.getTheme()));

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconSelected(iconFileName);
            }
        });

        return linearLayout;
    }

    public void setupXmlIds(){
        iBlank = findViewById(R.id.blank);
        iconGrid = findViewById(R.id.icongrid);

    }

    public void setupListeners(){

        iBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconSelected("0");
            }
        });

    }

    public void iconSelected(String iconFileName){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("iconSelected", iconFileName);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public static int getImageId(Context context, String imageName) {
        if (imageName=="0"){
            return context.getResources().getIdentifier("drawable/" + "ic_no_icon", null, context.getPackageName());
        } else {
            return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
        }

    }

}