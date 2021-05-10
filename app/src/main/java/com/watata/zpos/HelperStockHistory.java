package com.watata.zpos;

public class HelperStockHistory {

    private  int stock_id;
    private String in_out, stock_name, qty, measure_used, time, username, cost;

    public HelperStockHistory() {
    }

    public HelperStockHistory(String in_out, int stock_id, String stock_name, String qty, String measure_used, String time, String username, String cost) {
        this.stock_id = stock_id;
        this.in_out = in_out;
        this.stock_name = stock_name;
        this.qty = qty;
        this.measure_used = measure_used;

        String sTime = time;

        try {
            int i = Integer.parseInt(time.substring(0,1));

            sTime = time.substring(3,6) + " " + time.substring(0,2) + " " + time.substring(7,11);
        } catch (NumberFormatException e){
        }

        this.time = sTime;
        this.username = username;
        this.cost = cost;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public String getIn_out() {
        return in_out;
    }

    public void setIn_out(String in_out) {
        this.in_out = in_out;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getMeasure_used() {
        return measure_used;
    }

    public void setMeasure_used(String measure_used) {
        this.measure_used = measure_used;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        String sTime = time;

        try {
            int i = Integer.parseInt(time.substring(0,1));

            sTime = time.substring(3,6) + " " + time.substring(0,2) + " " + time.substring(7,11);
        } catch (NumberFormatException e){
        }

        this.time = sTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

}
