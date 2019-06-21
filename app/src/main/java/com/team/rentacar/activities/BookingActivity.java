package com.team.rentacar.activities;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.team.rentacar.R;
import com.team.rentacar.adapters.BookingsAdapter;
import com.team.rentacar.adapters.VendorsAdapter;
import com.team.rentacar.models.VendorsDetailModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BookingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView bookingRecycler;
    private DatabaseReference bookingReference;
    ArrayList<VendorsDetailModel> bookingList = new ArrayList<>();
    private BookingsAdapter bookingAdapter;
    private LinearLayoutManager layoutManager;
    private DatabaseReference vendorsDetailReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        toolbar = findViewById(R.id.booking_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Bookings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookingReference = FirebaseDatabase.getInstance().getReference().child("Bookings");

        vendorsDetailReference = FirebaseDatabase.getInstance().getReference().child("Vendors");

        bookingRecycler = findViewById(R.id.booking_recycler);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        setAdapter();
    }
    private void setAdapter() {

        populateArray();
        bookingAdapter = new BookingsAdapter(BookingActivity.this,bookingList );
        layoutManager = new LinearLayoutManager(BookingActivity.this, RecyclerView.VERTICAL, false);
        bookingRecycler.setLayoutManager(layoutManager);
        bookingRecycler.setAdapter(bookingAdapter);


    }
    private void populateArray() {
        bookingReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!bookingList.isEmpty())
                    bookingList.clear();
                HashMap hashMap = (HashMap) dataSnapshot.getValue();
                try {
                    Set keys = hashMap.keySet();
                    Object[] array = new String[6];

                    array = keys.toArray();
                    for (Object o : array) {
                      //  vendorsDetailReference.child(o.toString())

                        HashMap map = (HashMap) dataSnapshot.child(o.toString()).child(FirebaseAuth.getInstance().getUid()).getValue();
                        Set setcarKeys = map.keySet();
                        Object[] arrayCarKeys=setcarKeys.toArray() ;
                        for (Object ob: arrayCarKeys){
                         vendorsDetailReference.child(o.toString()).child("vendor_cars").child(ob.toString()).addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
//                        Integer id=dataSnapshot.child(o.toString()).child("id").getValue(Integer.class);
                                 String id = dataSnapshot2.child("id").getValue(String.class);
                                 String carName = dataSnapshot2.child("car_name").getValue(String.class);
                                 String carAddress = dataSnapshot2.child("car_address").getValue(String.class);
                                 String carImage = dataSnapshot2.child("car_thumb_image").getValue(String.class);
                                 String hourlyRate = dataSnapshot.child(o.toString()).child(FirebaseAuth.getInstance().getUid()).child(ob.toString()).child("rent_price").getValue(String.class);
                                 String vendorName = dataSnapshot2.child("vendor_name").getValue(String.class);

                                 bookingList.add(new VendorsDetailModel(id, carImage, carName, vendorName, carAddress, hourlyRate));
                                 bookingAdapter.notifyDataSetChanged();

                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {

                             }
                         }


                         );
                        }

                        //                        Integer id=dataSnapshot.child(o.toString()).child("id").getValue(Integer.class);


//
//                        String id = dataSnapshot.child(o.toString()).child("id").getValue(String.class);
//                        String carName = dataSnapshot.child(o.toString()).child("car_name").getValue(String.class);
//                        String carAddress = dataSnapshot.child(o.toString()).child("car_address").getValue(String.class);
//                        String carImage = dataSnapshot.child(o.toString()).child("car_thumb_image").getValue(String.class);
//                        String hourlyRate = dataSnapshot.child(o.toString()).child("hourly_rate").getValue(String.class);
//                        String vendorName = dataSnapshot.child(o.toString()).child("vendor_name").getValue(String.class);
//
//
//                        bookingList.add(new VendorsDetailModel(id, carImage, carName, vendorName, carAddress, hourlyRate));
//

                    }
                } catch (Exception e) {

                }
                bookingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

                                               }
        );
//        arrayList.add(new VendorsDetailModel(5,6,R.drawable.bookings, "Bmw","Bmw","Qartaba Chowk","5000 Rs"));

    }
}
