package com.team.rentacar.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.team.rentacar.adapters.VendorsAdapter;
import com.team.rentacar.baseclasses.BaseActivity;
import com.team.rentacar.contracts.Communicator;
import com.team.rentacar.models.VendorsDetailModel;
import com.team.rentacar.models.VendorsModel;
import es.dmoral.toasty.Toasty;

import java.util.*;

public class VendorsDetailActivity extends BaseActivity implements Communicator.IRent {

    Toolbar toolbar;
    private RecyclerView vendorsDetailRecycler;
    private LinearLayoutManager layoutManager;
    private VendorsAdapter vendorsAdapter;
    private String vendorName = "";
    DatabaseReference vendorsDetailReference;
    private DatabaseReference userReference;
    private DatabaseReference bookingReference;
    Spinner rentDaysSpinner;

    int rentDays = 1;
    ArrayList<VendorsDetailModel> arrayList = new ArrayList<>();
    String[] days = new String[]{"1 Day", "2 Days", "3 Days"};
    private int rentValue=0;
    private String discount="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors_detail);
        toolbar = findViewById(R.id.vendors_detail_toolbar);
        vendorName = getIntent().getStringExtra("vendor");
        vendorsDetailRecycler = findViewById(R.id.vendors_detail_recycler);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vendors Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        vendorsDetailReference = FirebaseDatabase.getInstance().getReference().child("Vendors").child(vendorName).child("vendor_cars");
        bookingReference = FirebaseDatabase.getInstance().getReference().child("Bookings").child(vendorName);
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());

        setAdapter();
    }

    private void setAdapter() {

        populateArray();
        vendorsAdapter = new VendorsAdapter(VendorsDetailActivity.this, arrayList, 2);
        layoutManager = new LinearLayoutManager(VendorsDetailActivity.this, RecyclerView.VERTICAL, false);
        vendorsAdapter.setRentCommunicator(this);
        vendorsDetailRecycler.setLayoutManager(layoutManager);
        vendorsDetailRecycler.setAdapter(vendorsAdapter);


    }

    private void populateArray() {
        vendorsDetailReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!arrayList.isEmpty())
                    arrayList.clear();
                HashMap hashMap = (HashMap) dataSnapshot.getValue();
                try {
                    Set keys = hashMap.keySet();
                    Object[] array = new String[6];
                    array = keys.toArray();
                    for (Object o : array) {

                        HashMap map = (HashMap) dataSnapshot.child(o.toString()).getValue();
//                        Integer id=dataSnapshot.child(o.toString()).child("id").getValue(Integer.class);
                        String id = dataSnapshot.child(o.toString()).child("id").getValue(String.class);
                        String carName = dataSnapshot.child(o.toString()).child("car_name").getValue(String.class);
                        String carAddress = dataSnapshot.child(o.toString()).child("car_address").getValue(String.class);
                        String carImage = dataSnapshot.child(o.toString()).child("car_thumb_image").getValue(String.class);
                        String hourlyRate = dataSnapshot.child(o.toString()).child("hourly_rate").getValue(String.class);
                        String driverName = dataSnapshot.child(o.toString()).child("driver_name").getValue(String.class);
                        String driverNumber = dataSnapshot.child(o.toString()).child("driver_number").getValue(String.class);
                        discount = dataSnapshot.child(o.toString()).child("discount").getValue(String.class);
                        String vendorName = dataSnapshot.child(o.toString()).child("vendor_name").getValue(String.class);
                        boolean isBooked = dataSnapshot.child(o.toString()).child("isBooked").getValue(Boolean.class);


                        arrayList.add(new VendorsDetailModel(id, carImage, carName, vendorName, carAddress, hourlyRate,"",isBooked,"",driverName,driverNumber,Integer.parseInt(discount)));
               //         vendorsAdapter.notifyDataSetChanged();

                    }
                } catch (Exception e) {

                }
                vendorsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        arrayList.add(new VendorsDetailModel(5,6,R.drawable.bookings, "Bmw","Bmw","Qartaba Chowk","5000 Rs"));
    }

    void showRentDialog(String carId) {
        Dialog rentDialog = new Dialog(this);
        rentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rentDialog.setCancelable(true);
        rentDialog.setContentView(R.layout.rent_dialog);
        rentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rentDaysSpinner = rentDialog.findViewById(R.id.rent_days_spinner);
        TextView rentPrice = rentDialog.findViewById(R.id.rent_price);
        TextView submit = rentDialog.findViewById(R.id.rent_it);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days);
        rentDaysSpinner.setAdapter(adapter);
        rentDaysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rentDays = Integer.parseInt(days[position].substring(0, 1));
                vendorsDetailReference.child(carId).child("hourly_rate").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        rentValue = Integer.parseInt(dataSnapshot.getValue(String.class));
                        String days = "";
                        if (rentDays == 1)
                            days = "Day";
                        else
                            days = "Days";
                        rentPrice.setText("You have to pay " + (rentValue * rentDays) + " for " + rentDays + " " + days);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        submit.setOnClickListener(v -> {
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.child("user_name").getValue(String.class);
                    String userCnic = dataSnapshot.child("user_cnic").getValue(String.class);
                    String userNumber = dataSnapshot.child("user_phone").getValue(String.class);
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("Booked",carId);
                    map.put("rent_days",String.valueOf(rentDays));
                    map.put("rent_price",String.valueOf(rentValue*rentDays-Integer.parseInt(discount)));
                    map.put("booked_by",username);
                    map.put("user_number",userNumber);
                    map.put("user_cnic",userCnic);
                    map.put("vendor_name",vendorName);
                    map.put("uid",FirebaseAuth.getInstance().getUid());
                    Map<String,Object> vendorDetail=new HashMap<String,Object>();
                    vendorDetail.put("isBooked",true);
                    vendorsDetailReference.child(carId).updateChildren(vendorDetail);
                    bookingReference.child(FirebaseAuth.getInstance().getUid()).child(carId).updateChildren(map).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    showSuccessMessage("Congratulation  Mr. " + username + " you have booked this vehicle successfully");
                                    rentDialog.dismiss();
                                }
                            });

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
        rentDialog.show();
    }

    @Override
    public void rentIt(String id) {
        showRentDialog(id);
    }
}
