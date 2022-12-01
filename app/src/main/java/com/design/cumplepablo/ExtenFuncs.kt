package com.design.cumplepablo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.design.cumplepablo.databinding.ActivityOnBoardingBinding
import java.io.File

object ExtenFuncs: AppCompatActivity() {

    fun ImageView.loadUrl(url: File? = null, radius: Int) {
        Glide.with(context)
            .load(url)
            .fitCenter()
            .transform(RoundedCorners(radius))
            .into(this)
    }

    fun batteryLevel(context: Context, binding: ActivityOnBoardingBinding){
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }
        val batteryPct: Float? = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }
        binding.tvbatteryLevel.text = batteryPct.toString() + " %"

        when(batteryPct!!) {
            in 0F..33F -> binding.tvbatteryLevel.setBackgroundColor(Color.rgb(255,100,51))
            in 34F..66F -> {binding.tvbatteryLevel.setBackgroundColor(Color.rgb(255,233,51))}
            in 67F..100F -> binding.tvbatteryLevel.setBackgroundColor(Color.rgb(97,255,51))
          }
    }
}
