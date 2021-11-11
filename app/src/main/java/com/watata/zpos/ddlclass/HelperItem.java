package com.watata.zpos.ddlclass;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class HelperItem implements Serializable {

    private int item_id, cat_id;
    private String item_name, item_image, item_selling_price, var_hdr_id, stock_hdr_id;

    public HelperItem() {
    }

    public HelperItem(int item_id, int cat_id, String item_name, String item_image, String item_selling_price, String var_hdr_id, String stock_hdr_id) {
        this.item_id = item_id;
        this.cat_id = cat_id;
        this.item_name = item_name;
        this.item_image = item_image;
        this.item_selling_price = item_selling_price;
        this.var_hdr_id = var_hdr_id;
        this.stock_hdr_id = stock_hdr_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_selling_price() {
        return item_selling_price;
    }

    public void setItem_selling_price(String item_selling_price) {
        this.item_selling_price = item_selling_price;
    }

    public String getVar_hdr_id() {
        return var_hdr_id;
    }

    public void setVar_hdr_id(String var_hdr_id) {
        this.var_hdr_id = var_hdr_id;
    }

    public String getStock_hdr_id() {
        return stock_hdr_id;
    }

    public void setStock_hdr_id(String stock_hdr_id) {
        this.stock_hdr_id = stock_hdr_id;
    }
}
