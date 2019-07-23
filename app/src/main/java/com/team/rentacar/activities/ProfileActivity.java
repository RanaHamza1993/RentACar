package com.team.rentacar.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.team.rentacar.R;
import com.team.rentacar.baseclasses.BaseActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import id.zelory.compressor.Compressor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends BaseActivity {

    private static final int ImageRequestCode = 1;
    private static final int PermissionRequestCode = 2;
    private Toolbar toolbar;
    private DatabaseReference userDataReference;
    private DatabaseReference adminDataReference;
    private FirebaseAuth mAuth;
    private StorageReference databasestorage;
    private StorageReference thumbImagetorage;
    Map userMap = new HashMap();
    String uid = "";
    Bitmap thumb_bitmap;
    CircularImageView userImage;
    EditText userName;
    EditText adminName;
    EditText adminEmail;
    EditText adminPassword;
    EditText adminNewPassword;
    EditText userCnic;
    EditText userPhoneNumber;
    EditText userAddress;
    TextView updateData;
    private String role;
    private String currentAdminPassowrd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedpreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        role = sharedpreferences.getString("role", "user");
        if(role.equals("user")) {
            setContentView(R.layout.activity_profile);

            userImage = findViewById(R.id.user_profile);
            userCnic = findViewById(R.id.cnic);
            userName = findViewById(R.id.name);
            userPhoneNumber = findViewById(R.id.phone_no);
            userAddress = findViewById(R.id.address);
            updateData=findViewById(R.id.update);
            uid = mAuth.getInstance().getCurrentUser().getUid();
            userDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            userDataReference.keepSynced(true);
            thumbImagetorage = FirebaseStorage.getInstance().getReference().child("thumb_Images");
            databasestorage = FirebaseStorage.getInstance().getReference().child("Profile_Images");
        }
        else {
            setContentView(R.layout.layout_admin_profile);
            adminName=findViewById(R.id.admin_name);
            adminEmail=findViewById(R.id.admin_email);
            adminPassword=findViewById(R.id.admin_pwd);
            adminNewPassword=findViewById(R.id.admin_new_pwd);
            userImage = findViewById(R.id.user_profile);
            adminDataReference = FirebaseDatabase.getInstance().getReference().child("Admin");
            updateData=findViewById(R.id.update);


            databasestorage = FirebaseStorage.getInstance().getReference().child("admin_Image");
            thumbImagetorage = FirebaseStorage.getInstance().getReference().child("admin_thumb_Image");
        }
        toolbar = findViewById(R.id.profile_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        mAuth = FirebaseAuth.getInstance();





        if(role.equals("user")) {
            userDataReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userName.setText(dataSnapshot.child("user_name").getValue(String.class));
                    userCnic.setText(dataSnapshot.child("user_cnic").getValue(String.class));
                    userPhoneNumber.setText(dataSnapshot.child("user_phone").getValue(String.class));
                    userAddress.setText(dataSnapshot.child("user_address").getValue(String.class));
                    String img = dataSnapshot.child("user_image").getValue().toString();
                    if (img.equals("default_profile"))
                        Glide.with(getApplicationContext()).load(R.drawable.cplaceholder).placeholder(R.drawable.cplaceholder).into(userImage);
                    else
                        Glide.with(getApplicationContext()).load(img).
                                placeholder(R.drawable.cplaceholder).into(userImage);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else if(role.equals("admin")){
            adminDataReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentAdminPassowrd=dataSnapshot.child("password").getValue(String.class);
                    adminName.setText(dataSnapshot.child("name").getValue(String.class));
                    adminEmail.setText(dataSnapshot.child("email").getValue(String.class));
                    String img = dataSnapshot.child("admin_thumb_image").getValue().toString();
                    if (img.equals("default_profile"))
                        Glide.with(getApplicationContext()).load(R.drawable.cplaceholder).placeholder(R.drawable.cplaceholder).into(userImage);
                    else
                        Glide.with(getApplicationContext()).load(img).
                                placeholder(R.drawable.cplaceholder).into(userImage);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        updateData.setOnClickListener(v->{
            if(role.equals("user"))
            updateProfile();
            else
                updateAdminProfile();
        });
        userImage.setOnClickListener(v->{
            chooseImage();
        });
    }

    private void updateAdminProfile() {

        String name = adminName.getText().toString();
        String email = adminEmail.getText().toString();
        String password = adminPassword.getText().toString();
        String newPassword = adminNewPassword.getText().toString();

        if (name.isEmpty()) {
            showErrorMessage("Please enter valid name");
            return;
        }
        if (email.isEmpty()) {
            showErrorMessage("Please enter valid email");
            return;
        }
        if (password.isEmpty()) {
            showErrorMessage("Please enter current password");
            return;
        }
        if (newPassword.isEmpty()) {
            showErrorMessage("Please enter confirm current password");
            return;
        }if (!password.equals(currentAdminPassowrd)) {
            showErrorMessage("You enter wrong current password please try again");

        }else {
            Map<String, Object> map = new HashMap<>();
            map.put("name", adminName.getText().toString());
            map.put("password", adminNewPassword.getText().toString());
            map.put("email", adminEmail.getText().toString());
            adminDataReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        showSuccessMessage("Data updated successfully");
                    else
                        showErrorMessage("Data not updated successfully");
                }

                ;
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImageRequestCode && resultCode == RESULT_OK && data != null && data.getData() != null) {


            Uri imageUri = data.getData();


            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            showDialog("Uploading Image", "Please wait while image uploading");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                File thumbImagePath = new File(resultUri.getPath());

                try {

                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(60)
                            .compressToBitmap(thumbImagePath);

                } catch (IOException e) {
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();
                if(role.equals("user")) {
                    StorageReference filePath = databasestorage.child(uid + ".jpg");
                    final StorageReference thumbFilePath = thumbImagetorage.child(uid + ".jpg");
                    filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {

                                //showSuccessMessage("Image uploaded Successfully");
                                final Task<Uri> uriTask = task.getResult().getStorage().getDownloadUrl();
                                UploadTask uploadTask = thumbFilePath.putBytes(thumb_byte);
                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                        Task<Uri> thumb_url = thumb_task.getResult().getStorage().getDownloadUrl();
                                        thumb_url.addOnSuccessListener(new OnSuccessListener<Uri>() {

                                            @Override
                                            public void onSuccess(Uri uri) {

                                                userMap.put("user_image", uri.toString());
                                                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        userMap.put("user_thumb_image", uri.toString());
                                                        userDataReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                // showSuccessMessage("Image updated successfully");
                                                                dismissDialog();
                                                            }

                                                        });
                                                    }
                                                });

//                                            userDataReference.child("user_thumb_image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    showSuccessMessage("thumb Image Uploaded Successfully");
//                                                }
//                                            });
                                            }
                                        });
                                    }
                                });


                            } else {
                                dismissDialog();

                                showErrorMessage("Error occured while uploading");
                            }
                        }
                    });
                }else{

                    StorageReference filePath = databasestorage.child("admin" + ".jpg");
                    final StorageReference thumbFilePath = thumbImagetorage.child("admin" + ".jpg");
                    filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {

                                //showSuccessMessage("Image uploaded Successfully");
                                final Task<Uri> uriTask = task.getResult().getStorage().getDownloadUrl();
                                UploadTask uploadTask = thumbFilePath.putBytes(thumb_byte);
                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                        Task<Uri> thumb_url = thumb_task.getResult().getStorage().getDownloadUrl();
                                        thumb_url.addOnSuccessListener(new OnSuccessListener<Uri>() {


                                            @Override
                                            public void onSuccess(Uri uri) {
                                                try {
                                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                                    map.put("admin_image", uri.toString());
                                                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            map.put("admin_thumb_image", uri.toString());
                                                            adminDataReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    // showSuccessMessage("Image updated successfully");
                                                                    dismissDialog();
                                                                }

                                                            });
                                                        }
                                                    });
                                                }catch (Exception e){}
//                                            userDataReference.child("user_thumb_image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    showSuccessMessage("thumb Image Uploaded Successfully");
//                                                }
//                                            });
                                            }
                                        });
                                    }
                                });


                            } else {
                                dismissDialog();

                                showErrorMessage("Error occured while uploading");
                            }
                        }
                    });
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
//            CropImage.activity(imageUri)
//                    .start(this);

    }

    public void AllowRunTimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            //   Toast.makeText(MainActivity.this,"READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 2:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    // Toast.makeText(SettingsActivity.this,"Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                    // Toast.makeText(MainActivity.this,"Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    private void chooseImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, ImageRequestCode);
    }

    void updateProfile() {
        String name = userName.getText().toString();
        String cnic = userCnic.getText().toString();
        String number = userPhoneNumber.getText().toString();

        if (name.isEmpty()) {
            showErrorMessage("Please enter valid name");
            return;
        }
        if (cnic.isEmpty()) {
            showErrorMessage("Please enter valid cnic");
            return;
        }
        if (number.isEmpty()) {
            showErrorMessage("Please enter valid phone number");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("user_address", userAddress.getText().toString());
            map.put("user_cnic", userCnic.getText().toString());
            map.put("user_name", userName.getText().toString());
            map.put("user_phone", userPhoneNumber.getText().toString());
            userDataReference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    showSuccessMessage("Data updated successfully");

                }
            }) ;
        }
    }
}
