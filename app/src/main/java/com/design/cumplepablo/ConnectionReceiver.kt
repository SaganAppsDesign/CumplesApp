package com.design.cumplepablo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast

class ConnectionReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        val hasConnection = networkInfo != null && networkInfo.isConnected
        Log.i("has connection", hasConnection.toString())
        if(!hasConnection) {
            Toast.makeText(context, "No tienes conexión a Internet", Toast.LENGTH_SHORT).show()
        }
    }
}