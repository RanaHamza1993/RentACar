package com.team.rentacar.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

    private static final int ImageRequestCode =1 ;
    private static final int PermissionRequestCode =2 ;
    private Toolbar toolbar;
    private DatabaseReference userDataReference;
    private FirebaseAuth mAuth;
    private StorageReference databasestorage;
    private StorageReference thumbImagetorage;
    Map userMap=new HashMap();
    String uid="";
    Bitmap thumb_bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImageRequestCode && resultCode == RESULT_OK && data != null && data.getData() != null) {


            Uri imageUri=data.getData();



            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            showDialog("Uploading Image","Please wait while image uploading");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                File thumbImagePath=new File(resultUri.getPath());

                try{

                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(60)
                            .compressToBitmap(thumbImagePath);

                }catch (IOException e){ }

                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
                final byte[] thumb_byte=byteArrayOutputStream.toByteArray();

                StorageReference filePath=databasestorage.child(uid+".jpg");
                final StorageReference thumbFilePath=thumbImagetorage.child(uid+".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            //showSuccessMessage("Image uploaded Successfully");
                            final Task<Uri> uriTask=task.getResult().getStorage().getDownloadUrl();
                            UploadTask uploadTask=thumbFilePath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    Task<Uri> thumb_url=thumb_task.getResult().getStorage().getDownloadUrl();
                                    thumb_url.addOnSuccessListener(new OnSuccessListener<Uri>() {

                                        @Override
                                        public void onSuccess(Uri uri) {

                                            userMap.put("user_image",uri.toString());
                                            uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    userMap.put("user_thumb_image",uri.toString());
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



                        } else{
                            dismissDialog();

                            showErrorMessage("Error occured while uploading");
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
//            CropImage.activity(imageUri)
//                    .start(this);

    }

    public void AllowRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {

            //   Toast.makeText(MainActivity.this,"READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(ProfileActivity.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

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
    private void chooseImage(){
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,ImageRequestCode);
    }
}
