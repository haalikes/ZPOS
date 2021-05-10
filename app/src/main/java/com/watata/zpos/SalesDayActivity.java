package com.watata.zpos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SalesDayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_day);

        BarChart barChart = findViewById(R.id.barChart);

        List<HelperSales> helperSales = new LinkedList<>();
        helperSales.clear();
        HelperDatabase helperDatabase = new HelperDatabase(this);
        helperSales = helperDatabase.listSummarySalesPerDay();

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.clear();
        ArrayList<String> dates = new ArrayList<>();
        dates.clear();

        for(int i = 0; i < helperSales.size(); i++){
            //Log.d("listSummarySalesPerDay4", helperSales.get(i).getSelling_price());
            //Log.d("listSummarySalesPerDay5", helperSales.get(i).getQty());
            //Log.d("listSummarySalesPerDay6", helperSales.get(i).getItem_name());
            entries.add(new BarEntry(Integer.parseInt(helperSales.get(i).getSelling_price()), Integer.parseInt(helperSales.get(i).getQty())));
            dates.add(helperSales.get(i).getItem_name());
            //only on this activity, because date is transform in helper, cannot use it
        }


        /*
        entries.add(new BarEntry(27, 0));
        entries.add(new BarEntry(135, 1));
        entries.add(new BarEntry(108, 2));

        dates.add("0301");
        dates.add("0302");
        dates.add("0304");
        */


        BarDataSet barDataSet = new BarDataSet(entries, "Sales");

        BarData mergeData = new BarData(dates, barDataSet);
        barChart.setData(mergeData);
        barChart.setTouchEnabled(true);



    }
}