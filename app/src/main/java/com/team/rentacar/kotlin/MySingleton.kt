package com.team.rentacar.kotlin

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MySingleton private constructor(context: Context) {
    companion object {

        @Volatile
        private var INSTANCE: MySingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MySingleton(context).also {
                    INSTANCE = it
                }
            }


//        @Volatile
//        private var   requestQue: RequestQueue?=null
//        fun getRequestQue(requestQueue: RequestQueue =
//                requestQue ?: synchronized(this)
//                {
//                    requestQue
//                            ?: Volley.newRequestQueue(context.applicationContext)
//                }
    }


    //    val requestQueue: RequestQueue
//        get() {
//            if (requestQue == null) {
//                requestQue =Volley.newRequestQueue(context.applicationContext)
//            }
//            return requestQue!!
//        }
    val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.

        Volley.newRequestQueue(context.applicationContext)

    }
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
//   private val requestQueue: RequestQueue
//
//    constructor(context: Context):this()
//    var context:Context?=null
//    init {
//        requestQueue = Volley.newRequestQueue(context?.applicationContext)
//
//        this.context=context
//    }
//
//    companion object {
//        private var mInstance: MySingleton? = null
//
//        val instance: MySingleton
//            get() {
//                if (mInstance == null) {
//                    mInstance =MySingleton()
//                }
//                return mInstance as MySingleton
//            }
//    }
//
//    fun getRequestQueue(): RequestQueue {
//        return this.requestQueue
//    }
//    fun <T> addToRequestQueue(req: Request<T>) {
//        requestQueue.add(req)
//    }
}