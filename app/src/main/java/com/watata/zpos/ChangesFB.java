package com.watata.zpos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangesFB {
    DatabaseReference reference;

    public ChangesFB() {
        reference = FirebaseDatabase.getInstance().getReference("changes").child("1");
    }

    public void ChangesInStockNames(){
        reference.child("stock_names").setValue("Y");
    }

    public void NoChangesInStockNames(){
        reference.child("stock_names").setValue("N");
    }

    public void ChangesInCategory(){
        reference.child("category").setValue("Y");
    }

    public void NoChangesInCategory(){
        reference.child("category").setValue("N");
    }

    public void ChangesItems(){
        reference.child("items").setValue("Y");
    }

    public void NoChangesItems(){
        reference.child("items").setValue("N");
    }

    public void ChangesInVariantsLinks(){
        reference.child("variants_links").setValue("Y");
    }

    public void NoChangesInVariantsLinks(){
        reference.child("variants_links").setValue("N");
    }

    public void ChangesVariantsHdr(){
        reference.child("variants_hdr").setValue("Y");
    }

    public void NoChangesVariantsHdr(){
        reference.child("variants_hdr").setValue("N");
    }

    public void ChangesVariantsDtls(){
        reference.child("variants_dtls").setValue("Y");
    }

    public void NoChangesVariantsDtls(){
        reference.child("variants_dtls").setValue("N");
    }

    public void ChangesCompositeLinks(){
        reference.child("composite_links").setValue("Y");
    }

    public void NoChangesCompositeLinks(){
        reference.child("composite_links").setValue("N");
    }

    public void ChangesStockHistories(){
        reference.child("stock_histories").setValue("Y");
    }

    public void NoChangesStockHistories(){
        reference.child("stock_histories").setValue("N");
    }

}
