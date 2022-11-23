package com.design.cumplepablo

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlin.collections.ArrayList

class SplashScreen : AppCompatActivity() {
    private var storage = Firebase.storage
    private val storageRef = storage.reference
    private var yearList: ArrayList<String> = arrayListOf()
    var name = ""
    var yearItem = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        getYearList()
        //shared preferences
        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        name = pref.getString("name", "").toString()

        if (name.isNotEmpty()) {
            activityMain(MainActivity::class.java)
        } else {
            activityOnboarding(OnBoarding::class.java)
        }
   }

    private fun activityOnboarding(activity: Class<OnBoarding>){
        Handler().postDelayed({
            val intent = Intent(this, activity)
            startActivity(intent)
            finish()
        }, 1500)
    }

    private fun activityMain(activity: Class<MainActivity>){
        Handler().postDelayed({
            val intent = Intent(this, activity)
            val pref = getSharedPreferences("datos", MODE_PRIVATE)
            name = pref.getString("name", "").toString()
            intent.putExtra("name", name)
            intent.putStringArrayListExtra("yearList",getYearList())
            finish()
            startActivity(intent)
        }, 1500)
    }


    private fun getYearList(): ArrayList<String>? {
        val spaceRef =  storageRef.child("imagenesCumple/")
        spaceRef.listAll()
            .addOnSuccessListener {
                for (i in it.items){
                    yearItem = i.toString().substring(i.toString().length-8,i.toString().length-4)
                    yearList = (yearList?.plus(yearItem)) as ArrayList<String>
                }
            }
            .addOnFailureListener {
                Log.e("yearList","Error charging list")
            }
        return yearList
    }

}