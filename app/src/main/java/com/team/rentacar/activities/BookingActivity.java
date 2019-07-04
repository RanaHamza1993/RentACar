package com.team.rentacar.activities;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.team.rentacar.contracts.Communicator;
import com.team.rentacar.models.VendorsDetailModel;

import java.util.*;

public class BookingActivity extends AppCompatActivity implements Communicator.IBookings {

    private Toolbar toolbar;
    private RecyclerView bookingRecycler;
    private DatabaseReference bookingReference;
    ArrayList<VendorsDetailModel> bookingList = new ArrayList<>();
    private BookingsAdapter bookingAdapter;
    private LinearLayoutManager layoutManager;
    private DatabaseReference vendorsDetailReference;
    private DatabaseReference userDefaultDataReference;
    private int extra;
    String id = "";
    String carName = "";
    String carAddress = "";
    String carImage = "";
    String hourlyRate = "";
    String vendorName = "";
    String uid = "";
    String bookedBy = "";
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        toolbar = findViewById(R.id.booking_toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedpreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        role = sharedpreferences.getString("role", "user");
        if (role.equals("user"))
            getSupportActionBar().setTitle("Your Bookings");
        else
            getSupportActionBar().setTitle("Bookings");
        extra = getIntent().getIntExtra("extra", 1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookingReference = FirebaseDatabase.getInstance().getReference().child("Bookings");
        userDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("Users");
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
        if (extra == 1)
            populateUserBookingArray();
        else if (extra == 2)
            populateadminBookingArray();
        if (role.equals("user"))
            bookingAdapter = new BookingsAdapter(BookingActivity.this, bookingList, 1);
        else
            bookingAdapter = new BookingsAdapter(BookingActivity.this, bookingList, 2);
        layoutManager = new LinearLayoutManager(BookingActivity.this, RecyclerView.VERTICAL, false);
        bookingRecycler.setLayoutManager(layoutManager);
        bookingRecycler.setAdapter(bookingAdapter);


    }

    private void populateUserBookingArray() {
        bookingReference.addValueEventListener
                (new ValueEventListener() {

                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         if (!bookingList.isEmpty())
                             bookingList.clear();
                         HashMap hashMap = (HashMap) dataSnapshot.getValue();
                         if(hashMap==null)
                             return;
                         try {
                             Set keys = hashMap.keySet();
                             Object[] array = new String[6];

                             array = keys.toArray();
                             if(array!=null)
                             for (Object o : array) {
                                 //  vendorsDetailReference.child(o.toString())

                                 HashMap map = (HashMap) dataSnapshot.child(o.toString()).child(FirebaseAuth.getInstance().getUid()).getValue();
                                 Set setcarKeys=null;
                                 Object[] arrayCarKeys=null;
                                 if(map!=null) {
                                     setcarKeys = map.keySet();
                                     arrayCarKeys = setcarKeys.toArray();
                                 }
                                 if(arrayCarKeys!=null)
                                 for (Object ob : arrayCarKeys) {
                                     vendorsDetailReference.child(o.toString()).child("vendor_cars").
                                             child(ob.toString()).
                                             addValueEventListener
                                                     (new ValueEventListener() {
                                                          @Override
                                                          public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
//                        Integer id=dataSnapshot.child(o.toString()).child("id").getValue(Integer.class);
                                                              String id = dataSnapshot2.child("id").getValue(String.class);
                                                              String carName = dataSnapshot2.child("car_name").getValue(String.class);
                                                              String carAddress = dataSnapshot2.child("car_address").getValue(String.class);
                                                              String carImage = dataSnapshot2.child("car_thumb_image").getValue(String.class);
                                                              String driverName = dataSnapshot2.child("driver_name").getValue(String.class);
                                                              String driverNumber = dataSnapshot2.child("driver_number").getValue(String.class);
                                                              String discount = dataSnapshot2.child("discount").getValue(String.class);

                                                              try {
                                                                  hourlyRate = dataSnapshot.child(o.toString()).child(FirebaseAuth.getInstance().getUid()).child(ob.toString()).child("rent_price").getValue(String.class);
                                                              }catch (Exception e){

                                                              }
                                                              String vendorName = dataSnapshot2.child("vendor_name").getValue(String.class);
                                                              bookingList.add(new VendorsDetailModel(id, carImage, carName, vendorName, carAddress, hourlyRate,"",true,"",driverName,driverNumber,Integer.parseInt(discount)));
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

    private void populateadminBookingArray() {

        bookingReference.addListenerForSingleValueEvent
                (new ValueEventListener() {

                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         bookingList.clear();
                         HashMap hashMap = (HashMap) dataSnapshot.getValue();
                         if(hashMap==null)
                             return;
                         try {
                             Set keys = hashMap.keySet();
                             Object[] array = keys.toArray();
                             if(array!=null)
                             for (Object o : array) {
                                 //  vendorsDetailReference.child(o.toString())

                                 HashMap map = (HashMap) dataSnapshot.child(o.toString()).getValue();
                                 Set setcarKeys=null;
                                 Object[] arrayCarKeys=null;
                                 if(map!=null)
                                  setcarKeys = map.keySet();
                                 if(setcarKeys!=null)
                                 arrayCarKeys = setcarKeys.toArray();
                                 if(arrayCarKeys!=null)
                                 for (Object ob : arrayCarKeys) {

                                     bookingReference.child(o.toString()).child(ob.toString()).addValueEventListener(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                             bookingList.clear();
                                             HashMap hashMap = (HashMap) dataSnapshot.getValue();
                                             Set keys = null;
                                             Object[] array;
                                             if(hashMap!=null)
                                                  keys = hashMap.keySet();
                                             array=null;
                                             if(keys!=null)
                                                 array = keys.toArray();
                                             if(array!=null)
                                             for (Object obb : array) {

                                                 bookingReference.child(o.toString()).child(ob.toString()).child(obb.toString()).addValueEventListener(new ValueEventListener() {
                                                     @Override
                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                         bookedBy = dataSnapshot.child("booked_by").getValue(String.class);
                                                         hourlyRate = dataSnapshot.child("rent_price").getValue(String.class);
                                                         uid = dataSnapshot.child("uid").getValue(String.class);


                                                     }

                                                     @Override
                                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                                     }
                                                 });
                                                 vendorsDetailReference.child(o.toString()).child("vendor_cars").
                                                         child(obb.toString()).
                                                         addListenerForSingleValueEvent(
                                                                 new ValueEventListener() {
                                                                      @Override
                                                                      public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
//                        Integer id=dataSnapshot.child(o.toString()).child("id").getValue(Integer.class);
                                                                          id = dataSnapshot2.child("id").getValue(String.class);
                                                                          carName = dataSnapshot2.child("car_name").getValue(String.class);
                                                                          carAddress = dataSnapshot2.child("car_address").getValue(String.class);
                                                                          carImage = dataSnapshot2.child("car_thumb_image").getValue(String.class);
                                                                          vendorName = dataSnapshot2.child("vendor_name").getValue(String.class);
                                                                          String driverName = dataSnapshot2.child("driver_name").getValue(String.class);
                                                                          String driverNumber = dataSnapshot2.child("driver_number").getValue(String.class);
                                                                          String discount = dataSnapshot2.child("discount").getValue(String.class);

                                                                          bookingList.add(new VendorsDetailModel(id, carImage, carName, vendorName, carAddress, hourlyRate,bookedBy,true,uid,driverName,driverNumber,Integer.parseInt(discount)));

                                                                          bookingAdapter.setIBookingListener(BookingActivity.this);
                                                                          bookingAdapter.notifyDataSetChanged();
                                                                      }

                                                                      @Override
                                                                      public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                      }
                                                                  }


                                                                 );


//                                                 String carAddress = dataSnapshot.child("car_address").getValue(String.class);
//                                                 String carImage = dataSnapshot.child("car_thumb_image").getValue(String.class);
//                                                 String hourlyRate = dataSnapshot.child(o.toString()).child(FirebaseAuth.getInstance().getUid()).child(ob.toString()).child("rent_price").getValue(String.class);
//                                                 String vendorName = dataSnapshot.child("vendor_name").getValue(String.class);
//                                                 bookingList.add(new VendorsDetailModel(id, carImage, carName, vendorName, carAddress, hourlyRate));
//                                                 bookingAdapter.notifyDataSetChanged();
                                             }


                                         }

                                         @Override
                                         public void onCancelled(@NonNull DatabaseError databaseError) {

                                         }
                                     });
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

    @Override
    public void cancel(String id,String vendorName,String uid,int position) {


        Map<String,Object> map=new HashMap<String,Object>();
        map.put("isBooked",false);

        vendorsDetailReference.child(vendorName).child("vendor_cars").child(id).updateChildren(map);
        bookingReference.child(vendorName).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           dataSnapshot.child(id).getRef().removeValue();
           bookingList.clear();
         //  bookingList.remove(position);
           bookingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
