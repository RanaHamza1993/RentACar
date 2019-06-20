package com.team.rentacar.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.team.rentacar.R;
import com.team.rentacar.adapters.MainPageAdapter;
import com.team.rentacar.baseclasses.BaseActivity;
import com.team.rentacar.contracts.Communicator;
import com.team.rentacar.models.MainPageModel;
import com.team.rentacar.utilities.StartNewActivity;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements Communicator.homeNavigator {

    private  LinearLayoutManager layoutManager;
    private FirebaseAuth mAuth;
    TextView signOut;
    String role="user";
    FirebaseUser currentUser;
    RecyclerView homeRecycler;
    private DatabaseReference databaseReference;
    Toolbar toolbar;
    ArrayList<MainPageModel> arrayList = new ArrayList<>();
    ArrayList<MainPageModel> adminArrayList = new ArrayList<>();
    private MainPageAdapter mainPageAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);
        SharedPreferences sharedpreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        role=sharedpreferences.getString("role","user");
        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Car Glide");
        //signOut=findViewById(R.id.signout);
        mAuth = FirebaseAuth.getInstance();
        homeRecycler=findViewById(R.id.home_recycler);
        setAdapter();

    }
    private void logout() {
        if(role.equals("user")) {
            SharedPreferences sharedpreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("token", "");
            editor.commit();
            mAuth.signOut();
        }
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
        // return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==R.id.action_settings){
            if(currentUser!=null&&role.equals("user")) {
                logout();
            }else if(role.equals("admin"))
                logout();
                else
                showErrorMessage("Invalid User");
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAdapter(){

        populateArray();
        populateAdminArray();

        if(role.equals("user")) {
            mainPageAdapter = new MainPageAdapter(MainActivity.this, arrayList);
        }else if(role.equals("admin"))
            mainPageAdapter = new MainPageAdapter(MainActivity.this, adminArrayList);
        layoutManager = new GridLayoutManager(MainActivity.this,2);
        mainPageAdapter.setCommunicatorNavigator(this);
        homeRecycler.setLayoutManager(layoutManager);
        homeRecycler.setAdapter(mainPageAdapter);
    }
    private void populateArray() {
        arrayList.add(new MainPageModel(1,R.drawable.profile, "Profile Activity"));
        arrayList.add(new MainPageModel(2,R.drawable.bookings, "Booking Activity"));
        arrayList.add(new MainPageModel(3,R.drawable.vendors, "Vendors"));
        arrayList.add(new MainPageModel(4,R.drawable.change_pwd, "Change Password"));
        arrayList.add(new MainPageModel(5,R.drawable.terms, "Terms and Conditions"));

    }

    private void populateAdminArray() {
        adminArrayList.add(new MainPageModel(6,R.drawable.discount, "Discount Activity"));
        adminArrayList.add(new MainPageModel(7,R.drawable.report, "Generate Report Activity"));
        adminArrayList.add(new MainPageModel(8,R.drawable.vendors, "Vendors"));
        adminArrayList.add(new MainPageModel(9,R.drawable.bookings, "Bookings Password"));
    }

    @Override
    public void navigateToOtherActivities(int id,String vendor) {
        if(id==1){
            new StartNewActivity<ProfileActivity>(MainActivity.this,ProfileActivity.class);
        }else if(id==2){
            new StartNewActivity<BookingActivity>(MainActivity.this,BookingActivity.class);
        }else if(id==3 || id==8){
            new StartNewActivity<VendorsActivity>(MainActivity.this,VendorsActivity.class);
        }else if(id==4){
            new StartNewActivity<ChangePassword>(MainActivity.this,ChangePassword.class);
        }else if(id==5){
            new StartNewActivity<TermsOfUse>(MainActivity.this,TermsOfUse.class);
        }
    }
}
