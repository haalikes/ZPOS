package com.watata.zpos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LayoutSummary {

    private Context context;
    private TableLayout tableSales;
    private List<HelperSales> listHelperSales = new LinkedList<HelperSales>();
    private String screen_size, all_date;
    private float scale;
    private final static int sale_sorter = 100;
    private boolean in_category;
    private boolean in_payment;
    private boolean display_dine_in_out;
    private int total_bill;
    private final int weightSum = 13;


    HelperDatabase helperDatabase;
    HelperSales helperSalesInit = new HelperSales();

    public LayoutSummary(Context context) {
        helperSalesInit.setCreated_by("admin");
        helperDatabase= new HelperDatabase(context);
        this.context = context;
        display_dine_in_out = true;


        //Maintenance wrong data in stock names and history
        if (!helperDatabase.maintainStockNames()){
            //Start alert
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Problem in tables");
            builder.setMessage( "Issues in names stock names and history" );

            builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            //End alert
        }

        //populateSales(helperSalesInit);
    }

    public void populateSales(){
        HelperSales helperSalesStart = new HelperSales();
        helperSalesStart.setCreated_by("admin");
        helperSalesStart.setMachine_name("pos1");
        populateSalesList(helperSalesStart);
        populateSalesTable();
    }

    public void populateSales(HelperSales helperSales){
        populateSalesList(helperSales);
        populateSalesTable();
    }

    public void populateSummarySales(){
        HelperSales helperSalesStart = new HelperSales();
        helperSalesStart.setCreated_by("admin");
        helperSalesStart.setMachine_name("pos1");
        populateSummarySalesList(helperSalesStart);
        populateSalesTable();
    }


    public void populateSalesList(HelperSales helperSales){
        listHelperSales.clear();
        listHelperSales = helperDatabase.listPreCompletedSales(helperSales);

    }

    public void populateSummarySalesList(HelperSales helperSales){
        listHelperSales.clear();
        //listHelperSales = helperDatabase.listSummaryPreCompletedSales(helperSales);
        listHelperSales = helperDatabase.listSummaryPreCompletedSalesNewLine(helperSales);
    }

    public void populateSummarySalesPaid(HelperSales helperSales){
        listHelperSales.clear();
        listHelperSales = helperDatabase.listSummaryPreCompletedSalesNewLine(helperSales);
    }

    public void populateSalesTable(){
        int total = 0;
        tableSales.removeAllViews();
        TableRow.LayoutParams param1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        TableRow.LayoutParams param2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        TableRow.LayoutParams param2wm = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 2);
        TableRow.LayoutParams param8 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 8);

        //Dine In / Take Out
        TableRow dineRow = new TableRow(context);
        dineRow.setWeightSum(weightSum);


        if(!in_payment) dineRow.addView(createTextViewTable(""), param1);
        dineRow.addView(createTextViewTable(""), param8);
        dineRow.addView(createTextViewTable(""), param2);
        dineRow.addView(createSwitch(), param2);
        if(display_dine_in_out) tableSales.addView(dineRow);

        // Header
        TableRow tableRowHdr = new TableRow(context);
        tableRowHdr.setWeightSum(weightSum);
        tableSales.addView(tableRowHdr);

            //CREATE
            TableRow.LayoutParams param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);

            if(!in_payment) tableRowHdr.addView(createTextViewTable("(-)", "header", true ), param1);

            tableRowHdr.addView(createTextViewTable("Item", "header", false ), param8);
            tableRowHdr.addView(createTextViewTable("Qty", "header", false ), param2);
            tableRowHdr.addView(createTextViewTable("Amount", "header", true ), param2);

        for (int row = listHelperSales.size() - 1; row >= 0 ; row--){
            TableRow tableRow = new TableRow(context);
            tableRow.setWeightSum(weightSum);
            tableSales.addView(tableRow);

            TextView tvItemName, tvQty, tvPrice;
            ImageView delImage;


            //CREATE
            if ((listHelperSales.get(row).getSort_order_id() % sale_sorter) == 0 ){
                tvItemName = createTextViewTable(listHelperSales.get(row).getItem_name(), "in", false );
                tvQty = createTextViewTable("" + listHelperSales.get(row).getQty(), "in", false );
                tvPrice = createTextViewTable("" + listHelperSales.get(row).getSelling_price() + ".00", "in", true );
            } else {
                tvItemName = createTextViewTable("-----" + listHelperSales.get(row).getItem_name(), "var", false );
                tvQty = createTextViewTable("" + listHelperSales.get(row).getQty(), "var", false );
                tvPrice = createTextViewTable("" + listHelperSales.get(row).getSelling_price() + ".00", "var", true );
            }
            if(!in_payment){
                if (listHelperSales.get(row).getVar_dtls_id()!=null){
                    delImage = delImageViewDisabled(listHelperSales.get(row));
                } else {
                    delImage = delImageView(listHelperSales.get(row));
                }
                tableRow.addView(delImage, param1);
            }


            //ADD VIEWS LOOP
            tvQty.setSingleLine();
            tvPrice.setSingleLine();
            tableRow.addView(tvItemName, param8);
            tableRow.addView(tvQty, param2wm);
            tableRow.addView(tvPrice, param2wm);

            total += Integer.parseInt(listHelperSales.get(row).getSelling_price());
        }

        //BOTTOM start

        View v = new View(context);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                20
        ));
        v.setBackgroundColor(Color.parseColor("#B3B3B3"));
        tableSales.addView(v);
        TextView t = new TextView(context);
        t.setTextSize(23);
        t.setGravity(Gravity.RIGHT);
        t.setText("Total Php   " + total + ".00");
        total_bill = total;
        tableSales.addView(t);

        TableRow tableRowHdrEnd = new TableRow(context);
        tableRowHdrEnd.setWeightSum(3);

        TextView t2 = new TextView(context);
        param0 = new TableRow.LayoutParams(10, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdrEnd.addView(t2, param0);

        if (!in_payment){
            if (listHelperSales.size() != 0){
                final Button b = new Button(context);

                if (listHelperSales.get(0).getCompleted().equals("W")){
                    b.setText("Add to Cart");
                } else {
                    b.setText("Check Out");
                }

                param0 = new TableRow.LayoutParams(10, TableRow.LayoutParams.WRAP_CONTENT, 1);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (b.getText().toString().equals("Add to Cart")){
                            helperDatabase.addToCart(listHelperSales.get(0));
                            populateSales(listHelperSales.get(0));

                            //((Activity)context).finish();
                        } else {
                            /*
                            List<HelperStockHistory> listStocksAddedToCart = helperDatabase.listStocksAddedToCart(listHelperSales.get(0));
                            for (int i = 0; i < listStocksAddedToCart.size(); i++){
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("stocks_history").push();
                                reference.setValue(listStocksAddedToCart.get(i));
                            }
                            */




                            ///helperDatabase.finishSale(listHelperSales.get(0));

                            Intent intent = new Intent(context, PaymentActivity.class);
                            if(context.getClass().getSimpleName().equals("ItemMenuActivity")){
                                ((Activity)context).finish();
                            }

                            //((Activity)context).startActivity(intent);

                            ((Activity)context).startActivityForResult(intent, 1);

                            //((Activity)context).finish();
                        }

                        //populateSales();
                    }
                });

                tableRowHdrEnd.addView(b, param0);

            }
        } else {

        }

        //BOTTOM end

        tableSales.addView(tableRowHdrEnd);

    }

    public void populateSalesTablePaid(){
        int total = 0;
        tableSales.removeAllViews();
        TableRow.LayoutParams param1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        TableRow.LayoutParams param2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        TableRow.LayoutParams param8 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 8);

        //Dine In / Take Out
        TableRow dineRow = new TableRow(context);
        dineRow.setWeightSum(weightSum);


        if(!in_payment) dineRow.addView(createTextViewTable(""), param1);
        dineRow.addView(createTextViewTable(""), param8);
        dineRow.addView(createTextViewTable(""), param2);
        dineRow.addView(createSwitch(), param2);
        if(display_dine_in_out) tableSales.addView(dineRow);

        // Header
        TableRow tableRowHdr = new TableRow(context);
        tableRowHdr.setWeightSum(weightSum);
        tableSales.addView(tableRowHdr);

        //CREATE
        TableRow.LayoutParams param0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);

        if(!in_payment) tableRowHdr.addView(createTextViewTable("(-)", "header", true ), param1);
        tableRowHdr.addView(createTextViewTable("Item", "header", false ), param8);
        tableRowHdr.addView(createTextViewTable("Qty", "header", false ), param2);
        tableRowHdr.addView(createTextViewTable("Amount", "header", true ), param2);

        for (int row = listHelperSales.size() - 1; row >= 0 ; row--){
            TableRow tableRow = new TableRow(context);
            tableRow.setWeightSum(weightSum);
            tableSales.addView(tableRow);

            TextView tvItemName, tvQty, tvPrice;
            ImageView delImage;


            //CREATE
            if ((listHelperSales.get(row).getSort_order_id() % sale_sorter) == 0 ){
                tvItemName = createTextViewTable(listHelperSales.get(row).getItem_name(), "in", false );
                tvQty = createTextViewTable("" + listHelperSales.get(row).getQty(), "in", false );
                tvPrice = createTextViewTable("" + listHelperSales.get(row).getSelling_price() + ".00", "in", true );
            } else {
                tvItemName = createTextViewTable("-----" + listHelperSales.get(row).getItem_name(), "var", false );
                tvQty = createTextViewTable("" + listHelperSales.get(row).getQty(), "var", false );
                tvPrice = createTextViewTable("" + listHelperSales.get(row).getSelling_price() + ".00", "var", true );
            }
            if(!in_payment){
                if (listHelperSales.get(row).getVar_dtls_id()!=null){
                    delImage = delImageViewDisabled(listHelperSales.get(row));
                } else {
                    delImage = delImageView(listHelperSales.get(row));
                }
                tableRow.addView(delImage, param1);
            }


            //ADD VIEWS LOOP
            tableRow.addView(tvItemName, param8);
            tableRow.addView(tvQty, param2);
            tableRow.addView(tvPrice, param2);

            total += Integer.parseInt(listHelperSales.get(row).getSelling_price());
        }

        //BOTTOM start

        View v = new View(context);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                20
        ));
        v.setBackgroundColor(Color.parseColor("#B3B3B3"));
        tableSales.addView(v);
        TextView t = new TextView(context);
        t.setTextSize(23);
        t.setGravity(Gravity.RIGHT);
        t.setText("Total Php   " + total + ".00");
        total_bill = total;
        tableSales.addView(t);

        TableRow tableRowHdrEnd = new TableRow(context);
        tableRowHdrEnd.setWeightSum(3);

        TextView t2 = new TextView(context);
        param0 = new TableRow.LayoutParams(10, TableRow.LayoutParams.WRAP_CONTENT, 2);
        tableRowHdrEnd.addView(t2, param0);

        if (!in_payment){
            if (listHelperSales.size() != 0){
                final Button b = new Button(context);

                if (listHelperSales.get(0).getCompleted().equals("W")){
                    b.setText("Add to Cart");
                } else {
                    b.setText("Check Out");
                }

                param0 = new TableRow.LayoutParams(10, TableRow.LayoutParams.WRAP_CONTENT, 1);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (b.getText().toString().equals("Add to Cart")){
                            helperDatabase.addToCart(listHelperSales.get(0));
                            populateSales(listHelperSales.get(0));

                            //((Activity)context).finish();
                        } else {
                            /*
                            List<HelperStockHistory> listStocksAddedToCart = helperDatabase.listStocksAddedToCart(listHelperSales.get(0));
                            for (int i = 0; i < listStocksAddedToCart.size(); i++){
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("stocks_history").push();
                                reference.setValue(listStocksAddedToCart.get(i));
                            }
                            */




                            ///helperDatabase.finishSale(listHelperSales.get(0));

                            Intent intent = new Intent(context, PaymentActivity.class);
                            if(context.getClass().getSimpleName().equals("ItemMenuActivity")){
                                ((Activity)context).finish();
                            }

                            //((Activity)context).startActivity(intent);

                            ((Activity)context).startActivityForResult(intent, 1);

                            //((Activity)context).finish();
                        }

                        //populateSales();
                    }
                });

                tableRowHdrEnd.addView(b, param0);

            }
        } else {

        }

        //BOTTOM end

        tableSales.addView(tableRowHdrEnd);

    }


    private TextView createTextViewTable(String sText, String sRowType, boolean right){
        TextView textView = new TextView(context);
        textView.setText(sText);
        //textView.setLines(1);
        textView.setPadding(10, 10, 30, 10);
        if (right){
            textView.setGravity(Gravity.RIGHT);
        }

        if (sRowType == "header") {
            textView.setBackgroundResource(R.drawable.custom_textview_hdr);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
        } else {
            if (sRowType == "out") {
                textView.setBackgroundResource(R.drawable.custom_text_out);
            } else {
                if (sRowType == "var"){
                    textView.setBackgroundResource(R.drawable.custom_text_var);
                } else {
                    textView.setBackgroundResource(R.drawable.custom_text_in);

                    //textView.setBackgroundResource(R.color.transFull);
                }
            }
        }

        textView.setTextSize(textSize());

        return(textView);
    }

    private TextView createTextViewTable(String sText){
        TextView textView = new TextView(context);
        textView.setText(sText);
        textView.setPadding(30, 10, 30, 10);
        textView.setBackgroundResource(R.drawable.custom_text_in_white);
        textView.setTextSize(textSize());
        return(textView);
    }

    private SwitchCompat createSwitch(){
        final SwitchCompat newSwitch = new SwitchCompat(context);
        //Switch newSwitch = new Switch(context);

        helperDatabase.dineExists();
        if (helperDatabase.dineIn()){
            newSwitch.setChecked(false);
        } else {
            newSwitch.setChecked(true);
        }


        newSwitch.setTextOn("Take-Out");
        newSwitch.setTextOff("Dine-In");
        newSwitch.setShowText(true);
        newSwitch.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        newSwitch.setThumbResource(R.drawable.dine_in_out);
        newSwitch.setTrackResource(R.drawable.dine_in_out_track);

        newSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newSwitch.isChecked()){
                    helperDatabase.updateDineInOut("N");
                } else {
                    helperDatabase.updateDineInOut("Y");
                }
            }
        });

        return newSwitch;
    }

    public ImageView delImageView(final HelperSales helperSale){
        int dp35 = (int) (35 * scale + 0.5f);
        LinearLayout.LayoutParams lp35dp = new LinearLayout.LayoutParams( dp35, dp35);

        //imageView delete
        final ImageView delImmage = new ImageView(context);
        lp35dp.weight = 1.0f;
        lp35dp.gravity = Gravity.RIGHT;
        delImmage.setLayoutParams(lp35dp);
        delImmage.setImageResource(R.drawable.ic_delete);
        delImmage.setBackground(context.getResources().getDrawable(R.drawable.custom_i_delete));

        delImmage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                helperDatabase.deleteSale(helperSale);

                //delete 1 each per variant
                List<HelperSales> helperSalesVariants = new LinkedList<HelperSales>();
                helperSalesVariants.clear();
                helperSalesVariants = helperDatabase.listVariantsInItem(helperSale);
                for (int i = 0; i < helperSalesVariants.size(); i++){
                    helperDatabase.deleteSale(helperSalesVariants.get(i));
                }



                if (in_category){
                    populateSummarySales();
                } else {
                    populateSales(helperSale);
                }


            }
        });

        return delImmage;
    }

    public ImageView delImageViewDisabled(final HelperSales helperSale){
        int dp35 = (int) (35 * scale + 0.5f);
        LinearLayout.LayoutParams lp35dp = new LinearLayout.LayoutParams( dp35, dp35);

        //imageView delete
        ImageView delImmage = new ImageView(context);
        lp35dp.weight = 1.0f;
        lp35dp.gravity = Gravity.RIGHT;
        delImmage.setLayoutParams(lp35dp);
        delImmage.setImageResource(R.drawable.ic_variants);

        return delImmage;
    }

    public void insertHelperSale(HelperSales helperSale){

        /*

        helperSale.setTransaction_counter(helperDatabase.nextTransactionCounter()); //max where completed = 'Y'
        helperSale.setTransaction_per_entry(helperDatabase.nextTransactionPerEntry());
        if (helperDatabase.variantExists(helperSale)){
            helperSale.setSort_order_id(helperDatabase.getSortOrderIdVariant(helperSale));
        } else {
            helperSale.setSort_order_id(helperDatabase.nextSortOrderIdVariant(helperSale));
        }
        */



        String sDateOnly = all_date;
        if(sDateOnly==null){
            Date currentTime = Calendar.getInstance().getTime();
            sDateOnly = DateFormat.getDateInstance().format(currentTime);
        }

        helperSale.setDate(sDateOnly);

        //testing start
        ///addSalesFB(helperSale);
        //testing end


        helperDatabase.insertSaleNew(helperSale);

        populateSales(helperSale);
    }

    public void addSalesFB(HelperSales helperSale){
        helperSale.setTransaction_id(helperDatabase.maxTransactionId());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("sales").push();
        reference.setValue(helperSale);
    }

    public int textSize(){
        if (screen_size == "phone") {
            return 12;
        } else {
            return 16;
        }
    }

    private void popMessage(String s){
        Toast.makeText(context, "" + s, Toast.LENGTH_LONG).show();
    }

    public TableLayout getTableSales() {
        return tableSales;
    }

    public void setTableSales(TableLayout tableSales) {
        this.tableSales = tableSales;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getScreen_size() {
        return screen_size;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setScreen_size(String screen_size) {
        this.screen_size = screen_size;
    }

    public boolean isIn_category() {
        return in_category;
    }

    public void setIn_category(boolean in_category) {
        this.in_category = in_category;
    }

    public boolean isIn_payment() {
        return in_payment;
    }

    public void setIn_payment(boolean in_payment) {
        this.in_payment = in_payment;
    }

    public int getTotal_bill() {
        return total_bill;
    }

    public void setTotal_bill(int total_bill) {
        this.total_bill = total_bill;
    }

    public boolean isDisplay_dine_in_out() {
        return display_dine_in_out;
    }

    public void setDisplay_dine_in_out(boolean display_dine_in_out) {
        this.display_dine_in_out = display_dine_in_out;
    }

    public String getAll_date() {
        return all_date;
    }

    public void setAll_date(String all_date) {
        this.all_date = all_date;
    }
}
