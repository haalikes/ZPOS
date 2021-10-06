package com.watata.zpos;

public class HelperCsvLinks {

    String csv_link_id, link_type, link_type_value, csv_link_name;

    public HelperCsvLinks() {
    }

    public HelperCsvLinks(String csv_link_id, String link_type, String link_type_value, String csv_link_name) {
        this.csv_link_id = csv_link_id;
        this.link_type = link_type;
        this.link_type_value = link_type_value;
        this.csv_link_name = csv_link_name;
    }

    public String getCsv_link_id() {
        return csv_link_id;
    }

    public void setCsv_link_id(String csv_link_id) {
        this.csv_link_id = csv_link_id;
    }

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public String getLink_type_value() {
        return link_type_value;
    }

    public void setLink_type_value(String link_type_value) {
        this.link_type_value = link_type_value;
    }

    public String getCsv_link_name() {
        return csv_link_name;
    }

    public void setCsv_link_name(String csv_link_name) {
        this.csv_link_name = csv_link_name;
    }
}
