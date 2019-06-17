package com.team.rentacar.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.team.rentacar.R;
import com.team.rentacar.baseclasses.BaseActivity;

public class MainActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    TextView signOut;
    FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signOut=findViewById(R.id.signout);
        mAuth = FirebaseAuth.getInstance();
        signOut.setOnClickListener(v->{
            if(currentUser!=null) {

                SharedPreferences sharedpreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("token", "");
                editor.commit();
                mAuth.signOut();
                logout();
            }else
                showErrorMessage("Invalid User");

        });
    }
    private void logout() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
