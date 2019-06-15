package com.team.rentacar.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.team.rentacar.R;
import com.team.rentacar.java.ConnectionDetector;
import es.dmoral.toasty.Toasty;

public class SplashActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//            ActionBar actionBar = getSupportActionBar();
//            actionBar.hide();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ConnectionDetector connectionDetector=new ConnectionDetector(this);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SharedPreferences sharedPreferences=getSharedPreferences("login", Context.MODE_PRIVATE);


                String token = sharedPreferences.getString("token", "");//"No name defined" is the default value.

                if(connectionDetector.isconnected()==true)
                {
                    if(!token.equals("")) {

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);

                        startActivity(intent);

                        SplashActivity.this.finish();

                    }
                    else
                    {

                        Intent intent =new Intent(SplashActivity.this, LoginActivity.class);

                        startActivity(intent);

                        SplashActivity.this.finish();
                    }
                }
                else if(connectionDetector.isconnected()==false)
                {
                    Toasty.info(SplashActivity.this, "Please connect to Internet", Toast.LENGTH_SHORT, true).show();

                    //      Toast.makeText(SplashScreen.this,"No Net connected pleasse try again",Toast.LENGTH_SHORT).show();

                    Snackbar snk = Snackbar.make(findViewById(R.id.splash), "Please connect to Internet.", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            startActivity(getIntent());
                        }
                    });
                    snk.show();

                }
                //finish();
            }
        }, secondsDelayed * 3000);



    }
    @Override
    protected void onStop() {
        super.onStop();

    }
}
