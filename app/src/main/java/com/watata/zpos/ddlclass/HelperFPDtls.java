package com.watata.zpos.ddlclass;

public class HelperFPDtls {
    private int fp_id;
    private String fp_date, fp_end_of_day_total, fp_payment_advice;

    public HelperFPDtls() {
    }

    public HelperFPDtls(int fp_id, String fp_date, String fp_end_of_day_total, String fp_payment_advice) {
        this.fp_id = fp_id;
        this.fp_date = fp_date;
        this.fp_end_of_day_total = fp_end_of_day_total;
        this.fp_payment_advice = fp_payment_advice;
    }

    public int getFp_id() {
        return fp_id;
    }

    public void setFp_id(int fp_id) {
        this.fp_id = fp_id;
    }

    public String getFp_date() {
        return fp_date;
    }

    public void setFp_date(String fp_date) {
        this.fp_date = fp_date;
    }

    public String getFp_end_of_day_total() {
        return fp_end_of_day_total;
    }

    public void setFp_end_of_day_total(String fp_end_of_day_total) {
        this.fp_end_of_day_total = fp_end_of_day_total;
    }

    public String getFp_payment_advice() {
        return fp_payment_advice;
    }

    public void setFp_payment_advice(String fp_payment_advice) {
        this.fp_payment_advice = fp_payment_advice;
    }


}
