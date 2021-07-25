package com.watata.zpos;

public class HelperChanges {
    private String change_all, stock_names, category, items, variants_links, variants_hdr, variants_dtls, composite_links, stock_histories, sales, fp_dtls ;

    public HelperChanges() {
    }

    public HelperChanges(String change_all, String stock_names, String category, String items, String variants_links, String variants_hdr, String variants_dtls, String composite_links, String stock_histories, String sales, String fp_dtls) {
        this.change_all = change_all;
        this.stock_names = stock_names;
        this.category = category;
        this.items = items;
        this.variants_links = variants_links;
        this.variants_hdr = variants_hdr;
        this.variants_dtls = variants_dtls;
        this.composite_links = composite_links;
        this.stock_histories = stock_histories;
        this.sales = sales;
        this.fp_dtls = fp_dtls;
    }

    public String getChange_all() {
        return change_all;
    }

    public void setChange_all(String change_all) {
        this.change_all = change_all;
    }


    public String getStock_names() {
        return stock_names;
    }

    public void setStock_names(String stock_names) {
        this.stock_names = stock_names;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getVariants_links() {
        return variants_links;
    }

    public void setVariants_links(String variants_links) {
        this.variants_links = variants_links;
    }

    public String getVariants_hdr() {
        return variants_hdr;
    }

    public void setVariants_hdr(String variants_hdr) {
        this.variants_hdr = variants_hdr;
    }

    public String getVariants_dtls() {
        return variants_dtls;
    }

    public void setVariants_dtls(String variants_dtls) {
        this.variants_dtls = variants_dtls;
    }

    public String getComposite_links() {
        return composite_links;
    }

    public void setComposite_links(String composite_links) {
        this.composite_links = composite_links;
    }

    public String getStock_histories() {
        return stock_histories;
    }

    public void setStock_histories(String stock_histories) {
        this.stock_histories = stock_histories;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }


    public String getFp_dtls() {
        return fp_dtls;
    }

    public void setFp_dtls(String fp_dtls) {
        this.fp_dtls = fp_dtls;
    }
}
