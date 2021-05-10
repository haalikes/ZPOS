package com.watata.zpos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    float scale;
    String screen_size;
    int item_id;
    TextView gHeader;
    EditText geAmountPaid;
    Button gbExactAmount, gDone, gb1000, gb500, gb100, gb50, gb20, gb10, gb5, gb1, gbAddMinus, gbClearAmount;
    Boolean bAdd;
    private int final_cash, final_bill;

    LinearLayout llDetails, llPayment;
    TableLayout tblDetails, tblSummary, tblPayment;
    LayoutSummary layoutSummary;
    HelperDatabase helperDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        layoutSummary = new LayoutSummary(this);
        helperDatabase  = new HelperDatabase(this);
        bAdd = true;

        setupXmlIds();
        setupListeners();

        startLayoutDetails();
        startLayoutSummary();
        startLayoutPayment();


    }

    public void setupXmlIds(){
        screen_size = getResources().getString(R.string.screen_type);
        scale = this.getResources().getDisplayMetrics().density;
        gHeader = findViewById(R.id.header);

        llDetails = findViewById(R.id.lldetails);
        llPayment = findViewById(R.id.llpayment);

        tblDetails = findViewById(R.id.tbldetails);
        tblSummary = findViewById(R.id.tblsummary);
        tblPayment = findViewById(R.id.tblpayment);

        geAmountPaid = findViewById(R.id.amountpaid);
        ///geAmountPaid.setText("0");
        gbExactAmount = findViewById(R.id.exactamount);
        gDone = findViewById(R.id.done);
        gb1000 = findViewById(R.id.add1000);
        gb500 = findViewById(R.id.add500);
        gb100 = findViewById(R.id.add100);
        gb50 = findViewById(R.id.add50);
        gb20 = findViewById(R.id.add20);
        gb10 = findViewById(R.id.add10);
        gb5 = findViewById(R.id.add5);
        gb1 = findViewById(R.id.add1);
        gbAddMinus = findViewById(R.id.addminus);
        gbClearAmount = findViewById(R.id.clearamount);

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
        geAmountPaid.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                geAmountPaid.removeTextChangedListener(this);

                try {
                    String givenstring = s.toString();
                    Long longval;
                    if (givenstring.contains(",")) {
                        givenstring = givenstring.replaceAll(",", "");
                    }
                    longval = Long.parseLong(givenstring);
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String formattedString = formatter.format(longval);
                    geAmountPaid.setText(formattedString);
                    geAmountPaid.setSelection(geAmountPaid.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                geAmountPaid.addTextChangedListener(this);

            }
        });

        gbExactAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geAmountPaid.setText("" + layoutSummary.getTotal_bill());
            }
        });

        gDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(billOk(geAmountPaid.getText().toString())){

                        //Start alert
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Payment Done?");
                        builder.setMessage("Proceed to finish payment?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                HelperSales helperSales = new HelperSales();
                                helperSales.setCreated_by("admin");
                                helperSales.setMachine_name("pos1");
                                List<HelperSales> listHelperSales = new LinkedList<>();
                                listHelperSales.clear();
                                listHelperSales = helperDatabase.listPreFinishSale(helperSales);
                                for (int i = 0; i < listHelperSales.size(); i++){
                                    addSalesFB(listHelperSales.get(i));
                                }

                                List<HelperStockHistory> stockHistories = new LinkedList<>();
                                stockHistories.clear();
                                stockHistories = helperDatabase.listStockUsedInPreFinishSale(listHelperSales);
                                for (int i = 0; i < stockHistories.size(); i++){
                                    addStocksHistoryFB(stockHistories.get(i));
                                }

                                ChangesFB changesFB = new ChangesFB();
                                changesFB.ChangesStockHistories();

                                helperDatabase.finishSale(helperSales);

                                dialog.dismiss();
                                openPaidActivity();
                                finish();

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
                        //End alert


                    }
            }
        });

        gb1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geAmountPaid.setText("" + getAmountPaid(1000));
            }
        });

        gb500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geAmountPaid.setText("" + getAmountPaid(500));
            }
        });

        gb100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geAmountPaid.setText("" + getAmountPaid(100));
            }
        });

        gb50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geAmountPaid.setText("" + getAmountPaid(50));
            }
        });

        gb20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geAmountPaid.setText("" + getAmountPaid(20));
            }
        });

        gb10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geAmountPaid.setText("" + getAmountPaid(10));
            }
        });

        gb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geAmountPaid.setText("" + getAmountPaid(5));
            }
        });

        gb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geAmountPaid.setText("" + getAmountPaid(1));
            }
        });

        gbAddMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bAdd) {
                    bAdd = false;
                    gbAddMinus.setText("+");

                    gb1000.setText("-1800");
                    gb500.setText("-500");
                    gb100.setText("-100");
                    gb50.setText("-50");
                    gb20.setText("-20");
                    gb10.setText("-10");
                    gb5.setText("-5");
                    gb1.setText("-1");

                } else {
                    bAdd = true;
                    gbAddMinus.setText("-");

                    gb1000.setText("+1000");
                    gb500.setText("+500");
                    gb100.setText("+100");
                    gb50.setText("+50");
                    gb20.setText("+20");
                    gb10.setText("+10");
                    gb5.setText("+5");
                    gb1.setText("+1");

                }
            }
        });

        gbClearAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geAmountPaid.setText("");
            }
        });

    }

    public void addSalesFB(HelperSales helperSale){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("sales").push();
        reference.setValue(helperSale);
    }

    public void addStocksHistoryFB(HelperStockHistory helperStockHistory){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("stocks_history").push();
        reference.setValue(helperStockHistory);
    }

    public int getAmountPaid(int n){
        String customer_money;
        int money;

        if(geAmountPaid.getText().toString()==null || geAmountPaid.getText().toString().equals("")) customer_money = "0";
        else customer_money = geAmountPaid.getText().toString();
        if(bAdd) money = Integer.parseInt(customer_money) + n;
        else money = Integer.parseInt(customer_money) - n;


        return money;
    }

    public void startLayoutDetails(){
        layoutSummary.setContext(this);
        layoutSummary.setTableSales(tblDetails);
        layoutSummary.setScreen_size(screen_size);
        layoutSummary.setScale(scale);
        layoutSummary.setIn_payment(true);
        layoutSummary.setDisplay_dine_in_out(false);
        HelperSales helperSales = new HelperSales();
        helperSales.setCreated_by("admin");
        helperSales.setMachine_name("pos1");
        helperSales.setCompleted("N");
        layoutSummary.populateSales(helperSales);
    }

    public void startLayoutSummary(){
        layoutSummary.setContext(this);
        layoutSummary.setTableSales(tblSummary);
        layoutSummary.setScreen_size(screen_size);
        layoutSummary.setScale(scale);
        layoutSummary.setIn_payment(true);
        layoutSummary.setDisplay_dine_in_out(true);
        HelperSales helperSales = new HelperSales();
        helperSales.setCreated_by("admin");
        helperSales.setMachine_name("pos1");
        helperSales.setCompleted("N");
        layoutSummary.populateSummarySales();

    }

    public void startLayoutPayment(){
        
        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                20
        ));
        v.setBackgroundColor(Color.parseColor("#B3B3B3"));
        tblPayment.addView(v);
        TextView t = new TextView(this);
        t.setTextSize(23);
        t.setGravity(Gravity.RIGHT);

        String number = "" + layoutSummary.getTotal_bill();
        double amount = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###,###.00");

        t.setText("Total Php   " + formatter.format(amount));
        tblPayment.addView(t);
    }

    private boolean billOk(String customer_money){

        if(customer_money==null || customer_money.equals("")) customer_money = "0";

        if(Integer.parseInt(customer_money.replace(",", "")) >= layoutSummary.getTotal_bill()){
            final_cash = (Integer.parseInt(customer_money.replace(",", "")));
            final_bill = layoutSummary.getTotal_bill();
            return true;
        } else {
            viewCloseAlert("Insufficient funds", "The amount you entered is not enough for the bill." );
            return false;
        }

    }

    private void viewCloseAlert(String title, String message){
        //Start alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage( message );

        builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        //End alert
    }

    private void viewOptionAlert(String title, String message){
        //Start alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
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
        //End alert
    }

    public void openPaidActivity(){
        Intent intent = new Intent(this, PaidActivity.class);
        intent.putExtra("final_cash", final_cash);
        intent.putExtra("final_bill", final_bill);
        startActivity(intent);
    }

}