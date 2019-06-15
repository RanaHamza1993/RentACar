package com.team.rentacar.baseclasses

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.error.AuthFailureError
import com.android.volley.request.JsonObjectRequest
import com.android.volley.request.StringRequest
import com.facebook.CallbackManager
import com.google.android.gms.auth.GoogleAuthException
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.team.rentacar.activities.MainActivity
import com.team.rentacar.extensions.showErrorMessage
import com.team.rentacar.extensions.showSuccessMessage
import com.team.rentacar.kotlin.MySingleton
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URL
import java.nio.charset.Charset
import java.util.HashMap

open class SocialLoginActivity : BaseActivity() {

    var uname:String?=null
    var fname :String?=null
    var lname :String?=null
    var email :String?=null
    var personId :String?=null
    var photourl:String?=null
    var RC_SIGN_IN = 9300
    var googleApiClient: GoogleApiClient? = null
    var callbackManager: CallbackManager? = null
    var fbtxt: TextView? = null
    var token = ""
    var username = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    inner class RetrieveTokenTask : AsyncTask<GoogleSignInAccount, Void, String>() {

        override fun doInBackground(vararg params: GoogleSignInAccount): String? {
            val account = params[0]
            val scopes = "oauth2:profile email"
            try {

                val authtoken = GoogleAuthUtil.getToken(this@SocialLoginActivity, account.account!!, scopes)
                uname = account?.getDisplayName()
                fname = account?.getGivenName()
                lname = account?.getFamilyName()
                email = account?.getEmail()
                personId = account?.getId()
                photourl = account?.getPhotoUrl().toString()
//                Log.d("google_info", "auth token " + token!!)
                runOnUiThread(Runnable {
                    socialLogin(fname!!, lname!!, uname!!, email!!, photourl!!, authtoken, "GOOGLE", personId!!)

                })

                //progressDialog.show();

            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: UserRecoverableAuthException) {
                //startActivityForResult(e.getIntent(), REQ_SIGN_IN_REQUIRED);
                e.printStackTrace()
            } catch (e: GoogleAuthException) {
                Log.e("Tag", e.message)
                e.printStackTrace()
            }

            return token
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)

            Log.d("google_info", "token aync ended ")
        }
    }

    fun socialLogin(fname: String, lname: String, name: String, email: String, photoUrl: String, authtoken: String, provider: String, id: String) {


        val rootObject = JSONObject()

        val innerObject = JSONObject()
        innerObject.put("name", name)
        innerObject.put("firstName", fname)
        innerObject.put("lastName", lname)
        innerObject.put("email", email)
        innerObject.put("photoUrl", photoUrl)
        innerObject.put("authToken", authtoken)
        innerObject.put("provider", provider)
        innerObject.put("id", id)

        rootObject.put("user", innerObject)

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, "", rootObject,
            Response.Listener { response ->

                try {
                    //        Toasty.success(this!!.applicationContext, response.toString(), Toast.LENGTH_SHORT, true).show()

                    //StyleableToast.makeText(context,"Item Added Successfully",Toast.LENGTH_LONG,R.style.mytoast).show()
                    showSuccessMessage("You have successfully logged in to CramFrenzy")

                    if (provider.equals("FACEBOOK")) {

                        token = response.getString("token")
                        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        // var user_id=obj.getString("user_id")
                        // editor.putString("user_id",user_id )
                        editor.putString("token", token)
                        editor.putString("Role", "U")
                        editor.putString("social", "fb")
                        //  val a=Role
                        editor.apply()


                        val Preferences = getSharedPreferences("current user", Context.MODE_PRIVATE)
                        val mEditor = Preferences.edit()
                        mEditor.putString("uname", username)
                        mEditor.putString("profilepic", id)

                        mEditor.apply()

                        // Toast.makeText(this,"Current user"+ user_id + mail.text.trim().toString(),Toast.LENGTH_LONG).show()


                        var intent = Intent(this@SocialLoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {

                        showSuccessMessage("You have successfully logged in to CramFrenzy")
                        token = response.getString("token")
                        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        // var user_id=obj.getString("user_id")
                        // editor.putString("user_id",user_id )
                        editor.putString("token", token)
                        editor.putString("Role", "U")
                        editor.putString("social", "google")
                        //  val a=Role
                        editor.apply()


                        val Preferences = getSharedPreferences("current user", Context.MODE_PRIVATE)
                        val mEditor = Preferences.edit()
                        mEditor.putString("uname", username)
                        mEditor.putString("profilepic", id)

                        mEditor.apply()

                        // Toast.makeText(this,"Current user"+ user_id + mail.text.trim().toString(),Toast.LENGTH_LONG).show()


                        var intent = Intent(this@SocialLoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()


                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }


            },
            Response.ErrorListener { error ->


                //                    statusCode = error.networkResponse.statusCode

                var body: String? = null;
                var charset: Charset = Charset.defaultCharset()
                //get status code here
                var statusC = error.networkResponse.statusCode.toString();
                //get response body and parse with appropriate encoding
                if (error.networkResponse.data != null) {
                    try {
                        body = String(error.networkResponse.data, Charset.forName("UTF-8"))
//                            var startindex=body.indexOf("{")
//                            var endindex=body.indexOf("}")
//                            var bod=body.substring(startindex,endindex+1)
                        //   Toasty.error(mcontext!!.applicationContext,""+body,Toast.LENGTH_LONG).show()

                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace();
                    }
                }

                // Toast.makeText(context, "" + error.toString(), Toast.LENGTH_LONG).show()
                // Toasty.error(context, "Could not add to watchlist", Toast.LENGTH_SHORT, true).show()


                // Toast.makeText(context, "" + error.toString(), Toast.LENGTH_LONG).show()
            }
        ) {


            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                return super.parseNetworkResponse(response)
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json; charset=utf-8"
//                if(!token.equals(""))
//                    headers.put("Authorization", "JWT " + token)
                return headers
            }
        }


        // val requestQue = Volley.newRequestQueue(context)
        //requestQue.add(jsonObjectRequest)
        jsonObjectRequest.setShouldCache(false)

        MySingleton.getInstance(this@SocialLoginActivity).addToRequestQueue(jsonObjectRequest)


    }
    fun getFacebookProfilePicture(userID: String): Bitmap {
        val imageURL = URL("https://graph.facebook.com/$userID/picture?type=large")

        return BitmapFactory.decodeStream(imageURL.openConnection().getInputStream())
    }
    fun retrieveJWTForGoogleTask(auth_token: String, user_id: String) {

        //sending image to server
//    StringRequest request = new StringRequest(Request.Method.POST, google_auth_url, new Response.Listener<String>() {
//        @Override
//        public void onResponse(String s_ss) {
//
//            //progressDialog.dismiss();
//            dialog.dismiss();
//
//            Log.d("google_info","Response : "+s_ss);
//
//
//            if (google_auth_status_code == 200 || google_auth_status_code == 201) {
//
//                String google_user_token="";
//                String google_user_email="";
//                String google_user_id="";
//                String google_user_first_name="";
//                String google_user_last_name="";
//
//                try {
//                    JSONObject mainObject=new JSONObject(s_ss);
//
//                    google_user_token=mainObject.getString("token");
//                    google_user_email=mainObject.getString("email");
//                    google_user_id=mainObject.getString("id");
//
//                    google_user_first_name=mainObject.getString("first_name");
//                    google_user_last_name=mainObject.getString("last_name");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//                Toast.makeText(LoginActivity.this, "Google login successful.", Toast.LENGTH_LONG).show();
//
//                Log.d("google_info","Response : "+s_ss);
//
//                if(!google_user_token.equals("") && !google_user_email.equals("")) {
//
//
//                    Log.d("login", "Creating Session UserName:"+google_user_email.split("@")[0]);
//                    session.createLoginSession(google_user_token, google_user_email.split("@")[0], google_user_id);
//                    //create additional session
//                    session.createAdditionalSession(google_user_first_name+" "+google_user_last_name, google_user_id, google_user_email, SessionManager.SOCIAL_TYPE_GOOGLE, social_image_url);
//
//
//                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
//                }
//                else{
//                    Toast.makeText(LoginActivity.this, "Unable to fetch user details.", Toast.LENGTH_LONG).show();
//                }
//
//            } else {
//                Toast.makeText(LoginActivity.this, "Some error occurred", Toast.LENGTH_LONG).show();
//            }
//        }
//    }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError volleyError) {
//            //progressDialog.dismiss();
//            dialog.dismiss();
//
//
//            displayErrorMessages(volleyError, LoginActivity.this);
//
//        }
//    })
//
//
//    {
//        @Override
//        protected Response<String> parseNetworkResponse(NetworkResponse response) {
//            google_auth_status_code = response.statusCode;
//            return super.parseNetworkResponse(response);
//        }
//
//        @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            Map<String, String> params = new HashMap<>();
//
//            params.put("Authorization", "Bearer google-oauth2 " + auth_token);
//
//            Log.d("google_info", "Params, Authorization "+"Bearer google-oauth2 " + auth_token);
//            return params;
//        }
//
//
//        //adding parameters to send
//        @Override
//        protected Map<String, String> getParams() throws AuthFailureError {
//            Map<String, String> parameters = new HashMap<String, String>();
//
//            parameters.put("uid", user_id);
//
//            return parameters;
//        }
//    };
//
//    request.setRetryPolicy(new DefaultRetryPolicy(
//            100000,
//            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//   / RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
//   // rQueue.add(request);
//
//
//
//

        val stringRequest = object : StringRequest(Request.Method.POST, "", Response.Listener { response ->
            try {

            } catch (e: JSONException) {
                e.printStackTrace()
                showErrorMessage(e.toString())
            }
        }, Response.ErrorListener { error ->
            error.printStackTrace()

//            if (spinner.ishowingg())
//                spinner.dismisss()
            showErrorMessage("Username or Password does not exist")
        }
        ) {


            override fun parseNetworkResponse(response: NetworkResponse): Response<String> {

                //  Log.i("StatusCode",response.statusCode.toString() )

                return super.parseNetworkResponse(response)
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                try {

                } catch (e: Exception) {
                    //     Log.i("", "getParams: " + e)
                }

                return params
            }
        }
        try {
//            val requestQueue = Volley.newRequestQueue(this)
//            requestQueue.add(stringRequest)
            MySingleton.getInstance(this).addToRequestQueue(stringRequest)

        } catch (e: Exception) {
            //  Log.i("", " " + e)
        }

    }
    fun updateUI(isLogin: Boolean) {

    }

    fun handleResult(results: GoogleSignInResult) {

        if (results.isSuccess) {
            val account = results?.signInAccount
            //val name=account?.displayName
            //val email=account?.email


            //  username = account?.displayName!!


            // userid = account?.displayName!!

            val scopes = "oauth2:profile email"
            RetrieveTokenTask().execute(account)
            //val authtoken = account?.idToken!!




//


//           val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
//            val editor = sharedPreferences.edit()
//            // var user_id=obj.getString("user_id")
//            // editor.putString("user_id",user_id )
//            editor.putString("token", token)
//            editor.putString("Role", "U")
//            editor.putString("social", "google")
//            //  val a=Role
//            editor.apply()
//
//
//            val Preferences = getSharedPreferences("current user", Context.MODE_PRIVATE)
//            val mEditor = Preferences.edit()
//            mEditor.putString("uname", username)
//
//            mEditor.apply()
//
//            // Toast.makeText(this,"Current user"+ user_id + mail.text.trim().toString(),Toast.LENGTH_LONG).show()
//
//
//            var intent = Intent(this@LoginActivity, MainActivity::class.java)
//            startActivity(intent)
//            finish()

        } else
            updateUI(false)
    }

}
