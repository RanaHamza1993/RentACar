package com.team.rentacar.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.team.rentacar.R;
import com.team.rentacar.adapters.BookingsAdapter;
import com.team.rentacar.baseclasses.BaseActivity;
import com.team.rentacar.contracts.Communicator;
import com.team.rentacar.models.VendorsDetailModel;

import java.util.ArrayList;
import java.util.HashMap;

public class PostDiscounts extends BaseActivity implements Communicator.IDiscount {

    private Toolbar toolbar;
    private RecyclerView discountRecycler;
    private DatabaseReference vendorsDetailReference;
    private String vendor;
    private BookingsAdapter bookingAdapter;
    ArrayList<VendorsDetailModel> bookingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_discounts);
        toolbar = findViewById(R.id.discount_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post Discounts");
        vendor=getIntent().getStringExtra("vendor");
        vendorsDetailReference = FirebaseDatabase.getInstance().getReference().child("Vendors");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        discountRecycler = findViewById(R.id.post_discount_recycler);
        bookingAdapter = new BookingsAdapter(PostDiscounts.this, bookingList, 3);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(PostDiscounts.this, RecyclerView.VERTICAL, false);
        discountRecycler.setLayoutManager(layoutManager);
        discountRecycler.setAdapter(bookingAdapter);


        vendorsDetailReference.child(vendor).child("vendor_cars").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotchild:dataSnapshot.getChildren()){
                    String id = dataSnapshotchild.child("id").getValue(String.class);
                    String carName = dataSnapshotchild.child("car_name").getValue(String.class);
                    String carAddress = dataSnapshotchild.child("car_address").getValue(String.class);
                    String carImage = dataSnapshotchild.child("car_thumb_image").getValue(String.class);
                    String driverName = dataSnapshotchild.child("driver_name").getValue(String.class);
                    String driverNumber = dataSnapshotchild.child("driver_number").getValue(String.class);
                    String discount = dataSnapshotchild.child("discount").getValue(String.class);
                    String hourlyRate=dataSnapshotchild.child("hourly_rate").getValue(String.class);
                    String vendorName=dataSnapshotchild.child("vendor_name").getValue(String.class);
                    String date = dataSnapshot.child("booked_date").getValue(String.class);
                    bookingList.add(new VendorsDetailModel(id, carImage, carName, vendorName, carAddress, hourlyRate, "", true, "", driverName, driverNumber, Integer.parseInt(discount),date));
                    bookingAdapter.setDiscountListener(PostDiscounts.this);
                    bookingAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void postDiscount(String id, String vendor) {
    showRentDialog(id,vendor);
    }
    void showRentDialog(String id, String vendor) {
        Dialog discountDialog = new Dialog(this);
        discountDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        discountDialog.setCancelable(true);
        discountDialog.setContentView(R.layout.post_discount_dialog);
        discountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText discount = discountDialog.findViewById(R.id.discount_price);
        TextView postDiscount = discountDialog.findViewById(R.id.post_disc);
        postDiscount.setOnClickListener(v->{
            if(discount.getText().toString().isEmpty()){
                showErrorMessage("Please enter discount value");
            }else{
                HashMap<String,Object> map=new HashMap<String,Object>();
                map.put("discount",discount.getText().toString());
                vendorsDetailReference.child(vendor).child("vendor_cars").child(id).updateChildren(map).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showSuccessMessage("Discount posted successfully");
                                discountDialog.dismiss();
                            }
                        });
            }
        });
        discountDialog.show();
    }
}
