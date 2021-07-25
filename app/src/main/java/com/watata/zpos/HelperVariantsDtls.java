package com.watata.zpos;

import java.io.Serializable;

public class HelperVariantsDtls implements Serializable {

    private int var_hdr_id, var_dtls_id;
    private String var_dtls_image, var_dtls_name, var_selling_price, var_dtls_default, var_dtls_add_on, composite_required;

    //var_hdr_id is fk, can be multiple
    //var_dtls_id is unique

    public HelperVariantsDtls() {
    }

    public HelperVariantsDtls(int var_hdr_id, int var_dtls_id, String var_dtls_image, String var_dtls_name, String var_selling_price, String var_dtls_default, String var_dtls_add_on, String composite_required) {
        this.var_hdr_id = var_hdr_id;
        this.var_dtls_id = var_dtls_id;
        this.var_dtls_image = var_dtls_image;
        this.var_dtls_name = var_dtls_name;
        this.var_selling_price = var_selling_price;
        this.var_dtls_default = var_dtls_default;
        this.var_dtls_add_on = var_dtls_add_on;
        this.composite_required = composite_required;
    }

    public int getVar_hdr_id() {
        return var_hdr_id;
    }

    public void setVar_hdr_id(int var_hdr_id) {
        this.var_hdr_id = var_hdr_id;
    }

    public int getVar_dtls_id() {
        return var_dtls_id;
    }

    public void setVar_dtls_id(int var_dtls_id) {
        this.var_dtls_id = var_dtls_id;
    }

    public String getVar_dtls_image() {
        return var_dtls_image;
    }

    public void setVar_dtls_image(String var_dtls_image) {
        this.var_dtls_image = var_dtls_image;
    }

    public String getVar_dtls_name() {
        return var_dtls_name;
    }

    public void setVar_dtls_name(String var_dtls_name) {
        this.var_dtls_name = var_dtls_name;
    }

    public String getVar_selling_price() {
        return var_selling_price;
    }

    public void setVar_selling_price(String var_selling_price) {
        this.var_selling_price = var_selling_price;
    }

    public String getVar_dtls_default() {
        return var_dtls_default;
    }

    public void setVar_dtls_default(String var_dtls_default) {
        this.var_dtls_default = var_dtls_default;
    }

    public String getVar_dtls_add_on() {
        return var_dtls_add_on;
    }

    public void setVar_dtls_add_on(String var_dtls_add_on) {
        this.var_dtls_add_on = var_dtls_add_on;
    }

    public String getComposite_required() {
        return composite_required;
    }

    public void setComposite_required(String composite_required) {
        this.composite_required = composite_required;
    }
}
