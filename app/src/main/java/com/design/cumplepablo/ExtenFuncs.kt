package com.design.cumplepablo

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.File

object ExtenFuncs: AppCompatActivity() {

    fun ImageView.loadUrl(url: File? = null, radius: Int) {
        Glide.with(context)
            .load(url)
            .fitCenter()
            .transform(RoundedCorners(radius))
            .into(this)
    }
}
