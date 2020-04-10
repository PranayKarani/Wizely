package com.wizely.utils

import android.content.Context
import android.net.ConnectivityManager
import com.wizely.MyApp


object NetworkManager {

    public fun isConnected() : Boolean {
        val connectivityManager = MyApp.get().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}