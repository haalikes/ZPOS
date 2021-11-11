package com.watata.zpos.ddlclass;

public class HelperCsvLinks {

    String csv_link_id, csv_link_name, z_link_item_id, z_link_var_hdr_id, z_link_var_dtls_id, csv_link_source;

    public HelperCsvLinks() {
    }

    public HelperCsvLinks(String csv_link_id, String csv_link_name, String z_link_item_id, String csv_link_source) {
        this.csv_link_id = csv_link_id;
        this.csv_link_name = csv_link_name;
        this.z_link_item_id = z_link_item_id;
        this.csv_link_source = csv_link_source;
    }

    public HelperCsvLinks(String csv_link_id, String csv_link_name, String z_link_item_id, String z_link_var_hdr_id, String z_link_var_dtls_id, String csv_link_source) {
        this.csv_link_id = csv_link_id;
        this.csv_link_name = csv_link_name;
        this.z_link_item_id = z_link_item_id;
        this.z_link_var_hdr_id = z_link_var_hdr_id;
        this.z_link_var_dtls_id = z_link_var_dtls_id;
        this.csv_link_source = csv_link_source;
    }

    public String getCsv_link_id() {
        return csv_link_id;
    }

    public void setCsv_link_id(String csv_link_id) {
        this.csv_link_id = csv_link_id;
    }

    public String getCsv_link_name() {
        return csv_link_name;
    }

    public void setCsv_link_name(String csv_link_name) {
        this.csv_link_name = csv_link_name;
    }

    public String getZ_link_item_id() {
        return z_link_item_id;
    }

    public void setZ_link_item_id(String z_link_item_id) {
        this.z_link_item_id = z_link_item_id;
    }

    public String getZ_link_var_hdr_id() {
        return z_link_var_hdr_id;
    }

    public void setZ_link_var_hdr_id(String z_link_var_hdr_id) {
        this.z_link_var_hdr_id = z_link_var_hdr_id;
    }

    public String getZ_link_var_dtls_id() {
        return z_link_var_dtls_id;
    }

    public void setZ_link_var_dtls_id(String z_link_var_dtls_id) {
        this.z_link_var_dtls_id = z_link_var_dtls_id;
    }

    public String getCsv_link_source() {
        return csv_link_source;
    }

    public void setCsv_link_source(String csv_link_source) {
        this.csv_link_source = csv_link_source;
    }

}
