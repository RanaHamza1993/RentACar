package com.team.rentacar.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.iid.FirebaseInstanceId;
import com.team.rentacar.R;
import com.team.rentacar.baseclasses.BaseActivity;
import com.team.rentacar.utilities.StartNewActivity;

public class LoginActivity extends BaseActivity {

    private Toolbar toolbar;
    FirebaseAuth mAuth;
    private AppCompatEditText email;
    private AppCompatEditText password;
    private TextView login;
    private TextView signUp;
   // private TextView termsTxt;
    private TextView termsOfUse;
    private TextView forgotPassword;
    private Switch termsSwitch;
    private CheckBox adminCheck;
    private DatabaseReference adminReference;
    private DatabaseReference userReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        signUp=findViewById(R.id.b_register);
        termsOfUse=findViewById(R.id.terms_of_use);
        forgotPassword=findViewById(R.id.forgot_password);
        adminCheck=findViewById(R.id.admin_chehck);
      //  termsTxt=findViewById(R.id.terms_txt);
        termsSwitch=findViewById(R.id.terms_switch);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
       // toolbar=findViewById(R.id.login_toolbar);
        mAuth=FirebaseAuth.getInstance();
        userReference= FirebaseDatabase.getInstance().getReference().child("Users");
       // setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Login");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                onBackPressed();
//            }
//        });
        email=findViewById(R.id.l_email);
        password=findViewById(R.id.l_password);
        login=findViewById(R.id.b_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String em=email.getText().toString();
                String pwd=password.getText().toString();
                loginUser(em,pwd);
            }
        });


        termsOfUse.setOnClickListener(v->{
            new StartNewActivity<TermsOfUse>(LoginActivity.this,TermsOfUse.class);
        });
        forgotPassword.setOnClickListener(v->{
            new StartNewActivity<ForgotPassword>(LoginActivity.this,ForgotPassword.class);
        });

    }

    private void loginUser(String em, String pwd) {
        if(em.isEmpty()) {
            showErrorMessage("Invalid Email");
            return;
        }
        if(pwd.isEmpty()) {
            showErrorMessage("Invalid Password");
            return;
        }
        if (!termsSwitch.isChecked()) {
            showErrorMessage("Please check terms of use checkbox");
        }
        else{

            showDialog("Loging into your account","Please wait while e are logging into your account");

            if(!adminCheck.isChecked()) {
                mAuth.signInWithEmailAndPassword(em, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            String currentUser = mAuth.getCurrentUser().getUid();

                            navigateToHome("user");
                            showSuccessMessage("Login successfull");
                            finish();
//                        userReference.child(currentUser).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//
//                            }
//                        });
//
                        } else {
                            showErrorMessage("Invalid Email or passowrd");
                        }
                        dismissDialog();
                    }
                });
            }else{
                adminReference= FirebaseDatabase.getInstance().getReference().child("Admin");
                adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!email.getText().toString().equals(dataSnapshot.child("email").getValue(String.class))){
                            showErrorMessage("Invalid login credentials");
                            dismissDialog();
                            return;
                        }else if(!password.getText().toString().equals(dataSnapshot.child("password").getValue(String.class))) {
                            dismissDialog();
                            showErrorMessage("Invalid login credentials");
                            return;
                        }
                        else {
                            navigateToHome("admin");
                            showSuccessMessage("Login as Admin");
                        }
                        dismissDialog();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dismissDialog();
                    }
                });
              //  dismissDialog();
            }
        }
    }
    private void navigateToHome(String role){
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedpreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(role.equals("user"))
        editor.putString("token", deviceToken);
        else
            editor.putString("token", "admin");
        editor.putString("role", role);
        editor.commit();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("role",role);
        startActivity(intent);
    }
}
