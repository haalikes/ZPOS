package com.watata.zpos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Helper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostLoginActivity extends AppCompatActivity {
    /*

    Updates required
    1. able to edit the summary, when selected, proceed to item/variants to edit the product.
    2. after check out, put details of payment with sukli.
           if possible, give option of instead of change of 9 pesos, user can ask of 1 peso for a change of 10.
                            ex. 100 cash, bill 41, change 59
                                          ask additional 1 peso and change of 60 or
                                          ask additional 11 pesos and change of 70 or
                                          ask additional 21 pesos and change of 80 or
                                          ask additional 31 pesos and change of 90 or
                                          ask additional 41 pesos and change of 100
                            ex. 100 cash, bill 43, change 57
                                          ask additional 3 peso and change of 60 or
                                          ask additional 5 peso and change of 62 or
                                          ask additional 10 peso and change of 67 or
                                          ask additional 13 pesos and change of 70 or
                                          ask additional 20 pesos and change of 77 or
                                          ask additional 23 pesos and change of 80 or
                                          ask additional 30 pesos and change of 87 or
                                          ask additional 33 pesos and change of 90 or
                                          ask additional 40 pesos and change of 97 or
                                          ask additional 43 pesos and change of 100 or
                                          ask additional 50 pesos and change of 107
                             ex. 100 cash, bill 53, change 47
                                          ask additional 3 peso and change of 50
                                          ask additional 5 peso and change of 52
                                          ask additional 10 peso and change 57
    3. add back button in all menu.
    4. add decrease_qty_on_sale tag in helperstocknames - this will tell if the composite will descrease on successful sale., see HelperDatabase.decQtyOnSale
    5. payment activity
        - done
        - add cash/gcash/foodpanda
    6. Add tax in Payment, discount
    7. dine in, take out button from start to payment
    8. able to add and select from existing composite ( additional spoon, additional cup, tissue )
    9. optional composite can still sell ( like no tissue, no tomato ), see HelperDatabase.optionalComposite
        should still be recorded in Stocks_History
    10. check one tag in all menus to force relogin for changes.
    11. include no. 20 tag in setting as apply changes to menu?
    12. Details sales and summary should be display at the same time. top details, bottom summary.
        can only proceed to payment when add_to_cart is clear.
    13. DONE - finish sale
    14. Receipt activity
    15. Delete N sales.


    1. DONE - If category has no item yet, donot display in Menu, only in Item setup.
                DONE - does not display 1 item - remarks - phone switch setup
    2. DONE - To remove all not added to cart when back is press.
    3. DONE - To remove finish() when press add to cart, so that he/she can select another.
    4. DONE - 2 separate order of the same variants should be resummarize.
    5. DONE - layout summary should always maximize sized.
    6. DONE - put add to cart also in item only without variants.
    7. DONE - use block icon for not available products but put override message when selling.
            removed overriding, fix stocks if want to sell
    8. DONE - item without variants should summarize and not separate row.
    9. Done - price display in item.
    10. Done - stocks display, alert/red when you reach threshold.
            no threshold yet
    11. DONE - auto insert of 1 item when moving to variants.



     */



    ProgressBar progressBar;
    HelperDatabase helperDatabase = new HelperDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);

        progressBar = findViewById(R.id.progressBar);

        generateRequiredData();
        //openMainMenuActivity();
    }

    public void generateRequiredData() {
        progressBar.setVisibility(View.VISIBLE);
        changes();
        ///downloadStockNames();
    }

    public void changes(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("changes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    List<HelperChanges> changes = new ArrayList<HelperChanges>();
                    changes.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        changes.add(snapshot.getValue(HelperChanges.class));
                    }
                    helperDatabase.refreshChanges(changes);
                    if (helperDatabase.dbChanged()){
                        Log.d("refreshChanges", "refresh yes");
                        downloadStockNames();
                    } else {
                        Log.d("refreshChanges", "refresh no");
                        openMainMenuActivity();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void downloadStockNames(){
        if(helperDatabase.dbChangedStockNames()){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("stock_names");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        List<HelperStockNames> stockNames = new ArrayList<HelperStockNames>();
                        stockNames.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            stockNames.add(snapshot.getValue(HelperStockNames.class));
                        }
                        helperDatabase.refreshStockNames(stockNames);

                        ChangesFB changesFB = new ChangesFB();
                        changesFB.NoChangesInStockNames();


                        downloadCategory();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            downloadCategory();
        }

    }

    public void downloadCategory(){
        if(helperDatabase.dbChangedCategory()){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("category");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        List<HelperCategory> helperCategory = new ArrayList<HelperCategory>();
                        helperCategory.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            helperCategory.add(snapshot.getValue(HelperCategory.class));
                        }
                        helperDatabase.refreshCategory(helperCategory);

                        ChangesFB changesFB = new ChangesFB();
                        changesFB.NoChangesInCategory();

                        downloadItems();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            downloadItems();
        }

    }

    public void downloadItems(){
        if(helperDatabase.dbChangedItems()){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("items");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        List<HelperItem> helperItems = new ArrayList<HelperItem>();
                        helperItems.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            helperItems.add(snapshot.getValue(HelperItem.class));
                        }
                        helperDatabase.refreshItems(helperItems);
                        ChangesFB changesFB = new ChangesFB();
                        changesFB.NoChangesItems();

                        downloadVariantsLinks();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            downloadVariantsLinks();
        }

    }

    public void downloadVariantsLinks(){
        if(helperDatabase.dbChangedVariantsLinks()){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("variants_links");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        List<HelperVariantsLinks> helperVariantsLinks = new ArrayList<HelperVariantsLinks>();
                        helperVariantsLinks.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            helperVariantsLinks.add(snapshot.getValue(HelperVariantsLinks.class));
                        }
                        helperDatabase.refreshVariantsLinks(helperVariantsLinks);

                        ChangesFB changesFB = new ChangesFB();
                        changesFB.NoChangesInVariantsLinks();

                        downloadVariantsHdr();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            downloadVariantsHdr();
        }

    }

    public void downloadVariantsHdr(){
        if(helperDatabase.dbChangedVariantsHdr()){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("variants_hdr");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        List<HelperVariantsHdr> helperVariantsHdr = new ArrayList<HelperVariantsHdr>();
                        helperVariantsHdr.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            helperVariantsHdr.add(snapshot.getValue(HelperVariantsHdr.class));
                        }
                        helperDatabase.refreshVariantsHdr(helperVariantsHdr);
                        ChangesFB changesFB = new ChangesFB();
                        changesFB.NoChangesVariantsHdr();
                        downloadVariantsDlts();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            downloadVariantsDlts();
        }

    }

    public void downloadVariantsDlts(){
        if(helperDatabase.dbChangedVariantsDtls()){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("variants_dtls");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        List<HelperVariantsDtls> helperVariantsDtls = new ArrayList<HelperVariantsDtls>();
                        helperVariantsDtls.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            helperVariantsDtls.add(snapshot.getValue(HelperVariantsDtls.class));
                        }
                        helperDatabase.refreshVariantsDtls(helperVariantsDtls);
                        ChangesFB changesFB = new ChangesFB();
                        changesFB.NoChangesVariantsDtls();
                        downloadCompositeLinks();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            downloadCompositeLinks();
        }

    }

    public void downloadCompositeLinks(){
        if(helperDatabase.dbChangedCompositeLinks()){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("composite_links");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        List<HelperCompositeLinks> helperCompositeLinks = new ArrayList<HelperCompositeLinks>();
                        helperCompositeLinks.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            helperCompositeLinks.add(snapshot.getValue(HelperCompositeLinks.class));
                        }
                        helperDatabase.refreshCompositeLinks(helperCompositeLinks);

                        ChangesFB changesFB = new ChangesFB();
                        changesFB.NoChangesCompositeLinks();

                        ///openMainMenuActivity();
                        downloadStocksHistory();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            downloadStocksHistory();
        }

    }

    public void downloadStocksHistory(){
        if(helperDatabase.dbChangedStockHistories()){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("stocks_history");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        List<HelperStockHistory> helperStockHistories = new ArrayList<HelperStockHistory>();
                        helperStockHistories.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            helperStockHistories.add(snapshot.getValue(HelperStockHistory.class));
                        }
                        helperDatabase.refreshStocksHistory(helperStockHistories);
                        ChangesFB changesFB = new ChangesFB();
                        changesFB.NoChangesStockHistories();
                        downloadSales();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            downloadSales();
        }

    }

    public void downloadSales(){
        if(helperDatabase.dbChangedSales()){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("sales");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        List<HelperSales> helperSales = new ArrayList<HelperSales>();
                        helperSales.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            helperSales.add(snapshot.getValue(HelperSales.class));
                        }
                        helperDatabase.refreshSales(helperSales);
                        ChangesFB changesFB = new ChangesFB();
                        changesFB.NoChangesSales();
                        openMainMenuActivity();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            openMainMenuActivity();
        }

    }

    public void openMainMenuActivity() {

        //Delete sales when completed status = W then the app crash.
        helperDatabase.deleteSalesW();

        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
        finish();
    }
}