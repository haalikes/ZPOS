package com.watata.zpos;

public class HelperSales {
    //transaction_id is unique per machine, auto increment
    //transaction_counter is transaction counter per successful sales per machine
    //transaction_per_entry  id per transaction that includes variants
        // ex. transaction_per_entry is  1 for 2 fries
        //                              1 for 2 reg
        //                              1 for 2 cheese
        //
        //                              2 for 1 fries
        //                              2 for 1 medium
        //                              2 for 1 bacon
        // total order is 2 fries reg cheese and 1 fries medium bacon, different transaction_per_entry
    //item_id from items
    //sort_order_id; if sort_order is 20, modulo 20
                // item - sort_order_id = 20
                // var - sort_order_id = 21 - 39
    //var_dtls_id is string so that no field when null in FB
    //var_dtls_id is string so that no field when null in FB
    //item_name is item_name if no var_dtls_id, if with var_dtls_id then item_name is var_dtls_name
    //completed     Y - completed
    //              N - about to checkout/to be completed(added to big cart)
    //              W - inside variants menu
    //                - added to small cart, consists of only 1 transaction_per_entry id
    //                - not visible to big cart, unless press done.
    //                - when done, set to N
    private int transaction_id, transaction_counter, transaction_per_entry, item_id, sort_order_id;
    private String machine_name, var_hdr_id, var_dtls_id, item_name, qty, selling_price, date, created_by, completed, dine_in_out;

    public HelperSales() {

    }

    public HelperSales(int transaction_id, int transaction_counter, int transaction_per_entry, int item_id, int sort_order_id, String machine_name, String var_hdr_id, String var_dtls_id, String item_name, String qty, String selling_price, String date, String created_by, String completed, String dine_in_out) {
        this.transaction_id = transaction_id;
        this.transaction_counter = transaction_counter;
        this.transaction_per_entry = transaction_per_entry;
        this.item_id = item_id;
        this.sort_order_id = sort_order_id;
        this.machine_name = machine_name;
        this.var_hdr_id = var_hdr_id;
        this.var_dtls_id = var_dtls_id;
        this.item_name = item_name;
        this.qty = qty;
        this.selling_price = selling_price;
        String sDate = date;

        try {
            int i = Integer.parseInt(date.substring(0,1));

            sDate = date.substring(3,6) + " " + date.substring(0,2) + " " + date.substring(7,11);
        } catch (NumberFormatException e){
        }

        this.date = sDate;
        this.created_by = created_by;
        this.completed = completed;
        this.dine_in_out = dine_in_out;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getTransaction_counter() {
        return transaction_counter;
    }

    public void setTransaction_counter(int transaction_counter) {
        this.transaction_counter = transaction_counter;
    }

    public int getTransaction_per_entry() {
        return transaction_per_entry;
    }

    public void setTransaction_per_entry(int transaction_per_entry) {
        this.transaction_per_entry = transaction_per_entry;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getSort_order_id() {
        return sort_order_id;
    }

    public void setSort_order_id(int sort_order_id) {
        this.sort_order_id = sort_order_id;
    }

    public String getMachine_name() {
        return machine_name;
    }

    public void setMachine_name(String machine_name) {
        this.machine_name = machine_name;
    }

    public String getVar_hdr_id() {
        return var_hdr_id;
    }

    public void setVar_hdr_id(String var_hdr_id) {
        this.var_hdr_id = var_hdr_id;
    }

    public String getVar_dtls_id() {
        return var_dtls_id;
    }

    public void setVar_dtls_id(String var_dtls_id) {
        this.var_dtls_id = var_dtls_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        String sDate = date;

        try {
            int i = Integer.parseInt(date.substring(0,1));

            sDate = date.substring(3,6) + " " + date.substring(0,2) + " " + date.substring(7,11);
        } catch (NumberFormatException e){
        }

        this.date = sDate;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getDine_in_out() {
        return dine_in_out;
    }

    public void setDine_in_out(String dine_in_out) {
        this.dine_in_out = dine_in_out;
    }
}
