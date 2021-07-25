package com.watata.zpos;

import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.core.content.res.ResourcesCompat;

public class LayoutVariantsDtls {

    //getter setter
    private Context context;
    private float scale;
    private String screen_size, all_date;
    private int item_id;
    private String item_image, item_name, selling_price;
    private LinearLayout linearLayout;
    private LayoutSummary layoutSummary;
    private HelperItem helperItemMenu;

    private int dp80, dp1, dp6, dp12, dp16, dp20, dp25, dp35;
    private LinearLayout.LayoutParams lp80dp;
    private LinearLayout.LayoutParams lp35dp;
    private HelperDatabase helperDatabase;
    private HelperSales helperItemToHelperSaleItem;
    private List<HelperVariantsLinks> listHelperVarLinks = new LinkedList<HelperVariantsLinks>();
    private List<HelperVariantsHdr> listHelperVarHdr = new LinkedList<HelperVariantsHdr>();
    private List<HelperVariantsDtls> listHelperVarDtls = new LinkedList<HelperVariantsDtls>();
    private List<HelperVariantsDtls> listHelperVarDtlsAll = new LinkedList<HelperVariantsDtls>();
    private List<HelperSales> listHelperSalesCurrentVar = new LinkedList<HelperSales>();
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
    //private GRadioGroup gRadioGroup;
    private boolean clickedVariantButn;

    public LayoutVariantsDtls() {
    }


    public void startLayoutVariantsDtls(){
        initializeVariables();
        populateList();
        populateData();
        autoInsertItem();
    }

    private void autoInsertItem() {
        //helperItemToHelperSaleItem already initialized
        if (!helperDatabase.itemExists(helperItemToHelperSaleItem)){

            /* old

            boolean stocksOk = true;

            if(!helperDatabase.stocksOk(helperItemToHelperSaleItem)){
                stocksOk = false;
            }

            if (stocksOk){
                listHelperSalesCurrentVar.clear();
                listHelperSalesCurrentVar = helperDatabase.listHelperSalesDefaultVariants(helperItemToHelperSaleItem);

                for (int i = 0; i < listHelperSalesCurrentVar.size(); i++){
                    if (!helperDatabase.stocksOk(listHelperSalesCurrentVar.get(i))){
                         stocksOk = false;
                         break;
                    }
                }
            }


            if (stocksOk){
                layoutSummary.insertHelperSale(helperItemToHelperSaleItem);
                insertDefaultVariantsDetails(helperItemToHelperSaleItem);
            }

            */ //old

            boolean stocksOk = false;

            if (helperDatabase.stocksAvailable(helperItemMenu)>0){
                listHelperSalesCurrentVar.clear();
                listHelperSalesCurrentVar = helperDatabase.listHelperSalesDefaultVariants(helperItemToHelperSaleItem);

                for (int i = 0; i < listHelperSalesCurrentVar.size(); i++){
                    stocksOk = true;
                    if (helperDatabase.stocksAvailable(listHelperSalesCurrentVar.get(i))<=0){
                        stocksOk = false;
                        break;
                    }
                }
            }


            if (stocksOk){
                layoutSummary.insertHelperSale(helperItemToHelperSaleItem);
                insertDefaultVariantsDetails(helperItemToHelperSaleItem);
            }

        }
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

        clickedVariantButn = false;
        listHelperSalesCurrentVar.clear();

        HelperSales helperSale = new HelperSales();
        helperSale.setItem_id(item_id);
        helperSale.setMachine_name("pos1");
        helperSale.setItem_name(item_name);
        helperSale.setQty("1");
        helperSale.setSelling_price(selling_price);

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
        helperSale.setSort_order_id(helperDatabase.nextSortOrderIdNoItem(helperSale));
        helperItemToHelperSaleItem = helperSale;
    }

    public void populateList(){
        listHelperVarLinks.clear();
        listHelperVarLinks = helperDatabase.listVariantsLinks(item_id);

        listHelperVarHdr.clear();
        listHelperVarHdr = helperDatabase.listVariantsHdr(listHelperVarLinks);

        listHelperVarDtlsAll.clear();
        listHelperVarDtlsAll = helperDatabase.listVariantsDtls(listHelperVarLinks);
    }


    public void populateData(){
        TextView textViewHdr;
        HorizontalScrollView hView;
        GridLayout grid;

        linearLayout.removeAllViews();

        //ITEM start
        HelperVariantsHdr helperVarHdr = new HelperVariantsHdr();
        helperVarHdr.setVar_hdr_name(item_name);
        textViewHdr = createTextViewItemAddName(helperVarHdr);
        hView = createHView();
        grid = createGrid(1);
            //ITEM cardview start
            HelperItem helperItem = new HelperItem();
            helperItem.setItem_id(item_id);
            helperItem.setItem_name(item_name);
            helperItem.setItem_image(item_image);
            helperItem.setItem_selling_price(selling_price);
            grid.addView(completeCardView(helperItem));
            hView.addView(grid);
            //ITEM cardview end
        linearLayout.addView(textViewHdr);
        linearLayout.addView(hView);
        //ITEM end

        //VARIANTS_HDR start
        for (int i = 0; i < listHelperVarHdr.size(); i++){
            textViewHdr = createTextViewVariantName(listHelperVarHdr.get(i));
            hView = createHView();

            //Radio
            GRadioGroup hdrRadioGroup = new GRadioGroup();

            //var details
            listHelperVarDtls.clear();
            listHelperVarDtls = helperDatabase.listVariantsDtls(listHelperVarHdr.get(i).getVar_hdr_id());

            grid = createGrid(listHelperVarDtls.size());

            ///
            ///

            for (int j = 0; j < listHelperVarDtls.size(); j++){

                Log.d("getVar_dtls_default", "" + j + listHelperVarDtls.get(j).getVar_dtls_default());

                HelperItem helperItemVar = new HelperItem();
                helperItemVar.setItem_id(listHelperVarDtls.get(j).getVar_dtls_id()); //set for var_dtls only
                helperItemVar.setItem_name(listHelperVarDtls.get(j).getVar_dtls_name());
                helperItemVar.setItem_image(listHelperVarDtls.get(j).getVar_dtls_image());
                helperItemVar.setItem_selling_price(listHelperVarDtls.get(j).getVar_selling_price());
                grid.addView(completeCardView(helperItemVar, hdrRadioGroup, listHelperVarHdr.get(i).getVar_hdr_id(), listHelperVarDtls.get(j).getVar_dtls_default()));
            }
            hView.addView(grid);
            linearLayout.addView(textViewHdr);
            linearLayout.addView(hView);
        }
        //VARIANTS_HDR end
    }

    public TextView createTextViewItemAddName(HelperVariantsHdr helperVariantsHdr){
        TextView textView = new TextView(context);
        textView.setLayoutParams(lpWrapWrap);

        Typeface typeface = ResourcesCompat.getFont(context, R.font.wendy_one);
        textView.setTypeface(typeface);
        textView.setTextColor(Color.parseColor("#2E2B2B"));
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        textView.setTextSize(24);
        if (helperVariantsHdr.getVar_hdr_name() != null){
            textView.setText("Click " + helperVariantsHdr.getVar_hdr_name() + " to add 1.");
        }
        return textView;
    }

    public TextView createTextViewVariantName(HelperVariantsHdr helperVariantsHdr){
        TextView textView = new TextView(context);
        textView.setLayoutParams(lpWrapWrap);

        Typeface typeface = ResourcesCompat.getFont(context, R.font.wendy_one);
        textView.setTypeface(typeface);
        textView.setTextColor(Color.parseColor("#2E2B2B"));
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        textView.setTextSize(24);
        if (helperVariantsHdr.getVar_hdr_name() != null){
            textView.setText(helperVariantsHdr.getVar_hdr_name());
        }
        return textView;
    }

    public HorizontalScrollView createHView(){
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        horizontalScrollView.setLayoutParams(lpWrapWrap);
        //horizontalScrollView.setBackgroundColor(Color.RED);
        return horizontalScrollView;
    }

    public GridLayout createGrid(int var_dtls_count){
        GridLayout gridLayout = new GridLayout(context);
        gridLayout.setLayoutParams(lpWrapWrap);
        gridLayout.setPadding(dp20, dp20, dp20, dp20);

        if(var_dtls_count>15){
            gridLayout.setRowCount(4);
            gridLayout.setColumnCount(var_dtls_count/4);
        } else {
            if(var_dtls_count>10){
                gridLayout.setRowCount(3);
                gridLayout.setColumnCount(var_dtls_count/3);
            } else {
                if(var_dtls_count>5){
                    gridLayout.setRowCount(2);
                    gridLayout.setColumnCount(var_dtls_count/2);
                } else {
                    gridLayout.setRowCount(1);
                    gridLayout.setColumnCount(var_dtls_count);
                }
            }
        }


        gridLayout.setAlignmentMode(GridLayout.ALIGN_MARGINS);
        return gridLayout;
    }

    //CARDVIEW w/o RB start
    public CardView completeCardView(HelperItem helperItem){
        TextView itextView, itemNameTextView, iPrice;
        RelativeLayout relativeLayout;
        LinearLayout linearLayout;
        CardView cardView;

        itextView = createIconTextView(helperItem);
        itemNameTextView = createItemNameTextView(helperItem);
        iPrice = createPriceTextViewPhp(helperItem);
        relativeLayout = createRelativeLayout();
        linearLayout = createLinearLayout(helperItem);
        cardView = createCardView();

        //addViews
        relativeLayout.addView(itextView);
        relativeLayout.addView(createIconImageView(helperItem));

        linearLayout.addView(relativeLayout);
        linearLayout.addView(itemNameTextView);
        linearLayout.addView(iPrice);
        cardView.addView(linearLayout);

        return cardView;
    }

    public ImageView createIconImageView(final HelperItem helperItem){
        //ImageView
        final ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(lp80dp);
        if (helperItem.getItem_image() != null){
            if (!helperItem.getItem_image().equals("0")) {
                imageView.setImageResource(getImageId(getContext(), helperItem.getItem_image()));
                imageView.setTag(helperItem.getItem_image());
            } else {
                if (!helperItem.getItem_name().equals("")){
                    helperItem.setItem_image("0");
                    imageView.setImageResource(R.drawable.ic_no_icon);
                    imageView.setTag("0");
                } else {
                    helperItem.setItem_image("ic_add");
                    imageView.setImageResource(R.drawable.ic_add);
                    imageView.setTag("ic_add");
                }
            }
        } else {
            if (helperItem.getItem_name() != null){
                helperItem.setItem_image("0");
                imageView.setImageResource(R.drawable.ic_no_icon);
                imageView.setTag("0");
            } else {
                helperItem.setItem_image("ic_add");
                imageView.setImageResource(R.drawable.ic_add);
                imageView.setTag("ic_add");
            }
        }

        ///if(!helperDatabase.stocksOk(helperItem)){
        if(helperDatabase.stocksAvailable(helperItem)<=0){
            imageView.setImageResource(R.drawable.ic_out_of_stock);
        }

        return imageView;
    }

    public void insertDefaultVariantsDetails(HelperSales helperSale){

        //for loop of Sale variants
        for(int i = 0; i < listHelperSalesCurrentVar.size(); i++){

            listHelperSalesCurrentVar.get(i).setTransaction_counter(helperDatabase.nextTransactionCounter());
            listHelperSalesCurrentVar.get(i).setTransaction_per_entry(helperDatabase.nextTransactionPerEntry());
            if (helperDatabase.variantExists(listHelperSalesCurrentVar.get(i))){
                listHelperSalesCurrentVar.get(i).setSort_order_id(helperDatabase.getSortOrderIdVariant(listHelperSalesCurrentVar.get(i)));
            } else {
                listHelperSalesCurrentVar.get(i).setSort_order_id(helperDatabase.nextSortOrderIdVariant(listHelperSalesCurrentVar.get(i)));
            }

            layoutSummary.insertHelperSale(listHelperSalesCurrentVar.get(i));
        }
    }

    public void insertAllCurrentVariantsDetails(){

        //for loop of Sale variants
        for(int i = 0; i < listHelperSalesCurrentVar.size(); i++){

            listHelperSalesCurrentVar.get(i).setTransaction_counter(helperDatabase.nextTransactionCounter());
            listHelperSalesCurrentVar.get(i).setTransaction_per_entry(helperDatabase.nextTransactionPerEntry());
            if (helperDatabase.variantExists(listHelperSalesCurrentVar.get(i))){
                listHelperSalesCurrentVar.get(i).setSort_order_id(helperDatabase.getSortOrderIdVariant(listHelperSalesCurrentVar.get(i)));
            } else {
                listHelperSalesCurrentVar.get(i).setSort_order_id(helperDatabase.nextSortOrderIdVariant(listHelperSalesCurrentVar.get(i)));
            }

            layoutSummary.insertHelperSale(listHelperSalesCurrentVar.get(i));
        }
    }

    public void insertThisCurrentVariantsDetails(HelperSales helperSale){
        //this is multiple records of summation
        for (int i = 0; i < helperDatabase.sumItemsPerTranPerEntry(helperSale); i ++ ){
            helperSale.setQty("1");
            layoutSummary.insertHelperSale(helperSale);
        }
    }

    public void updateListCurrentVariantsDetails(HelperSales helperSalesCurrentVariant){
        List<HelperSales> listTemp = new LinkedList<>();
        listTemp.clear();

        //for loop of Sale variants
        for(int i = 0; i < listHelperSalesCurrentVar.size(); i++){
            if (listHelperSalesCurrentVar.get(i).getVar_hdr_id().equals(helperSalesCurrentVariant.getVar_hdr_id())){
                listTemp.add(helperSalesCurrentVariant);
            } else {
                listTemp.add(listHelperSalesCurrentVar.get(i));
            }
        }

        listHelperSalesCurrentVar.clear();
        listHelperSalesCurrentVar.addAll(listTemp);

    }

    //CARDVIEW w/o RB end

    //CARDVIEW with RB start
    public CardView completeCardView(HelperItem helperItemVar, GRadioGroup hdrRadioGroup, int var_hdr_id, String var_dtls_default){
        RadioButton radioButton;
        TextView itextView, itemNameTextView, iPrice;
        RelativeLayout relativeLayout;
        LinearLayout linearLayout;
        CardView cardView;

        radioButton = createRadioButton(helperItemVar);
        if (var_dtls_default != null){
            if (var_dtls_default.equals("Y")){
                Log.d("radioButton", "true");
                radioButton.setChecked(true);
            }
        }
        hdrRadioGroup.stackRadios(radioButton);
        itextView = createIconTextView(helperItemVar);
        itemNameTextView = createItemNameTextView(helperItemVar);
        iPrice = createPriceTextView(helperItemVar, var_hdr_id);
        relativeLayout = createRelativeLayout();
        linearLayout = createLinearLayout(helperItemVar, radioButton, hdrRadioGroup, var_hdr_id);
        cardView = createCardView();

        //addViews
        relativeLayout.addView(itextView);
        relativeLayout.addView(createIconImageView(helperItemVar, radioButton, hdrRadioGroup, var_hdr_id));

        linearLayout.addView(relativeLayout);
        linearLayout.addView(itemNameTextView);
        linearLayout.addView(iPrice);
        linearLayout.addView(radioButton);
        cardView.addView(linearLayout);

        return cardView;
    }

    public RadioButton createRadioButton(HelperItem helperItem){
        RadioButton radioButton = new RadioButton(context);
        radioButton.setLayoutParams(lpWrapWrap);
        radioButton.setClickable(false);

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {

                        Color.BLACK //disabled
                        ,Color.BLUE //enabled

                }
        );


        radioButton.setButtonTintList(colorStateList);

        return radioButton;
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

    public TextView createPriceTextView(HelperItem helperItem, int var_hdr_id){
        TextView textView = new TextView(context);
        textView.setLayoutParams(lpWrapWrap);
        //textView.setHint("Click to Add");
        textView.setTextSize(12);
        textView.setGravity(Gravity.CENTER);
        String availableOrders = helperDatabase.stocksAvailable(helperItem, "" + item_id, "" + var_hdr_id) + " order(s)";
        if (helperItem.getItem_selling_price() != null && !helperItem.getItem_selling_price().equals("0")){
            textView.setText("(+" + helperItem.getItem_selling_price() + ".00)\n" + availableOrders);
        } else {
            textView.setText(availableOrders);
        }

        glasttextView = textView;
        return textView;
    }

    public TextView createPriceTextViewPhp(HelperItem helperItem){
        TextView textView = new TextView(context);
        textView.setLayoutParams(lpWrapWrap);
        textView.setHint("Click to Add");
        textView.setTextSize(12);
        textView.setGravity(Gravity.CENTER);
        if (helperItem.getItem_selling_price() != null && !helperItem.getItem_selling_price().equals("0")){
            textView.setText("(Php " + helperItem.getItem_selling_price() + ".00)");
        } else {
            textView.setText(" ");
        }

        glasttextView = textView;
        return textView;
    }

    public RelativeLayout createRelativeLayout(){
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(lpWrapWrap);
        return relativeLayout;
    }



    public LinearLayout createLinearLayout(final HelperItem helperItem){
        //LinearLayout for Item only
        //LinearLayout
        LinearLayout linearLayout = new LinearLayout(context);
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
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean stocksOk = false;
                    String var_hdr_id_temp = "", var_dtls_id_temp = "", item_id_temp = "";

                    //verify onclick the item has stocks
                    if(helperDatabase.stocksAvailable(helperItem)>0){
                        stocksOk = true;
                    }

                    //verify current variants has stocks
                    if (stocksOk){
                        for (int i = 0; i < listHelperSalesCurrentVar.size(); i++){
                            ///if (!helperDatabase.stocksOk(listHelperSalesCurrentVar.get(i))){
                            if (helperDatabase.stocksAvailable(listHelperSalesCurrentVar.get(i))<=0){
                                var_hdr_id_temp = listHelperSalesCurrentVar.get(i).getVar_hdr_id();
                                var_dtls_id_temp = listHelperSalesCurrentVar.get(i).getVar_dtls_id();
                                stocksOk = false;
                                break;
                            }
                        }
                    }

                    if (stocksOk){
                        helperItemToHelperSaleItem.setTransaction_per_entry(helperDatabase.nextTransactionPerEntry());
                        layoutSummary.insertHelperSale(helperItemToHelperSaleItem);
                        insertAllCurrentVariantsDetails();
                    } else {


                        //List no stocks Start
                        List<String> listOutOfStockNames = new LinkedList<>();
                        listOutOfStockNames.clear();
                        //listOutOfStockNames = helperDatabase.listOutOfStocks(helperItem);


                        //This part is use to verify selected options are correct
                        if(var_hdr_id_temp!= null && !var_hdr_id_temp.equals("")){
                            HelperItem helperItemTemp = new HelperItem();
                            helperItemTemp.setItem_id(Integer.parseInt(var_dtls_id_temp));
                            listOutOfStockNames = helperDatabase.listOfStocks(helperItemTemp, "" + helperItem.getItem_id(), var_hdr_id_temp);
                        } else {
                            listOutOfStockNames = helperDatabase.listOfStocks(helperItem);
                        }

                        //Get list of out of stocks in item, if none, get list for variants
                        /*
                        listOutOfStockNames = helperDatabase.listOfStocks(helperItem);
                        if (listOutOfStockNames.size()==0){
                            HelperItem helperItemTemp = new HelperItem();
                            helperItemTemp.setItem_id(Integer.parseInt(var_dtls_id_temp));
                            listOutOfStockNames = helperDatabase.listOfStocks(helperItemTemp, "" + helperItem.getItem_id(), var_hdr_id_temp);
                        }
                        */


                        String stockNames = "";
                        for(int i = 0; i < listOutOfStockNames.size(); i++){
                            stockNames += " * " + listOutOfStockNames.get(i) + "\n";
                        }
                        //List no stocks End
                        //Start alert
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                        if((stockNames.equals(""))){
                            builder.setTitle("Out of stock - LVD01");
                            builder.setMessage( "Cannot add another order, please check option select or stocks available." );
                        } else {
                            builder.setTitle("Out of Stock - LVD02");
                            builder.setMessage(helperItem.getItem_name() + " is out of stocks for the ff:\n\n" + stockNames );
                        }

                        builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        //End alert
                    }


                }
            });

        }


        return linearLayout;
    }

    public LinearLayout createLinearLayout(final HelperItem helperItem, final RadioButton rb, final GRadioGroup hdrRadioGroup, final int var_hdr_id){
        //LinearLayout
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(lpMatchMatch);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(dp16, dp16, dp16, dp16);
        linearLayout.setClickable(true);

        ///if(!helperDatabase.stocksOk(helperItem, item_id, var_hdr_id)){
        if(helperDatabase.stocksAvailable(helperItem, "" + item_id, "" + var_hdr_id)<=0){
            linearLayout.setBackground(context.getResources().getDrawable(R.drawable.custom_ll_out_of_stock, context.getTheme()));
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> listOutOfStockNames = new LinkedList<>();
                    listOutOfStockNames.clear();
                    //listOutOfStockNames = helperDatabase.listOutOfStocks(helperItem, item_id, var_hdr_id);
                    listOutOfStockNames = helperDatabase.listOfStocks(helperItem, "" + item_id, "" + var_hdr_id);

                    String stockNames = "";
                    for(int i = 0; i < listOutOfStockNames.size(); i++){
                        stockNames += " - " + listOutOfStockNames.get(i) + " \n";
                    }
                    //Start alert
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    if(stockNames==null || stockNames.equals("")){
                        builder.setTitle("No Stocks Setup - CLL01");
                        builder.setMessage("Please setup stocks for variant " + helperItem.getItem_name() );
                    } else {
                        builder.setTitle("Out of Stock - LVD03");
                        builder.setMessage(helperItem.getItem_name() + " is out of stocks for the ff:\n\n" + stockNames );
                    }
                    builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    //End alert

                }
            });


        } else {
            linearLayout.setBackground(context.getResources().getDrawable(R.drawable.custom_ll_variants, context.getTheme()));
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (helperDatabase.stocksAvailableVarOnly(helperItem, "" + item_id, "" + var_hdr_id) <= 0 && !rb.isChecked() && helperDatabase.compositeRequired(var_hdr_id, helperItem.getItem_id() ) ) {
                        linearLayout.setBackground(context.getResources().getDrawable(R.drawable.custom_ll_out_of_stock, context.getTheme()));


                        List<String> listOutOfStockNames = new LinkedList<>();
                        listOutOfStockNames.clear();
                        //listOutOfStockNames = helperDatabase.listOutOfStocks(helperItem, item_id, var_hdr_id);
                        listOutOfStockNames = helperDatabase.listOfStocks(helperItem, "" + item_id, "" + var_hdr_id);

                        String stockNames = "";
                        for(int i = 0; i < listOutOfStockNames.size(); i++){
                            stockNames += " - " + listOutOfStockNames.get(i) + " \n";
                        }
                        //Start alert
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        if(stockNames==null || stockNames.equals("")){
                            builder.setTitle("No Stocks Setup - CLL01");
                            builder.setMessage("Please setup stocks for variant " + helperItem.getItem_name() );
                        } else {
                            builder.setTitle("Out of Stock - LVD04");
                            builder.setMessage(helperItem.getItem_name() + " is out of stocks for the ff:\n\n" + stockNames );
                        }
                        builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        //End alert

                    } else {
                        linearLayout.setBackground(context.getResources().getDrawable(R.drawable.custom_ll_variants, context.getTheme()));

                        HelperSales helperSaleVar = new HelperSales();
                        helperSaleVar.setItem_id(item_id);
                        helperSaleVar.setItem_name(helperItem.getItem_name());
                        helperSaleVar.setSelling_price(helperItem.getItem_selling_price());
                        helperSaleVar.setMachine_name("pos1");
                        helperSaleVar.setCreated_by("admin");
                        helperSaleVar.setCompleted("W");
                        helperSaleVar.setQty("1");
                        helperSaleVar.setVar_hdr_id("" + var_hdr_id);
                        helperSaleVar.setVar_dtls_id("" + helperItem.getItem_id());
                        helperSaleVar.setTransaction_counter(helperDatabase.nextTransactionCounter());
                        helperSaleVar.setTransaction_per_entry(helperDatabase.nextTransactionPerEntry());
                        if (helperDatabase.variantExists(helperSaleVar)) {
                            helperSaleVar.setSort_order_id(helperDatabase.getSortOrderIdVariant(helperSaleVar));
                        } else {
                            helperSaleVar.setSort_order_id(helperDatabase.nextSortOrderIdVariant(helperSaleVar));
                        }

                        updateListCurrentVariantsDetails(helperSaleVar);

                        if (helperDatabase.itemExists(helperSaleVar)) {
                            clickedVariantButn = true;
                            helperDatabase.deleteVariant(helperSaleVar);
                            insertThisCurrentVariantsDetails(helperSaleVar);
                            //layoutSummary.changeInsertVariant(helperSaleVar);
                            //layoutSummary.insertSaleVariant(helperItem, item_id, item_name, var_hdr_id);
                        }
                        /*
                        else {
                            popMessage("Please select item first before selecting variant");
                        }
                        */

                        hdrRadioGroup.enableRadio(rb);

                    }

                }
            });
        }

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

    public ImageView createIconImageView(final HelperItem helperItem, final RadioButton rb, final GRadioGroup hdrRadioGroup, final int var_hdr_id){
        //ImageView
        final ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(lp80dp);
        if (helperItem.getItem_image() != null){
            if (!helperItem.getItem_image().equals("0")) {
                imageView.setImageResource(getImageId(getContext(), helperItem.getItem_image()));
                imageView.setTag((helperItem.getItem_image()));

            } else {
                if (!helperItem.getItem_name().equals("")){
                    helperItem.setItem_image("0");
                    imageView.setImageResource(R.drawable.ic_no_icon);
                    imageView.setTag("0");
                } else {
                    helperItem.setItem_image("ic_add");
                    imageView.setImageResource(R.drawable.ic_add);
                    imageView.setTag("ic_add");
                }
            }
        } else {
            if (helperItem.getItem_name() != null){
                helperItem.setItem_image("0");
                imageView.setImageResource(R.drawable.ic_no_icon);
                imageView.setTag("0");
            } else {
                helperItem.setItem_image("ic_add");
                imageView.setImageResource(R.drawable.ic_add);
                imageView.setTag("ic_add");
            }
        }

        ///if(!helperDatabase.stocksOk(helperItem, item_id, var_hdr_id)){
        //Log.d("currentstocks", "current stock" + helperDatabase.stocksAvailable(helperItem, "" + item_id, "" + var_hdr_id));
        if(helperDatabase.stocksAvailable(helperItem, "" + item_id, "" + var_hdr_id)<=0){
            imageView.setImageResource(R.drawable.ic_out_of_stock);
        }

        return imageView;
    }
    //CARDVIEW with RB end

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    private void popMessage(String s){
        Toast.makeText(context, "" + s, Toast.LENGTH_LONG).show();
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

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public LayoutSummary getLayoutSummary() {
        return layoutSummary;
    }

    public void setLayoutSummary(LayoutSummary layoutSummary) {
        this.layoutSummary = layoutSummary;
    }

    public HelperItem getHelperItemMenu() {
        return helperItemMenu;
    }

    public void setHelperItemMenu(HelperItem helperItemMenu) {
        this.helperItemMenu = helperItemMenu;
    }

    public String getAll_date() {
        return all_date;
    }

    public void setAll_date(String all_date) {
        this.all_date = all_date;
    }
}
