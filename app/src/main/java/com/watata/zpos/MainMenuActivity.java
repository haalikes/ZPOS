package com.watata.zpos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class MainMenuActivity extends AppCompatActivity {

    String username, all_date;
    LinearLayout llstocks, llmenu, llsummary, llsetting, lldate, llsalesnstocks, llfpdtls, llfpgraph, llfptable, llprocessexportfile;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    TextView mainMenuText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        username = getIntent().getStringExtra("username");
        setupXmlIds();
        setupListeners();

    }

    public void setupXmlIds(){
        llstocks = findViewById(R.id.llstocks);
        llmenu = findViewById(R.id.llmenu);
        llsummary = findViewById(R.id.llsummary);
        llsalesnstocks = findViewById(R.id.llSalesNStocks);
        llsetting = findViewById(R.id.llsetting);
        lldate = findViewById(R.id.lldate);
        mainMenuText = findViewById(R.id.mainmenutext);
        llfpdtls = findViewById(R.id.llfpdtls);
        llfpgraph = findViewById(R.id.llfpgraph);
        llfptable = findViewById(R.id.llfptable);
        llprocessexportfile = findViewById(R.id.llprocessexportfile);
    }

    public void setupListeners(){

        llstocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStocksActivity();
            }
        });

        llmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryMenuActivity();
            }
        });

        llsummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSalesDayActivity();
            }
        });

        llsalesnstocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSalesNStocksActivity();
            }
        });

        llsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.equals("admen") || username.equals("admin")) {
                    openSettingActivity();
                }
            }
        });

                        /*
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

                alert.setTitle("Title");
                alert.setMessage("Message");

                // Set an EditText view to get user input
                final EditText input = new EditText(view.getContext());
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        all_date = input.getText().toString();
                        // Do something with value!
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
                */

        lldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int mm = cal.get(Calendar.MONTH);
                int dd = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainMenuActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, mm, dd);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yyyy, int mm, int d) {
                String mon, dd;
                switch(mm){
                    case 0:
                        mon = "Jan";
                        break;
                    case 1:
                        mon = "Feb";
                        break;
                    case 2:
                        mon = "Mar";
                        break;
                    case 3:
                        mon = "Apr";
                        break;
                    case 4:
                        mon = "May";
                        break;
                    case 5:
                        mon = "Jun";
                        break;
                    case 6:
                        mon = "Jul";
                        break;
                    case 7:
                        mon = "Aug";
                        break;
                    case 8:
                        mon = "Sep";
                        break;
                    case 9:
                        mon = "Oct";
                        break;
                    case 10:
                        mon = "Nov";
                        break;
                    case 11:
                        mon = "Dec";
                        break;
                    default:
                        mon = "Jan";
                        break;
                }
                if(d<=9){
                    dd ="0" + d;
                } else {
                    dd = "" + d;
                }

                all_date = mon + " " + dd + ", " + yyyy;
                mainMenuText.setText("Main Menu " + all_date);
            }
        };

        llfpdtls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFPDtlsActivity();
            }
        });

        llfpgraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFPGraphActivity();
            }
        });

        llfptable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFPTableActivity();
            }
        });

        llprocessexportfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProcessExportFileActivity();
            }
        });
    }

    public void openStocksActivity(){
        Intent intent = new Intent(this, StocksActivity.class);
        intent.putExtra("all_date", all_date);
        //intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    public void openCategoryMenuActivity(){
        Intent intent = new Intent(this, CategoryMenuActivity.class);
        intent.putExtra("all_date", all_date);
        //intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    public void openSalesDayActivity(){
        Intent intent = new Intent(this, SalesDayActivity.class);
        //intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    public void openSalesNStocksActivity(){
        Intent intent = new Intent(this, SalesAndStocksActivity.class);
        //intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    public void openSettingActivity(){
        Intent intent = new Intent(this, SettingActivity.class);
        //intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    public void openFPDtlsActivity(){
        Intent intent = new Intent(this, FPDtlsActivity.class);
        intent.putExtra("all_date", all_date);
        //intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    public void openFPGraphActivity(){
        Intent intent = new Intent(this, EODGraphFPActivity.class);
        intent.putExtra("all_date", all_date);
        //intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    public void openFPTableActivity(){
        Intent intent = new Intent(this, FPDtlsTableActivity.class);
        intent.putExtra("all_date", all_date);
        //intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    public void openProcessExportFileActivity(){
        Intent intent = new Intent(this, ProcessExportFileActivity.class);
        intent.putExtra("all_date", all_date);
        //intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    /*
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.llstocks:
            case R.id.ivstocks:
            case R.id.tvstocks:
                //openStocksActivity();
                break;
            default:
                break;
        }
    }
    */
}