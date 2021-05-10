package com.watata.zpos;

public class HelperStockNamesSorter {

    private int stock_id;
    private String stock_name, measure_used;

    public HelperStockNamesSorter() {
    }

    public HelperStockNamesSorter(int stock_id, String stock_name, String measure_used) {
        this.stock_id = stock_id;
        this.stock_name = stock_name;
        this.measure_used = measure_used;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getMeasure_used() {
        return measure_used;
    }

    public void setMeasure_used(String measure_used) {
        this.measure_used = measure_used;
    }
}
