package com.watata.zpos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;

public class MainMenuActivity extends AppCompatActivity {

    String username, all_date;
    LinearLayout llstocks, llmenu, llsummary, llsetting, lldate;
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
        llsetting = findViewById(R.id.llsetting);
        lldate = findViewById(R.id.lldate);
        mainMenuText = findViewById(R.id.mainmenutext);
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
                    case 1:
                        mon = "Jan";
                        break;
                    case 2:
                        mon = "Feb";
                        break;
                    case 3:
                        mon = "Mar";
                        break;
                    case 4:
                        mon = "Apr";
                        break;
                    case 5:
                        mon = "May";
                        break;
                    case 6:
                        mon = "Jun";
                        break;
                    case 7:
                        mon = "Jul";
                        break;
                    case 8:
                        mon = "Aug";
                        break;
                    case 9:
                        mon = "Sep";
                        break;
                    case 10:
                        mon = "Oct";
                        break;
                    case 11:
                        mon = "Nov";
                        break;
                    case 12:
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

    public void openSettingActivity(){
        Intent intent = new Intent(this, SettingActivity.class);
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