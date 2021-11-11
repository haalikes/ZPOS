package com.watata.zpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.watata.zpos.ddlclass.HelperFPDtls;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class FPDtlsActivity extends AppCompatActivity {

    float scale;
    String all_date;
    TextView eDate;
    EditText eFpEndOfDayTotal, ePaymentAdvice;
    Button dateBtn, addBtn, backBtn;
    TableLayout tlFPDtls;
    ProgressBar progressBar;
    List<HelperFPDtls> listHelperFPDtls = new LinkedList<>();
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpdtls);

        initializeVariables();
        setupXmlIds();
        setupListeners();
        autoFPDtls();
    }

    public void initializeVariables() {
        scale = this.getResources().getDisplayMetrics().density;
    }

    public void setupXmlIds(){
        eDate = findViewById(R.id.tvdate);
        dateBtn = findViewById(R.id.btndate);
        addBtn = (Button) findViewById(R.id.add);
        backBtn = (Button) findViewById(R.id.back);
        eFpEndOfDayTotal = findViewById(R.id.fp_end_of_day_total);
        ePaymentAdvice = findViewById(R.id.fp_payment_advice);
        progressBar = findViewById(R.id.progressBar);
        tlFPDtls = findViewById(R.id.tblHistoryDtls);

    }

    public void setupListeners(){

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperFPDtls helperFPDtls;
                if (listHelperFPDtls.size() == 0){
                    helperFPDtls = new HelperFPDtls(
                            0
                            , all_date
                            , eFpEndOfDayTotal.getText().toString()
                            , ePaymentAdvice.getText().toString()
                    );
                } else {
                    helperFPDtls = new HelperFPDtls(
                            listHelperFPDtls.get(listHelperFPDtls.size()-1).getFp_id() + 1
                            , all_date
                            , eFpEndOfDayTotal.getText().toString()
                            , ePaymentAdvice.getText().toString()
                    );
                }


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("fp_dtls");
                reference.child("" + helperFPDtls.getFp_id()).setValue(helperFPDtls);

                ChangesFB changesFB = new ChangesFB();
                changesFB.ChangesFPDtls();

                popMessage(all_date + " added.");
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int mm = cal.get(Calendar.MONTH);
                int dd = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        FPDtlsActivity.this,
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
                eDate.setText("Date:" + all_date);
            }
        };


    }


    public void autoFPDtls(){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("fp_dtls");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listHelperFPDtls.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listHelperFPDtls.add(snapshot.getValue(HelperFPDtls.class));
                    }
                    populateFPDtls(listHelperFPDtls);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void populateFPDtls(final List<HelperFPDtls> listHelperFPDtls){
        tlFPDtls.removeAllViews();

        // Header
        TableRow tableRowHdr = new TableRow(this);
        tableRowHdr.setWeightSum(14);
        tlFPDtls.addView(tableRowHdr);
        //columns
        TableRow.LayoutParams param2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);
        TableRow.LayoutParams param4 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2);



        tableRowHdr.addView(createTextViewTable("Del?", "header" ), param2);
        tableRowHdr.addView(createTextViewTable("Date", "header" ), param4);
        tableRowHdr.addView(createTextViewTable("EOD Total", "header" ), param4);
        tableRowHdr.addView(createTextViewTable("PmtAdvice", "header" ), param4);

        for (int row = listHelperFPDtls.size() - 1; row >= 0 ; row--){
            TableRow tableRow = new TableRow(this);
            tableRow.setWeightSum(13);
            tlFPDtls.addView(tableRow);

            //columns

            ImageView delImage;
            delImage = delImageVIew(listHelperFPDtls.get(row));
            tableRow.addView(delImage, param2);
            tableRow.addView(createTextViewTable("" + listHelperFPDtls.get(row).getFp_date()), param4);
            tableRow.addView(createTextViewTable("" + listHelperFPDtls.get(row).getFp_end_of_day_total()), param4);
            tableRow.addView(createTextViewTable("" + listHelperFPDtls.get(row).getFp_payment_advice()), param4);

        }


    }


    public ImageView delImageVIew(final HelperFPDtls helperFPDtl){
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
                builder.setMessage("Delete " + helperFPDtl.getFp_id() + "?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFB(helperFPDtl);
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


    private TextView createTextViewTable(String sText, String sRowType){
        TextView textView = new TextView(this);
        textView.setText(sText);
        textView.setPadding(30, 10, 30, 10);

        if (sRowType == "header") {
            textView.setBackgroundResource(R.drawable.custom_textview_hdr);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
        } else {
            if (sRowType == "out") {
                textView.setBackgroundResource(R.drawable.custom_text_out);
            } else {
                textView.setBackgroundResource(R.drawable.custom_text_in);
            }
        }

        textView.setTextSize(18);
        return(textView);
    }

    private TextView createTextViewTable(String sText){
        TextView textView = new TextView(this);
        textView.setText(sText);
        textView.setPadding(30, 10, 30, 10);
        textView.setBackgroundResource(R.drawable.custom_text_in);



        textView.setTextSize(18);

        return(textView);
    }

    public void deleteFB(HelperFPDtls helperFPDtl){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("fp_dtls").orderByChild("fp_id").equalTo(helperFPDtl.getFp_id());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                ChangesFB changesFB = new ChangesFB();
                changesFB.ChangesFPDtls();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }


    private void popMessage(String s){
        Toast.makeText(this, "" + s, Toast.LENGTH_LONG).show();
    }

}