package com.team.rentacar.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.error.AuthFailureError
import com.android.volley.request.JsonObjectRequest
import com.android.volley.request.StringRequest
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Scope
import com.google.android.material.textfield.TextInputLayout
import com.team.rentacar.R
import com.team.rentacar.baseclasses.SocialLoginActivity
import com.team.rentacar.extensions.showErrorMessage
import com.team.rentacar.extensions.showSuccessMessage
import com.team.rentacar.kotlin.LoadingDialog
import com.team.rentacar.kotlin.MySingleton
import com.team.rentacar.kotlin.StaticFunctions
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.HashMap

class LoginActivity : SocialLoginActivity() {
    lateinit var mail: EditText
    lateinit var pass: EditText
    lateinit var spinner: LoadingDialog
    var loginButton: LoginButton? = null
    var signInButton: CardView? = null
    var fblogin: LinearLayout? = null
    var emailLayout: TextInputLayout? = null
    var passLayout: TextInputLayout? = null
    var Role = "U"
    // var isValidUsername=false
    var loginRoot: RelativeLayout?=null
    var currentUser: SharedPreferences? = null
    var socialLayout: ConstraintLayout?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* code for hiding status bar from activity*/
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)


        setContentView(R.layout.activity_login)
        loginRoot=findViewById(R.id.login_root)
        socialLayout=findViewById(R.id.login_social)
        //fbtxt = findViewById(R.id.fbtxt)
        currentUser = getSharedPreferences("login", Context.MODE_PRIVATE)
        val isfbLogin = currentUser?.getString("social", "")
//        if (!isfbLogin.equals(""))
//            fbtxt?.setText("Signout")
//        else
//            fbtxt?.setText("Login with Facebook")
        //    Role = userLogin?.getString("Role", "U")
        //  token = userLogin?.getString("token", "")
        //  social = userLogin?.getString("social", "")
        val serverClientId = getString(R.string.server_client_id)
        //token=""
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
            .requestEmail()
            .build()
        // val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        loginButton = findViewById(R.id.fb_login)
        fblogin = socialLayout?.findViewById(R.id.fblinear)
        callbackManager = CallbackManager.Factory.create()
        emailLayout = findViewById(R.id.input_layout_email)
        passLayout = findViewById(R.id.input_layout_password)

        signInButton = socialLayout?.findViewById(R.id.googlecard)
        //   signInButton?.setSize(SignInButton.SIZE_WIDE)


        googleApiClient = GoogleApiClient.Builder(this).enableAutoManage(this, 0, GoogleApiClient.OnConnectionFailedListener { }).addApi(
            Auth.GOOGLE_SIGN_IN_API, gso).build()
        signInButton?.setOnClickListener({

            googleSignIn()
        })


        mail = findViewById(R.id.l_mail)
        pass = findViewById(R.id.l_password)
        spinner = LoadingDialog("", this)
        //SSL.sslCertificates()

//        mail.setOnFocusChangeListener { v, hasFocus ->
//
//            if(!hasFocus&&mail.text.toString().trim().length>0)
//           //     userNameAuth()
//        }
        pass!!.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                if (s.toString().trim().length > 0)
                    input_layout_password.isPasswordVisibilityToggleEnabled = true
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {


            }

        })

        fblogin?.setOnClickListener({

            loginButton?.performClick()
            loginButton?.setReadPermissions("email")


        })
        loginButton?.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {

                // LoginManager?.logInWithReadPermissions(LoginActivity@this, Arrays.asList("email", "public_profile", "user_birthday"))

                //             var profile: Profile? = null
//                if(Profile.getCurrentProfile()==null)
//                 object: ProfileTracker() {
//                     override fun onCurrentProfileChanged(oldProfile: Profile?, currentProfile: Profile?) {
//
//                         val name=currentProfile?.name
//                         val pic=currentProfile?.getProfilePictureUri(100,100)
//
//                     }
//
//                 };
//
//
//            else {
//                    profile= Profile.getCurrentProfile()
//
//            }

                // username = loginResult.accessToken.userId
                token = loginResult.accessToken.token

                fbtxt?.setText("Signout")
                val request = GraphRequest.newMeRequest(loginResult.accessToken) { jsonObject, response ->
                    try {

                        username = jsonObject.getString("name")
                        val picid = jsonObject.getString("id")
                        val email = jsonObject.getString("email")
                        val fname = jsonObject.getString("first_name")
                        val lname = jsonObject.getString("last_name")
                        val photourl = "https://graph.facebook.com/" + picid + "/picture?type=large"
                        val authtoken = token
                        val provider = "FACEBOOK"
                        socialLogin(fname, lname, username, email, photourl, authtoken, provider, picid)
                        //    =jsonObject.getString("email")


                    } catch (e: Exception) {
                        e.printStackTrace()
                        // dismissDialogLogin()
                    }
                }

                val parameters = Bundle()
                parameters.putString("fields", "name,email,id,picture.type(large),first_name,last_name")
                request.parameters = parameters
                request.executeAsync()


            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {

            }
        })


        loginRoot?.setOnClickListener {

            StaticFunctions.hideKeyboard(it,this)
        }
    }

    fun Login(view: View) {
        when (view.id) {
            R.id.b_login -> {
                StaticFunctions.hideKeyboard(view,this)
                UserAuth()
            }
            R.id.b_register -> {
                var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

    }


    private fun loginAPI() {


        var username = mail.text.trim().toString()
        var password = pass.text.trim().toString()

        var mStatusCode = 0
        val stringRequest = object : StringRequest(Request.Method.POST, "", Response.Listener { response ->
            try {
                if (spinner.ishowingg())
                    spinner.dismisss()
                if (mStatusCode == 200) {


                    // showSuccessMessage("Login Successfull")
                    showSuccessMessage("You have successfully logged in to CramFrenzy")
                    val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    val obj: JSONObject = JSONObject(response)
                    // var user_id=obj.getString("user_id")
                    val token = obj.getString("token")
                    // editor.putString("user_id",user_id )
                    editor.putString("token", token)
                    editor.putString("Role", Role)
                    editor.putString("social", "")
                    val a = Role
                    editor.apply()


                    val Preferences = getSharedPreferences("current user", Context.MODE_PRIVATE)
                    val mEditor = Preferences.edit()
                    mEditor.putString("uname", mail.text.trim().toString())

                    mEditor.apply()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            } catch (e: JSONException) {
                e.printStackTrace()
                Toasty.error(this@LoginActivity, e.toString(), Toast.LENGTH_SHORT, true).show()
            }
        }, Response.ErrorListener { error ->
            error.printStackTrace()

            if (spinner.ishowingg())
                spinner.dismisss()
            Toasty.error(this@LoginActivity, "Username or Password does not exist", Toast.LENGTH_SHORT, true).show()
        }
        ) {
            override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response)
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                try {
                    params.put("username", username.toString())
                    params.put("password", password.toString())

                } catch (e: Exception) {
                    //     Log.i("", "getParams: " + e)
                }
                return params
            }
        }
        try {
            MySingleton.getInstance(this).addToRequestQueue(stringRequest)
        } catch (e: Exception) {
        }
    }

    private fun UserAuth() {


        var username = mail.text.trim().toString()
        var password = pass.text.trim().toString()
        val objj = JSONObject()
        objj.put("username", username)
        objj.put("password", password)

        if (username.isEmpty()) {

            //Toast.makeText(this@LoginActivity, "Enter username", Toast.LENGTH_SHORT).show()
            mail.setError("Enter Username")
            return
        }
        if (password.isEmpty()) {

            //Toasty.error(this@LoginActivity, "Please Enter Password", Toast.LENGTH_SHORT,true).show()

            input_layout_password.isPasswordVisibilityToggleEnabled = false
            pass.setError("Enter Password")
            return
        }
        //    spinner.showdialog()
        var mStatusCode = 0
        //

        val stringRequest = object : JsonObjectRequest(Request.Method.POST, "", objj, Response.Listener { response ->
            try {

                // Log.i("Token", mStatusCode.toString())
                if (spinner.ishowingg())
                    spinner.dismisss()
                if (mStatusCode == 200) {
                    Role = response.getString("Role")
                    loginAPI()


                } else if (mStatusCode == 400) {
                    Toasty.error(this, "Profile is Inactive", Toast.LENGTH_LONG, true).show()
                } else if (mStatusCode == 500) {
                    Toasty.error(this, "Profile does Not Exist", Toast.LENGTH_LONG, true).show()
                }

//                else if (mStatusCode == 404) {
//                    if(isValidUsername)
//                    Toasty.error(this, "You entered incorrect username", Toast.LENGTH_LONG, true).show()
//                    else
//                    Toasty.error(this, "You eneterd incorrect Password", Toast.LENGTH_LONG, true).show()
//
//                }

            } catch (e: JSONException) {
                e.printStackTrace()
                Toasty.error(this@LoginActivity, e.toString(), Toast.LENGTH_SHORT, true).show()
            }
        }, Response.ErrorListener { error ->
            error.printStackTrace()

            if (spinner.ishowingg())
                spinner.dismisss()
            var body: String? = null;
            var charset: Charset = Charset.defaultCharset()
            //get status code here
            var statusC = error.networkResponse.statusCode;
            //get response body and parse with appropriate encoding
            if (error.networkResponse.data != null) {
                try {
                    body = String(error.networkResponse.data, Charset.forName("UTF-8"))
                    val a = 5

                    //   Toasty.error(this,JSONObject(body).getString("message") , Toast.LENGTH_LONG, true).show()
//                            var startindex=body.indexOf("{")
//                            var endindex=body.indexOf("}")
//                            var bod=body.substring(startindex,endindex+1)
                    //   Toasty.error(mcontext!!.applicationContext,""+body,Toast.LENGTH_LONG).show()

                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace();
                }
            }
            if (statusC == 400) {
                Toasty.error(this@LoginActivity, "Please activate your profile first", Toast.LENGTH_SHORT, true).show()

            }
//            else if(statusC==401)
//                Toasty.error(this, "You entered incorrect username", Toast.LENGTH_LONG, true).show()
//            else if(statusC==404){
//             //   if(isValidUsername)
//              //  else
//                    Toasty.error(this, "You eneterd incorrect Password", Toast.LENGTH_LONG, true).show()
//
//            }
//                Toasty.error(this@LoginActivity, "Username or Password does not exist", Toast.LENGTH_SHORT, true).show()

            showErrorMessage("Username or Password is incorrect")
        }
        ) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
                mStatusCode = response.statusCode;
                //  Log.i("StatusCode",response.statusCode.toString() )
                return super.parseNetworkResponse(response)
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


    fun userNameAuth(){

        //if(!spinner.ishowingg())
        // spinner.showdialog()
        val username = mail.text.trim().toString()
        val obj= JSONObject()
        obj.put("username",username)
        var statusC=0
        val stringRequest = object : JsonObjectRequest(Request.Method.POST, "",obj , Response.Listener { response ->
            try {

                //     spinner.dismisss()
                //  isValidUsername=true
                //mail?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.username_done, 0)



            } catch (e: JSONException) {
                e.printStackTrace()
                spinner.dismisss()
                Toasty.error(this@LoginActivity, e.toString(), Toast.LENGTH_SHORT, true).show()
            }
        }, Response.ErrorListener { error ->
            error.printStackTrace()


            showErrorMessage("Invalid Username")
            spinner.dismisss()
            if (statusC == 404) {
                //Toasty.error(this@LoginActivity, "Please activate your profile first", Toast.LENGTH_SHORT, true).show()

            }
          //  mail?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.username_invalid, 0)
        }
        ) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
                //  Log.i("StatusCode",response.statusCode.toString() )
                statusC=response.statusCode
                return super.parseNetworkResponse(response)
            }
        }



        stringRequest.setShouldCache(false)
        MySingleton.getInstance(this).addToRequestQueue(stringRequest)

    }

    override fun onBackPressed() {
        super.onBackPressed()


        this.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RC_SIGN_IN) {

            val results = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            val statusCode = results.getStatus().getStatusCode()
            handleResult(results)
        } else
            callbackManager?.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun googleSignIn() {

        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun googleSignOut() {

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(ResultCallback {

            updateUI(false)
        })

    }





    override fun onStop() {
        super.onStop()
        if (googleApiClient != null && googleApiClient!!.isConnected()) {
            googleApiClient!!.stopAutoManage(this);
            googleApiClient!!.disconnect()
        }
    }

}
