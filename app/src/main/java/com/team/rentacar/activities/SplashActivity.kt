package com.team.rentacar.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.team.rentacar.R
import com.team.rentacar.kotlin.ConnectionDetector
import es.dmoral.toasty.Toasty

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val connectionDetector = ConnectionDetector(this)

        val secondsDelayed = 1
        Handler().postDelayed({
            val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)


            val token = sharedPreferences.getString("token", "")//"No name defined" is the default value.

            if (connectionDetector.isconnected() === true) {
                if (token != "") {

                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)

                    startActivity(intent)

                    this@SplashActivity.finish()

                } else {

                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)

                    startActivity(intent)

                    this@SplashActivity.finish()
                }
            } else if (connectionDetector.isconnected() === false) {
                Toasty.info(this@SplashActivity, "Please connect to Internet", Toast.LENGTH_SHORT, true).show()

                //      Toast.makeText(SplashScreen.this,"No Net connected pleasse try again",Toast.LENGTH_SHORT).show();

                val snk =
                    Snackbar.make(findViewById(R.id.splash), "Please connect to Internet.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry",
                            View.OnClickListener {
                                finish()
                                startActivity(getIntent())
                            })
                snk.show()

            }
            //finish();
        }, (secondsDelayed * 3000).toLong())

    }

}
