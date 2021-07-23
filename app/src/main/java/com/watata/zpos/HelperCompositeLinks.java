package com.watata.zpos;

public class HelperCompositeLinks {

    //ex. stocks 1 box chicken
    //qty = 1 , unit = pc chicken

    //ex. stocks  1 pack terriyaki
    //qty = 1 , unit = part of terriyaki

    //ex. no stock_id for
    //apple juice and orange juice
    //both use same variants for small medium large, but different stock_id

    private int composite_link_id, item_id, stock_id;
    private String var_hdr_id, var_dtls_id, qty, unit, inc_by_var, req;

    public HelperCompositeLinks() {
    }

    public HelperCompositeLinks(int composite_link_id, int item_id, int stock_id, String var_hdr_id, String var_dtls_id, String qty, String unit, String inc_by_var, String req) {
        this.composite_link_id = composite_link_id;
        this.item_id = item_id;
        this.stock_id = stock_id;
        this.var_hdr_id = var_hdr_id;
        this.var_dtls_id = var_dtls_id;
        this.qty = qty;
        this.unit = unit;
        this.inc_by_var = inc_by_var;
        this.req = req;
    }

    public int getComposite_link_id() {
        return composite_link_id;
    }

    public void setComposite_link_id(int composite_link_id) {
        this.composite_link_id = composite_link_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getInc_by_var() {
        return inc_by_var;
    }

    public void setInc_by_var(String inc_by_var) {
        this.inc_by_var = inc_by_var;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }
}
