package com.team.rentacar.baseclasses;

import android.app.ProgressDialog;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import es.dmoral.toasty.Toasty;

public class BaseActivity extends AppCompatActivity {

    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    protected void showSuccessMessage(String message){
        Toasty.success(this,message, Toast.LENGTH_SHORT,true).show();
    }
    void showInfoMessage(String message){
        Toasty.info(this,message, Toast.LENGTH_SHORT,true).show();
    }
    protected void showErrorMessage(String message){
        Toasty.error(this,message, Toast.LENGTH_SHORT,true).show();
    }
    protected void showDialog(String title, String message){
        loadingBar=new ProgressDialog(this);
        loadingBar.setTitle(title);
        loadingBar.setMessage(message);
        loadingBar.show();
    }
    protected void dismissDialog(){
        if(loadingBar.isShowing())
            loadingBar.dismiss();
    }
}
