package com.team.rentacar.activities;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.team.rentacar.R;
import com.team.rentacar.baseclasses.BaseActivity;

public class ForgotPassword extends BaseActivity {

    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    AppCompatEditText email;
    TextView submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.et_mail);
        submit=findViewById(R.id.bfp_submit);
        toolbar = findViewById(R.id.forgot_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        submit.setOnClickListener(v->{
            if(email.getText().toString().isEmpty()) {
                showErrorMessage("Please enter email to receive password link");
            return;
            }
                FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //  Log.d(TAG, "Email sent.");
                                    showSuccessMessage("Email sent successfully");
                                } else{
                                    showErrorMessage("No account found of this email");
                                }
                            }
                        });

        });

    }
}
