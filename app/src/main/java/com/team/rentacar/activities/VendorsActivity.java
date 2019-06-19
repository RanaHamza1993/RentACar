package com.team.rentacar.activities;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.team.rentacar.R;
import com.team.rentacar.adapters.VendorsAdapter;
import com.team.rentacar.contracts.Communicator;
import com.team.rentacar.models.VendorsModel;

import java.util.ArrayList;

public class VendorsActivity extends AppCompatActivity implements Communicator.homeNavigator {

    private Toolbar toolbar;
    private LinearLayoutManager layoutManager;
    private RecyclerView vendorsRecycler;
    private VendorsAdapter vendorsAdapter;
    ArrayList<VendorsModel> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors);
        toolbar = findViewById(R.id.vendors_toolbar);
        vendorsRecycler=findViewById(R.id.vendors_recycler);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vendors");
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
    //    populateAdminArray();

            vendorsAdapter = new VendorsAdapter(VendorsActivity.this, arrayList);
        layoutManager = new LinearLayoutManager(VendorsActivity.this,RecyclerView.VERTICAL,false);
        vendorsAdapter.setCommunicatorNavigator(this);
        vendorsRecycler.setLayoutManager(layoutManager);
        vendorsRecycler.setAdapter(vendorsAdapter);


    }
    private void populateArray() {
        arrayList.add(new VendorsModel(1,R.drawable.bookings, "Toyota"));
        arrayList.add(new VendorsModel(2,R.drawable.bookings, "Honda"));
        arrayList.add(new VendorsModel(3,R.drawable.bookings, "Suzuki"));
        arrayList.add(new VendorsModel(4,R.drawable.bookings, "Japanese"));
        arrayList.add(new VendorsModel(5,R.drawable.bookings, "Bmw"));
        arrayList.add(new VendorsModel(6,R.drawable.bookings, "Others"));
    }

    @Override
    public void navigateToOtherActivities(int id) {
        Intent intent=new Intent(VendorsActivity.this,VendorsDetailActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}
