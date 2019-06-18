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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.team.rentacar.R;
import com.team.rentacar.baseclasses.BaseActivity;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends BaseActivity {
    private Toolbar toolbar;
    AppCompatEditText currentPassword;
    AppCompatEditText newPassword;
    AppCompatEditText confirmPassword;
    private DatabaseReference usersReference;
    private FirebaseAuth mAuth;
    private TextView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        toolbar = findViewById(R.id.change_pwd_toolbar);
        setSupportActionBar(toolbar);
        submit = findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        currentPassword = findViewById(R.id.current_pwd);
        newPassword = findViewById(R.id.new_pwd);
        confirmPassword = findViewById(R.id.cnfrm_pwd);
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid()).child("user_email");
        submit.setOnClickListener(v -> {
            try {
                updatePassword(currentPassword.getText().toString(), newPassword.getText().toString(),
                        confirmPassword.getText().toString());
            } catch (Exception e) {
                showErrorMessage(e.toString());
            }
        });
    }

    void updatePassword(String currentPassword, String newPassword, String confirmPassword) {
        if (currentPassword.isEmpty()) {
            showErrorMessage("Please enter current passowrd");
            return;
        }
        if (newPassword.isEmpty()) {
            showErrorMessage("Please enter new password");
            return;
        }
        if (confirmPassword.isEmpty()) {
            showErrorMessage("Please enter confirm passowrd");
        }
        if (!newPassword.equals(confirmPassword)) {
            showErrorMessage("Please enter same new password and confirm passowrd");
            return;
        }
        if (currentPassword.equals(confirmPassword)) {
            showErrorMessage("Current and new password is same, No need to update");
            return;
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.

            usersReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String email = dataSnapshot.getValue(String.class);
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, currentPassword);

// Prompt the user to re-provide their sign-in credentials
                    ((FirebaseUser) user).reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //  Log.d(TAG, "Password updated");
                                                    showSuccessMessage("Password updated successfully");
                                                } else {
                                                    // Log.d(TAG, "Error password not updated")
                                                    showErrorMessage("Password not updated");
                                                }
                                            }
                                        });
                                    } else {
                                        // Log.d(TAG, "Error auth failed")
                                        showErrorMessage("Authentication failed");
                                    }
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}
