package com.team.rentacar.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.team.rentacar.R;
import com.team.rentacar.baseclasses.BaseActivity;

public class RegisterActivity extends BaseActivity {
    private Toolbar toolbar;
    private AppCompatEditText name;
    private AppCompatEditText email;
    private AppCompatEditText password;
    private AppCompatEditText cnicNumber;
    private AppCompatEditText phoneNumber;
    private TextView register;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference userDefaultDataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Signup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        name = findViewById(R.id.name);
        password = findViewById(R.id.pwd);
        email = findViewById(R.id.email);
        register = findViewById(R.id.register);
        cnicNumber = findViewById(R.id.cnic);
        phoneNumber = findViewById(R.id.phone_no);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String nm = name.getText().toString();
                String pwd = password.getText().toString();
                String em = email.getText().toString();
                String cnic = cnicNumber.getText().toString();
                String phonno = phoneNumber.getText().toString();
                RegisterAccount(nm, em, pwd,cnic,phonno);
            }
        });
    }

    private void RegisterAccount(final String name, final String em, final String pwd,final String cnic,final String phonNo) {

        if (name.isEmpty()) {
            showErrorMessage("Please enter valid name");
            return;
        }
        // Toasty.error(this, "Please enter valid name", Toast.LENGTH_LONG, true).show();

        if (em.isEmpty()) {
            showErrorMessage("Please enter valid email");
            return;
        }
        //Toasty.error(this, "Please enter valid email", Toast.LENGTH_LONG, true).show();

        if (pwd.isEmpty()) {
            showErrorMessage("Please enter valid password");
            return;
        }
        if (cnic.isEmpty()) {
            showErrorMessage("Please enter valid cnic number");
            return;
        }
        if (phonNo.isEmpty()) {
            showErrorMessage("Please enter valid phone number");

        }
            // Toasty.error(this, "Please enter valid password", Toast.LENGTH_LONG, true).show();
        else {
            showDialog("Creating new Account", "wait, while we are creating account for you");
            mAuth.createUserWithEmailAndPassword(em, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Toasty.success(SignupActivity.this, "Register Successfully", Toast.LENGTH_LONG, true).show();

                        String uid = mAuth.getCurrentUser().getUid();
                        String deviceToken= FirebaseInstanceId.getInstance().getToken();
                        userDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                        userDefaultDataReference.child("user_name").setValue(name);
                         userDefaultDataReference.child("user_email").setValue(em);
                          userDefaultDataReference.child("user_cnic").setValue(cnic);
                          userDefaultDataReference.child("user_phone").setValue(phonNo);
                          userDefaultDataReference.child("user_image").setValue("default_profile");
                          userDefaultDataReference.child("user_address").setValue("");
                        userDefaultDataReference.child("device_token").setValue(deviceToken)
                      //  userDefaultDataReference.child("user_status").setValue("Hey i am using weChat developed by Hamza");
                       // userDefaultDataReference.child("user_image").setValue("default_profile");
                       // userDefaultDataReference.child("user_thumb_image").setValue("default_profile")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {

                                        }
                                    }
                                });
                        showSuccessMessage("Register Successfully");

                    } else {
                        // Toasty.error(SignupActivity.this,"Not register successfully",Toast.LENGTH_SHORT,true).show();
                        showErrorMessage("Not register successfully");
                    }
                    dismissDialog();
                }
            });
        }


    }
}
