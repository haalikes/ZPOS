package com.watata.zpos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EODGraphFPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eodgraph_fpactivity);

        LineChart lineChart;
        HelperDatabase helperDatabase = new HelperDatabase(this);

        lineChart = findViewById(R.id.lineChart);

        List<HelperSales> helperSales = new LinkedList<>();
        helperSales.clear();
        helperSales = helperDatabase.listEODGraphFPWeekly();

        //List<HelperSales> helperStockSales = new LinkedList<>();
        //helperStockSales.clear();

        ArrayList<Entry> saleEntries = new ArrayList<>();
        saleEntries.clear();
        ArrayList<Entry> paymentAdvice = new ArrayList<>();
        paymentAdvice.clear();
        ArrayList<Entry> estimatedCost = new ArrayList<>();
        estimatedCost.clear();
        ArrayList<String> dates = new ArrayList<>();
        dates.clear();

        for(int i = 0; i < helperSales.size(); i++){
            saleEntries.add(new Entry(Integer.parseInt(helperSales.get(i).getSelling_price()), Integer.parseInt(helperSales.get(i).getQty())));
            paymentAdvice.add(new Entry(helperDatabase.paymentAdviceWeekly(helperSales.get(i).getItem_name(), helperSales.get(i).getCreated_by()), Integer.parseInt(helperSales.get(i).getQty())));
            estimatedCost.add(new Entry(helperDatabase.estimatedCostPerWeek(helperSales.get(i).getItem_name(), helperSales.get(i).getCreated_by()), Integer.parseInt(helperSales.get(i).getQty())));
            dates.add(helperSales.get(i).getItem_name());
        }

        LineDataSet lineDataSetSales = new LineDataSet(saleEntries, "EOD FP");
        lineDataSetSales.setDrawCircles(false);
        lineDataSetSales.setColor(Color.BLUE);

        LineDataSet lineDataSetStocks = new LineDataSet(paymentAdvice, "PmtAdvice");
        lineDataSetStocks.setDrawCircles(false);
        lineDataSetStocks.setColor(Color.RED);

        LineDataSet lineDataSetEstimatedCost = new LineDataSet(estimatedCost, "Cost");
        lineDataSetEstimatedCost.setDrawCircles(false);
        lineDataSetEstimatedCost.setColor(Color.GREEN);


        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        lineDataSets.add(lineDataSetSales);
        lineDataSets.add(lineDataSetStocks);
        lineDataSets.add(lineDataSetEstimatedCost);


        lineChart.setData(new LineData(dates, lineDataSets));


    }
}