package com.team.rentacar.baseclasses

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.error.VolleyError
import es.dmoral.toasty.Toasty
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

open class BaseActivity : AppCompatActivity() {

    var loadingBar: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    fun showDialog(title: String, message: String) {
        loadingBar = ProgressDialog(this)
        loadingBar?.setTitle(title)
        loadingBar?.setMessage(message)
        loadingBar?.show()
    }

    fun dismissDialog() {
        if (loadingBar!!.isShowing())
            loadingBar?.dismiss()
    }
    fun showErrorBody(error: VolleyError?){
        var body: String? = null;
        var charset: Charset = Charset.defaultCharset()
        //get status code here
        var statusC = error?.networkResponse?.statusCode.toString()
        //get response body and parse with appropriate encoding
        if (error?.networkResponse?.data != null) {
            try {
                body = String(error.networkResponse.data, Charset.forName("UTF-8"))
                val a=5
                try {
                    val message = JSONObject(body)
                    Toasty.error(this,message.getString("Message"), Toast.LENGTH_SHORT,true).show()

                }catch(e:Exception){
                    Toasty.error(this,body, Toast.LENGTH_SHORT,true).show()

                }

            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace();
            }
        }
    }
}
