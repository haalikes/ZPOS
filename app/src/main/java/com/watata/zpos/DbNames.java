package com.watata.zpos;

public class DbNames {

    private static final int sale_sorter = 100;
    private static final int days_covered = 120;

    //stock_names
    private static final String TBL_STOCK_NAMES = "TBL_STOCK_NAMES";
    private static final String col_stock_id = "stock_id";
    private static final String COL_STOCK_NAME = "STOCK_NAME";
    private static final String COL_MEASURE_USED = "MEASURE_USED";

    //stocks_history
    private static final String tbl_stocks_history = "stocks_history";
    private static final String col_in_out = "in_out";
    //private static final String col_stock_id = "stock_id";
    //private static final String COL_STOCK_NAME = "STOCK_NAME";
    //private static final String col_qty = "qty";
    //private static final String COL_MEASURE_USED = "MEASURE_USED";
    private static final String col_time = "time";
    private static final String col_username = "username";
    private static final String col_cost = "cost";

    //category_tbl
    private static final String TBL_CATEGORY = "TBL_CATEGORY";
    //private static final String COL_CAT_ID = "CAT_ID";
    //private static final String COL_CAT_IMAGE = "CAT_IMAGE";
    //private static final String COL_CAT_NAME = "CAT_NAME";
    private static final String col_cat_id = "cat_id";
    private static final String col_cat_image = "cat_image";
    private static final String col_cat_name = "cat_name";

    //items
    private static final String tbl_items = "tbl_items";
    private static final String col_item_id = "item_id";
    //private static final String col_cat_id = "cat_id";
    private static final String col_item_name = "item_name";
    private static final String col_item_image = "item_image";
    private static final String col_item_selling_price = "item_selling_price";
    private static final String col_var_hdr_id = "var_hdr_id";
    private static final String col_stock_link_id = "stock_link_id";

    //variants_links
    private static final String tbl_variants_links = "variants_links";
    private static final String col_link_id = "link_id";
    //private static final String col_item_id = "item_id";
    //private static final String col_var_hdr_id = "var_hdr_id";

    //variants_hdr
    private static final String tbl_variants_hdr = "variants_hdr";
    //private static final String col_var_hdr_id = "var_hdr_id";
    private static final String col_var_hdr_image = "var_hdr_image";
    private static final String col_var_hdr_name = "var_hdr_name";


    //variants_details
    private static final String tbl_variants_dtls = "variants_dtls";
    //private static final String col_var_hdr_id = "var_hdr_id";
    private static final String col_var_dtls_id = "var_dtls_id";
    private static final String col_var_dtls_image = "var_dtls_image";
    private static final String col_var_dtls_name = "var_dtls_name";
    private static final String col_var_selling_price = "var_selling_price";
    private static final String col_var_dtls_default = "var_dtls_default";
    private static final String col_var_dtls_add_on = "var_dtls_add_on";
    private static final String col_composite_required = "composite_required"; // null = Yes, N = No

    //sales
    private static final String tbl_sales = "sales";
    private static final String col_transaction_id = "transaction_id";
    private static final String col_transaction_counter = "transaction_counter";
    private static final String col_transaction_per_entry = "transaction_per_entry";
    //private static final String col_item_id = "item_id";
    private static final String col_sort_order_id = "sort_order_id";
    private static final String col_machine_name = "machine_name";
    //private static final String col_var_hdr_id = "var_hdr_id"; // the only string id, to null the FB
    //private static final String col_var_dtls_id = "var_dtls_id"; // the only string id, to null the FB
    //private static final String col_item_name = "item_name";
    private static final String col_qty = "qty";
    private static final String col_selling_price = "selling_price";
    private static final String col_date = "date";
    private static final String col_created_by = "created_by";
    private static final String col_completed = "completed";
    private static final String col_dine_in_out = "dine_in_out";

    //changes
    private static final String tbl_changes = "changes";
    private static final String col_change_all = "change_all";
    private static final String col_stock_names = "stock_names";
    private static final String col_category = "category";
    private static final String col_items = "items";
    private static final String col_variants_links = "variants_links";
    private static final String col_variants_hdr = "variants_hdr";
    private static final String col_variants_dtls = "variants_dtls";
    private static final String col_composite_links = "composite_links";
    private static final String col_stock_histories = "stock_histories";
    private static final String col_sales = "sales";
    private static final String col_fp_dtls = "fp_dtls";
    private static final String col_csv_links= "csv_links";

    //composite_links
    private static final String tbl_composite_links = "composite_links";
    private static final String col_composite_link_id = "composite_link_id";
    //private static final String col_item_id = "item_id";
    //private static final String col_stock_id = "stock_id";
    //private static final String col_var_hdr_id = "var_hdr_id";
    //private static final String col_var_dtls_id = "var_dtls_id";
    //private static final String col_qty = "qty";
    private static final String col_unit = "unit";
    private static final String col_req = "req";

    //dine in out
    private static final String tbl_dine_in_out = "dine_in_out";
    //private static final String col_dine_in_out = "dine_in_out";

    //fp details
    private static final String tbl_fp_dtls = "fp_dtls";
    private static final String col_fp_id = "fp_id";
    private static final String col_fp_date = "fp_date";
    private static final String col_fp_end_of_day_total = "fp_end_of_day_total";
    private static final String col_fp_payment_advice = "fp_payment_advice";

    //csv links
    private static final String tbl_csv_links = "csv_links";
    private static final String col_csv_link_id = "csv_link_id";
    private static final String col_csv_link_name = "csv_link_name";
    private static final String col_z_link_item_id = "z_link_item_id";
    private static final String col_z_link_var_hdr_id = "z_link_var_hdr_id";
    private static final String col_z_link_var_dtls_id = "z_link_var_dtls_id";
    private static final String col_csv_link_source = "csv_link_source";

    public DbNames() {
    }

    public static int getSale_sorter() {
        return sale_sorter;
    }

    public static int getDays_covered() {
        return days_covered;
    }

    public static String getTblStockNames() {
        return TBL_STOCK_NAMES;
    }

    public static String getCol_stock_id() {
        return col_stock_id;
    }

    public static String getColStockName() {
        return COL_STOCK_NAME;
    }

    public static String getColMeasureUsed() {
        return COL_MEASURE_USED;
    }

    public static String getTbl_stocks_history() {
        return tbl_stocks_history;
    }

    public static String getCol_in_out() {
        return col_in_out;
    }

    public static String getCol_time() {
        return col_time;
    }

    public static String getCol_username() {
        return col_username;
    }

    public static String getCol_cost() {
        return col_cost;
    }

    public static String getTblCategory() {
        return TBL_CATEGORY;
    }

    public static String getCol_cat_id() {
        return col_cat_id;
    }

    public static String getCol_cat_image() {
        return col_cat_image;
    }

    public static String getCol_cat_name() {
        return col_cat_name;
    }

    public static String getTbl_items() {
        return tbl_items;
    }

    public static String getCol_item_id() {
        return col_item_id;
    }

    public static String getCol_item_name() {
        return col_item_name;
    }

    public static String getCol_item_image() {
        return col_item_image;
    }

    public static String getCol_item_selling_price() {
        return col_item_selling_price;
    }

    public static String getCol_var_hdr_id() {
        return col_var_hdr_id;
    }

    public static String getCol_stock_link_id() {
        return col_stock_link_id;
    }

    public static String getTbl_variants_links() {
        return tbl_variants_links;
    }

    public static String getCol_link_id() {
        return col_link_id;
    }

    public static String getTbl_variants_hdr() {
        return tbl_variants_hdr;
    }

    public static String getCol_var_hdr_image() {
        return col_var_hdr_image;
    }

    public static String getCol_var_hdr_name() {
        return col_var_hdr_name;
    }

    public static String getTbl_variants_dtls() {
        return tbl_variants_dtls;
    }

    public static String getCol_var_dtls_id() {
        return col_var_dtls_id;
    }

    public static String getCol_var_dtls_image() {
        return col_var_dtls_image;
    }

    public static String getCol_var_dtls_name() {
        return col_var_dtls_name;
    }

    public static String getCol_var_selling_price() {
        return col_var_selling_price;
    }

    public static String getCol_var_dtls_default() {
        return col_var_dtls_default;
    }

    public static String getCol_var_dtls_add_on() {
        return col_var_dtls_add_on;
    }

    public static String getCol_composite_required() {
        return col_composite_required;
    }

    public static String getTbl_sales() {
        return tbl_sales;
    }

    public static String getCol_transaction_id() {
        return col_transaction_id;
    }

    public static String getCol_transaction_counter() {
        return col_transaction_counter;
    }

    public static String getCol_transaction_per_entry() {
        return col_transaction_per_entry;
    }

    public static String getCol_sort_order_id() {
        return col_sort_order_id;
    }

    public static String getCol_machine_name() {
        return col_machine_name;
    }

    public static String getCol_qty() {
        return col_qty;
    }

    public static String getCol_selling_price() {
        return col_selling_price;
    }

    public static String getCol_date() {
        return col_date;
    }

    public static String getCol_created_by() {
        return col_created_by;
    }

    public static String getCol_completed() {
        return col_completed;
    }

    public static String getCol_dine_in_out() {
        return col_dine_in_out;
    }

    public static String getTbl_changes() {
        return tbl_changes;
    }

    public static String getCol_change_all() {
        return col_change_all;
    }

    public static String getCol_stock_names() {
        return col_stock_names;
    }

    public static String getCol_category() {
        return col_category;
    }

    public static String getCol_items() {
        return col_items;
    }

    public static String getCol_variants_links() {
        return col_variants_links;
    }

    public static String getCol_variants_hdr() {
        return col_variants_hdr;
    }

    public static String getCol_variants_dtls() {
        return col_variants_dtls;
    }

    public static String getCol_composite_links() {
        return col_composite_links;
    }

    public static String getCol_stock_histories() {
        return col_stock_histories;
    }

    public static String getCol_sales() {
        return col_sales;
    }

    public static String getCol_fp_dtls() {
        return col_fp_dtls;
    }

    public static String getCol_csv_links() {
        return col_csv_links;
    }

    public static String getTbl_composite_links() {
        return tbl_composite_links;
    }

    public static String getCol_composite_link_id() {
        return col_composite_link_id;
    }

    public static String getCol_unit() {
        return col_unit;
    }

    public static String getCol_req() {
        return col_req;
    }

    public static String getTbl_dine_in_out() {
        return tbl_dine_in_out;
    }

    public static String getTbl_fp_dtls() {
        return tbl_fp_dtls;
    }

    public static String getCol_fp_id() {
        return col_fp_id;
    }

    public static String getCol_fp_date() {
        return col_fp_date;
    }

    public static String getCol_fp_end_of_day_total() {
        return col_fp_end_of_day_total;
    }

    public static String getCol_fp_payment_advice() {
        return col_fp_payment_advice;
    }

    public static String getTbl_csv_links() {
        return tbl_csv_links;
    }

    public static String getCol_csv_link_id() {
        return col_csv_link_id;
    }

    public static String getCol_csv_link_name() {
        return col_csv_link_name;
    }

    public static String getCol_z_link_item_id() {
        return col_z_link_item_id;
    }

    public static String getCol_z_link_var_hdr_id() {
        return col_z_link_var_hdr_id;
    }

    public static String getCol_z_link_var_dtls_id() {
        return col_z_link_var_dtls_id;
    }

    public static String getCol_csv_link_source() {
        return col_csv_link_source;
    }
}
