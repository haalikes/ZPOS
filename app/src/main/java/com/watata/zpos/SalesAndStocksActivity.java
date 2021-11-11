package com.watata.zpos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.watata.zpos.ddlclass.HelperSales;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SalesAndStocksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_and_stocks);

        LineChart lineChart;
        HelperDatabase helperDatabase = new HelperDatabase(this);

        lineChart = findViewById(R.id.lineChart);

        List<HelperSales> helperSales = new LinkedList<>();
        helperSales.clear();
        helperSales = helperDatabase.listSummarySalesPerDay();

        //List<HelperSales> helperStockSales = new LinkedList<>();
        //helperStockSales.clear();

        ArrayList<Entry> saleEntries = new ArrayList<>();
        saleEntries.clear();
        ArrayList<Entry> stockEntries = new ArrayList<>();
        stockEntries.clear();
        ArrayList<Entry> estimatedCost = new ArrayList<>();
        estimatedCost.clear();
        ArrayList<Entry> estimatedReducedSales = new ArrayList<>();
        estimatedReducedSales.clear();
        ArrayList<Entry> fpOnlySales = new ArrayList<>();
        fpOnlySales.clear();
        ArrayList<String> dates = new ArrayList<>();
        dates.clear();

        for(int i = 0; i < helperSales.size(); i++){
            saleEntries.add(new Entry(Integer.parseInt(helperSales.get(i).getSelling_price()), Integer.parseInt(helperSales.get(i).getQty())));
            stockEntries.add(new Entry(helperDatabase.sumCostStocksPerDay(helperSales.get(i).getItem_name()), Integer.parseInt(helperSales.get(i).getQty())));
            estimatedCost.add(new Entry(helperDatabase.estimatedCostPerDay(helperSales.get(i).getItem_name()), Integer.parseInt(helperSales.get(i).getQty())));
            estimatedReducedSales.add(new Entry(helperDatabase.estimatedReducedSales(helperSales.get(i).getItem_name()), Integer.parseInt(helperSales.get(i).getQty())));
            fpOnlySales.add(new Entry(helperDatabase.estimatedFPSales(helperSales.get(i).getItem_name()), Integer.parseInt(helperSales.get(i).getQty())));
            dates.add(helperSales.get(i).getItem_name());
        }

        LineDataSet lineDataSetSales = new LineDataSet(saleEntries, "LOY+FP");
        lineDataSetSales.setDrawCircles(false);
        lineDataSetSales.setColor(Color.BLUE);

        LineDataSet lineDataSetStocks = new LineDataSet(stockEntries, "Stock Bought");
        lineDataSetStocks.setDrawCircles(false);
        lineDataSetStocks.setColor(Color.RED);

        LineDataSet lineDataSetEstimatedCost = new LineDataSet(estimatedCost, "Cost");
        lineDataSetEstimatedCost.setDrawCircles(false);
        lineDataSetEstimatedCost.setColor(Color.GREEN);

        LineDataSet lineDataSetEstimatedReducedFP = new LineDataSet(estimatedReducedSales, "LOY+FP(80%)");
        lineDataSetEstimatedReducedFP.setDrawCircles(false);
        lineDataSetEstimatedReducedFP.setColor(Color.GRAY);

        LineDataSet lineDataSetFPOnly = new LineDataSet(fpOnlySales, "FP(80%)");
        lineDataSetFPOnly.setDrawCircles(false);
        lineDataSetFPOnly.setColor(Color.MAGENTA);

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        lineDataSets.add(lineDataSetSales);
        lineDataSets.add(lineDataSetStocks);
        lineDataSets.add(lineDataSetEstimatedCost);
        lineDataSets.add(lineDataSetEstimatedReducedFP);
        lineDataSets.add(lineDataSetFPOnly);

        lineChart.setData(new LineData(dates, lineDataSets));

    }
}