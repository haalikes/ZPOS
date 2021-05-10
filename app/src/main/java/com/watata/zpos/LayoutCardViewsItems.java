package com.watata.zpos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LayoutCardViewsItems {

    //getter setter
    private Context context;
    private float scale;
    private String screen_size, all_date;
    private int cat_id;
    private GridLayout gGrid;
    private LayoutSummary layoutSummary;

    private int dp80, dp1, dp6, dp12, dp16, dp20, dp25, dp35;
    private LinearLayout.LayoutParams lp80dp;
    private LinearLayout.LayoutParams lp35dp;
    private HelperDatabase helperDatabase;
    private List<HelperItem> listItems = new LinkedList<HelperItem>();
    private List<HelperItem> listItemsAll = new LinkedList<HelperItem>();
    private List<HelperVariantsLinks> listVariantsLinks = new LinkedList<HelperVariantsLinks>();
    private final LinearLayout.LayoutParams lpWrapWrap = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );
    private final LinearLayout.LayoutParams lpMatchMatch = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
    );

    //global
    private ImageView gImageView;
    private TextView gTextView, gnTextView, glasttextView;

    public LayoutCardViewsItems() {

    }

    public void startCardViews(){
        initializeVariables();
        populateList();
        populateGrid();
    }

    public void initializeVariables(){
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

        helperDatabase= new HelperDatabase(context);


    }

    public void populateList(){
        listItems.clear();
        listItems = helperDatabase.listItems(cat_id);

        listItemsAll.clear();
        listItemsAll = helperDatabase.listItems();

        listVariantsLinks.clear();
        listVariantsLinks = helperDatabase.listVariantsLinks();
    }

    public void populateGrid(){
        gGrid.removeAllViews();
        for (int i = 0; i < listItems.size(); i++){
            gGrid.addView(completeCardView(listItems.get(i)));
        }
        setGridColumn();
        popMessage("grid col =" + gGrid.getColumnCount() + " listItems size =" + listItems.size());
    }

    public CardView completeCardView(HelperItem helperItem){
        TextView itextView, itemNameTextView, iPrice;
        RelativeLayout relativeLayout;
        ImageView imageView;
        LinearLayout linearLayout;
        CardView cardView;

        itextView = createIconTextView(helperItem);
        itemNameTextView = createItemNameTextView(helperItem);
        iPrice = createPriceTextView(helperItem);
        relativeLayout = createRelativeLayout();
        cardView = createCardView();

        //addViews
        relativeLayout.addView(itextView);
        imageView = createIconImageView(helperItem, itemNameTextView, itextView);
        relativeLayout.addView(imageView);
        linearLayout = createLinearLayout(helperItem, itemNameTextView, itextView, imageView);

        linearLayout.addView(relativeLayout);
        linearLayout.addView(itemNameTextView);
        linearLayout.addView(iPrice);
        cardView.addView(linearLayout);

        return cardView;
    }

    public TextView createIconTextView(HelperItem helperItem){
        TextView textView = new TextView(context);
        textView.setLayoutParams(lp80dp);
        textView.setTextSize(dp20);
        textView.setGravity(Gravity.CENTER);
        if (helperItem.getItem_name() != null){
            textView.setText(helperItem.getItem_name().substring(0,1));
        }
        return textView;
    }

    public TextView createItemNameTextView(HelperItem helperItem){
        TextView textView = new TextView(context);
        textView.setLayoutParams(lpWrapWrap);
        textView.setHint("Click to Add");
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);
        if (helperItem.getItem_name() != null){
            textView.setText(helperItem.getItem_name());
        }
        glasttextView = textView;
        return textView;
    }

    public TextView createPriceTextView(HelperItem helperItem){
        TextView textView = new TextView(context);
        textView.setLayoutParams(lpWrapWrap);
        textView.setHint("Click to Add");
        textView.setTextSize(12);
        textView.setGravity(Gravity.CENTER);
        int maxPrice = helperDatabase.priceInItemMenu(helperItem);
        String availableOrders = helperDatabase.stocksAvailable(helperItem) + " order(s)";
        if (maxPrice!=0){
            if (helperItem.getItem_selling_price() != null && !helperItem.getItem_selling_price().equals("0")){
                textView.setText("(Php " + helperItem.getItem_selling_price() + ".00 - " + ( Integer.parseInt(helperItem.getItem_selling_price()) + maxPrice ) + ".00)\n" + availableOrders);
            } else {
                textView.setText(" ");
            }
        } else {
            if (helperItem.getItem_selling_price() != null && !helperItem.getItem_selling_price().equals("0")){

                textView.setText("(Php " + helperItem.getItem_selling_price() + ".00)\n" + availableOrders);
            } else {
                textView.setText(" ");
            }
        }

        glasttextView = textView;
        return textView;
    }

    public RelativeLayout createRelativeLayout(){
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(lpWrapWrap);
        return relativeLayout;
    }

    public LinearLayout createLinearLayout(final HelperItem helperItem, final TextView itemNameTextView, final TextView iconTextView, final ImageView imageView){
        //LinearLayout
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(lpMatchMatch);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(dp16, dp16, dp16, dp16);
        linearLayout.setClickable(true);

        ///if(!helperDatabase.stocksOk(helperItem)){
        if(helperDatabase.stocksAvailable(helperItem)<=0){
            linearLayout.setBackground(context.getResources().getDrawable(R.drawable.custom_ll_out_of_stock, context.getTheme()));
        } else {
            linearLayout.setBackground(context.getResources().getDrawable(R.drawable.custom_ll_item, context.getTheme()));
        }

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///if(!helperDatabase.stocksOk(helperItem)){
                if(helperDatabase.stocksAvailable(helperItem)<=0){
                    linearLayout.setBackground(context.getResources().getDrawable(R.drawable.custom_ll_out_of_stock, context.getTheme()));
                    ///imageView.setImageResource(R.drawable.ic_out_of_stock);
                    //List no stocks Start
                    List<String> listOutOfStockNames = new LinkedList<>();
                    listOutOfStockNames.clear();
                    //listOutOfStockNames = helperDatabase.listOutOfStocks(helperItem);
                    listOutOfStockNames = helperDatabase.listOfStocks(helperItem);

                    String stockNames = "";
                    for(int i = 0; i < listOutOfStockNames.size(); i++){
                        stockNames += " - " + listOutOfStockNames.get(i) + "\n";
                    }
                    //List no stocks End

                    //Start alert
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    if((stockNames.equals(""))){
                        builder.setTitle("No Stocks Setup");
                        builder.setMessage("Please setup stocks for item " + helperItem.getItem_name() );
                    } else {
                        builder.setTitle("Out of Stock");
                        builder.setMessage(helperItem.getItem_name() + " is out stocks for the ff:\n\n" + stockNames );
                    }
                    builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ///if(helperDatabase.stocksOk(helperItem)) ((Activity)context).finish();
                            if(helperDatabase.stocksAvailable(helperItem)>0) ((Activity)context).finish();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    //End alert
                } else {
                    linearLayout.setBackground(context.getResources().getDrawable(R.drawable.custom_ll_item, context.getTheme()));
                    ///imageView.setImageResource(R.drawable.ic_no_icon);
                    openItemAddActivity(helperItem, imageView, itemNameTextView, iconTextView);
                }

            }
        });

        /*
        if(!helperDatabase.stocksOk(helperItem)){
            linearLayout.setBackground(context.getResources().getDrawable(R.drawable.custom_ll_out_of_stock, context.getTheme()));
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> listOutOfStockNames = new LinkedList<>();
                    listOutOfStockNames.clear();
                    listOutOfStockNames = helperDatabase.listOutOfStocks(helperItem);

                    String stockNames = "";
                    for(int i = 0; i < listOutOfStockNames.size(); i++){
                        stockNames += " - " + listOutOfStockNames.get(i) + "\n";
                    }
                    //Start alert
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        //verify again stocks
                        if((stockNames==null || stockNames.equals("")) && (!helperDatabase.stocksOk(helperItem))){
                            builder.setTitle("No Stocks Setup");
                            builder.setMessage("Please setup stocks for item " + helperItem.getItem_name() );
                        } else {
                            if(stockNames==null|| stockNames.equals("")){
                                builder.setTitle("Go Back to Category");
                                builder.setMessage( "Available stocks was changed, go back to Category." );
                            } else {
                                builder.setTitle("Out of Stock");
                                builder.setMessage(helperItem.getItem_name() + " is out stocks for the ff:\n\n" + stockNames );
                            }
                        }

                        builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if(helperDatabase.stocksOk(helperItem)) ((Activity)context).finish();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    //End alert

                }
            });
        } else {
            linearLayout.setBackground(context.getResources().getDrawable(R.drawable.custom_ll_item, context.getTheme()));
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!helperDatabase.stocksOk(helperItem)){
                        //Start alert
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Go Back to Category");
                        builder.setMessage( "Available stocks was changed, go back to Category." );
                        builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ((Activity)context).finish();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        //End alert
                    } else {
                        openItemAddActivity(helperItem, imageView, itemNameTextView, iconTextView);
                    }

                }
            });
        }
        */

        return linearLayout;
    }

    public CardView createCardView(){
        CardView cardView = new CardView(context);
        cardView.setCardElevation(dp6);
        cardView.setRadius(dp12);
        cardView.setPadding(dp6,dp6,dp6,dp6);
        GridLayout.LayoutParams parem = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
        parem.setMargins(dp12,dp12,dp12,dp12);
        cardView.setLayoutParams(parem);
        cardView.setBackgroundColor(context.getResources().getColor(R.color.transFull));
        return cardView;
    }

    public ImageView createIconImageView(final HelperItem helperItem, final TextView itemNameTextView, final TextView iconTextView){
        //ImageView
        final ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(lp80dp);
        if (helperItem.getItem_image() != null){
            if (!helperItem.getItem_image().equals("0")) {
                imageView.setImageResource(Integer.parseInt(helperItem.getItem_image()));
                imageView.setTag(Integer.parseInt(helperItem.getItem_image()));

            } else {
                if (!helperItem.getItem_name().equals("")){
                    helperItem.setItem_image("" + R.drawable.ic_no_icon);
                    imageView.setImageResource(R.drawable.ic_no_icon);
                    imageView.setTag(R.drawable.ic_no_icon);
                } else {
                    helperItem.setItem_image("" + R.drawable.ic_add);
                    imageView.setImageResource(R.drawable.ic_add);
                    imageView.setTag(R.drawable.ic_add);
                }
            }
        } else {
            if (helperItem.getItem_name() != null){
                helperItem.setItem_image("" + R.drawable.ic_no_icon);
                imageView.setImageResource(R.drawable.ic_no_icon);
                imageView.setTag(R.drawable.ic_no_icon);
            } else {
                helperItem.setItem_image("" + R.drawable.ic_add);
                imageView.setImageResource(R.drawable.ic_add);
                imageView.setTag(R.drawable.ic_add);
            }
        }

        /*

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemAddActivity(helperItem, imageView, itemNameTextView, iconTextView);
            }
        });
        */

        ///if(!helperDatabase.stocksOk(helperItem)){
        if(helperDatabase.stocksAvailable(helperItem)<=0){
            imageView.setImageResource(R.drawable.ic_out_of_stock);
        }

        return imageView;
    }

    public void openItemAddActivity(HelperItem helperItem, ImageView imageView, TextView textView, TextView ntextView) {
        if (helperItem.getItem_image() != null){
            if (helperItem.getItem_image().equals("" + R.drawable.ic_add)) {
                helperItem.setItem_image("0");
            }
        }

        //Intent intent = new Intent(this, ItemAddActivity.class);
        //intent.putExtra("cat_id", cat_id);
        //intent.putExtra("helperItem", helperItem);
        //startActivityForResult(intent, 1);


        gImageView = imageView;
        gTextView = textView;
        gnTextView = ntextView;

        if (helperDatabase.variantsExists(helperItem.getItem_id())){
            //Check item, if none, put at least 1. start
            /*
            HelperSales helperSale = new HelperSales();
            helperSale.setItem_id(helperItem.getItem_id());
            helperSale.setMachine_name("pos1");
            helperSale.setCreated_by("admin");
            if (!helperDatabase.itemExists(helperSale)){
                layoutSummary.insertSale(helperItem);
            }
            */
            //Check item, if none, put at least 1. end
            openVariantsMenuActivity(helperItem);
        } else {
            //layoutSummary.insertSaleNoVariant(helperItem);

            HelperSales helperSale = new HelperSales();
            helperSale.setItem_id(helperItem.getItem_id());
            helperSale.setMachine_name("pos1");
            helperSale.setItem_name(helperItem.getItem_name());
            helperSale.setQty("1");
            helperSale.setSelling_price(helperItem.getItem_selling_price());

            String sDateOnly = all_date;
            if(sDateOnly==null){
                Date currentTime = Calendar.getInstance().getTime();
                sDateOnly = DateFormat.getDateInstance().format(currentTime);
            }

            helperSale.setDate(sDateOnly);
            helperSale.setCreated_by("admin");
            helperSale.setCompleted("W");
            helperSale.setTransaction_counter(helperDatabase.nextTransactionCounter());
            helperSale.setTransaction_per_entry(helperDatabase.nextTransactionPerEntry());
            helperSale.setSort_order_id(helperDatabase.getSortOrderIdItem(helperSale));
            layoutSummary.insertHelperSale(helperSale);
            startCardViews();
        }
    }

    public void setGridColumn(){
        switch(screen_size){
            case "phone":
                switch(listItems.size()){
                    case 0:
                        gGrid.setColumnCount(1);
                        break;
                    case 1:
                        gGrid.setColumnCount(1);
                        break;
                    default:
                        gGrid.setColumnCount(2);
                        break;
                }
                break;
            default:
                switch(listItems.size()) {
                    case 0:
                        gGrid.setColumnCount(1);
                        break;
                    case 1:
                        gGrid.setColumnCount(1);
                        break;
                    case 2:
                        gGrid.setColumnCount(2);
                        break;
                    default:
                        gGrid.setColumnCount(3);
                        break;
                }
                break;
        }
    }

    public void openVariantsMenuActivity(HelperItem helperItem){
        Intent intent = new Intent(context, VariantsMenuActivity.class);
        intent.putExtra("helperItem", helperItem);
        intent.putExtra("all_date", all_date);
        ((Activity)context).startActivityForResult(intent, 1);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getScreen_size() {
        return screen_size;
    }

    public void setScreen_size(String screen_size) {
        this.screen_size = screen_size;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public GridLayout getgGrid() {
        return gGrid;
    }

    public void setgGrid(GridLayout gGrid) {
        this.gGrid = gGrid;
    }

    public LayoutSummary getLayoutSummary() {
        return layoutSummary;
    }

    public void setLayoutSummary(LayoutSummary layoutSummary) {
        this.layoutSummary = layoutSummary;
    }

    public String getAll_date() {
        return all_date;
    }

    public void setAll_date(String all_date) {
        this.all_date = all_date;
    }

    private void popMessage(String s){
        Toast.makeText(context, "" + s, Toast.LENGTH_LONG).show();
    }

}
