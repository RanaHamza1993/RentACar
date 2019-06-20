package com.team.rentacar.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class PostCarsOfVendors extends BaseActivity {

    private Toolbar toolbar;
    private DatabaseReference vendorsReference;
    private static final int ImageRequestCode = 1;
    private static final int PermissionRequestCode = 2;
    private StorageReference databasestorage;
    private StorageReference thumbImagetorage;
    Map userMap = new HashMap();
    Bitmap thumb_bitmap;
    CircularImageView carImage;
    EditText carName;
    EditText vendorName;
    EditText carAddress;
    EditText hourlyPrice;
    TextView addData;
    Spinner vendorsSpinner;
    String vendName="";
    Intent galleryIntent;
    File thumbImagePath;
    Uri resultUri;
    String[] items = new String[]{"Toyota", "Honda", "Suzuki", "Japanese", "Bmw", "Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_cars_of_vendors);
        toolbar = findViewById(R.id.post_car_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post Car");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        carName = findViewById(R.id.name);
        carAddress = findViewById(R.id.location);
        carImage = findViewById(R.id.car_image);
        carAddress = findViewById(R.id.location);
        hourlyPrice = findViewById(R.id.hourly_rate);
        addData = findViewById(R.id.add_car);
        vendorsSpinner = findViewById(R.id.vendorsSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        vendorsSpinner.setAdapter(adapter);
        vendorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vendName = items[position];
                vendorsReference = FirebaseDatabase.getInstance().getReference().child("Vendors").child(vendName);

               // showSuccessMessage(vendName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        carImage.setOnClickListener(v->{
            chooseImage();
        });
        databasestorage = FirebaseStorage.getInstance().getReference().child("car_Images");
        thumbImagetorage = FirebaseStorage.getInstance().getReference().child("car_thumb_Images");
        addData.setOnClickListener(v->{
            postCar();
        });

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
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                thumbImagePath = new File(resultUri.getPath());

                try {

                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(60)
                            .compressToBitmap(thumbImagePath);

                } catch (IOException e) {
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }
    public void AllowRunTimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(PostCarsOfVendors.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            //   Toast.makeText(MainActivity.this,"READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(PostCarsOfVendors.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

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
        galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, ImageRequestCode);
    }

    void postCar() {

        if(resultUri==null){
            showErrorMessage("Please upload a car image");
            return;
        }
        if (carName.getText().toString().isEmpty()) {
            showErrorMessage("Ivalid car name");
            return;
        }
        if (vendName.equals("")) {
            showErrorMessage("Invalid vendor name");
            return;
        }
        if (carAddress.getText().toString().isEmpty()) {
            showErrorMessage("Invalid address");
            return;
        }
        if(hourlyPrice.getText().toString().isEmpty()){
            showErrorMessage("Invalid hourly price");
        }

        else {
            showDialog("Uploading Image", "Please wait while image uploading");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            final byte[] thumb_byte = byteArrayOutputStream.toByteArray();
            vendorsReference=vendorsReference.push();
            StorageReference filePath = databasestorage.child( vendorsReference.push()+ ".jpg");
            final StorageReference thumbFilePath = thumbImagetorage.child(vendorsReference + ".jpg");
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
                                        userMap.put("car_image", uri.toString());
                                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                userMap.put("car_thumb_image", uri.toString());
                                                vendorsReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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


        Map<String, Object> map = new HashMap<>();
            map.put("car_name", carName.getText().toString());
            map.put("vendor_name", vendName);
            map.put("car_address", carAddress.getText().toString());
            map.put("hourly_rate", hourlyPrice.getText().toString());
            vendorsReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        showSuccessMessage("Data Posted successfully");
                    else
                        showErrorMessage("Data did not Post successfully");
                }

                ;
            });
        }
    }

}
