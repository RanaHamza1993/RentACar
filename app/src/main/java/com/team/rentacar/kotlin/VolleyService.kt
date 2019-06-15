package com.team.rentacar.kotlin

import android.content.Context
import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonArrayRequest
import com.android.volley.request.JsonObjectRequest
import com.android.volley.request.StringRequest
import com.team.rentacar.interfaces.IVolleResult
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class VolleyService constructor(resultCallback: IVolleResult?, context: Context) {
    var mResultCallback: IVolleResult? = null
    lateinit var mContext: Context


    init {
        mResultCallback = resultCallback
        mContext = context
    }

    fun putDataVolley(requestType: String, url: String, sendObj: JSONObject, token: String) {

        if (requestType.equals("Object", true)) {

            try {


                val jsonObj = object : JsonObjectRequest(Request.Method.PUT, url, sendObj, object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject) {
                        if (mResultCallback != null)
                            mResultCallback!!.notifySuccess(requestType, response, url)
                    }
                }, Response.ErrorListener {

                    if (mResultCallback != null)
                        mResultCallback!!.notifyError(requestType, it, url)
                    Log.d("error",it.toString())


                }) {


                    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                        return super.parseNetworkResponse(response)
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        if (!token.equals(""))
                            headers.put("Authorization", "JWT " + token)
                        return headers
                    }
                }


                //if(url.contains(Url.urlGetWatchlist)||url.contains(Url.urlGetCart))
                jsonObj.setShouldCache(false)
                //else
                // jsonObj.setShouldCache(true)

                MySingleton.getInstance(mContext).addToRequestQueue(jsonObj)

            } catch (e: Exception) {

            }

        } else if (requestType.equals("String", true)) {

            try {


                val jsonObj = object : StringRequest(Request.Method.POST, url, object : Response.Listener<String> {
                    override fun onResponse(response: String) {
                        if (mResultCallback != null)
                            mResultCallback!!.notifySuccess(requestType, response, url)
                    }
                }, Response.ErrorListener {

                    if (mResultCallback != null)
                        mResultCallback!!.notifyError(requestType, it, url)
                    Log.d("error",it.toString())
                }) {

                    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                        return super.parseNetworkResponse(response)
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        if (!token.equals(""))
                            headers.put("Authorization", "JWT " + token)
                        return headers
                    }
                }


                //if(url.contains(Url.urlGetWatchlist)||url.contains(Url.urlGetCart))
                jsonObj.setShouldCache(false)
                //else
                // jsonObj.setShouldCache(true)

                MySingleton.getInstance(mContext).addToRequestQueue(jsonObj)

            } catch (e: Exception) {

            }
        }
    }
    fun postDataVolley(requestType: String, url: String, sendObj: JSONObject, token: String) {

        if (requestType.equals("Object", true)) {

            try {


                val jsonObj = object : JsonObjectRequest(Request.Method.POST, url, sendObj, object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject) {
                        if (mResultCallback != null)
                            mResultCallback!!.notifySuccess(requestType, response, url)
                    }
                }, Response.ErrorListener {

                    if (mResultCallback != null)
                        mResultCallback!!.notifyError(requestType, it, url)
                    Log.d("error",it.toString())


                }) {


                    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                        return super.parseNetworkResponse(response)
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        if (!token.equals(""))
                            headers.put("Authorization", "JWT " + token)
                        return headers
                    }
                }


                //if(url.contains(Url.urlGetWatchlist)||url.contains(Url.urlGetCart))
                jsonObj.setShouldCache(false)
                //else
                // jsonObj.setShouldCache(true)

                MySingleton.getInstance(mContext).addToRequestQueue(jsonObj)

            } catch (e: Exception) {

            }

        } else if (requestType.equals("String", true)) {

            try {


                val jsonObj = object : StringRequest(Request.Method.POST, url, object : Response.Listener<String> {
                    override fun onResponse(response: String) {
                        if (mResultCallback != null)
                            mResultCallback!!.notifySuccess(requestType, response, url)
                    }
                }, Response.ErrorListener {

                    if (mResultCallback != null)
                        mResultCallback!!.notifyError(requestType, it, url)
                    Log.d("error",it.toString())
                }) {

                    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                        return super.parseNetworkResponse(response)
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        if (!token.equals(""))
                            headers.put("Authorization", "JWT " + token)
                        return headers
                    }
                }


                //if(url.contains(Url.urlGetWatchlist)||url.contains(Url.urlGetCart))
                jsonObj.setShouldCache(false)
                //else
                // jsonObj.setShouldCache(true)

                MySingleton.getInstance(mContext).addToRequestQueue(jsonObj)

            } catch (e: Exception) {

            }
        }
    }

    fun postDataVolley(requestType: String, url: String, sendObj: JSONObject, token: String, urlflag:String) {

        var netWorkResponse=0
        if (requestType.equals("Object", true)) {

            try {


                val jsonObj = object : JsonObjectRequest(Request.Method.POST, url, sendObj, object : Response.Listener<JSONObject> {
                    override fun onResponse(response: JSONObject) {
                        if (mResultCallback != null)
                            mResultCallback!!.notifySuccess(requestType, response, url+urlflag,netWorkResponse)
                    }
                }, Response.ErrorListener {

                    if (mResultCallback != null)
                        mResultCallback!!.notifyError(requestType, it, url+urlflag)
                    Log.d("error",it.toString())


                }) {


                    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                        try {
                            netWorkResponse = response?.statusCode!!
                        }catch(e:Exception){}
                        return super.parseNetworkResponse(response)
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        if (!token.equals(""))
                            headers.put("Authorization", "JWT " + token)
                        return headers
                    }
                }


                //if(url.contains(Url.urlGetWatchlist)||url.contains(Url.urlGetCart))
                jsonObj.setShouldCache(false)
                //else
                // jsonObj.setShouldCache(true)
                MySingleton.getInstance(mContext).addToRequestQueue(jsonObj)

            } catch (e: Exception) {

            }

        } else if (requestType.equals("String", true)) {

            try {


                val jsonObj = object : StringRequest(Request.Method.POST, url, object : Response.Listener<String> {
                    override fun onResponse(response: String) {
                        if (mResultCallback != null)
                            mResultCallback!!.notifySuccess(requestType, response, url)
                    }
                }, Response.ErrorListener {

                    if (mResultCallback != null)
                        mResultCallback!!.notifyError(requestType, it, url)
                    Log.d("error",it.toString())
                }) {

                    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                        return super.parseNetworkResponse(response)
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        if (!token.equals(""))
                            headers.put("Authorization", "JWT " + token)
                        return headers
                    }
                }


                //if(url.contains(Url.urlGetWatchlist)||url.contains(Url.urlGetCart))
                jsonObj.setShouldCache(false)
                //else
                // jsonObj.setShouldCache(true)
                MySingleton.getInstance(mContext).addToRequestQueue(jsonObj)

            } catch (e: Exception) {

            }
        }
    }



    fun getDataVolley(requestType: String, url: String, token: String) {

        if (requestType.equals("Array", true)) {

            try {

                val jsonObj = object : JsonArrayRequest(Request.Method.GET, url, null, Response.Listener { response ->
                    if (mResultCallback != null)
                        mResultCallback!!.notifySuccess(requestType, response, url)
                }, Response.ErrorListener { error ->
                    if (mResultCallback != null)
                        mResultCallback!!.notifyError(requestType, error, url)
                    Log.d("error",error.toString())
                }) {


                    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONArray> {
                        return super.parseNetworkResponse(response)
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        if (!token.equals(""))
                            headers.put("Authorization", "JWT " + token)
                        return headers
                    }
                }
                jsonObj.tag="arrayRequest"
                jsonObj.setShouldCache(false)
                MySingleton.getInstance(mContext).addToRequestQueue(jsonObj)

            } catch (e: Exception) {

            }


        } else if (requestType.equals("String", true)) {

            try {

                val jsonObj = object : StringRequest(Request.Method.GET, url, Response.Listener { response ->
                    if (mResultCallback != null)
                        mResultCallback!!.notifySuccess(requestType, response, url)
                }, Response.ErrorListener { error ->
                    if (mResultCallback != null)
                        mResultCallback!!.notifyError(requestType, error, url)
                    Log.d("error",error.toString())
                }) {


                    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                        return super.parseNetworkResponse(response)

//                        try{
//                            var cacheEntry: Cache.Entry? = HttpHeaderParser.parseCacheHeaders(response)
//                            if (cacheEntry == null) {
//                                cacheEntry = Cache.Entry()
//                            }
//
//                            val cacheHitButRefreshed = (3 * 60 * 1000).toLong() // in 3 minutes cache will be hit, but also refreshed on background
//                            val cacheExpired = (1 * 60 * 60 * 1000).toLong() // in 24 hours this cache entry expires completely
//                            val now = System.currentTimeMillis()
//                            val softExpire = now + cacheHitButRefreshed
//                            val ttl = now + cacheExpired
//                            cacheEntry.data = response?.data
//                            cacheEntry.softTtl = softExpire
//                            cacheEntry.ttl = ttl
//                            var headerValue: String?
//                            headerValue = response!!.headers["Date"]!!
//                            if (headerValue != null) {
//                                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                            }
//                            headerValue = response.headers.get("Last-Modified");
//                            if (headerValue != null) {
//                                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                            }
//                            cacheEntry.responseHeaders = response.headers
//                            val jsonString = String(response.data,
//                                    Charset.forName(HttpHeaderParser.parseCharset(response.headers)))
//                            return Response.success<String>((jsonString), cacheEntry)
//                        }
//                        catch (e: UnsupportedEncodingException) {
//                            return Response.error(ParseError(e));
//                        }

                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        if (!token.equals(""))
                            headers.put("Authorization", "JWT " + token)
                        return headers
                    }
                }


//                             if(url.contains(Url.urlGetWatchlist)||url.contains(Url.urlGetCart)||url.contains(Url.urlCourseBidHistory)
//                                             ||url.contains(Url.urlNoteBidHistory)||url.contains(Url.urlBookBidHistory)||url.contains(Url.urlFlashBidHistory))
//                jsonObj.setShouldCache(false)
//                           else
                jsonObj.setShouldCache(false)
                jsonObj.tag="stringRequest"
                MySingleton.getInstance(mContext).addToRequestQueue(jsonObj)

            } catch (e: Exception) {

            }


        } else if (requestType.equals("Object")) {
            try {

                val jsonObj = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
                    if (mResultCallback != null)
                        mResultCallback!!.notifySuccess(requestType, response, url)
                }, Response.ErrorListener { error ->
                    if (mResultCallback != null)
                        mResultCallback!!.notifyError(requestType, error, url)
                    Log.d("error",error.toString())
                }) {


                    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                        return super.parseNetworkResponse(response)
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        if (!token.equals(""))
                            headers.put("Authorization", "JWT " + token)
                        return headers
                    }
                }


                //if(url.contains(Url.urlGetWatchlist)||url.contains(Url.urlGetCart))
                //   jsonObj.setShouldCache(false)
                // else
                jsonObj.setShouldCache(false)
                jsonObj.tag="objectRequest"
                MySingleton.getInstance(mContext).addToRequestQueue(jsonObj)

            } catch (e: Exception) {
            }
        }
    }

    fun getDataVolley(requestType: String, url: String, token: String, urlflag: String) {

        if (requestType.equals("Array", true)) {

            try {

                val jsonObj = object : JsonArrayRequest(Request.Method.GET, url, null, Response.Listener { response ->
                    if (mResultCallback != null)
                        mResultCallback!!.notifySuccess(requestType, response, url)
                }, Response.ErrorListener { error ->
                    if (mResultCallback != null)
                        mResultCallback!!.notifyError(requestType, error, url)
                    Log.d("error",error.toString())
                }) {


                    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONArray> {
                        return super.parseNetworkResponse(response)
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        if (!token.equals(""))
                            headers.put("Authorization", "JWT " + token)
                        return headers
                    }
                }
                jsonObj.tag="flagArraytRequest"
                jsonObj.setShouldCache(false)
                MySingleton.getInstance(mContext).addToRequestQueue(jsonObj)

            } catch (e: Exception) {

            }


        } else if (requestType.equals("String", true)) {

            try {

                val jsonObj = object : StringRequest(Request.Method.GET, url, Response.Listener { response ->
                    if (mResultCallback != null)
                        mResultCallback!!.notifySuccess(requestType, response, url + urlflag)
                }, Response.ErrorListener { error ->
                    if (mResultCallback != null)
                        mResultCallback!!.notifyError(requestType, error, url + urlflag)
                }) {


                    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                        return super.parseNetworkResponse(response)
//
//                        try{
//                            var cacheEntry: Cache.Entry? = HttpHeaderParser.parseCacheHeaders(response)
//                            if (cacheEntry == null) {
//                                cacheEntry = Cache.Entry()
//                            }
//
//                            val cacheHitButRefreshed = (1 * 60 * 1000).toLong() // in 3 minutes cache will be hit, but also refreshed on background
//                            val cacheExpired = (1 * 60 * 60 * 1000).toLong() // in 24 hours this cache entry expires completely
//                            val now = System.currentTimeMillis()
//                            val softExpire = now + cacheHitButRefreshed
//                            val ttl = now + cacheExpired
//                            cacheEntry.data = response?.data
//                            cacheEntry.softTtl = softExpire
//                            cacheEntry.ttl = ttl
//                            var headerValue: String?
//                            headerValue = response!!.headers["Date"]!!
//                            if (headerValue != null) {
//                                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                            }
//                            headerValue = response.headers.get("Last-Modified");
//                            if (headerValue != null) {
//                                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                            }
//                            cacheEntry.responseHeaders = response.headers
//                            val jsonString = String(response.data,
//                                    Charset.forName(HttpHeaderParser.parseCharset(response.headers)))
//                            return Response.success<String>((jsonString), cacheEntry)
//                        }
//                        catch (e: UnsupportedEncodingException) {
//                            return Response.error(ParseError(e));
//                        }

                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        if (!token.equals(""))
                            headers.put("Authorization", "JWT " + token)
                        return headers
                    }
                }


//                if(url.contains(Url.urlGetWatchlist)||url.contains(Url.urlGetCart))
//                jsonObj.setShouldCache(false)
//                              else
                jsonObj.tag="flagStringtRequest"
                jsonObj.setShouldCache(false)
                MySingleton.getInstance(mContext).addToRequestQueue(jsonObj)

            } catch (e: Exception) {

            }


        } else if (requestType.equals("Object")) {
            try {

                val jsonObj = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
                    if (mResultCallback != null)
                        mResultCallback!!.notifySuccess(requestType, response, url)
                }, Response.ErrorListener { error ->
                    if (mResultCallback != null)
                        mResultCallback!!.notifyError(requestType, error, url)
                    Log.d("error",error.toString())
                }) {


                    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                        return super.parseNetworkResponse(response)
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        if (!token.equals(""))
                            headers.put("Authorization", "JWT " + token)
                        return headers
                    }
                }
                jsonObj.tag="flagObjectRequest"
                jsonObj.setShouldCache(false)
                MySingleton.getInstance(mContext).addToRequestQueue(jsonObj)

            } catch (e: Exception) {
            }
        }
    }


    fun deleteDataVolley(requestType: String, url: String, token: String) {

        if (requestType.equals("String", true)) {

            try {


                val jsonObj = object : StringRequest(Request.Method.DELETE, url, object : Response.Listener<String> {
                    override fun onResponse(response: String) {
                        if (mResultCallback != null)
                            mResultCallback!!.notifySuccess(requestType, response, url)
                    }
                }, Response.ErrorListener {

                    if (mResultCallback != null)
                        mResultCallback!!.notifyError(requestType, it, url)
                    Log.d("error",it.toString())
                }) {


                    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                        return super.parseNetworkResponse(response)
                    }

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        if (!token.equals(""))
                            headers.put("Authorization", "JWT " + token)
                        return headers
                    }
                }
                jsonObj.setShouldCache(false)
                MySingleton.getInstance(mContext).addToRequestQueue(jsonObj)

            } catch (e: Exception) {

            }
        }
    }

}