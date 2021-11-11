package com.watata.zpos.display;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.watata.zpos.R;
import com.watata.zpos.ddlclass.HelperStockNamesSorter;

public class DisplayObjects {
    public DisplayObjects() {
    }

    public TextView createTextViewTable(Context context, String sText){
        TextView textView = new TextView(context);
        textView.setText(sText);
        textView.setPadding(30, 10, 30, 10);
        textView.setBackgroundResource(R.drawable.custom_text_in);
        textView.setTextSize(18);
        return(textView);
    }

    public TextView createTextViewTable(Context context, String sText, String sRowType){
        TextView textView = new TextView(context);
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



}
