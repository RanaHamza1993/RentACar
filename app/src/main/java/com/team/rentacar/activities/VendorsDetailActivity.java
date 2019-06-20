package com.team.rentacar.activities;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import com.team.rentacar.R;
import com.team.rentacar.adapters.VendorsAdapter;
import com.team.rentacar.models.VendorsDetailModel;
import com.team.rentacar.models.VendorsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class VendorsDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    private RecyclerView vendorsDetailRecycler;
    private LinearLayoutManager layoutManager;
    private VendorsAdapter vendorsAdapter;
    private String vendorName="";
    DatabaseReference vendorsDetailReference;
    ArrayList<VendorsDetailModel> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors_detail);
        toolbar = findViewById(R.id.vendors_detail_toolbar);
        vendorName=getIntent().getStringExtra("vendor");
        vendorsDetailRecycler=findViewById(R.id.vendors_detail_recycler);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vendors Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        vendorsDetailReference= FirebaseDatabase.getInstance().getReference().child("Vendors").child(vendorName).child("vendor_cars");
        setAdapter();
    }

    private void setAdapter(){

        populateArray();
        ArrayList<VendorsDetailModel> list=new ArrayList<VendorsDetailModel>();
//        for (int i = 0; i <arrayList.size() ; i++) {
//            if(arrayList.get(i).getVendorId()==vendorID){
//                list.add(arrayList.get(i));
//            }
//        }
        vendorsAdapter = new VendorsAdapter(VendorsDetailActivity.this, list,2);
        layoutManager = new LinearLayoutManager(VendorsDetailActivity.this,RecyclerView.VERTICAL,false);
        //vendorsAdapter.setCommunicatorNavigator(this);
        vendorsDetailRecycler.setLayoutManager(layoutManager);
        vendorsDetailRecycler.setAdapter(vendorsAdapter);


    }
    private void populateArray() {
        vendorsDetailReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!arrayList.isEmpty())
                    arrayList.clear();
                HashMap hashMap = (HashMap) dataSnapshot.getValue();
                try {
                    Set keys = hashMap.keySet();
                    Object[] array = new String[6];
                    array = keys.toArray();
                    for (Object o : array) {

                        HashMap map= (HashMap) dataSnapshot.child(o.toString()).getValue();
//                        Integer id=dataSnapshot.child(o.toString()).child("id").getValue(Integer.class);
                        String id=dataSnapshot.child(o.toString()).child("id").getValue(String.class);
                        String carName=dataSnapshot.child(o.toString()).child("car_name").getValue(String.class);
                        String carAddress=dataSnapshot.child(o.toString()).child("car_address").getValue(String.class);
                        String carImage=dataSnapshot.child(o.toString()).child("car_thumb_image").getValue(String.class);
                        String hourlyRate=dataSnapshot.child(o.toString()).child("hourly_rate").getValue(String.class);
                        String vendorName=dataSnapshot.child(o.toString()).child("vendor_name").getValue(String.class);


                        arrayList.add(new VendorsDetailModel(id,carImage, carName,vendorName,carAddress,hourlyRate));

                    }
                }catch (Exception e){

                }
                vendorsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        arrayList.add(new VendorsDetailModel(1,1,R.drawable.bookings, "Gli","Toyota","Gulberg","2000 Rs"));
//        arrayList.add(new VendorsDetailModel(1,2,R.drawable.bookings, "Xli","Toyota","Model Town","1700 Rs"));
//        arrayList.add(new VendorsDetailModel(2,3,R.drawable.bookings, "Civic","Honda","Green Town","2800 Rs"));
//        arrayList.add(new VendorsDetailModel(3,4,R.drawable.bookings, "Cultus","Suzuki","Faisal Town","1500 Rs"));
//        arrayList.add(new VendorsDetailModel(4,5,R.drawable.bookings, "Vitz","Japanese","Johar Town","1500 Rs"));
//        arrayList.add(new VendorsDetailModel(5,6,R.drawable.bookings, "Bmw","Bmw","Qartaba Chowk","5000 Rs"));

    }
}
