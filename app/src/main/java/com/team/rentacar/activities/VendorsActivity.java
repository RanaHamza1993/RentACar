package com.team.rentacar.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.database.*;
import com.team.rentacar.R;
import com.team.rentacar.adapters.VendorsAdapter;
import com.team.rentacar.contracts.Communicator;
import com.team.rentacar.models.VendorsModel;
import com.team.rentacar.utilities.StartNewActivity;

import java.lang.reflect.Array;
import java.util.*;

public class VendorsActivity extends AppCompatActivity implements Communicator.homeNavigator {

    private static final String TAG = VendorsActivity.class.getSimpleName();
    private Toolbar toolbar;
    String role="user";
    private LinearLayoutManager layoutManager;
    private RecyclerView vendorsRecycler;
    private VendorsAdapter vendorsAdapter;
    ArrayList<VendorsModel> arrayList = new ArrayList<>();
    DatabaseReference vendorsReference;
    int flag=1;
    int[] drawables=new int[]{R.drawable.toyota_cov,R.drawable.honda_cov,R.drawable.suzuki_cov,R.drawable.jap_cov,R.drawable.bmw_cov,R.drawable.others_cov};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors);
        SharedPreferences sharedpreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        role=sharedpreferences.getString("role","");
        toolbar = findViewById(R.id.vendors_toolbar);
        vendorsRecycler=findViewById(R.id.vendors_recycler);
        setSupportActionBar(toolbar);
        flag=getIntent().getIntExtra("extra",1);
        getSupportActionBar().setTitle("Vendors");
        vendorsReference=FirebaseDatabase.getInstance().getReference().child("Vendors");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        setAdapter();
    }

    private void setAdapter(){

        populateArray();
        vendorsAdapter = new VendorsAdapter(VendorsActivity.this, arrayList,drawables);
        layoutManager = new LinearLayoutManager(VendorsActivity.this,RecyclerView.VERTICAL,false);
        vendorsAdapter.setCommunicatorNavigator(this);
        vendorsRecycler.setLayoutManager(layoutManager);
        vendorsRecycler.setAdapter(vendorsAdapter);


    }
    private void populateArray() {
        vendorsReference.addValueEventListener(new ValueEventListener() {

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

                        String name=dataSnapshot.child(o.toString()).child("vendor_name").getValue(String.class);
                        Integer id=dataSnapshot.child(o.toString()).child("id").getValue(Integer.class);

                        arrayList.add(new VendorsModel(id, R.drawable.bookings, name));
                        Collections.sort(arrayList);

                    }
                }catch (Exception e){

                }
                vendorsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
//        arrayList.add(new VendorsModel(2,R.drawable.bookings, "Honda"));
//        arrayList.add(new VendorsModel(3,R.drawable.bookings, "Suzuki"));
//        arrayList.add(new VendorsModel(4,R.drawable.bookings, "Japanese"));
//        arrayList.add(new VendorsModel(5,R.drawable.bookings, "Bmw"));
//        arrayList.add(new VendorsModel(6,R.drawable.bookings, "Others"));
    }

    @Override
    public void navigateToOtherActivities(int id,String vendor) {
        if(role.equals("user")) {
            Intent intent = new Intent(VendorsActivity.this, VendorsDetailActivity.class);
            intent.putExtra("vendor", vendor);
            startActivity(intent);
        }else{
            Intent intent = new Intent(VendorsActivity.this, PostDiscounts.class);
            intent.putExtra("vendor", vendor);
            startActivity(intent);        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
