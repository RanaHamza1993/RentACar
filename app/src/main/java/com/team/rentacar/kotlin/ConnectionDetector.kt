package com.team.rentacar.kotlin

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionDetector(context: Context) {


//    override
//    fun onReceive(context: Context, intent: Intent) {
//        val type = intArrayOf(ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI)
//        if (isNetrworkAvailable(context, type) == false) {
//            return
//        } else {
//            Toast.makeText(context, "No Net", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//   companion object {
//
//       fun isNetrworkAvailable(context: Context, typeNetworks: IntArray): Boolean {
//           try {
//               val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//               for (typeNetwork in typeNetworks) {
//                   val networkInfo = cm.getNetworkInfo(typeNetwork)
//                   if (networkInfo != null && networkInfo.state == NetworkInfo.State.CONNECTED) {
//                       return true
//                   }
//               }
//           } catch (e: Exception) {
//               return false
//           }
//
//           return false
//       }
//
//   }














    val context: Context =context

    fun isconnected() :Boolean
    {

        val connectivity: ConnectivityManager =context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(connectivity!=null)
        {
            var networkInfo: NetworkInfo?=connectivity?.activeNetworkInfo
            if(networkInfo!=null)
            {

                if(networkInfo.state== NetworkInfo.State.CONNECTED)
                {
                    return true
                }

            }
            return false



        }
        return true
    }
}