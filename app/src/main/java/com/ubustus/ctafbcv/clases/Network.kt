package com.ubustus.ctafbcv.clases

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity


class Network{
    companion object {
        fun hayRed(activity: AppCompatActivity):Boolean{
            val conectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = conectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}