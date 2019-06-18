package com.team.rentacar.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
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
    FirebaseUser currentUser;
    RecyclerView homeRecycler;
    private DatabaseReference databaseReference;
    Toolbar toolbar;
    ArrayList<MainPageModel> arrayList = new ArrayList<>();
    private MainPageAdapter mainPageAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Car Glide");
        //signOut=findViewById(R.id.signout);
        mAuth = FirebaseAuth.getInstance();
        homeRecycler=findViewById(R.id.home_recycler);
        setAdapter();

    }
    private void logout() {
        SharedPreferences sharedpreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("token", "");
        editor.commit();
        mAuth.signOut();
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
            if(currentUser!=null) {
                logout();
            }else
                showErrorMessage("Invalid User");
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAdapter(){

        populateArray();

        mainPageAdapter = new MainPageAdapter(MainActivity.this, arrayList);
        layoutManager = new GridLayoutManager(MainActivity.this,2);
        mainPageAdapter.setCommunicatorNavigator(this);
        homeRecycler.setLayoutManager(layoutManager);
        homeRecycler.setAdapter(mainPageAdapter);
    }
    private void populateArray() {
        arrayList.add(new MainPageModel(1,R.drawable.googleicon, "Profile Activity"));
        arrayList.add(new MainPageModel(2,R.drawable.googleicon, "Booking Activity"));
        arrayList.add(new MainPageModel(3,R.drawable.googleicon, "Vendors"));
        arrayList.add(new MainPageModel(4,R.drawable.googleicon, "Fast"));
    }

    @Override
    public void navigateToOtherActivities(int id) {
        if(id==0){
            new StartNewActivity<ProfileActivity>(MainActivity.this,ProfileActivity.class);
        }else if(id==1){
            new StartNewActivity<BookingActivity>(MainActivity.this,BookingActivity.class);
        }
    }
}
