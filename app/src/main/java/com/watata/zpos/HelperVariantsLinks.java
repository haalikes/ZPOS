package com.watata.zpos;

public class HelperVariantsLinks {
    private int link_id, item_id, var_hdr_id;

    public HelperVariantsLinks() {
    }

    public HelperVariantsLinks( int item_id, int var_hdr_id) {
        this.item_id = item_id;
        this.var_hdr_id = var_hdr_id;
    }

    public HelperVariantsLinks(int link_id, int item_id, int var_hdr_id) {
        this.link_id = link_id;
        this.item_id = item_id;
        this.var_hdr_id = var_hdr_id;
    }

    public int getLink_id() {
        return link_id;
    }

    public void setLink_id(int link_id) {
        this.link_id = link_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getVar_hdr_id() {
        return var_hdr_id;
    }

    public void setVar_hdr_id(int var_hdr_id) {
        this.var_hdr_id = var_hdr_id;
    }
}
