package com.watata.zpos;

import java.io.Serializable;

public class HelperVariantsHdr implements Serializable {

    private int var_hdr_id;
    private String var_hdr_image, var_hdr_name;

    public HelperVariantsHdr() {
    }

    public HelperVariantsHdr(int var_hdr_id, String var_hdr_image, String var_hdr_name) {
        this.var_hdr_id = var_hdr_id;
        this.var_hdr_image = var_hdr_image;
        this.var_hdr_name = var_hdr_name;
    }

    public int getVar_hdr_id() {
        return var_hdr_id;
    }

    public void setVar_hdr_id(int var_hdr_id) {
        this.var_hdr_id = var_hdr_id;
    }

    public String getVar_hdr_image() {
        return var_hdr_image;
    }

    public void setVar_hdr_image(String var_hdr_image) {
        this.var_hdr_image = var_hdr_image;
    }

    public String getVar_hdr_name() {
        return var_hdr_name;
    }

    public void setVar_hdr_name(String var_hdr_name) {
        this.var_hdr_name = var_hdr_name;
    }
}
